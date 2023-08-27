package br.com.jcv.commons.library.connection.restconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestConsumerMicroserviceA extends AbstractRestConsumer implements IRestConsumer<String>{

    @Autowired private RestTemplate restTemplate;
    private final static String URL_REQUEST_MAPPING = "http://localhost:8081/v1/microservicea/";
    @Override
    public String executeGet(String urlGetMapping, Class<String> responseType) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL_REQUEST_MAPPING+urlGetMapping,responseType);
        return responseEntity.getBody();
    }
}
