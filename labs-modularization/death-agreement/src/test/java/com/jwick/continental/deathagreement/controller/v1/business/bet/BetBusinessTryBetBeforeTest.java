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
import com.jwick.continental.deathagreement.exception.BetDeathDateInvalidException;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.exception.BtcAddressNotBelongThisUserException;
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
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetBusinessTryBetBeforeTest {

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
    public void yourBetDateMustBeAfter30DaysFromNow() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        LocalDate deathLocalDateMock = LocalDate.of(2023,10,16);
        Date deathDateMock = Date.from(deathLocalDateMock.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date allowBetDateFromMock = DateUtility.addDays(dateTimeMock.getToday(),30);
        BetRequest betRequestMock = BetRequestBuilder.newBetRequestTestBuilder()
                .bet(2.0)
                .btcAddress(BTC_ADDRESS)
                .whoUUID(uuidMock)
                .deathDateBet(deathLocalDateMock)
                .now();

        dateUtilityMockedStatic.when(() -> DateUtility.addDays(dateTimeMock.getToday(), 30)).thenReturn(allowBetDateFromMock);
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(deathLocalDateMock)).thenReturn(deathDateMock);
        dateUtilityMockedStatic.when(() -> DateUtility.compare(allowBetDateFromMock, deathDateMock)).thenReturn(-1);
        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), deathDateMock)).thenReturn(1);

        // action
        BetDeathDateInvalidException exception = Assertions.assertThrows(BetDeathDateInvalidException.class,
                ()->createBetService.execute(processId,betRequestMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains("Your bet date must be after"));
    }
}
