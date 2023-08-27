package br.com.jcv.commons.library.connection.restconsumer;

public interface IRestConsumer<T> {
    T executeGet(String urlGetMapping, Class<T> responseType);
}
