package br.com.jcv.commons.library.helper;

import br.com.jcv.commons.library.commodities.dto.MensagemResponse;

public class MensagemResponseHelperService {

    private MensagemResponseHelperService() {}

    public static MensagemResponse getInstance(String msgcode, String message){
        return MensagemResponse.builder()
                .msgcode(msgcode)
                .mensagem(message)
                .build();
    }
}
