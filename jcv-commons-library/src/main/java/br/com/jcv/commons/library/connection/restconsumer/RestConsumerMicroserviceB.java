package br.com.jcv.commons.library.connection.restconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestConsumerMicroserviceB extends AbstractRestConsumer implements IRestConsumer<String>{
    private final static String URL_REQUEST_MAPPING = "http://localhost:8082/v1/microserviceb/";
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public String executeGet(String urlGetMapping, Class<String> responseType) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL_REQUEST_MAPPING+urlGetMapping,responseType);
        return responseEntity.getBody();
    }
}
