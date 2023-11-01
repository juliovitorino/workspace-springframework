package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.FieldDescriptor;
import br.com.jcv.codegen.codegenerator.dto.WritableCode;
import br.com.jcv.codegen.codegenerator.enums.CodeGeneratorTags;
import br.com.jcv.codegen.codegenerator.enums.FieldTypeConverterEnum;
import br.com.jcv.codegen.codegenerator.enums.FieldTypeEnum;
import br.com.jcv.codegen.codegenerator.enums.IncludeExtraCommandEnum;
import br.com.jcv.codegen.codegenerator.exception.CodeGeneratorFolderStructureNotFound;
import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import br.com.jcv.commons.library.utility.StringUtility;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpStatus;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

@Slf4j
public abstract class AbstractCodeGenerator {
    public static final String TARGET_EXTENSION_JAVA = "java";
    public static final String TARGET_EXTENSION_XML = "xml";
    public static final String INCLUDE = "#include ";
    public static final String INCLUDE_ONCE = "#include_once ";
    private String basePackage;
    @Autowired protected Gson gson;
    @Autowired protected ResourceLoader resourceLoader;

    protected void readTemplate(String template, StringBuffer sb, CodeGeneratorDTO codeGeneratorDTO){
//        Resource fileTemplateResource = resourceLoader.getResource("classpath:" + template);
        try {
            ResourcePatternResolver resourcePatResolver = new PathMatchingResourcePatternResolver();
//            Resource[] allResources = resourcePatResolver.getResources("classpath*:static/*.template");
            Resource[] allResources = resourcePatResolver.getResources("classpath*:" + template);
            if(allResources.length == 0) {
                throw new FileNotFoundException("The file named as " + template + " not found in JAR file. Check it out!");
            }
            if(allResources.length > 1) {
                throw new RuntimeException("There are " + String.valueOf(allResources.length) + " of " + template + ". Only must be one!");
            }

            InputStream isTemplate = allResources[0].getInputStream();
            Scanner scannerFileTemplate = new Scanner(isTemplate);
            while( scannerFileTemplate.hasNextLine()) {
                final String line = scannerFileTemplate.nextLine();
                boolean isIncludeTag = line.indexOf(INCLUDE) == 0 ;
                boolean isIncludeOnceTag = line.indexOf(INCLUDE_ONCE) == 0;
                if (isIncludeOnceTag || isIncludeTag) {
                    final StringBuffer sbInclude = checkForIncludeLines(line, isIncludeTag ? INCLUDE : INCLUDE_ONCE);

                    if(isIncludeOnceTag) {
                        checkIncludeOnceCommandsDenied(line);
                        assert sbInclude != null;
                        sb.append(changeTagsUsing(sbInclude.toString(),codeGeneratorDTO));
                    }

                    if(isIncludeTag) {
                        processIgnoredFields(sb, codeGeneratorDTO, line, sbInclude);
                    }
                } else {
                    String lineChanged = changeTagsUsing(line,codeGeneratorDTO);
                    sb.append(lineChanged);
                }
                sb.append(System.getProperty("line.separator"));
            }
            scannerFileTemplate.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processExcludeCharAtEndFromLastField(FieldDescriptor field, StringBuffer sb, CodeGeneratorDTO codeGeneratorDTO, String line, String sbInclude) {
        char[] excludeChars = getExcludeCharAtEndOfLineFromLastField(line).toCharArray();
        assert sbInclude != null;
        String blockAfterChangeTag = changeTagsUsing(sbInclude, codeGeneratorDTO, field);
        for (char excludeChar : excludeChars) {
            blockAfterChangeTag = blockAfterChangeTag.replaceAll("\\".concat(String.valueOf(excludeChar)), "");
        }
        sb.append(blockAfterChangeTag);
    }
    private void processExcludeCharAtBeginFromFirstField(FieldDescriptor field, StringBuffer sb, CodeGeneratorDTO codeGeneratorDTO, String line, String sbInclude) {
        char[] excludeChars = getExcludeCharAtBeginFromFirstField(line).toCharArray();
        assert sbInclude != null;
        String blockAfterChangeTag = changeTagsUsing(sbInclude, codeGeneratorDTO, field);
        for (char excludeChar : excludeChars) {
            blockAfterChangeTag = blockAfterChangeTag.replaceAll("\\".concat(String.valueOf(excludeChar)), "");
        }
        sb.append(blockAfterChangeTag);
    }
    private void processIgnoredFields(StringBuffer sb, CodeGeneratorDTO codeGeneratorDTO, String line, StringBuffer sbInclude) {
        log.info("readTemplate :: checking out for extra commands = {}", Arrays.toString(IncludeExtraCommandEnum.values()));
        String[] excludeFields = getExcludeFields(line);
        log.info("readTemplate :: excluded fields are = {}", Arrays.toString(excludeFields));
        int idx = 0;
        int idxLastField = codeGeneratorDTO.getFieldDescriptorList().size() - 1;
        for(FieldDescriptor fieldItem: codeGeneratorDTO.getFieldDescriptorList()) {
            String fieldExcluded = Arrays.stream(excludeFields)
                    .filter(fieldExclude -> fieldExclude.equals(fieldItem.getFieldReferenceInDto()))
                    .findFirst()
                    .orElse(null);
            if(Objects.isNull(fieldExcluded)) {
                if(idx == 0) {
                    processExcludeCharAtBeginFromFirstField(fieldItem, sb, codeGeneratorDTO, line, sbInclude.toString());
                } else if(idx == idxLastField) {
                    processExcludeCharAtEndFromLastField(fieldItem, sb, codeGeneratorDTO, line, sbInclude.toString());
                } else {
                    sb.append(changeTagsUsing(sbInclude.toString(), codeGeneratorDTO, fieldItem));
                }
                idx++;
            }
        }
    }

    private void checkIncludeOnceCommandsDenied(String line) {
        String[] excludeFields = getExcludeFields(line);
        if(excludeFields.length > 0) {
            throw new RuntimeException("IncludeExtraCommand "
                    + IncludeExtraCommandEnum.excludeFields.getCommand() + " not allowed for " + line);
        }

        if(!getExcludeCharAtBeginFromFirstField(line).isEmpty()){
            throw new RuntimeException("IncludeExtraCommand "
                    + IncludeExtraCommandEnum.excludeCharAtBeginFromFirstField.getCommand() + " not allowed for " + line);
        }
        if(!getExcludeCharAtEndOfLineFromLastField(line).isEmpty()){
            throw new RuntimeException("IncludeExtraCommand "
                    + IncludeExtraCommandEnum.excludeCharAtEndOfLineFromLastField.getCommand()
                    + " not allowed for " + line);
        }
    }

    private String[] getExcludeFields(String line) {
        int position = line.indexOf(IncludeExtraCommandEnum.excludeFields.getCommand());
        if(position > -1) {
            String extractedFields = line.substring(position + IncludeExtraCommandEnum.excludeFields.getCommand().length(), line.length() -1);
            return extractedFields.split(",");
        }
        return new String[]{};
    }

    private String getExcludeCharAtBeginFromFirstField(String line) {
        int position = line.indexOf(IncludeExtraCommandEnum.excludeCharAtBeginFromFirstField.getCommand());
        if(position > -1) {
            return line.substring(position + IncludeExtraCommandEnum.excludeCharAtBeginFromFirstField.getCommand().length(), line.length() -1);
        }
        return "";
    }

    private String getExcludeCharAtEndOfLineFromLastField(String line) {
        int position = line.indexOf(IncludeExtraCommandEnum.excludeCharAtEndOfLineFromLastField.getCommand());
        if(position > -1) {
            return line.substring(position + IncludeExtraCommandEnum.excludeCharAtEndOfLineFromLastField.getCommand().length(), line.length() -1);
        }
        return "";
    }

    protected StringBuffer readFile(String filePath) {
        StringBuffer sb = new StringBuffer();
//        Resource fileTemplateResource = resourceLoader.getResource("classpath:" + filePath);

        try {
            ResourcePatternResolver resourcePatResolver = new PathMatchingResourcePatternResolver();
            Resource[] allResources = resourcePatResolver.getResources("classpath*:" + filePath);
            if(allResources.length == 0) {
                throw new FileNotFoundException("The file named as " + filePath + " not found in JAR file. Check it out!");
            }
            if(allResources.length > 1) {
                throw new RuntimeException("There are " + String.valueOf(allResources.length) + " of " + filePath + ". Only must be one!");
            }

            InputStream isTemplate = allResources[0].getInputStream();
            Scanner scannerFileTemplate = new Scanner(isTemplate);
            while( scannerFileTemplate.hasNextLine()) {
                sb.append(scannerFileTemplate.nextLine());
                sb.append(System.getProperty("line.separator"));
            }
            scannerFileTemplate.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }

    protected String fullClassNameToSingle(String fullClassName) {
        return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }
    protected <Input> CodeGeneratorDTO prepareCodeGeneratorFromModel(Class<Input> inputClassModel) {

        CodeGeneratorDTO codegen = new CodeGeneratorDTO();

        final String modelPackage = inputClassModel.getPackage().getName();
        basePackage = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        log.info("prepareCodeGeneratorFromModel :: base package is -> {}", basePackage);

        CodeGeneratorDescriptor codeGeneratorDescriptor = inputClassModel.getAnnotation(CodeGeneratorDescriptor.class);
        Table tableAnnotation =  inputClassModel.getAnnotation(Table.class);
        if(codeGeneratorDescriptor == null) {
            log.info("prepareCodeGeneratorFromModel :: input class {} model hasn't been annoteded", inputClassModel.getClass().hashCode());
            throw new RuntimeException("The input class model hasn't been annoteded");
        }

        codegen.setBasePackage(getContent(codeGeneratorDescriptor.basePackage(), basePackage));
        codegen.setOutputDir(codeGeneratorDescriptor.outputDir());
        codegen.setProject(codeGeneratorDescriptor.project());
        codegen.setFullDescription(codeGeneratorDescriptor.fullDescription());
        codegen.setAuthor(codeGeneratorDescriptor.author());
        codegen.setBaseClass(inputClassModel.getName().substring(inputClassModel.getName().lastIndexOf(".")+1));

        if(tableAnnotation != null) {
            codegen.setTableName(tableAnnotation.name());
            codegen.setSchema(tableAnnotation.schema());
        } else {
            codegen.setTableName(codegen.getBaseClass());
            codegen.setSchema(null);
        }

        List<FieldDescriptor> fieldDescriptors = processCodeGeneratorFieldDescriptorAnnotation(inputClassModel);
        codegen.setFieldDescriptorList(fieldDescriptors);

        createFolderStructureIfNeed(codegen);
        return codegen;
    }

    private void createFolderStructureIfNeed(CodeGeneratorDTO codegen) {
        final String PING = "/ping.txt";
        final String[] folders = new String[] {"config","builder","analyser","constantes","controller","dto","enums"
                ,"exception","interfaces","repository","service","service/impl"};

        log.info("code generator Output Directory -> {}", codegen.getOutputDir());
        log.info("code generator base package -> {}", codegen.getBasePackage());

        for(String folder: folders) {
//            log.info("Path => {}",codegen.getOutputDir() + "/" + codegen.getBasePackageSlash() + "/" + folder + "/ping.txt");
            if(!folderExists(codegen.getOutputDir() + "/" + codegen.getBasePackageSlash() + "/" + folder + PING)) {
                System.out.println("*** Folder [" + folder + "] does not exist");
                System.out.println("*** You must have to create and check the following folder structure");
                System.out.println(codegen.getOutputDir());
                System.out.println("+-- /analyser");
                System.out.println("+-- /builder");
                System.out.println("+-- /config");
                System.out.println("+-- /constantes");
                System.out.println("+-- /controller");
                System.out.println("+-- /dto");
                System.out.println("+-- /enums");
                System.out.println("+-- /exception");
                System.out.println("+-- /interfaces");
                System.out.println("+-- /repository");
                System.out.println("+-- /service");
                System.out.println("+-- /service/impl");
                throw new CodeGeneratorFolderStructureNotFound("Your project must have identical project folder structure in resource's folder");
            } else {
                log.info("Folder {} is OK!", folder );
            }
        }
    }

    private boolean folderExists(String file) {
        Path parent = Paths.get(file).getParent();
//        System.out.println("Parent => " + parent.toAbsolutePath().toString());
        return parent != null && Files.isDirectory(parent);
    }
    private String absolutePathFromRelative(String file) {
        Path parent = Paths.get(file).getParent();
        String absolutePath = parent.toAbsolutePath().toString();
        int pos = absolutePath.indexOf(parent.toString());
        if(pos > -1) {
            return absolutePath.substring(0, pos);
        } else {
            return "";
        }
    }
    private String changeTagsUsing(String content, CodeGeneratorDTO codegen) {
        return changeTagsUsing(content,codegen, null);
    }

    private String changeTagsUsing(String content, CodeGeneratorDTO codegen, FieldDescriptor field) {
        log.debug("changeTagsUsing :: field in working => {}", gson.toJson(field));
        final String schemap = codegen.getSchema() != null && !codegen.getSchema().isEmpty()
                ? codegen.getSchema().concat(".")
                : ""  ;
        final FieldDescriptor fieldStatus = codegen.getFieldDescriptorList()
                .stream()
                .filter(fieldItem -> fieldItem.getFieldName().equals("status"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("changeTagsUsing :: There is no field [status] definded in Model " + codegen.getBaseClass()));
        final FieldDescriptor fieldPK = codegen.getFieldDescriptorList()
                .stream()
                .filter(FieldDescriptor::isPrimaryKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("changeTagsUsing :: There is no Primary Key definded in Model " + codegen.getBaseClass()));
        log.debug("changeTagsUsing :: PK => {}", gson.toJson(fieldPK));

        String newContent = content.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass());
        newContent = newContent.replaceAll(CodeGeneratorTags.STATUS_CAMPO.getTag(), fieldStatus.getFieldTableName());
        newContent = newContent.replaceAll(CodeGeneratorTags.PROJETO.getTag(), codegen.getProject());
        newContent = newContent.replaceAll(CodeGeneratorTags.AUTHOR.getTag(), codegen.getBaseClass());
        newContent = newContent.replaceAll(CodeGeneratorTags.AUTOR.getTag(), codegen.getBaseClass());
        newContent = newContent.replaceAll(CodeGeneratorTags.NOW.getTag(), Calendar.getInstance().getTime().toString());
        newContent = newContent.replaceAll(CodeGeneratorTags.AGORA.getTag(), Calendar.getInstance().getTime().toString());
        newContent = newContent.replaceAll(CodeGeneratorTags.BASE_PACKAGE.getTag(), codegen.getBasePackage());
        newContent = newContent.replaceAll(CodeGeneratorTags.TABELA.getTag(), codegen.getTableName());
        newContent = newContent.replaceAll(CodeGeneratorTags.TABLE.getTag(), codegen.getTableName());
        newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMAP.getTag(), schemap);
        newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMA.getTag(), codegen.getSchema());
        newContent = newContent.replaceAll(CodeGeneratorTags.PK.getTag(), fieldPK.getFieldTableName());
        newContent = newContent.replaceAll(CodeGeneratorTags.PKDTO.getTag(), fieldPK.getFieldReferenceInDto());
        newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS_LOWER.getTag(), codegen.getBaseClass().toLowerCase());
        newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS_UPPER.getTag(), codegen.getBaseClass().toUpperCase());
        newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_LONG.getTag(),
                Long.valueOf(StringUtility.getRandomCodeNumber(5)) + "L");
        if(field != null) {
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass());
            newContent = newContent.replaceAll(CodeGeneratorTags.REGEX_VALIDATION.getTag(), getRegexValidationAnnotation(field.getRegexValidation()));
            newContent = newContent.replaceAll(CodeGeneratorTags.STATUS_CAMPO.getTag(), fieldStatus.getFieldTableName());
            newContent = newContent.replaceAll(CodeGeneratorTags.PROJETO.getTag(), codegen.getProject());
            newContent = newContent.replaceAll(CodeGeneratorTags.AUTHOR.getTag(), codegen.getBaseClass());
            newContent = newContent.replaceAll(CodeGeneratorTags.AUTOR.getTag(), codegen.getBaseClass());
            newContent = newContent.replaceAll(CodeGeneratorTags.NOW.getTag(), Calendar.getInstance().getTime().toString());
            newContent = newContent.replaceAll(CodeGeneratorTags.AGORA.getTag(), Calendar.getInstance().getTime().toString());
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_PACKAGE.getTag(), codegen.getBasePackage());
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS_LOWER.getTag(), codegen.getBaseClass().toLowerCase());
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS_UPPER.getTag(), codegen.getBaseClass().toUpperCase());
            newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMAP.getTag(), schemap);
            newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMA.getTag(), codegen.getSchema());
            newContent = newContent.replaceAll(CodeGeneratorTags.TABELA.getTag(), codegen.getTableName());
            newContent = newContent.replaceAll(CodeGeneratorTags.TABLE.getTag(), codegen.getTableName());
            newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO.getTag(), field.getFieldTableName());
            newContent = newContent.replaceAll(CodeGeneratorTags.FIELD.getTag(), field.getFieldTableName());
            newContent = newContent.replaceAll(CodeGeneratorTags.TYPE.getTag(), field.getFieldType());
            newContent = newContent.replaceAll(CodeGeneratorTags.ADTO.getTag(), capitalize(field.getFieldReferenceInDto()));
            newContent = newContent.replaceAll(CodeGeneratorTags.UDTO.getTag(), field.getFieldReferenceInDto().toUpperCase());
            newContent = newContent.replaceAll(CodeGeneratorTags.CCDTO.getTag(), camelCase(field.getFieldReferenceInDto()));
            newContent = newContent.replaceAll(CodeGeneratorTags.DTO.getTag(), field.getFieldReferenceInDto());
            newContent = newContent.replaceAll(CodeGeneratorTags.CONVERT_FROM_FIELDTYPE_TO_FINDBYFILTERTYPE.getTag(),
                    (FieldTypeConverterEnum.fromFieldType(field.getFieldType())).getFindByFilterType());
            newContent = newContent.replaceAll(CodeGeneratorTags.CONVERT_FROM_FIELDTYPE_TO_VALUEOF.getTag(),
                    (FieldTypeConverterEnum.fromFieldType(field.getFieldType())).getValueOf());
            newContent = newContent.replaceAll(CodeGeneratorTags.CONVERT_FROM_FIELDTYPE_TO_CASTVALUE.getTag(),
                    (FieldTypeConverterEnum.fromFieldType(field.getFieldType())).getCastType());
            switch (FieldTypeEnum.fromType(field.getFieldType())) {
                case Long:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            Long.valueOf(StringUtility.getRandomCodeNumber(5)) + "L");
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                            Long.valueOf(StringUtility.getRandomCodeNumber(5)) + "L");
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                            Long.valueOf(StringUtility.getRandomCodeNumber(5)) + "L");
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            field.getFieldTableName());
                    break;
                case Integer:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            String.valueOf(Integer.valueOf(StringUtility.getRandomCodeNumber(5))));
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                            String.valueOf(Integer.valueOf(StringUtility.getRandomCodeNumber(5))));
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                            String.valueOf(Integer.valueOf(StringUtility.getRandomCodeNumber(5))));
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            field.getFieldTableName());
                    break;

                case Date:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            "Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant())");
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                            '"' +"2025-10-07" + '"');
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                            "Date.from(LocalDate.of(2027,7,25).atStartOfDay(ZoneId.systemDefault()).toInstant())");
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            "to_char(" + field.getFieldTableName() + ", 'YYYY-MM-DD')");
                    break;


                case Double:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            Long.valueOf(StringUtility.getRandomCodeNumber(4))+ ".0");
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                            Long.valueOf(StringUtility.getRandomCodeNumber(4))+ ".0");
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                            Long.valueOf(StringUtility.getRandomCodeNumber(4))+ ".0");
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            field.getFieldTableName());
                    break;


                case UUID:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            "UUID.fromString(" +
                                    '"' + UUID.randomUUID().toString() + '"'+
                                    ")"
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                            "UUID.fromString(" +
                                    '"' + UUID.randomUUID().toString() + '"'+
                                    ")"
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                                    "UUID.fromString(" +
                                    '"' + UUID.randomUUID().toString() + '"'+
                            ")"
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            field.getFieldTableName());
                    break;

                case String:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            '"' +
                                    StringUtility.getRandomCodeNumberUpperLower(50)+
                                    '"'
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                        '"' +
                                StringUtility.getRandomCodeNumberUpperLower(50)+
                                '"'
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                            '"' +
                            StringUtility.getRandomCodeNumberUpperLower(50)+
                                    '"'
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            field.getFieldTableName());
                    break;

                case LocalDate:
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT.getTag(),
                            "LocalDate.of(" + Long.valueOf(StringUtility.getRandomCodeNumber(4)) + ","
                             + StringUtility.getRandomMonth() + ","
                             + StringUtility.getRandomDay() + ")"
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_FILTER.getTag(),
                            '"' +"2025-10-07" + '"'
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.MAGIC_CONTENT_SECONDARY.getTag(),
                            "LocalDate.of(" + Long.valueOf(StringUtility.getRandomCodeNumber(4)) + ","
                             + StringUtility.getRandomMonth() + ","
                             + StringUtility.getRandomDay() + ")"
                    );
                    newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO_DATE_FIX.getTag(),
                            "to_char(" + field.getFieldTableName() + ", 'YYYY-MM-DD')");
                    break;

            }
        }
        return newContent;
    }

    private String getRegexValidationAnnotation(String regexValidation) {
        if(regexValidation.isEmpty()) return "";
        return "@RegexValidation(regex = " + String.valueOf('"') + regexValidation + String.valueOf('"') +")";
    }

    private String camelCase(String field) {
        return field.toUpperCase();
    }
    private String getContent(String content, String defaultContent) {
        return content.isBlank()
                ? defaultContent
                : content;
    }

    private <T> List<FieldDescriptor> processCodeGeneratorFieldDescriptorAnnotation(Class<T> inputClassModel) {
        Field[] fields = inputClassModel.getDeclaredFields();
        log.info("prepareCodeGeneratorFromModel :: Input class has {} Declared Fields", fields.length);

        if(!checkFieldsStatusDateCreatedDateUpdated(fields)) {
            throw new RuntimeException("Fields [status, dateCreated and dateUpdated] must be in " + inputClassModel.getName());
        }

        List<FieldDescriptor> fieldDescriptorList = new ArrayList<>();
        for( Field fieldItem : fields) {
            log.info("prepareCodeGeneratorFromModel :: field name -> {}",fieldItem.getName());

            CodeGeneratorFieldDescriptor codeGeneratorFieldDescriptor = fieldItem.getAnnotation(CodeGeneratorFieldDescriptor.class);
            if(codeGeneratorFieldDescriptor != null) {
                FieldDescriptor fieldDescriptor = getFieldDescriptor(fieldItem, codeGeneratorFieldDescriptor);
                fieldDescriptorList.add(fieldDescriptor);
            }
        }
        return fieldDescriptorList;
    }

    private String capitalize(String content) {
        if(content.isEmpty()) return content;
        return content.substring(0,1).toUpperCase().concat(content.substring(1));
    }

    private boolean checkFieldsStatusDateCreatedDateUpdated(Field[] fields) {
        boolean hasStatus = false;
        boolean hasDateCreated = false;
        boolean hasDateUpdated = false;
        for(Field fieldItem: fields) {
            if(fieldItem.getName().equals("status")) {
                hasStatus = true;
            }
            if(fieldItem.getName().equals("dateCreated")) {
                hasDateCreated = true;
            }
            if(fieldItem.getName().equals("dateUpdated")) {
                hasDateUpdated = true;
            }
        }
        return hasStatus && hasDateCreated && hasDateUpdated;
    }

    private StringBuffer checkForIncludeLines(String line, String includeTag) {
        int posInclude = line.indexOf(includeTag);
        if(posInclude == 0) {
            log.info("checkForIncludeLines :: checking out for extra commands");
            String includeFile;
            int pos = line.indexOf("&&");
            if( pos == -1) {
                includeFile = line.substring(includeTag.length()).trim();
            } else {
                includeFile = line.substring(includeTag.length(), pos).trim();
            }
            log.info("checkForIncludeLines :: get include lines at -> {}", includeFile);
            return readFile(includeFile);
        }
        return null;
    }

    private static FieldDescriptor getFieldDescriptor(Field fieldItem, CodeGeneratorFieldDescriptor codeGeneratorFieldDescriptor) {
        FieldDescriptor fieldDescriptor = new FieldDescriptor();
        fieldDescriptor.setFieldName(fieldItem.getName());
        String typeName = fieldItem.getType().getTypeName();
        fieldDescriptor.setFieldType(typeName.substring(typeName.lastIndexOf(".")+1));
        fieldDescriptor.setFieldReferenceInDto(codeGeneratorFieldDescriptor.fieldReferenceInDto().isEmpty()
                ?  fieldItem.getName()
                : codeGeneratorFieldDescriptor.fieldReferenceInDto());
        fieldDescriptor.setFieldDescription(codeGeneratorFieldDescriptor.fieldDescription());
        fieldDescriptor.setRegexValidation(codeGeneratorFieldDescriptor.regexValidation());

        log.info("getFieldDescriptor :: collecting info for @Column Annotation for field -> {}", fieldItem.getName());
        Column columnAnnotation = fieldItem.getAnnotation(Column.class);
        if(columnAnnotation != null) {
            fieldDescriptor.setUnique(columnAnnotation.unique());
            fieldDescriptor.setNullable(columnAnnotation.nullable());
            fieldDescriptor.setInsertable(columnAnnotation.insertable());
            fieldDescriptor.setUpdatable(columnAnnotation.updatable());
            fieldDescriptor.setLength(columnAnnotation.length());
            fieldDescriptor.setPrecision(columnAnnotation.precision());
            fieldDescriptor.setScale(columnAnnotation.scale());
            fieldDescriptor.setFieldTableName(! columnAnnotation.name().isEmpty()
                    ? columnAnnotation.name()
                    : fieldItem.getName()
            );
        } else {
            throw new RuntimeException("field " + fieldItem.getName() + " hasn't been annoteded with @Column");
        }

        Id idAnnotation = fieldItem.getAnnotation(Id.class);
        if( idAnnotation != null) {
            fieldDescriptor.setIsPrimaryKey(true);
        }
        return fieldDescriptor;
    }


    protected void flushCode(List<WritableCode> codes) {
        for(WritableCode writableCode: codes) {
            log.info("flushCode :: is flushing source code -> {}.{}",
                    writableCode.getTargetFileCodeInfo().getTargetPathFile(),
                    writableCode.getTargetFileCodeInfo().getTargetExtension());

            try {
                writeCode(writableCode.getSourceCode(),
                        writableCode.getCodeGenerator(),
                        writableCode.getTargetFileCodeInfo().getTargetPathFile(),
                        writableCode.getTargetFileCodeInfo().getTargetExtension());
            } catch (IOException e) {
                throw new CommoditieBaseException(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

    }

    private void writeCode(StringBuffer code, CodeGeneratorDTO codegen, String filename, String extension) throws IOException {

        String outputFileName = codegen.getOutputDir() + "/" + codegen.getBasePackageSlash() +filename + "." + extension;
        FileOutputStream fos = new FileOutputStream(outputFileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        try (fos; bos; DataOutputStream outStream = new DataOutputStream(bos)) {
            outStream.write(code.toString().getBytes(StandardCharsets.UTF_8));
        }
    }


}
