package br.com.jcv.commons.library.commodities.exception;


import br.com.jcv.commons.library.commodities.MensagemResponse;
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
