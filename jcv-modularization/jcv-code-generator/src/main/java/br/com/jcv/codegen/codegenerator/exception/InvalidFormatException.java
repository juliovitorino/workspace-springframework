package br.com.jcv.codegen.codegenerator.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class InvalidFormatException extends CommoditieBaseException {
  public InvalidFormatException(
      String input, HttpStatus httpStatus, String msgcode, Map<String, String> mapParams) {
    super(input, httpStatus, msgcode, mapParams);
  }

  public InvalidFormatException(String input, HttpStatus httpStatus, String msgcode) {
    this(input, httpStatus, msgcode, new HashMap<>());
  }
  public InvalidFormatException(String input, HttpStatus httpStatus) {
    this(input, httpStatus, null, new HashMap<>());
  }

  public InvalidFormatException(String input, int httpStatus) {
    this(input, HttpStatus.valueOf(httpStatus), null, new HashMap<>());
  }

}
