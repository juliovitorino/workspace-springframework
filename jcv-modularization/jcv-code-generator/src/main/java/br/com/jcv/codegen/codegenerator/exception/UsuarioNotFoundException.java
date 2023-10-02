package br.com.jcv.codegen.codegenerator.exception;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import java.util.Map;


public class UsuarioNotFoundException extends CommoditieBaseException {
    public UsuarioNotFoundException(String input, HttpStatus httpStatus, String msgcode, Map<String,String> mapParams) {
        super(input, httpStatus, msgcode, mapParams);
    }

    public UsuarioNotFoundException(String input, HttpStatus httpStatus, String msgcode) {
        this(input, httpStatus, msgcode, null);
    }

    public UsuarioNotFoundException(String input, HttpStatus httpStatus) {
      this(input, httpStatus, null, new HashMap<>());
    }

    public UsuarioNotFoundException(String input, int httpStatus) {
      this(input, HttpStatus.valueOf(httpStatus), null, new HashMap<>());
    }
}
