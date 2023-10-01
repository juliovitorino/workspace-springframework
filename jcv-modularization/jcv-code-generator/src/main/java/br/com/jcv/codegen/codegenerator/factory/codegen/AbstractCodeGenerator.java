package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.FieldDescriptor;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractCodeGenerator {

    private String basePackage;
    @Autowired protected Gson gson;

    protected <Input> CodeGeneratorDTO prepareCodeGeneratorFromModel(Class<Input> inputClassModel) {

        CodeGeneratorDTO codegen = new CodeGeneratorDTO<>();

        final String modelPackage = inputClassModel.getPackage().getName();
        basePackage = modelPackage.substring(0, modelPackage.lastIndexOf("."));
        log.info("prepareCodeGeneratorFromModel :: base package is -> {}", basePackage);

        CodeGeneratorDescriptor codeGeneratorDescriptor = inputClassModel.getAnnotation(CodeGeneratorDescriptor.class);
        Table tableAnnotation =  inputClassModel.getAnnotation(Table.class);
        if(codeGeneratorDescriptor == null) {
            log.info("prepareCodeGeneratorFromModel :: input class {} model hasn't been annoteded", inputClassModel.getClass().hashCode());
            throw new RuntimeException("The input class model hasn't been annoteded");
        }

        codegen.setBasePackage(codeGeneratorDescriptor.basePackage());
        codegen.setProject(codeGeneratorDescriptor.project());
        codegen.setFullDescription(codeGeneratorDescriptor.fullDescription());

        if(tableAnnotation != null) {
            codegen.setTableName(tableAnnotation.name());
            codegen.setSchema(tableAnnotation.schema());
        } else {
            codegen.setTableName(inputClassModel.getName());
            codegen.setSchema(null);
        }

        Field[] fields = inputClassModel.getDeclaredFields();
        log.info("prepareCodeGeneratorFromModel :: Input class has {} Declared Fields", fields.length);

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        for( Field fieldItem : fields) {
            log.info("prepareCodeGeneratorFromModel :: field name -> {}",fieldItem.getName());

            CodeGeneratorFieldDescriptor codeGeneratorFieldDescriptor = fieldItem.getAnnotation(CodeGeneratorFieldDescriptor.class);
            if(codeGeneratorFieldDescriptor != null) {
                FieldDescriptor fieldDescriptor = new FieldDescriptor();
                fieldDescriptor.setName(fieldItem.getName());
                fieldDescriptor.setFieldType(fieldItem.getType().getTypeName());
                fieldDescriptor.setFieldDescription(codeGeneratorFieldDescriptor.fieldDescription());
                fieldDescriptor.setDtoFieldReference(codeGeneratorFieldDescriptor.dtoFieldReference().isBlank() ?  fieldItem.getName() : codeGeneratorFieldDescriptor.dtoFieldReference());
                fieldDescriptors.add(fieldDescriptor);
            }
        }
        codegen.setFieldDescriptorList(fieldDescriptors);
        return codegen;
    }
}
