package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.CodeGeneratorTags;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.FieldDescriptor;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.util.FileCopyUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public abstract class AbstractCodeGenerator {

    private String basePackage;
    @Autowired protected Gson gson;
    @Autowired protected ResourceLoader resourceLoader;

    protected StringBuffer readTemplateModeOne(String template){
        StringBuffer sb = new StringBuffer();
        Resource fileTemplateResource = resourceLoader.getResource(template);
        try {
            InputStream isFileTemplate = new FileInputStream(fileTemplateResource.getFile());
            byte[] bdata = FileCopyUtils.copyToByteArray(isFileTemplate);
            sb.append(new String(bdata));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }
    protected void readTemplate(String template, StringBuffer sb, CodeGeneratorDTO codeGeneratorDTO){
        Resource fileTemplateResource = resourceLoader.getResource("classpath:" + template);
        try {
            Scanner scannerFileTemplate = new Scanner(fileTemplateResource.getFile());
            while( scannerFileTemplate.hasNextLine()) {
                String line = changeTagsUsing(scannerFileTemplate.nextLine(),codeGeneratorDTO);
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
            }
            scannerFileTemplate.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    protected void writeCode(StringBuffer code, CodeGeneratorDTO codegen, String filename, String extension){
        String OutputFilename = codegen.getOutputDir() + codegen.getBaseClass() + filename + "." + extension;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(OutputFilename);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(code.toString());
            outStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
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
        String newContent = content.replaceAll(CodeGeneratorTags.BASE_CLASS.getTag(), codegen.getBaseClass())
                .replaceAll(CodeGeneratorTags.BASE_PACKAGE.getTag(), codegen.getBasePackage());
        return newContent;
    }


    private String getContent(String content, String defaultContent) {
        return content.isBlank()
                ? defaultContent
                : content;
    }

    private <T> List<FieldDescriptor> processCodeGeneratorFieldDescriptorAnnotation(Class<T> inputClassModel) {
        Field[] fields = inputClassModel.getDeclaredFields();
        log.info("prepareCodeGeneratorFromModel :: Input class has {} Declared Fields", fields.length);

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
        }

        Id idAnnotation = fieldItem.getAnnotation(Id.class);
        if( idAnnotation != null) {
            fieldDescriptor.setIsPrimaryKey(true);
        }
        return fieldDescriptor;
    }
}
