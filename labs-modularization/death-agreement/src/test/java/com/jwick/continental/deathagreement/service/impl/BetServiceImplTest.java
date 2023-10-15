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
                .id(735L)
                .idPunter(7012L)
                .idBetObject(77038L)
                .bet(5117.0)
                .bitcoinAddress("fThNVa11vDsUqdkwHbG8EL9GCfcfhssKJ0ebKbAwlgPQecA0Oz")
                .ticket(UUID.fromString("2d5d4dbc-16a6-41de-8d4b-34ae69928326"))
                .deathDate(LocalDate.of(2140,9,8))

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
                .id(67813L)
                .idPunter(65255L)
                .idBetObject(58758L)
                .bet(4213.0)
                .bitcoinAddress("vNXev09dWHwqVwJ73cHLa3QGnnm4XRCQhCLj7a0UdJWVlH7271")
                .ticket(UUID.fromString("a8389d28-5820-4284-a240-a200a1b08c0c"))
                .deathDate(LocalDate.of(2058,10,25))

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
                .idPunter(4010L)
                .idBetObject(61582L)
                .bet(3726.0)
                .bitcoinAddress("hKilx1Id3WfBpazFdkMlwwths69Qj6wfLDfkWICNMV2c1xznsp")
                .ticket(UUID.fromString("b4c29ae3-e8b3-47f6-91ca-ba9ca99ac7fd"))
                .deathDate(LocalDate.of(2771,1,27))

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

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(574L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(574L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(4401L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(4401L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(60852L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(60852L, "A");

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

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(3510.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(3510.0, "A");

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

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("DHCspBvHy8P0hBVo4kQ5i2eu8P1G0fyEIkBoWBJs0O80br6r72", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("DHCspBvHy8P0hBVo4kQ5i2eu8P1G0fyEIkBoWBJs0O80br6r72", "A");

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

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("a293e18d-0635-47f8-a162-64dfa94712f4"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("a293e18d-0635-47f8-a162-64dfa94712f4"), "A");

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

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(864,8,9), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(864,8,9), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(2171L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(2171L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(2171L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(2171L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdPunterAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(44826L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(44826L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdPunterAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(44826L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(44826L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdBetObjectAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(38211L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(38211L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdBetObjectAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(38211L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(38211L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBetAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(1763.0, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(1763.0, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBetAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(1763.0, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(1763.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBitcoinAddressAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("l6d353jzd5oMjvblODmOvL7ML7d6TFWCGyOO7SVLvxlCJVFrzI", "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus("l6d353jzd5oMjvblODmOvL7ML7d6TFWCGyOO7SVLvxlCJVFrzI", "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBitcoinAddressAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("l6d353jzd5oMjvblODmOvL7ML7d6TFWCGyOO7SVLvxlCJVFrzI", "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus("l6d353jzd5oMjvblODmOvL7ML7d6TFWCGyOO7SVLvxlCJVFrzI", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByTicketAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("40bc1797-afff-49be-bde4-d638bd485b4d"), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(UUID.fromString("40bc1797-afff-49be-bde4-d638bd485b4d"), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetTicketAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("40bc1797-afff-49be-bde4-d638bd485b4d"), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(UUID.fromString("40bc1797-afff-49be-bde4-d638bd485b4d"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByDeathDateAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(2146,10,17), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(LocalDate.of(2146,10,17), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetDeathDateAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(2146,10,17), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(LocalDate.of(2146,10,17), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
    }

}

