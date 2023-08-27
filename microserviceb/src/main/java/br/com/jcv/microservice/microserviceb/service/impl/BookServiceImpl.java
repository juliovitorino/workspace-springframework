package br.com.jcv.microservice.microserviceb.service.impl;

import br.com.jcv.exchange.dto.book.BookDTO;
import br.com.jcv.microservice.microserviceb.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Override
    public BookDTO findBook(Long id) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Bitcoin Red Pill");
        bookDTO.setEditora("Sextante");
        bookDTO.setIsin("1234567890");
        return bookDTO;
    }
}
