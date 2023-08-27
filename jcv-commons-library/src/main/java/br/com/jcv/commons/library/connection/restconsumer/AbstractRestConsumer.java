package br.com.jcv.commons.library.connection.restconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

public abstract class AbstractRestConsumer implements IRestConsumer {

    @Autowired protected RestTemplate restTemplate;

    @Override
    public <T> T executeGet(UUID processId, String url, Class<T> classType) throws Exception {
        ResponseEntity<T> response = null;
        response = restTemplate.getForEntity(url,classType);
        return (T) response;
    }

    public <T> T executePost(UUID processId, final String url, final Object payLoad, final Class<T> classType) {
        return restTemplate.postForObject(url, payLoad, classType);
    }


}
