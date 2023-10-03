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


package br.com.jcv.codegen.codegenerator.exception;


import br.com.jcv.codegen.codegenerator.dto.MensagemResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class CommoditieBaseException extends RuntimeException{
    public static final String DEFAULT_SIGNER_UNKNOWN_CODE = "SGNR-0000";
    private final HttpStatus httpStatus;
    private final String msgcode;
    private final MensagemResponse mensagemResponse;

    public CommoditieBaseException(String input, HttpStatus httpStatus, String msgcode, Map<String, String> mapParams) {
        super(input);
        this.msgcode = msgcode;
        this.httpStatus = httpStatus;
        this.mensagemResponse = new MensagemResponse();
        this.mensagemResponse.setMsgcode(Objects.isNull(msgcode) ? DEFAULT_SIGNER_UNKNOWN_CODE : msgcode);
        this.mensagemResponse.setMensagem(input);

        Pattern er = Pattern.compile("^[A-Z]{3}-[0-9]{4}:.*");
        Matcher result = er.matcher(input);
        if( result.matches()) {
            int pos = input.indexOf(":");
            if(pos > -1) {
                this.mensagemResponse.setMsgcode(input.substring(0,pos-1));
                this.mensagemResponse.setMensagem(input.substring(pos+1));
            }
        }

        Pattern mapRegex = Pattern.compile("\\$\\{[A-Za-z0-9_\\-]+\\}");
        Matcher mapRegexResult = mapRegex.matcher(this.mensagemResponse.getMensagem());
        if(mapRegexResult.matches()) {
            for (Map.Entry entry : mapParams.entrySet()) {
                this.mensagemResponse.setMensagem(this.mensagemResponse.getMensagem().replaceAll(entry.getKey().toString(), entry.getValue().toString()));
            }
        }
    }

    public CommoditieBaseException(String input, HttpStatus httpStatus, String msgcode) {
        this(input, httpStatus, msgcode, new HashMap<>());
    }
    public CommoditieBaseException(String input, HttpStatus httpStatus) {
        this(input, httpStatus, null, new HashMap<>());
    }

    public CommoditieBaseException(String input, int httpStatus) {
        this(input, HttpStatus.valueOf(httpStatus), null, new HashMap<>());
    }

}
