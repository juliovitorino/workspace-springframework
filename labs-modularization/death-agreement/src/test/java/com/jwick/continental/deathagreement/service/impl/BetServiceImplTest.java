/*
Copyright <YEAR> <COPYRIGHT HOLDER>

This software is Open Source and is under MIT license agreement

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.jwick.continental.deathagreement.service.impl;

import br.com.jcv.commons.library.utility.DateTime;
import br.com.jcv.commons.library.utility.DateUtility;
import com.jwick.continental.deathagreement.builder.BetDTOBuilder;
import com.jwick.continental.deathagreement.builder.BetModelBuilder;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.repository.BetRepository;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
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
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetServiceImplTest {
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;

    public static final String BET_NOTFOUND_WITH_ID = "Bet não encontrada com id = ";
    public static final String BET_NOTFOUND_WITH_IDPUNTER = "Bet não encontrada com idPunter = ";
    public static final String BET_NOTFOUND_WITH_IDBETOBJECT = "Bet não encontrada com idBetObject = ";
    public static final String BET_NOTFOUND_WITH_BET = "Bet não encontrada com bet = ";
    public static final String BET_NOTFOUND_WITH_BITCOINADDRESS = "Bet não encontrada com bitcoinAddress = ";
    public static final String BET_NOTFOUND_WITH_TICKET = "Bet não encontrada com ticket = ";
    public static final String BET_NOTFOUND_WITH_DEATHDATE = "Bet não encontrada com deathDate = ";
    public static final String BET_NOTFOUND_WITH_STATUS = "Bet não encontrada com status = ";
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
    public void shouldReturnBetNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 12000L;
        Optional<Bet> betNonExistentMock = Optional.empty();
        Mockito.when(betRepositoryMock.findById(idMock)).thenReturn(betNonExistentMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldThrowBetNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 3741L;
        Mockito.when(betRepositoryMock.findById(idMock))
                .thenThrow(new BetNotFoundException(BET_NOTFOUND_WITH_ID,
                HttpStatus.NOT_FOUND,
                BET_NOTFOUND_WITH_ID ));

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldReturnBetDTOAfterUpdateStatusById() {
        // scenario
        Long idMock = 31635L;
        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(idMock)
                        .idPunter(3816L)
                        .idBetObject(26661L)
                        .bet(5088.0)
                        .bitcoinAddress("6tw63hKgCtin5Ges1zaRTyktsHULKDphNy5dVlL1anoxXaHXBR")
                        .ticket(UUID.fromString("b67222aa-0e61-4e73-9fcd-89ceddac1330"))
                        .deathDate(LocalDate.of(3816,4,3))

                        .status("X")
                        .now()
        );
        Bet betToSaveMock = betModelMock.orElse(null);
        Bet betSavedMck = BetModelBuilder.newBetModelTestBuilder()
                        .id(85462L)
                        .idPunter(57145L)
                        .idBetObject(56127L)
                        .bet(3068.0)
                        .bitcoinAddress("wC430c0PNhBvRFF0fNM54nY7JIotxG5750qqLpLz30i0fMp1sy")
                        .ticket(UUID.fromString("575e72a5-c1cb-4e12-94ad-910feb93e030"))
                        .deathDate(LocalDate.of(2818,4,4))

                        .status("A")
                        .now();
        Mockito.when(betRepositoryMock.findById(idMock)).thenReturn(betModelMock);
        Mockito.when(betRepositoryMock.save(betToSaveMock)).thenReturn(betSavedMck);

        // action
        BetDTO result = betService.updateStatusById(idMock, "A");

        // validate
        Assertions.assertEquals("A",result.getStatus());

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
    public void shouldSearchBetByIdAndReturnDTO() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .id(27270L)
                .idPunter(672L)
                .idBetObject(10660L)
                .bet(776.0)
                .bitcoinAddress("onSE2PkIm7sGYaRDPhYoKLt3ffg6B2QzB8MsP6J4gV0a14wosu")
                .ticket(UUID.fromString("68fed646-0700-40ec-901a-5562a59b403d"))
                .deathDate(LocalDate.of(2425,2,17))

                .status("A")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now());
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findById(1L);

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
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
                .id(46400L)
                .idPunter(54040L)
                .idBetObject(40206L)
                .bet(4155.0)
                .bitcoinAddress("P91o0YDq5yDyNaLANKC4vgYU8Pu6oQ8kbXe0LlOryQkXSBHm7R")
                .ticket(UUID.fromString("18dab95a-3650-443e-af4e-5dc6cff20713"))
                .deathDate(LocalDate.of(2275,9,9))

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betMock = BetModelBuilder.newBetModelTestBuilder()
                .id(betDTOMock.getId())
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betSavedMock = BetModelBuilder.newBetModelTestBuilder()
                .id(betDTOMock.getId())
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

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
                .id(null)
                .idPunter(15045L)
                .idBetObject(87L)
                .bet(676.0)
                .bitcoinAddress("ilGnsSuiskErfGGi6g0cWlFmR4oJhxceVlMkj9oF76bVQQHkPl")
                .ticket(UUID.fromString("3210c72a-a584-4743-9dd7-4d47b1285c98"))
                .deathDate(LocalDate.of(3517,9,4))

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betModelMock = BetModelBuilder.newBetModelTestBuilder()
                .id(null)
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betSavedMock = BetModelBuilder.newBetModelTestBuilder()
                .id(501L)
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betRepositoryMock.save(betModelMock)).thenReturn(betSavedMock);

        // action
        BetDTO betSaved = betService.salvar(betDTOMock);

        // validate
        Assertions.assertInstanceOf(BetDTO.class, betSaved);
        Assertions.assertNotNull(betSaved.getId());
        Assertions.assertEquals("P",betSaved.getStatus());
    }

    @Test
    public void shouldExecutePartialUpdateWithSucess() {
        // scenario
        Map<String, Object> mapBetDTOMock = new HashMap<>();
        mapBetDTOMock.put(BetConstantes.IDPUNTER,78174L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,43708L);
        mapBetDTOMock.put(BetConstantes.BET,4145.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"FRioPIqxgN8inngtEbGPmEGLa0vSxJXwPzcPwohL8yzAPNEVsC");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("3d81446d-ae9e-46a7-92ce-9dfa416f4308"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(6004,12,28));
        mapBetDTOMock.put(BetConstantes.STATUS,"1bmEN2swjhHYGt6L5vsYVgTuneSRgUGB5tol3dnS7NGejpHTvX");


        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(2646L)
                        .idPunter(46010L)
                        .idBetObject(11266L)
                        .bet(6172.0)
                        .bitcoinAddress("3R0IprR9bnAB4F5q04wNAAVT5JGhVVTQ5xJcxzSineiT1cbWOy")
                        .ticket(UUID.fromString("e316ef7e-d7a7-4e49-864b-6133f1f6e155"))
                        .deathDate(LocalDate.of(851,7,23))
                        .status("jraGon6wzb18ubmrKaPWmVJak0QyLcG7VUTywM3jdVrKnKJlbQ")

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,61085L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,12535L);
        mapBetDTOMock.put(BetConstantes.BET,1324.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"FB6wbg9T05qRSFgv3KusYadd1oS1LIbeTPXI0bEXTlYPHkp6tS");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("13f1e71d-bfb8-4607-af32-99fd674305d4"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(240,3,22));
        mapBetDTOMock.put(BetConstantes.STATUS,"aBXqbCQhfcruEut90KtNQBbjWq59yWbLxhNFoby0RUlenXDJjU");


        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.partialUpdate(1L, mapBetDTOMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains("Bet não encontrada com id = "));
        Assertions.assertEquals(404,exception.getHttpStatus().value());

    }

    @Test
    public void shouldReturnBetListWhenFindAllBetByIdAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(45052L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(45052L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByIdPunterAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(84013L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(84013L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByIdBetObjectAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(56055L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(56055L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByBetAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(6701.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(6701.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByBitcoinAddressAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("8HReCKAQfGlI7SoLpYhbrvOPO0iyGMcafur736swS70hlEMPIF", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("8HReCKAQfGlI7SoLpYhbrvOPO0iyGMcafur736swS70hlEMPIF", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByTicketAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("b7b7928e-7cbe-49c6-93d2-d5907847b324"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("b7b7928e-7cbe-49c6-93d2-d5907847b324"), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByDeathDateAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(4050,3,28), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(4050,3,28), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(60215L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(60215L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(60215L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(60215L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdPunterAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(81055L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(81055L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdPunterAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(81055L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(81055L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdBetObjectAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(86722L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(86722L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdBetObjectAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(86722L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(86722L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBetAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(7068.0, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(7068.0, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBetAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(7068.0, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(7068.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBitcoinAddressAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("vApgmhRSI9cAwBj73aPtnd6d8eoz5JQMdV3CnGyQCpDuNNoDAT", "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus("vApgmhRSI9cAwBj73aPtnd6d8eoz5JQMdV3CnGyQCpDuNNoDAT", "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBitcoinAddressAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("vApgmhRSI9cAwBj73aPtnd6d8eoz5JQMdV3CnGyQCpDuNNoDAT", "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus("vApgmhRSI9cAwBj73aPtnd6d8eoz5JQMdV3CnGyQCpDuNNoDAT", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByTicketAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("82fd31c0-300a-41a3-8caa-fa13eb6a44cc"), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(UUID.fromString("82fd31c0-300a-41a3-8caa-fa13eb6a44cc"), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetTicketAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("82fd31c0-300a-41a3-8caa-fa13eb6a44cc"), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(UUID.fromString("82fd31c0-300a-41a3-8caa-fa13eb6a44cc"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByDeathDateAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(6026,1,12), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(LocalDate.of(6026,1,12), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetDeathDateAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(6026,1,12), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(LocalDate.of(6026,1,12), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
    }

    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdPunterById() {
        // scenario
        Long idPunterUpdateMock = 73812L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateIdPunterById(420L, idPunterUpdateMock);

        // action
        betService.updateIdPunterById(420L, idPunterUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateIdPunterById(420L, idPunterUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdBetObjectById() {
        // scenario
        Long idBetObjectUpdateMock = 4582L;
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
    public void shouldReturnBetDTOWhenUpdateExistingBetById() {
        // scenario
        Double betUpdateMock = 7505.0;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateBetById(420L, betUpdateMock);

        // action
        betService.updateBetById(420L, betUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateBetById(420L, betUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingBitcoinAddressById() {
        // scenario
        String bitcoinAddressUpdateMock = "dT10b28YMu4tgG1lXoC6XfhUHXCPaJJJKizxPM9HJ2jlXP9uaN";
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateBitcoinAddressById(420L, bitcoinAddressUpdateMock);

        // action
        betService.updateBitcoinAddressById(420L, bitcoinAddressUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateBitcoinAddressById(420L, bitcoinAddressUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingTicketById() {
        // scenario
        UUID ticketUpdateMock = UUID.fromString("72ae0fe7-adda-4e9b-afd2-77da5c9364e8");
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateTicketById(420L, ticketUpdateMock);

        // action
        betService.updateTicketById(420L, ticketUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateTicketById(420L, ticketUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingDeathDateById() {
        // scenario
        LocalDate deathDateUpdateMock = LocalDate.of(4570,10,22);
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateDeathDateById(420L, deathDateUpdateMock);

        // action
        betService.updateDeathDateById(420L, deathDateUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateDeathDateById(420L, deathDateUpdateMock);
    }



    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 75568L;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .id(idMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(idMock);

        // validate
        Assertions.assertEquals(idMock, result.getId());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 75568L;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(idMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdPunterAndStatusActiveAnonimous() {
        // scenario
        Long idPunterMock = 16220L;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .idPunter(idPunterMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(idPunterMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(idPunterMock);

        // validate
        Assertions.assertEquals(idPunterMock, result.getIdPunter());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdPunterAndStatusActiveAnonimous() {
        // scenario
        Long idPunterMock = 16220L;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(idPunterMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(idPunterMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdBetObjectAndStatusActiveAnonimous() {
        // scenario
        Long idBetObjectMock = 33071L;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .idBetObject(idBetObjectMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(idBetObjectMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(idBetObjectMock);

        // validate
        Assertions.assertEquals(idBetObjectMock, result.getIdBetObject());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdBetObjectAndStatusActiveAnonimous() {
        // scenario
        Long idBetObjectMock = 33071L;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(idBetObjectMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(idBetObjectMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByBetAndStatusActiveAnonimous() {
        // scenario
        Double betMock = 52.0;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .bet(betMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(betMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(betMock);

        // validate
        Assertions.assertEquals(betMock, result.getBet());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByBetAndStatusActiveAnonimous() {
        // scenario
        Double betMock = 52.0;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(betMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(betMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByBitcoinAddressAndStatusActiveAnonimous() {
        // scenario
        String bitcoinAddressMock = "v0yl6QASbsA6tvTfzQFmrGs7w7LXBof1qQGOMYwTvCrKzLnkAo";
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .bitcoinAddress(bitcoinAddressMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus(bitcoinAddressMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus(bitcoinAddressMock);

        // validate
        Assertions.assertEquals(bitcoinAddressMock, result.getBitcoinAddress());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByBitcoinAddressAndStatusActiveAnonimous() {
        // scenario
        String bitcoinAddressMock = "v0yl6QASbsA6tvTfzQFmrGs7w7LXBof1qQGOMYwTvCrKzLnkAo";
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus(bitcoinAddressMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus(bitcoinAddressMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByTicketAndStatusActiveAnonimous() {
        // scenario
        UUID ticketMock = UUID.fromString("e1bcf5c9-fccb-4c53-b1c4-3a82601c00c2");
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .ticket(ticketMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(ticketMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(ticketMock);

        // validate
        Assertions.assertEquals(ticketMock, result.getTicket());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByTicketAndStatusActiveAnonimous() {
        // scenario
        UUID ticketMock = UUID.fromString("e1bcf5c9-fccb-4c53-b1c4-3a82601c00c2");
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(ticketMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(ticketMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByDeathDateAndStatusActiveAnonimous() {
        // scenario
        LocalDate deathDateMock = LocalDate.of(2354,6,14);
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .deathDate(deathDateMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(deathDateMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(deathDateMock);

        // validate
        Assertions.assertEquals(deathDateMock, result.getDeathDate());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByDeathDateAndStatusActiveAnonimous() {
        // scenario
        LocalDate deathDateMock = LocalDate.of(6354,6,14);
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(deathDateMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(deathDateMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

}

