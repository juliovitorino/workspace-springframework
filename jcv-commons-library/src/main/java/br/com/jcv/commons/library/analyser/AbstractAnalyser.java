package br.com.jcv.commons.library.analyser;

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





















































































































































