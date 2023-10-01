package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.dto.CodeGeneratorDTO;

public interface ICodeGenerator {
    <Input> StringBuffer generate(Class<Input> inputClassModel);
}
