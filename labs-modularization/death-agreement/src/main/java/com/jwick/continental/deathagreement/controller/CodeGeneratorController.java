package com.jwick.continental.deathagreement.controller;

import br.com.jcv.codegen.codegenerator.exception.CodeGeneratorFolderStructureNotFound;
import br.com.jcv.codegen.codegenerator.factory.codegen.ICodeGeneratorBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/death-agreement")
public class CodeGeneratorController {

    @Autowired
    private @Qualifier("CodeGeneratorMainStreamInstance") ICodeGeneratorBatch generatorMainStream;

        @GetMapping("/model")
    public ResponseEntity generateCode() {
        try {
            /*
            List<WritableCode> BetCodes = generatorMainStream.generate(Bet.class);
            generatorMainStream.flushCode(BetCodes);

            List<WritableCode> betObjectCodes = generatorMainStream.generate(BetObject.class);
            generatorMainStream.flushCode(betObjectCodes);

            List<WritableCode> userCodes = generatorMainStream.generate(User.class);
            generatorMainStream.flushCode(userCodes);
            */

            return ResponseEntity.ok().build();
        } catch(CodeGeneratorFolderStructureNotFound e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
