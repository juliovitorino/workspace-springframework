package br.com.jcv.commons.library.integration;


import java.lang.reflect.Type;
import java.util.Map;

public interface IRestConsumer {
    <T> T executeGet(String targetURL, Type type);
    <T> T executeGet(String targetURL, Type type, String token, boolean isBearer);
    <T> T executeGet(String targetURL, Type type, String token);
    <T> T executeGet(String targetURL, Type type, String token, Map<String,String> params);

    void executePut(String targetURL);

    <T> T executePostWithSec(String targetURL, Class<T> type, String json);
    <T> T executePost(String targetURL, Class<T> type, String json);
    <T> T executePostWithBody(String targetURL, Class<T> type, String json);
    <T> T executePostWithHeaderParameter(String targetURL, Class<T> type, String json, Map<String,String> parameterHeader);

    void executeGetIntegration(String targetURL);

    String getFile(String targetUrl);

}
