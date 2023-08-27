package br.com.jcv.commons.library.connection.restconsumer;

import java.util.UUID;

public interface IRestConsumer {
    <T> T executeGet(UUID processId,final String url, final Class<T> classType) throws Exception;
    <T> T executePost(UUID processId, final String url, final Object payLoad, final Class<T> classType);


}
