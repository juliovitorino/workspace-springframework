package br.com.jcv.microservice.microserviceb.service;

import br.com.jcv.exchange.dto.book.BookDTO;

public interface BookService {
    BookDTO findBook(Long id);
}
