package br.com.jcv.microservice.microservicea.service.impl;

import br.com.jcv.commons.library.connection.restconsumer.IRestConsumer;
import br.com.jcv.commons.library.connection.restconsumer.RestConsumerFactory;
import br.com.jcv.exchange.dto.book.BookDTO;
import br.com.jcv.microservice.microservicea.service.MicroserviceAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MicroserviceAServiceImpl implements MicroserviceAService {

    @Autowired
    private RestConsumerFactory restConsumerFactory;

    @Override
    public String getWelcomeFromB() {
        IRestConsumer iRestConsumer = restConsumerFactory.getInstanceMicroserviceB();
        UUID processId = UUID.randomUUID();
        try {
            return "Service A calling B -> " + iRestConsumer.executeGet(processId,"/welcome",String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public BookDTO callBookOnB(BookDTO bookDTO) {
        IRestConsumer iRestConsumer = restConsumerFactory.getInstanceMicroserviceB();
        UUID processId = UUID.randomUUID();
        return iRestConsumer.executePost(processId,"/book", bookDTO, BookDTO.class);
    }
}
