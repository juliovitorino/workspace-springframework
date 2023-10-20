package com.jwick.continental.deathagreement.service.impl;

import br.com.jcv.commons.library.utility.DateTime;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.builder.BetDTOBuilder;
import com.jwick.continental.deathagreement.builder.BetModelBuilder;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.repository.BetRepository;
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
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetServiceImplOriginalTest {
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;
    public static final String BET_NOTFOUND_WITH_ID = "Bet não encontrada com id = ";
    public static final String BET_NOTFOUND_WITH_TICKET = "Bet não encontrada com ticket = ";
    public static final String BET_NOTFOUND_WITH_ID_PUNTER = "Bet não encontrada com idPunter = ";
    public static final String BET_NOTFOUND_WITH_ID_BET_OBJECT = "Bet não encontrada com idBetObject = ";
    public static final String BET_NOTFOUND_WITH_BET = "Bet não encontrada com bet = ";
    public static final String BET_NOTFOUND_WITH_BITCOIN_ADDRESS = "Bet não encontrada com bitcoinAddress = ";
    public static final String BET_NOTFOUND_WITH_DEATH_DATE = "Bet não encontrada com deathDate = ";
    public static final String BET_NOTFOUND_WITH_DATECREATED = "Bet não encontrada com dateCreated = ";
    public static final String BET_NOTFOUND_WITH_DATEUPDATED = "Bet não encontrada com dateUpdated = ";
    @Mock
    private BetRepository betRepositoryMock;

    @InjectMocks
    private BetService betService;
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);
    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));
        Mockito.when(dateTimeMock.now()).thenReturn(DateUtility.getDate(12,10,2023));

        betService = new BetServiceImpl();
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
    public void showReturnListOfBetWhenAskedForFindAllByStatus() {
        // scenario
        List<Bet> listOfBetModelMock = new ArrayList<>();
        listOfBetModelMock.add(BetModelBuilder.newBetModelTestBuilder()
                .idBetObject(1L)
                .now());
        listOfBetModelMock.add(BetModelBuilder.newBetModelTestBuilder()
                .idBetObject(2L)
                .now());

        Mockito.when(betRepositoryMock.findAllByStatus("A")).thenReturn(listOfBetModelMock);

        // action
        List<BetDTO> listOfBets = betService.findAllByStatus("A");

        // validate
        Assertions.assertTrue(!listOfBets.isEmpty());
        Assertions.assertEquals(2, listOfBets.size());
    }
    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 321L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .id(idMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(899L);
        Mockito.when(betRepositoryMock.findById(899L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(idMock);

        // validate
        Assertions.assertEquals(idMock, result.getId());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 321L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(idMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Optional<Bet> betNonExistentMock = Optional.empty();
        Mockito.when(betRepositoryMock.findById(255L)).thenReturn(betNonExistentMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.updateStatusById(255L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldThrowBetNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Mockito.when(betRepositoryMock.findById(255L))
                .thenThrow(new BetNotFoundException(BET_NOTFOUND_WITH_ID,
                HttpStatus.NOT_FOUND,
                BET_NOTFOUND_WITH_ID ));

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.updateStatusById(255L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldReturnBetDTOAfterUpdateStatusById() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(255L)
                        .idPunter(1L)
                        .idBetObject(1L)
                        .status("X")
                        .bitcoinAddress("7d8dej39dldekd9dldkd")
                        .bet(20.0)
                        .ticket(uuidMock)
                        .deathDate(LocalDate.of(2025,9,16))
                        .now()
        );
        Bet betToSaveMock = betModelMock.orElse(null);
        Bet betSavedMck = BetModelBuilder.newBetModelTestBuilder()
                        .id(255L)
                        .idPunter(1L)
                        .idBetObject(1L)
                        .status("A")
                        .bitcoinAddress("7d8dej39dldekd9dldkd")
                        .bet(20.0)
                        .ticket(uuidMock)
                        .deathDate(LocalDate.of(2025,9,16))
                        .now();
        Mockito.when(betRepositoryMock.findById(255L)).thenReturn(betModelMock);
        Mockito.when(betRepositoryMock.save(betToSaveMock)).thenReturn(betSavedMck);

        // action
        BetDTO result = betService.updateStatusById(255L, "A");

        // validate
        Assertions.assertEquals("A",result.getStatus());

    }
    @Test
    public void shouldExecutePartialUpdateWithSucess() {
        // scenario
        Map<String, Object> mapBetDTOMock = new HashMap<>();
        mapBetDTOMock.put(BetConstantes.IDPUNTER,5L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,10L);
        mapBetDTOMock.put(BetConstantes.BET,52.3);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"9626ghdfgh92578hsdfkghdfksgh");
        mapBetDTOMock.put(BetConstantes.TICKET,uuidMock);
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(2020,5,10));

        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(1L)
                        .ticket(uuidMock)
                        .bitcoinAddress("udjdki")
                        .bet(26.0)
                        .idBetObject(20L)
                        .idPunter(34L)
                        .now()
        );

        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        boolean executed = betService.partialUpdate(1L, mapBetDTOMock);

        // validate
        Assertions.assertTrue(executed);

    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenTrySearchNotExistentId() {
        // scenario
        Map<String, Object> mapBetDTOMock = new HashMap<>();
        mapBetDTOMock.put(BetConstantes.IDPUNTER,5L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,10L);
        mapBetDTOMock.put(BetConstantes.BET,52.3);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"9626ghdfgh92578hsdfkghdfksgh");
        mapBetDTOMock.put(BetConstantes.TICKET,uuidMock);
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(2020,5,10));

        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.partialUpdate(1L, mapBetDTOMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains("Bet não encontrada com id = "));
        Assertions.assertEquals(404,exception.getHttpStatus().value());

    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdBetObjectById() {
        // scenario
        Long idBetObjectUpdateMock = 289L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateIdBetObjectById(420L, idBetObjectUpdateMock);

        // action
        betService.updateIdBetObjectById(420L, idBetObjectUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateIdBetObjectById(420L, idBetObjectUpdateMock);

    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(1L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(1L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(420L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(420L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByIdAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(520L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(520L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldSearchBetByIdAndReturnDTO() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .id(501L)
                .idBetObject(1L)
                .idPunter(2L)
                .deathDate(LocalDate.of(2025, 10, 12))
                .bet(260.0)
                .bitcoinAddress("546456grteyt90gh8fhdf098hfgh09dfg80fghdfgh")
                .ticket(uuidMock)
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .status("A")
                .now());
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findById(1L);

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldSearchBetByAnyNonExistenceIdAndReturnBetNotFoundException() {
        // scenario
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()-> betService.findById(-1000L));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldDeleteBetByIdWithSucess() {
        // scenario
        Optional<Bet> bet = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().id(1L).now());
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(bet);

        // action
        betService.delete(1L);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenTryDeleteNotExistenceBetShouldReturnBetNotFoundException() {
        // scenario
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(
                BetNotFoundException.class, () -> betService.delete(1L)
        );

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void ShouldSaveUpdateExistingBetWithSucess() {
        // scenario
        BetDTO betDTOMock = BetDTOBuilder.newBetDTOTestBuilder()
                .id(20L)
                .idBetObject(1L)
                .idPunter(100L)
                .bet(150.0)
                .deathDate(LocalDate.of(2050,12,1))
                .bitcoinAddress("bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh")
                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betMock = BetModelBuilder.newBetModelTestBuilder()
                .id(betDTOMock.getId())
                .idBetObject(betDTOMock.getIdBetObject())
                .idPunter(betDTOMock.getIdPunter())
                .bet(betDTOMock.getBet())
                .deathDate(betDTOMock.getDeathDate())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betSavedMock = BetModelBuilder.newBetModelTestBuilder()
                .id(betDTOMock.getId())
                .idBetObject(betMock.getIdBetObject())
                .idPunter(betMock.getIdPunter())
                .bet(betMock.getBet())
                .bitcoinAddress(betMock.getBitcoinAddress())
                .deathDate(betDTOMock.getDeathDate())
                .ticket(uuidMock)
                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betRepositoryMock.save(betMock)).thenReturn(betSavedMock);

        // action
        BetDTO betSaved = betService.salvar(betDTOMock);

        // validate
        Assertions.assertInstanceOf(BetDTO.class, betSaved);
        Assertions.assertNotNull(betSaved.getId());
    }

    @Test
    public void ShouldSaveNewBetWithSucess() {
        // scenario
        BetDTO betDTOMock = BetDTOBuilder.newBetDTOTestBuilder()
                .idBetObject(1L)
                .idPunter(100L)
                .bet(150.0)
                .deathDate(LocalDate.of(2050,12,1))
                .bitcoinAddress("bc1qxy2kgdygjrsqtzq2n0yrf2493p83kkfjhx0wlh")
                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betMock = BetModelBuilder.newBetModelTestBuilder()
                .idBetObject(betDTOMock.getIdBetObject())
                .idPunter(betDTOMock.getIdPunter())
                .bet(betDTOMock.getBet())
                .deathDate(betDTOMock.getDeathDate())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betSavedMock = BetModelBuilder.newBetModelTestBuilder()
                .id(501L)
                .idBetObject(betMock.getIdBetObject())
                .idPunter(betMock.getIdPunter())
                .bet(betMock.getBet())
                .bitcoinAddress(betMock.getBitcoinAddress())
                .deathDate(betDTOMock.getDeathDate())
                .ticket(uuidMock)
                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betRepositoryMock.save(betMock)).thenReturn(betSavedMock);

        // action
        BetDTO betSaved = betService.salvar(betDTOMock);

        // validate
        Assertions.assertInstanceOf(BetDTO.class, betSaved);
        Assertions.assertNotNull(betSaved.getId());
        Assertions.assertEquals("P",betSaved.getStatus());
    }
}

