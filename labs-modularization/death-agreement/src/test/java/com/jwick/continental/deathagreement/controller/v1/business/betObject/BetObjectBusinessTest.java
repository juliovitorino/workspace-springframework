package com.jwick.continental.deathagreement.controller.v1.business.betObject;

import com.jwick.continental.deathagreement.bulder.BetObjectDTOBuilder;
import com.jwick.continental.deathagreement.bulder.BetObjectRequestBuilder;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.service.BetObjectService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetObjectBusinessTest {
    private static MockedStatic<UUID> uuidMockedStatic;
    private final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    @Mock
    private BetObjectService betObjectServiceMock;
    @InjectMocks
    private BetObjectBusinessService betObjectBusinessService;

    @BeforeAll
    public void setup() {
        uuidMockedStatic = Mockito.mockStatic(UUID.class, Mockito.RETURNS_DEEP_STUBS);
        betObjectBusinessService = new BetObjectBusinessServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @AfterAll
    public void tearDown() {
        uuidMockedStatic.close();
    }
    @Test
    public void shouldSaveBetObjectWithSuccess() {
        // cenario
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);

        BetObjectRequest betObjectRequestMock = BetObjectRequestBuilder.newBetObjectRequestTestBuilder().now();
        BetObjectDTO betObjectToSave = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .externalUUID(uuidMock)
                .now();
        BetObjectDTO betObjectSaved = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .status("P")
                .externalUUID(uuidMock)
                .now();

        Mockito.when(betObjectServiceMock.salvar(betObjectToSave)).thenReturn(betObjectSaved);

        // acao
        BetObjectResponse executed = betObjectBusinessService.execute(uuidMock, betObjectRequestMock);

        // validação
        Assertions.assertEquals("3dc936e6-478e-4d21-b167-67dee8b730af",executed.getWhoUUID().toString());
    }
}
