package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.bulder.BetDTOBuilder;
import com.jwick.continental.deathagreement.bulder.BetObjectBuilder;
import com.jwick.continental.deathagreement.bulder.BetRequestBuilder;
import com.jwick.continental.deathagreement.bulder.BetResponseBuilder;
import com.jwick.continental.deathagreement.bulder.UserDTOBuilder;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.dto.UserDTO;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.service.UserService;
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
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetBusinessTest {

    private static MockedStatic<UUID> uuidMockedStatic;
    private final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    @Mock
    private BetService betServiceMock;
    @Mock
    private BetObjectService betObjectServiceMock;
    @Mock
    private UserService userServiceMock;

    @InjectMocks private CreateBetService createBetService;

    @BeforeAll
    public void setup() {
        createBetService = new CreateBetServiceImpl();
        MockitoAnnotations.initMocks(this);

        uuidMockedStatic = Mockito.mockStatic(UUID.class, Mockito.RETURNS_DEEP_STUBS);
    }

    @AfterAll
    public void tearDown() {
        uuidMockedStatic.close();
    }

    @Test
    public void shouldCreateBetWithSuccess() {
        // cenario
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);

//        UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
        UserDTO userMock = UserDTOBuilder.newUserTestBuilder().now();
        BetObjectDTO betObjectDTOMock = BetObjectBuilder.newBetObjectTestBuilder().now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(userMock.getNickname())
                .btcAddress(userMock.getBtcAddress())
                .bet(250.0)
                .whoUUID(betObjectDTOMock.getExternalUUID())
                .deathDateBet(DateUtility.getDate(15,12,2030))
                .now();
        UserDTO userToSaveMock = UserDTOBuilder.newUserTestBuilder()
                .id(null)
                .btcAddress(betRequestMock.getBtcAddress())
                .nickname(betRequestMock.getNickname())
                .status(null)
                .now();
        BetResponse betResponseMock = BetResponseBuilder.newBetResponseTestBuilder().now();
        BetDTO betMock = BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(userMock.getId())
                .idBetObject(betObjectDTOMock.getId())
                .bet(betRequestMock.getBet())
                .ticket(uuidMock)
                .bitcoinAddress(userMock.getBtcAddress())
                .deathDate(betRequestMock.getDeathDateBet())
                .now();
        BetDTO betSavedMock = BetDTOBuilder.newBetDTOTestBuilder()
                .id(1L)
                .idPunter(betMock.getIdPunter())
                .idBetObject(betMock.getIdBetObject())
                .bet(betMock.getBet())
                .ticket(uuidMock)
                .bitcoinAddress(betMock.getBitcoinAddress())
                .deathDate(betMock.getDeathDate())
                .status("P")
                .now();
//
//        MockedStatic<UUID> uuidStatic = Mockito.mockStatic(UUID.class);
//        uuidStatic.when(UUID::randomUUID).thenReturn(uuidMock);


        Mockito.when(userServiceMock.findById(Mockito.anyLong())).thenReturn(userMock);
        Mockito.when(userServiceMock.findUserByBtcAddressAndStatus(betRequestMock.getBtcAddress())).thenReturn(userMock);
        Mockito.when(userServiceMock.findUserByNicknameAndStatus(betRequestMock.getNickname())).thenReturn(userMock);
        Mockito.when(userServiceMock.salvar(userToSaveMock)).thenReturn(userMock);
        Mockito.when(userServiceMock.updateStatusById(userMock.getId(), "A")).thenReturn(userMock);

        Mockito.when(betServiceMock.salvar(betMock)).thenReturn(betSavedMock);
        Mockito.when(betServiceMock
                .findBetByIdPunterAndIdBetObjectAndStatus(userMock.getId(),
                        betObjectDTOMock.getId(),
                        GenericStatusEnums.PENDENTE.getShortValue())).thenThrow(new BetNotFoundException("Not Found", HttpStatus.NOT_FOUND));

        Mockito.when(betObjectServiceMock
                .findBetObjectByExternalUUIDAndStatus(betRequestMock.getWhoUUID()))
                    .thenReturn(betObjectDTOMock);

        //ação
        BetResponse executed = createBetService.execute(uuidMock, betRequestMock);

        // valiadção
        Assertions.assertEquals("3dc936e6-478e-4d21-b167-67dee8b730af", executed.getTicket().toString());
    }
}
