package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.enums.CodeGeneratorTags;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.FieldDescriptor;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

@Slf4j
public abstract class AbstractCodeGenerator {
    public static final String TARGET_EXTENSION_JAVA = "java";
    public static final String TARGET_EXTENSION_XML = "xml";
    private String basePackage;
    @Autowired protected Gson gson;
    @Autowired protected ResourceLoader resourceLoader;

    protected void readTemplate(String template, StringBuffer sb, CodeGeneratorDTO codeGeneratorDTO){
        Resource fileTemplateResource = resourceLoader.getResource("classpath:" + template);
        try {
            Scanner scannerFileTemplate = new Scanner(fileTemplateResource.getFile());
            while( scannerFileTemplate.hasNextLine()) {
                final String line = scannerFileTemplate.nextLine();
                final StringBuffer sbInclude = checkForIncludeLines(line);
                if (sbInclude == null) {
                    String lineChanged = changeTagsUsing(line,codeGeneratorDTO);
                    sb.append(lineChanged);
                } else {
                    for(FieldDescriptor fieldItem: codeGeneratorDTO.getFieldDescriptorList()) {
                        String includeBlock = changeTagsUsing(sbInclude.toString(),codeGeneratorDTO, fieldItem);
                        sb.append(includeBlock);
                    }
                }
                sb.append(System.getProperty("line.separator"));
            }
            scannerFileTemplate.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected StringBuffer readFile(String filePath) {
        StringBuffer sb = new StringBuffer();
        Resource fileTemplateResource = resourceLoader.getResource("classpath:" + filePath);
        try {
            Scanner scannerFileTemplate = new Scanner(fileTemplateResource.getFile());
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
            codegen.setTableName(inputClassModel.getName());
            codegen.setSchema(null);
        }

        List<FieldDescriptor> fieldDescriptors = processCodeGeneratorFieldDescriptorAnnotation(inputClassModel);
        codegen.setFieldDescriptorList(fieldDescriptors);
        return codegen;
    }

    private String changeTagsUsing(String content, CodeGeneratorDTO codegen) {
//        String newContent = content.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass())
//                .replaceAll(CodeGeneratorTags.BASE_PACKAGE.getTag(), codegen.getBasePackage())
//                .replaceAll(CodeGeneratorTags.BASE_CLASS_LOWER.getTag(), codegen.getBaseClass().toLowerCase());
        return changeTagsUsing(content,codegen, null);
    }

    private String changeTagsUsing(String content, CodeGeneratorDTO codegen, FieldDescriptor field) {
        log.info("changeTagsUsing :: field in working => {}", gson.toJson(field));
        log.info("changeTagsUsing :: content => {}", content);
        final String schemap = codegen.getSchema() != null && !codegen.getSchema().isEmpty()
                ? codegen.getSchema().concat(".")
                : ""  ;
        final FieldDescriptor fieldPK = codegen.getFieldDescriptorList()
                .stream()
                .filter(FieldDescriptor::isPrimaryKey)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("changeTagsUsing :: There is no Primary Key definded in Model " + codegen.getBaseClass()));
        log.info("changeTagsUsing :: PK => {}", gson.toJson(fieldPK));

        String newContent = content.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass());
        newContent = newContent.replaceAll(CodeGeneratorTags.AUTHOR.getTag(), codegen.getBaseClass());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.AUTOR.getTag(), codegen.getBaseClass());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.NOW.getTag(), Calendar.getInstance().getTime().toString());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.AGORA.getTag(), Calendar.getInstance().getTime().toString());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.BASE_PACKAGE.getTag(), codegen.getBasePackage());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.TABELA.getTag(), codegen.getTableName());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.TABLE.getTag(), codegen.getTableName());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMAP.getTag(), schemap);
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMA.getTag(), codegen.getSchema());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.PK.getTag(), fieldPK.getFieldTableName());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.PKDTO.getTag(), fieldPK.getFieldReferenceInDto());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS_LOWER.getTag(), codegen.getBaseClass().toLowerCase());
//        log.info("changeTagsUsing :: newContent => {}", newContent);
        if(field != null) {
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.AUTHOR.getTag(), codegen.getBaseClass());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.AUTOR.getTag(), codegen.getBaseClass());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.NOW.getTag(), Calendar.getInstance().getTime().toString());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.AGORA.getTag(), Calendar.getInstance().getTime().toString());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_PACKAGE.getTag(), codegen.getBasePackage());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.BASE_CLASS_LOWER.getTag(), codegen.getBaseClass().toLowerCase());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMAP.getTag(), schemap);
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.SCHEMA.getTag(), codegen.getSchema());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.TABELA.getTag(), codegen.getTableName());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.TABLE.getTag(), codegen.getTableName());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.CAMPO.getTag(), field.getFieldTableName());
//            log.info("changeTagsUsing :: {} => {}", CodeGeneratorTags.CAMPO.getTag(), field.getFieldTableName());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.FIELD.getTag(), field.getFieldTableName());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.TYPE.getTag(), field.getFieldType());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.ADTO.getTag(), capitalize(field.getFieldReferenceInDto()));
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.UDTO.getTag(), field.getFieldReferenceInDto().toUpperCase());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.CCDTO.getTag(), camelCase(field.getFieldReferenceInDto()));
//            log.info("changeTagsUsing :: {} => {}", CodeGeneratorTags.CCDTO.getTag(), camelCase(field.getFieldReferenceInDto()));
//            log.info("changeTagsUsing :: newContent => {}", newContent);
            newContent = newContent.replaceAll(CodeGeneratorTags.DTO.getTag(), field.getFieldReferenceInDto());
//            log.info("changeTagsUsing :: {} => {}", CodeGeneratorTags.DTO.getTag(), field.getFieldReferenceInDto());
//            log.info("changeTagsUsing :: newContent => {}", newContent);
        }
        return newContent;
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

    private StringBuffer checkForIncludeLines(String line) {
        final String INCLUDE = "#include ";
        int posInclude = line.indexOf(INCLUDE);
        if(posInclude == 0) {
            final String includeFile = line.substring(INCLUDE.length());
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
}
