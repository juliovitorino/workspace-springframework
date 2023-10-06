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


package br.com.jcv.commons.library.commodities.analyser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAnalyser implements IAnalyser<String> {

    protected static final String DEFAULT_MESSAGE = "FWL-0000:O conteúdo ${input} não está no formato correto. O formato correto deve ser ${format}";
    protected static final String DEFAULT_MESSAGE_URL = "FWL-0000:O conteúdo ${input} não está no formato correto";
    protected static final String DEFAULT_MESSAGE_NULL = "FWL-0000:O conteúdo ${goal} não pode estar nulo.";

    protected String getErrorMessage(String input, String format) {
        String response = DEFAULT_MESSAGE;
        response = response.replaceAll("\\$\\{input\\}", input);
        response = response.replaceAll("\\$\\{format\\}", format);
        return response;
    }
    protected String getUrlErrorMessage(String input) {
        String response = DEFAULT_MESSAGE_URL;
        response = response.replaceAll("\\$\\{url\\}", input);
        return response;
    }
    protected String getErrorNullMessage(String goal) {
        String response = DEFAULT_MESSAGE_NULL;
        response = response.replaceAll("\\$\\{goal\\}", goal);
        return response;
    }

}
