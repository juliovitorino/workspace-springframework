package br.com.jcv.exchange.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDTO implements Serializable {
    private Long id;
    private String editora;
    private String title;
    private String isin;
}
