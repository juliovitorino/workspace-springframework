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


package com.jwick.continental.deathagreement.exception;

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
