package br.com.jcv.commons.library.connection.restconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestConsumerFactory {

    @Autowired private RestConsumerMicroserviceA restConsumerMicroserviceA;
    @Autowired private RestConsumerMicroserviceB restConsumerMicroserviceB;

    public IRestConsumer getInstanceMicroserviceA() {
        return restConsumerMicroserviceA;
    }
    public IRestConsumer getInstanceMicroserviceB() {
        return restConsumerMicroserviceB;
    }
}
