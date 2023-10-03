/*
Copyright <YEAR> <COPYRIGHT HOLDER>

This software is Open Source and is under MIT license agreement

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.
*/


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

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
