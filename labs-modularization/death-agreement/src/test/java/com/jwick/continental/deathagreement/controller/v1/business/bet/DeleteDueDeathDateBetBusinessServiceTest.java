package com.jwick.continental.deathagreement.controller.v1.business.bet;

import br.com.jcv.commons.library.commodities.enums.GenericStatusEnums;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.bulder.BetDTOBuilder;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.service.DateTime;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class DeleteDueDeathDateBetBusinessServiceTest {
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    public static final String BTC_ADDRESS = "bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh";
    private static final String PROCESS_ID = "a98de2c9-ea34-448c-9110-eafd93cc8d48";
    @Mock private BetService betServiceMock;
    @InjectMocks private DeleteDueDeathDateBetBusinessService deleteDueDeathDateBetBusinessService;
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);
    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));

        deleteDueDeathDateBetBusinessService = new DeleteDueDeathDateBetBusinessServiceImpl();
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
    public void shouldDeleteDueDeathDateBet() {
        // scenario
        UUID processId = UUID.fromString(PROCESS_ID);
        List<BetDTO> bets = provideListOfBetsDueDeathDate();
        Mockito.when(betServiceMock.findAllByStatus(GenericStatusEnums.ATIVO.getShortValue())).thenReturn(bets);

        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), toDate(bets.get(0).getDeathDate()))).thenReturn(-1);
        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), toDate(bets.get(1).getDeathDate()))).thenReturn(-1);
        dateUtilityMockedStatic.when(() -> DateUtility.compare(dateTimeMock.getToday(), toDate(bets.get(2).getDeathDate()))).thenReturn(1);

        dateUtilityMockedStatic.when(() -> DateUtility.getDate(bets.get(0).getDeathDate())).thenReturn(toDate(bets.get(0).getDeathDate()));
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(bets.get(1).getDeathDate())).thenReturn(toDate(bets.get(1).getDeathDate()));
        dateUtilityMockedStatic.when(() -> DateUtility.getDate(bets.get(2).getDeathDate())).thenReturn(toDate(bets.get(2).getDeathDate()));

        // action
        Boolean executed = deleteDueDeathDateBetBusinessService.execute(processId, null);

        // validate
        Assertions.assertTrue(executed);
    }
    private java.util.Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private static List<BetDTO> provideListOfBetsDueDeathDate() {
        // scenario
        return List.of(
            BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(3L)
                .idBetObject(5L)
                .bet(1.205)
                .ticket(uuidMock)
                .status("P")
                .deathDate(LocalDate.of(2000,10,29))
                .bitcoinAddress(BTC_ADDRESS)
                .now(),
            BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(3L)
                .idBetObject(5L)
                .bet(1.205)
                .ticket(uuidMock)
                .status("P")
                .deathDate(LocalDate.of(2010,11,29))
                .bitcoinAddress(BTC_ADDRESS)
                .now(),
            BetDTOBuilder.newBetDTOTestBuilder()
                .idPunter(3L)
                .idBetObject(5L)
                .bet(1.205)
                .ticket(uuidMock)
                .status("P")
                .deathDate(LocalDate.of(2029,5,29))
                .bitcoinAddress(BTC_ADDRESS)
                .now()
        );
    }
}
