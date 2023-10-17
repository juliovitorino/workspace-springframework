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
                .id(46010L)
                .idPunter(35587L)
                .idBetObject(24865L)
                .bet(5187.0)
                .bitcoinAddress("kTBErU0KIQvYwH4AYqMyMAzyk7WBxr8rsLu9Ix1q5XTpWR8YRo")
                .ticket(UUID.fromString("4fe8cb09-b3b8-4223-81ae-211c58c0b3ba"))
                .deathDate(LocalDate.of(2017,5,11))

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
                .id(81218L)
                .idPunter(10742L)
                .idBetObject(1656L)
                .bet(1885.0)
                .bitcoinAddress("fvg0cV6bV3mfYsAv5x4Ji08R7pFK83Xn11NttEMmWaqbxoddwc")
                .ticket(UUID.fromString("72481f3b-e3b0-484d-b052-8a80aaeb6845"))
                .deathDate(LocalDate.of(2261,9,12))

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
                .idPunter(31706L)
                .idBetObject(47078L)
                .bet(4076.0)
                .bitcoinAddress("1iQNX1cSeJwv0904o8wKBkCulW1gYFG4r9ziHc0CoksNf0j07D")
                .ticket(UUID.fromString("5563186a-10e1-4424-b135-15d9a67f6408"))
                .deathDate(LocalDate.of(2430,7,10))

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,58051L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,18024L);
        mapBetDTOMock.put(BetConstantes.BET,5748.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"4JBrndc8a9MLd2Jw86Vcee2P9jRY0GIa1GC80c070JvGVP23e5");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("42bc8efb-d425-4ff2-89c8-cc3df520186b"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(4441,1,12));
        mapBetDTOMock.put(BetConstantes.STATUS,"fEBEK0raW7cSiyYYBWcnQnvvY3Omwj0rIMfYnuMe4SDrcfw5Wc");


        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(68044L)
                        .idPunter(52165L)
                        .idBetObject(706L)
                        .bet(3700.0)
                        .bitcoinAddress("h72N08D0oHWTJyzwsqPRnwrLGXwj1CGFOi0M1sEUQGOkHWI31I")
                        .ticket(UUID.fromString("6165681f-1df9-43f2-8647-acbb0b5892f6"))
                        .deathDate(LocalDate.of(536,5,29))
                        .status("Y0Bg1xEpkGzbjfOBs7AogGmlNS9P73OXxgkw5wzqSjx2HYPuwz")

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,53113L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,1252L);
        mapBetDTOMock.put(BetConstantes.BET,5450.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"PT8jNH1FipI4hvg7HeoS9eg9I7WQFg7tUUPzcjRj8msGqQyYYu");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("e198ae7b-42d0-46ac-bf8a-4d3879ec81eb"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(5204,8,3));
        mapBetDTOMock.put(BetConstantes.STATUS,"tza7gTlOR3M9HqfxhO0BAdtzBR1nEjC7ry0hB0XjJyVoPDwXdU");


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

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(56870L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(56870L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(7513L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(7513L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(26506L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(26506L, "A");

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

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(34.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(34.0, "A");

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

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("10n6piAm9cM6lgR6GEB09WCt9U0KNawob7M1k7HIsgT5hjCBl3", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("10n6piAm9cM6lgR6GEB09WCt9U0KNawob7M1k7HIsgT5hjCBl3", "A");

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

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("8481f523-9d85-432d-9c76-e7f4760d2102"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("8481f523-9d85-432d-9c76-e7f4760d2102"), "A");

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

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(102,4,13), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(102,4,13), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(7375L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(7375L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(7375L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(7375L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdPunterAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(31834L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(31834L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdPunterAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(31834L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(31834L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdBetObjectAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(622L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(622L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdBetObjectAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(622L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(622L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBetAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(810.0, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(810.0, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBetAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(810.0, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(810.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBitcoinAddressAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("42dPvJKGNiSQQBIh7hBtHxxKeSDm1C1CHL0MkFA4LwGXS82m6F", "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus("42dPvJKGNiSQQBIh7hBtHxxKeSDm1C1CHL0MkFA4LwGXS82m6F", "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBitcoinAddressAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("42dPvJKGNiSQQBIh7hBtHxxKeSDm1C1CHL0MkFA4LwGXS82m6F", "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus("42dPvJKGNiSQQBIh7hBtHxxKeSDm1C1CHL0MkFA4LwGXS82m6F", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByTicketAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("15165d88-911d-47ef-aba0-f86195d0d96d"), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(UUID.fromString("15165d88-911d-47ef-aba0-f86195d0d96d"), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetTicketAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("15165d88-911d-47ef-aba0-f86195d0d96d"), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(UUID.fromString("15165d88-911d-47ef-aba0-f86195d0d96d"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByDeathDateAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(6405,5,14), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(LocalDate.of(6405,5,14), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetDeathDateAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(6405,5,14), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(LocalDate.of(6405,5,14), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
    }

    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdPunterById() {
        // scenario
        Long idPunterUpdateMock = 15800L;
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
        Long idBetObjectUpdateMock = 15605L;
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
        Double betUpdateMock = 4371.0;
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
        String bitcoinAddressUpdateMock = "KnTGc66Y7g0GWeXXHsExk71fyxIftQb7WRou9DVkrUOsJrBp7X";
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
        UUID ticketUpdateMock = UUID.fromString("150463a6-35a5-4496-bd32-bc9de02bdf5e");
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
        LocalDate deathDateUpdateMock = LocalDate.of(4526,11,7);
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

}

