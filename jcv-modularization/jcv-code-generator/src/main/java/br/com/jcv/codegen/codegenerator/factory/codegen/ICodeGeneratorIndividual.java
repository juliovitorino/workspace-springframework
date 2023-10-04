package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.dto.WritableCode;

public interface ICodeGeneratorIndividual {
    <Input> WritableCode generate(Class<Input> inputClassModel);
}
