package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorDescriptor;
import br.com.jcv.codegen.codegenerator.annotation.CodeGeneratorFieldDescriptor;
import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;
import br.com.jcv.codegen.codegenerator.dto.FieldDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component("CodeGeneratorBusinessService")
@Slf4j
public class CodeGeneratorBusinessService extends AbstractCodeGenerator implements ICodeGenerator {
    @Override
    public <Input> StringBuilder generate(Class<Input> inputClassModel) {
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        CodeGeneratorDTO<Input> codegen = super.prepareCodeGeneratorFromModel(inputClassModel);


        log.info("generate :: CodeGeneratorDTO has been prepared -> {}", gson.toJson(codegen));
        log.info("generate :: has been executed");
        return null;
    }

    public <Input> StringBuilder generate(Class<Input> inputClassModel, int i) {
        log.info("generate :: is reading {} attributes", inputClassModel.getClass().hashCode());

        CodeGeneratorDTO<Input> codegen = new CodeGeneratorDTO<>();

        CodeGeneratorDescriptor codeGeneratorDescriptor = inputClassModel.getAnnotation(CodeGeneratorDescriptor.class);
        Table tableAnnotation = inputClassModel.getAnnotation(Table.class);
        if(codeGeneratorDescriptor == null) {
            log.info("generate :: input class {} model hasn't been annoteded", inputClassModel.getClass().hashCode());
            return new StringBuilder();
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
        log.info("generate :: Input class has {} Declared Fields", fields.length);

        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();
        for( Field fieldItem : fields) {
            log.info("generate :: field name -> {}",fieldItem.getName());

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

        log.info("generate :: CodeGeneratorDTO has been prepared -> {}", gson.toJson(codegen));
        log.info("generate :: has been executed");
        return null;
    }
}
