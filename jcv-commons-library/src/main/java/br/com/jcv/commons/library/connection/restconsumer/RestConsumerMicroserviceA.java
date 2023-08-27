package br.com.jcv.commons.library.connection.restconsumer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RestConsumerMicroserviceA extends AbstractRestConsumer {
    private final static String URL_REQUEST_MAPPING = "http://localhost:8081/v1/microservicea/";
    @Override
    public <T> T executeGet(UUID processId, String url, Class<T> classType) {
        ResponseEntity<T> response = null;
        try {
            response = (ResponseEntity<T>) super.executeGet(processId,URL_REQUEST_MAPPING + url,classType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response.getBody();
    }
}
