package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateTime;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.builder.BetDTOBuilder;
import com.jwick.continental.deathagreement.builder.BetObjectBuilder;
import com.jwick.continental.deathagreement.builder.BetRequestBuilder;
import com.jwick.continental.deathagreement.builder.UserPunterDTOBuilder;
import com.jwick.continental.deathagreement.config.ContinentalConfig;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.exception.BetCouldntMadeinThePastException;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.exception.BtcAddressNotBelongThisUserException;
import com.jwick.continental.deathagreement.exception.NextBetMustBeDoubleValueOfPreviousBetException;
import com.jwick.continental.deathagreement.exception.NumberOfBetsAchieveMaximumException;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.service.UserPunterService;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class CreateBetBusinessTest {

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
    private UserPunterService userServiceMock;
    @Mock
    private ContinentalConfig configMock;
    @InjectMocks private CreateBetService createBetService;
    @InjectMocks private ConfirmBetBusinessService confirmBetBusinessService;
    private final ArrayList<BetDTO> betsMock  = Mockito.mock(ArrayList.class);
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
    public void shouldReturnNumberOfBetsAchieveMaximumExceptionWhenNewBetExceed() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2023,12,26);
        Date deathDateBetMockDate = Date.from(deathDateBetMockLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date allowDate = DateUtility.addDays(dateTimeMock.getToday(),30);

        UserPunterDTO existingPunter = UserPunterDTOBuilder.newUserPunterDTOTestBuilder().now();

        BetRequest betRequest = BetRequestBuilder.newBetRequestTestBuilder()
                .bet(1.0)
                .nickname("Fulano de tal 1")
                .btcAddress("5526245hdfh346456ghdf4654646")
                .whoUUID(uuidMock)
                .deathDateBet(deathDateBetMockLocalDate)
                .now();
        BetObjectDTO targetMock = BetObjectBuilder.newBetObjectTestBuilder().now();
        List<BetDTO> betsMock = provideBetList();

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(deathDateBetMockLocalDate)).thenReturn(deathDateBetMockDate);
        dateUtilityMockedStatic.when(() -> DateUtility.addDays(deathDateBetMockDate,30)).thenReturn(allowDate);

        Mockito.when(userServiceMock.findUserPunterByBtcAddressAndStatus(betRequest.getBtcAddress())).thenReturn(existingPunter);
        Mockito.when(betObjectServiceMock.findBetObjectByExternalUUIDAndStatus(betRequest.getWhoUUID())).thenReturn(targetMock);

        Mockito.when(configMock.getMaximumBetsInMonth()).thenReturn((long) betsMock.size());
        Mockito.when(betServiceMock.findAllBetByIdPunterAndIdBetObjectAndYearMonthAndStatus(
                existingPunter.getId(),
                targetMock.getId(),
                betRequest.getDeathDateBet().getYear(),
                betRequest.getDeathDateBet().getMonthValue(),
                "A"
        )).thenReturn(betsMock);
        // action
        NumberOfBetsAchieveMaximumException exception = Assertions.assertThrows(NumberOfBetsAchieveMaximumException.class,
                ()->createBetService.execute(processId,betRequest));

        // validate
        final String expected = "Maximum bets in the same month has been achieved. Your total bets is [0-9]+ and maximum allowed is [0-9]+";
        Pattern pattern = Pattern.compile(expected);
        Matcher matcher = pattern.matcher(exception.getMessage());
        Assertions.assertTrue(matcher.matches());

    }
    @Test
    public void shouldReturnNextBetMustBeDoubleValueOfPreviousBetExceptionWhenNewBet() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2023,12,26);
        Date deathDateBetMockDate = Date.from(deathDateBetMockLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date allowDate = DateUtility.addDays(dateTimeMock.getToday(),30);

        UserPunterDTO existingPunter = UserPunterDTOBuilder.newUserPunterDTOTestBuilder().now();

        BetRequest betRequest = BetRequestBuilder.newBetRequestTestBuilder()
                .bet(1.0)
                .nickname("Fulano de tal 1")
                .btcAddress("5526245hdfh346456ghdf4654646")
                .whoUUID(uuidMock)
                .deathDateBet(deathDateBetMockLocalDate)
                .now();
        BetObjectDTO targetMock = BetObjectBuilder.newBetObjectTestBuilder().now();
        List<BetDTO> betsMock = new ArrayList<>();
        betsMock.add(BetDTOBuilder.newBetDTOTestBuilder().bet(5000.0).now());

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(deathDateBetMockLocalDate)).thenReturn(deathDateBetMockDate);
        dateUtilityMockedStatic.when(() -> DateUtility.addDays(deathDateBetMockDate,30)).thenReturn(allowDate);

        Mockito.when(userServiceMock.findUserPunterByBtcAddressAndStatus(betRequest.getBtcAddress())).thenReturn(existingPunter);
        Mockito.when(betObjectServiceMock.findBetObjectByExternalUUIDAndStatus(betRequest.getWhoUUID())).thenReturn(targetMock);

        Mockito.when(configMock.getMaximumBetsInMonth()).thenReturn(20L);
        Mockito.when(betServiceMock.findAllBetByIdPunterAndIdBetObjectAndYearMonthAndStatus(
                existingPunter.getId(),
                targetMock.getId(),
                betRequest.getDeathDateBet().getYear(),
                betRequest.getDeathDateBet().getMonthValue(),
                "A"
        )).thenReturn(betsMock);
        // action
        NextBetMustBeDoubleValueOfPreviousBetException exception = Assertions.assertThrows(NextBetMustBeDoubleValueOfPreviousBetException.class,
                ()->createBetService.execute(processId,betRequest));

        // validate
        final String expected = "Your bet must be double value from the previous bet in the same month.";
        Assertions.assertEquals(expected, exception.getMessage());

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
    public void shouldReturnBetObjectNotFoundException() {
        // scenario
        uuidMockedStatic.when(UUID::randomUUID).thenReturn(uuidMock);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2040,9,13);
        Date deathDateBetMockDate = Date.from(deathDateBetMockLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);

        UUID processId = UUID.fromString(PROCESS_ID);
        UserPunterDTO punter = UserPunterDTOBuilder.newUserPunterDTOTestBuilder()
                .id(1L)
                .now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(punter.getNickname())
                .btcAddress(punter.getBtcAddress())
                .bet(180.0)
                .whoUUID(UUID.fromString("7bed3f75-ff6a-4f87-901a-2c300469165a"))
                .deathDateBet(deathDateBetMockLocalDate)
                .now();

        Mockito.when(userServiceMock.findUserPunterByBtcAddressAndStatus(betRequestMock.getBtcAddress())).thenReturn(punter);
        Mockito.when(userServiceMock.findUserPunterByNicknameAndStatus(betRequestMock.getNickname())).thenReturn(punter);
        Mockito.when(userServiceMock.findById(punter.getId())).thenReturn(punter);

        Mockito.when(betObjectServiceMock.findBetObjectByExternalUUIDAndStatus(betRequestMock.getWhoUUID())).thenThrow(
                new BetObjectNotFoundException("BetObject not found", HttpStatus.BAD_REQUEST)
        );

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                () -> createBetService.execute(processId, betRequestMock));

        // validate
        Assertions.assertEquals("Bet Object does not exist", exception.getMessage());
        Assertions.assertEquals(400, exception.getHttpStatus().value());
    }
    @Test
    public void shouldReturnBtcAddressNotBelongThisUserException() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        LocalDate deathDateBetMockLocalDate = LocalDate.of(2040,9,13);
        Date deathDateBetMockDate = Date.from(deathDateBetMockLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        UserPunterDTO user1 = UserPunterDTOBuilder.newUserPunterDTOTestBuilder()
                .nickname("Jane Doe")
                .btcAddress(BTC_ADDRESS)
                .now();
        UserPunterDTO user2 = UserPunterDTOBuilder.newUserPunterDTOTestBuilder()
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
        Mockito.when(userServiceMock.findUserPunterByBtcAddressAndStatus(user1.getBtcAddress())).thenReturn(user2);


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
        Date deathDateBetMockDate = Date.from(deathDateBetMockLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateBetMockDate)).thenReturn(1);

        UserPunterDTO userMock = UserPunterDTOBuilder.newUserPunterDTOTestBuilder().now();
        BetObjectDTO targetMock = BetObjectBuilder.newBetObjectTestBuilder().now();
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .nickname(userMock.getNickname())
                .btcAddress(userMock.getBtcAddress())
                .bet(250.0)
                .whoUUID(targetMock.getExternalUUID())
                .deathDateBet(deathDateBetMockLocalDate)
                .now();
        UserPunterDTO userToSaveMock = UserPunterDTOBuilder.newUserPunterDTOTestBuilder()
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
        Mockito.when(userServiceMock.findUserPunterByBtcAddressAndStatus(betRequestMock.getBtcAddress())).thenReturn(userMock);
        Mockito.when(userServiceMock.findUserPunterByNicknameAndStatus(betRequestMock.getNickname())).thenReturn(userMock);
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

    public static List<BetDTO> provideBetList() {
        List<BetDTO> bets = new ArrayList<>();
        bets.add(Mockito.mock(BetDTO.class));
        bets.add(Mockito.mock(BetDTO.class));
        bets.add(Mockito.mock(BetDTO.class));
        bets.add(Mockito.mock(BetDTO.class));
        bets.add(Mockito.mock(BetDTO.class));
        bets.add(Mockito.mock(BetDTO.class));
        return bets;
    }

}
