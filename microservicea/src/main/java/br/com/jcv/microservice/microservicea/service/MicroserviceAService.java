package br.com.jcv.microservice.microservicea.service;

import br.com.jcv.exchange.dto.book.BookDTO;

public interface MicroserviceAService {
    String getWelcomeFromB();
    BookDTO callBookOnB(BookDTO bookDTO);
}
