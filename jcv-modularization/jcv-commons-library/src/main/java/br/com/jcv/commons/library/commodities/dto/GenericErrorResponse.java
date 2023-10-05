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


package br.com.jcv.commons.library.commodities.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GenericErrorResponse<Type> {
    private final Integer statusCode;
    private final String message;
    private final List<Map<String, ? extends Serializable>> stackTraceList;
    private final Type errors;
    private final String msgcode;

    public GenericErrorResponse(
            Integer statusCode,
            String message,
            List<Map<String, ? extends Serializable>> stackTraceList,
            Type errors,
            String msgcode
    ) {
        this.statusCode = statusCode;
        this.message = message;
        this.stackTraceList = stackTraceList;
        this.errors = errors;
        this.msgcode = msgcode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public List<Map<String, ? extends Serializable>> getStackTraceList() {
        return Collections.unmodifiableList(stackTraceList);
    }

    public Type getErrors() {
        return errors;
    }

    public String getMsgcode() { return msgcode; }
}
