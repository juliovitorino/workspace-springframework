package br.com.jcv.commons.library.exception;

import br.com.jcv.commons.library.commodities.exception.CommoditieBaseException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class AnalyserException extends CommoditieBaseException {
  public AnalyserException(
      String input, HttpStatus httpStatus, String msgcode, Map<String, String> mapParams) {
    super(input, httpStatus, msgcode, mapParams);
  }

  public AnalyserException(String input, HttpStatus httpStatus, String msgcode) {
    this(input, httpStatus, msgcode, new HashMap<>());
  }
  public AnalyserException(String input, HttpStatus httpStatus) {
    this(input, httpStatus, null, new HashMap<>());
  }

  public AnalyserException(String input, int httpStatus) {
    this(input, HttpStatus.valueOf(httpStatus), null, new HashMap<>());
  }

}
