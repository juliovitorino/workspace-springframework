package com.jwick.continental.deathagreement.controller.v1.business.bet;

import com.jwick.continental.deathagreement.bulder.BetRequestBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

public class BetBusinessTest {

    @InjectMocks private CreateBetService createBetService;

    @BeforeEach
    public void setup() {
        createBetService = new CreateBetServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreateBetWithSuccess() {
        // cenario
        UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
        BetRequest betRequest = BetRequestBuilder.newBetRequest().now();

        MockedStatic<UUID> uuidStatic = Mockito.mockStatic(UUID.class);
        uuidStatic.when(UUID::randomUUID).thenReturn(uuidMock);

        Mockito.when(null).thenReturn(null);

        //ação
        createBetService.execute(uuidMock, betRequest);

        // valiadção
        Assertions.fail("Not implemented yet.");
    }
}
