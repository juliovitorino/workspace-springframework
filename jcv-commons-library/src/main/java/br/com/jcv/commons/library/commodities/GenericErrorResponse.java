package br.com.jcv.commons.library.commodities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class GenericErrorResponse<Type> {
    private UUID processId;
    private Integer statusCode;
    private String message;
    private Type errors;
    private String msgcode;
    private String url;
}

