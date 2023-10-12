package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.bulder.BetDTOBuilder;
import com.jwick.continental.deathagreement.bulder.BetObjectBuilder;
import com.jwick.continental.deathagreement.bulder.BetObjectDTOBuilder;
import com.jwick.continental.deathagreement.bulder.BetRequestBuilder;
import com.jwick.continental.deathagreement.bulder.UserDTOBuilder;
import com.jwick.continental.deathagreement.config.ContinentalConfig;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.dto.UserDTO;
import com.jwick.continental.deathagreement.exception.BetCouldntMadeinThePastException;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.exception.BtcAddressNotBelongThisUserException;
import com.jwick.continental.deathagreement.exception.PendingBetWaitingTransferFundsException;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.service.DateTime;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetBusinessTest {

    private static final String PROCESS_ID = "a98de2c9-ea34-448c-9110-eafd93cc8d48";
    public static final String BTC_ADDRESS = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh";
    public static final String CONTINENTAL_BTC_ADDRESS = "bc1qupua5993486zf5g5g00e6nax4w5pd4p0ulx4v0";
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;
    private final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    @Mock
    private BetService betServiceMock;
    @Mock
    private BetObjectService betObjectServiceMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private ContinentalConfig configMock;
    @InjectMocks private CreateBetService createBetService;
    @InjectMocks private ConfirmBetBusinessService confirmBetBusinessService;
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);
    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));
        Mockito.when(dateTimeMock.now()).thenReturn(DateUtility.getDate(12,10,2023));

        createBetService = new CreateBetServiceImpl();
        confirmBetBusinessService = new ConfirmBetBusinessServiceImpl();
        MockitoAnnotations.initMocks(this);

        uuidMockedStatic = Mockito.mockStatic(UUID.class, Mockito.RETURNS_DEEP_STUBS);
        dateUtilityMockedStatic = Mockito.mockStatic(DateUtility.class, Mockito.RETURNS_DEEP_STUBS);
    }

    @AfterAll
    public void tearDown() {
        uuidMockedStatic.close();
        dateUtilityMockedStatic.close();
    }

    @Test
    public void shouldReceiveBetCouldntMadeinThePastExceptionWhenTryBetInThePast() throws ParseException {
        // scenario
        Date deathDateMock = sdfYMD.parse("2000-09-13");
        LocalDate pastLocalDate = LocalDate.of(2000,9,13);

        UUID processId = UUID.fromString(PROCESS_ID);
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .bet(180.0)
                .whoUUID(UUID.fromString("7bed3f75-ff6a-4f87-901a-2c300469165a"))
                .deathDateBet(pastLocalDate)
                .now();

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateMock)).thenReturn(-1);
        // action
        BetCouldntMadeinThePastException exception = Assertions.assertThrows(BetCouldntMadeinThePastException.class,
                () -> createBetService.execute(processId,betRequestMock)
        );

        // validate
        Assertions.assertEquals("You must to do your bet to the future", exception.getMessage());
    }
    @Test
    public void shouldConfirmBetWhenConfirmTicketFund() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        BetDTO betMock = BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(3L)
                .idBetObject(5L)
                .bet(1.205)
                .ticket(uuidMock)
                .status("P")
                .deathDate(DateUtility.getLocalDate(2030,12,10))
                .bitcoinAddress(BTC_ADDRESS)
                .now();
        BetObjectDTO targetMock = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .who("Muleke Travesso")
                .externalUUID(UUID.fromString("afd8c05b-002d-4918-9897-6f150234d420"))
                .jackpotPending(betMock.getBet())
                .jackpot(0.0)
                .now();
        BetObjectDTO targetUpdatedMock = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .who("Muleke Travesso")
                .externalUUID(UUID.fromString("afd8c05b-002d-4918-9897-6f150234d420"))
                .jackpot(betMock.getBet())
                .jackpotPending(targetMock.getJackpotPending()-betMock.getBet())
                .now();

        Mockito.when(betServiceMock.findBetByTicketAndStatus(uuidMock, "P")).thenReturn(betMock);

        Mockito.when(betObjectServiceMock.findById(Mockito.anyLong())).thenReturn(targetMock);
        Mockito.when(betObjectServiceMock.salvar(targetMock)).thenReturn(targetUpdatedMock);

        // action
        Boolean executed = confirmBetBusinessService.execute(processId, uuidMock);

        // validate
        Assertions.assertTrue(executed);
    }

    @Test
    public void shouldReturnBetObjectNotFoundException() {
        // scenario
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 13);
        cal.set(Calendar.MONTH, 9 - 1);
        cal.set(Calendar.YEAR, 2040);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2040,9,13);
        Date deathDateBetMockDate = cal.getTime();
        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);

        UUID processId = UUID.fromString(PROCESS_ID);
        UserDTO punter = UserDTOBuilder.newUserTestBuilder()
                .id(1L)
                .now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(punter.getNickname())
                .btcAddress(punter.getBtcAddress())
                .bet(180.0)
                .whoUUID(UUID.fromString("7bed3f75-ff6a-4f87-901a-2c300469165a"))
                .deathDateBet(deathDateBetMockLocalDate)
                .now();

        Mockito.when(userServiceMock.findUserByBtcAddressAndStatus(betRequestMock.getBtcAddress())).thenReturn(punter);
        Mockito.when(userServiceMock.findUserByNicknameAndStatus(betRequestMock.getNickname())).thenReturn(punter);
        Mockito.when(userServiceMock.findById(punter.getId())).thenReturn(punter);

        Mockito.when(betObjectServiceMock.findBetObjectByExternalUUIDAndStatus(betRequestMock.getWhoUUID())).thenReturn(null);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                () -> createBetService.execute(processId, betRequestMock));

        // validate
        Assertions.assertEquals("Bet Object does not exist", exception.getMessage());
    }
    @Test
    public void shouldReturnPendingBetWaitingTransferFundsException() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2040,9,13);
        Date deathDateBetMockDate = DateUtility.getDate(13,9,2040);
        UserDTO punterMock = UserDTOBuilder.newUserTestBuilder()
                .id(1L)
                .now();
        BetObjectDTO targetMock = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .externalUUID(UUID.fromString("6fa33a6f-6f7a-4edf-90b8-c0d226ade640"))
                .now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(punterMock.getNickname())
                .btcAddress(punterMock.getBtcAddress())
                .bet(180.0)
                .whoUUID(targetMock.getExternalUUID())
                .deathDateBet(deathDateBetMockLocalDate)
                .now();
        BetDTO pendingBetMock = BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(punterMock.getId())
                .idBetObject(targetMock.getId())
                .bet(betRequestMock.getBet())
                .status("P")
                .now();
        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);

        Mockito.when(userServiceMock.findUserByBtcAddressAndStatus(betRequestMock.getBtcAddress())).thenReturn(punterMock);
        Mockito.when(userServiceMock.findUserByNicknameAndStatus(betRequestMock.getNickname())).thenReturn(punterMock);
        Mockito.when(userServiceMock.findById(punterMock.getId())).thenReturn(punterMock);

        Mockito.when(betObjectServiceMock.findBetObjectByExternalUUIDAndStatus(betRequestMock.getWhoUUID())).thenReturn(targetMock);

        Mockito.when(betServiceMock
                .findBetByIdPunterAndIdBetObjectAndStatus(punterMock.getId(), targetMock.getId(), "P")).thenReturn(pendingBetMock);

        // action
        PendingBetWaitingTransferFundsException exception =
                Assertions.assertThrows(PendingBetWaitingTransferFundsException.class,
                () -> createBetService.execute(processId, betRequestMock));

        // validate
        Assertions.assertEquals("Bet is pending and waiting confirmation", exception.getMessage());
    }
    @Test
    public void shouldCaptureExceptionForSameBtcAddressForDifferentNickname() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2040,9,13);
        Date deathDateBetMockDate = DateUtility.getDate(13,9,2040);

        UserDTO user1 = UserDTOBuilder.newUserTestBuilder()
                .nickname("Jane Doe")
                .btcAddress(BTC_ADDRESS)
                .now();
        UserDTO user2 = UserDTOBuilder.newUserTestBuilder()
                .nickname("Nicolas gauger")
                .btcAddress(BTC_ADDRESS)
                .now();
        BetObjectDTO betObjectDTOMock = BetObjectBuilder.newBetObjectTestBuilder().now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(user1.getNickname())
                .btcAddress(user1.getBtcAddress())
                .bet(250.0)
                .whoUUID(betObjectDTOMock.getExternalUUID())
                .deathDateBet(deathDateBetMockLocalDate)
                .now();

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);
        Mockito.when(userServiceMock.findUserByBtcAddressAndStatus(user1.getBtcAddress())).thenReturn(user2);


        // action
        BtcAddressNotBelongThisUserException exception = Assertions.assertThrows(BtcAddressNotBelongThisUserException.class,
                () -> createBetService.execute(processId, betRequestMock));

        // validate
        Assertions.assertEquals("Other user is using this btc address", exception.getMessage());
    }
    @Test
    public void shouldCreateBetWithSuccess() {
        // scenario
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2040,9,13);
        Date deathDateBetMockDate = DateUtility.getDate(13,9,2040);

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);

        UserDTO userMock = UserDTOBuilder.newUserTestBuilder().now();
        BetObjectDTO targetMock = BetObjectBuilder.newBetObjectTestBuilder().now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(userMock.getNickname())
                .btcAddress(userMock.getBtcAddress())
                .bet(250.0)
                .whoUUID(targetMock.getExternalUUID())
                .deathDateBet(deathDateBetMockLocalDate)
                .now();
        UserDTO userToSaveMock = UserDTOBuilder.newUserTestBuilder()
                .id(null)
                .btcAddress(betRequestMock.getBtcAddress())
                .nickname(betRequestMock.getNickname())
                .status(null)
                .now();
        BetDTO betMock = BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(userMock.getId())
                .idBetObject(targetMock.getId())
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

        Mockito.when(userServiceMock.findById(Mockito.anyLong())).thenReturn(userMock);
        Mockito.when(userServiceMock.findUserByBtcAddressAndStatus(betRequestMock.getBtcAddress())).thenReturn(userMock);
        Mockito.when(userServiceMock.findUserByNicknameAndStatus(betRequestMock.getNickname())).thenReturn(userMock);
        Mockito.when(userServiceMock.salvar(userToSaveMock)).thenReturn(userMock);
        Mockito.when(userServiceMock.updateStatusById(userMock.getId(), "A")).thenReturn(userMock);

        Mockito.when(betServiceMock.salvar(betMock)).thenReturn(betSavedMock);
        Mockito.when(betServiceMock
                .findBetByIdPunterAndIdBetObjectAndStatus(userMock.getId(),
                        targetMock.getId(),
                        GenericStatusEnums.PENDENTE.getShortValue())).thenThrow(new BetNotFoundException("Not Found", HttpStatus.NOT_FOUND));

        Mockito.when(betObjectServiceMock.findById(targetMock.getId())).thenReturn(targetMock);
        Mockito.when(betObjectServiceMock
                .findBetObjectByExternalUUIDAndStatus(betRequestMock.getWhoUUID()))
                    .thenReturn(targetMock);

        Mockito.when(configMock.getContinentalBtcAddress()).thenReturn(CONTINENTAL_BTC_ADDRESS);

        //action
        BetResponse executed = createBetService.execute(uuidMock, betRequestMock);

        // validate
        Assertions.assertEquals("3dc936e6-478e-4d21-b167-67dee8b730af", executed.getTicket().toString());
    }

}
