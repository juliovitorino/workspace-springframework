package br.com.jcv.codegen.codegenerator.controller;

import br.com.jcv.codegen.codegenerator.dto.GenericErrorResponse;
import br.com.jcv.codegen.codegenerator.exception.CommoditieBaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
public class ApiUsuarioControllerAdvice {
    @ResponseBody
    @ExceptionHandler({CommoditieBaseException.class})
    public ResponseEntity<GenericErrorResponse<Map<String, ? extends Serializable>>> handle(CommoditieBaseException e) {
        List<Map<String, ? extends Serializable>> stackTraceList = Stream
                .of(e.getStackTrace())
                .filter(filter -> filter.getClassName().startsWith("br.com.jcv.codegen.codegenerator"))
                .map(map -> Map.of("class", map.getClassName(), "lineNumber", map.getLineNumber()))
                .collect(Collectors.toList());

        var response =
                new GenericErrorResponse<Map<String, ? extends Serializable>>(
                        e.getHttpStatus().value() ,
                        e.getMensagemResponse().getMensagem(),
                        new ArrayList<>(),
                        Map.of(),
                        e.getMensagemResponse().getMsgcode());
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

}
