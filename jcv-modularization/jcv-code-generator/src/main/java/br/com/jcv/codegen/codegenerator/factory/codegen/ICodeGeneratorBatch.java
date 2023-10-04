package br.com.jcv.codegen.codegenerator.factory.codegen;

import br.com.jcv.codegen.codegenerator.dto.WritableCode;

import java.util.List;

public interface ICodeGeneratorBatch {
    <Input> List<WritableCode> generate(Class<Input> inputClassModel);
    void flushCode(List<WritableCode> codes);
}
