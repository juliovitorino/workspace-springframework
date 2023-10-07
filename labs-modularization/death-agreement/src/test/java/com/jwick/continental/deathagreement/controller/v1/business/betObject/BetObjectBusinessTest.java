package com.jwick.continental.deathagreement.controller.v1.business.betObject;

import com.jwick.continental.deathagreement.bulder.BetObjectDTOBuilder;
import com.jwick.continental.deathagreement.bulder.BetObjectRequestBuilder;
import com.jwick.continental.deathagreement.bulder.BetObjectResponseBuilder;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.service.BetObjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

public class BetObjectBusinessTest {

    @Mock
    private BetObjectService betObjectServiceMock;
    @InjectMocks
    private BetObjectBusinessService betObjectBusinessService = new BetObjectBusinessServiceImpl();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveBetObjectWithSuccess() {
        // cenario
        UUID uuidmock = UUID.fromString("c744d321-d44a-443b-a1ee-fe17af267677");

        BetObjectRequest betObjectRequest = BetObjectRequestBuilder.newBetObjectRequest().now();
        BetObjectResponse betObjectResponse = BetObjectResponseBuilder.newBetObjectResponse().now();
        BetObjectDTO betObjectDTO = BetObjectDTOBuilder.newBetObjectDTO().now();
        BetObjectDTO betObjectDTOResponse = BetObjectDTOBuilder.newBetObjectDTO()
                .externalUUID(uuidmock)
                .now();

        MockedStatic<UUID> uuid = Mockito.mockStatic(UUID.class);
        uuid.when(UUID::randomUUID).thenReturn(uuidmock);
        Mockito.when(betObjectServiceMock.salvar(betObjectDTO)).thenReturn(betObjectDTOResponse);

        // acao
        BetObjectResponse executed = betObjectBusinessService.execute(uuidmock, betObjectRequest);

        // validação
        Assertions.assertEquals(betObjectResponse.getWhoUUID().toString(),"c744d321-d44a-443b-a1ee-fe17af267677");
    }
}
