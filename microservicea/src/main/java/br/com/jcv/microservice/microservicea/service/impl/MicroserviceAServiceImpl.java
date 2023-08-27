package br.com.jcv.microservice.microservicea.service.impl;

import br.com.jcv.commons.library.connection.restconsumer.IRestConsumer;
import br.com.jcv.commons.library.connection.restconsumer.RestConsumerFactory;
import br.com.jcv.microservice.microservicea.service.MicroserviceAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MicroserviceAServiceImpl implements MicroserviceAService {

    @Autowired
    private RestConsumerFactory restConsumerFactory;

    @Override
    public String getWelcomeFromB() {
        IRestConsumer<String> iRestConsumer = restConsumerFactory.getInstanceMicroserviceB();
        return "Service A calling B -> " + iRestConsumer.executeGet("/welcome",String.class);

    }
}
