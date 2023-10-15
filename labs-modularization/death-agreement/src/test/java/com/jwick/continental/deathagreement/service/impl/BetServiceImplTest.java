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
                .id(53200L)
                .idPunter(37260L)
                .idBetObject(74034L)
                .bet(4802.0)
                .bitcoinAddress("CcMkoyWO7uM3LItt1cO8A23DJHCKAmbXt9yyi2pw8wBQQ9rNr2")
                .ticket(UUID.fromString("d4626746-6bdb-46d9-a0c7-3c187dba9b0e"))
                .deathDate(LocalDate.of(3704,10,13))

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
                .id(32500L)
                .idPunter(1060L)
                .idBetObject(4660L)
                .bet(648.0)
                .bitcoinAddress("hUvyQwktrGH3DDsBgPoYawyOnWn20XF0HBDXOG0lkear0B98j3")
                .ticket(UUID.fromString("270bf08d-1762-4d8e-8687-2d60580ce24f"))
                .deathDate(LocalDate.of(2100,3,22))

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
                .idPunter(7726L)
                .idBetObject(85471L)
                .bet(8786.0)
                .bitcoinAddress("QS24grzMGwtFKH1VmKrcE0UFk0pQJdG1aUkYLBnzm1OdyXSbm4")
                .ticket(UUID.fromString("86104157-12e8-4f4b-98bc-d3d68c25726e"))
                .deathDate(LocalDate.of(2025,11,19))

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
    public void shouldReturnBetListWhenFindAllBetByIdAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(64463L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(64463L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(65067L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(65067L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(70075L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(70075L, "A");

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

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(7463.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(7463.0, "A");

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

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("QQ8CR290us07bTyzVph7vfuCDYeepfi91lRl0JOEkf9Wnw8dSz", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("QQ8CR290us07bTyzVph7vfuCDYeepfi91lRl0JOEkf9Wnw8dSz", "A");

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

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("302ea6e8-3617-4121-8758-e55cd9c527e0"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("302ea6e8-3617-4121-8758-e55cd9c527e0"), "A");

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

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(5068,10,26), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(5068,10,26), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

}

