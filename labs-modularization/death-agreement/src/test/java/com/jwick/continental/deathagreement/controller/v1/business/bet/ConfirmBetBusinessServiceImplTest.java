package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateTime;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.builder.BetDTOBuilder;
import com.jwick.continental.deathagreement.builder.BetObjectDTOBuilder;
import com.jwick.continental.deathagreement.controller.v1.business.bet.ConfirmBetBusinessService;
import com.jwick.continental.deathagreement.controller.v1.business.bet.ConfirmBetBusinessServiceImpl;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.exception.PendingDeathDateDueException;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.service.BetService;
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
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class ConfirmBetBusinessServiceImplTest {
    private static final String PROCESS_ID = "a98de2c9-ea34-448c-9110-eafd93cc8d48";
    public static final String BTC_ADDRESS = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh";
    public static final String CONTINENTAL_BTC_ADDRESS = "bc1qupua5993486zf5g5g00e6nax4w5pd4p0ulx4v0";
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;
    private final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);
    @Mock
    private BetService betServiceMock;
    @Mock
    private BetObjectService betObjectServiceMock;
    @InjectMocks private ConfirmBetBusinessService confirmBetBusinessService;
    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));
        Mockito.when(dateTimeMock.now()).thenReturn(DateUtility.getDate(12,10,2023));

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
    public void shouldReturnPendingDeathDateDueExceptionWhenTryConfirmDueDeathDate() {
        //scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        LocalDate dueDeathDateMock = LocalDate.of(2005,10,29);
        Date dateMock = Date.from(dueDeathDateMock.atStartOfDay(ZoneId.systemDefault()).toInstant());
        BetDTO betPendingMock = BetDTOBuilder.newBetDTOTestBuilder()
                .deathDate(dueDeathDateMock)
                .status("P")
                .now();

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), dateMock)).thenReturn(-1);
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(dueDeathDateMock)).thenReturn(dateMock);
        Mockito.when(betServiceMock.findBetByTicketAndStatus(uuidMock, "P")).thenReturn(betPendingMock);

        // action
        PendingDeathDateDueException exception = Assertions.assertThrows(PendingDeathDateDueException.class,
                ()->confirmBetBusinessService.execute(processId, uuidMock));

        // validate
        Assertions.assertEquals("Pending death date is due",exception.getMessage());
        Assertions.assertEquals(400, exception.getHttpStatus().value());


    }
    @Test
    public void shouldConfirmBetWhenConfirmTicketFund() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        LocalDate deathDateMock = LocalDate.of(2030,10,29);
        Date dateMock = Date.from(deathDateMock.atStartOfDay(ZoneId.systemDefault()).toInstant());
        BetDTO betMock = BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(3L)
                .idBetObject(5L)
                .bet(1.205)
                .ticket(uuidMock)
                .status("P")
                .deathDate(deathDateMock)
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

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), dateMock)).thenReturn(1);
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(deathDateMock)).thenReturn(dateMock);
        Mockito.when(betServiceMock.findBetByTicketAndStatus(uuidMock, "P")).thenReturn(betMock);

        Mockito.when(betObjectServiceMock.findById(Mockito.anyLong())).thenReturn(targetMock);
        Mockito.when(betObjectServiceMock.salvar(targetMock)).thenReturn(targetUpdatedMock);

        // action
        Boolean executed = confirmBetBusinessService.execute(processId, uuidMock);

        // validate
        Assertions.assertTrue(executed);
    }

}
