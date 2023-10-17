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
        Long idMock = 24230L;
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
        Long idMock = 80375L;
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
        Long idMock = 56600L;
        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(idMock)
                        .idPunter(22613L)
                        .idBetObject(4430L)
                        .bet(5746.0)
                        .bitcoinAddress("jnwhWEguCbUGBjXaxRIVyXsTD08wIXr3kb9spXd3E22wJE630n")
                        .ticket(UUID.fromString("aeb855e2-f0be-4faa-9ee6-69a277bdfcb9"))
                        .deathDate(LocalDate.of(2287,12,17))

                        .status("X")
                        .now()
        );
        Bet betToSaveMock = betModelMock.orElse(null);
        Bet betSavedMck = BetModelBuilder.newBetModelTestBuilder()
                        .id(34042L)
                        .idPunter(7272L)
                        .idBetObject(6800L)
                        .bet(4661.0)
                        .bitcoinAddress("1iazF7yNKfKyJUPkPt9nG3qz2uyQAOEIYVzoGagc4YgeGr6sw4")
                        .ticket(UUID.fromString("67762602-f18f-4df0-ac61-8cbb3f8f0d64"))
                        .deathDate(LocalDate.of(2654,10,30))

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
                .id(38705L)
                .idPunter(80547L)
                .idBetObject(81344L)
                .bet(8450.0)
                .bitcoinAddress("O0DhMGV58h04OJyUTKGCBRNJVb3jdfHeFR2vURcVgrJfXcNShG")
                .ticket(UUID.fromString("6020981f-6093-4bbb-b2ae-d27efdb93b66"))
                .deathDate(LocalDate.of(3407,10,25))

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
                .id(52408L)
                .idPunter(38630L)
                .idBetObject(31636L)
                .bet(7403.0)
                .bitcoinAddress("L9M7TKEplt00tfbXkY8ewnoD6ct91It4FD5s9WHGEwCEXcJTWM")
                .ticket(UUID.fromString("432dd5ea-ed5c-4ebf-9209-361154d30e30"))
                .deathDate(LocalDate.of(2202,8,5))

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
                .idPunter(31107L)
                .idBetObject(32661L)
                .bet(2040.0)
                .bitcoinAddress("fbMFvXB6yTlSlB0v448YbdxOGlRSVgYgj7GyDnhQBta1ixuIt0")
                .ticket(UUID.fromString("d9f1e86c-9550-432c-9809-69c8624482ed"))
                .deathDate(LocalDate.of(2560,6,19))

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,55785L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,64003L);
        mapBetDTOMock.put(BetConstantes.BET,6453.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"6Rt8JVp5012LndOlrOkRGVVc8DSGuceworUispisSYK51dtDqA");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("35fc8a3b-822f-4422-86d1-06f1d2d628af"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(8147,3,11));
        mapBetDTOMock.put(BetConstantes.STATUS,"2R25kqbjWwtCNIxb4KPcDSEPVj7vg261V8NgQAI7CzXgrVuu1H");


        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(82001L)
                        .idPunter(22424L)
                        .idBetObject(55883L)
                        .bet(2206.0)
                        .bitcoinAddress("JsfXzl1YhNTzr6kcBkcS1hba0HHvgKwDcj6KpFIYhXC9szA05v")
                        .ticket(UUID.fromString("aa4f049a-2b6f-465a-baa9-084f90259a7c"))
                        .deathDate(LocalDate.of(5518,6,6))
                        .status("hfz4HyUF7kjq8aVbP0WgynkqsFhn15qR300pKs99Fb5xbkkytD")

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,78341L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,17428L);
        mapBetDTOMock.put(BetConstantes.BET,4220.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"vT651BWNSiC9iLgT85LTHoXgDKhq0QNPQlOUOy8lbPiOx7yr5R");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("79172a70-fad3-4d65-88bf-03b2dc47993e"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(3551,11,23));
        mapBetDTOMock.put(BetConstantes.STATUS,"tJJB5DA8GKQSaPmY0bJJD5ucf30ox2kEOR92QKxNAxJVSlIdzA");


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

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(1611L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(1611L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(75605L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(75605L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(4273L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(4273L, "A");

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

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(118.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(118.0, "A");

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

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("Mobj6S25Fpsz1dMMYWMz63EGArXsDt1NryxgiUpLok0BgfltPC", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("Mobj6S25Fpsz1dMMYWMz63EGArXsDt1NryxgiUpLok0BgfltPC", "A");

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

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("36d722a7-188c-4af0-aed6-ca5e13fe31d1"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("36d722a7-188c-4af0-aed6-ca5e13fe31d1"), "A");

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

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(46,1,18), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(46,1,18), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(80104L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(80104L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(80104L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(80104L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdPunterAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(36735L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(36735L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdPunterAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(36735L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(36735L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdBetObjectAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(56455L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(56455L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdBetObjectAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(56455L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(56455L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBetAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(1588.0, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(1588.0, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBetAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(1588.0, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(1588.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBitcoinAddressAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("HBRgPYfrOzTSLdHxv1X7JSHEOVC2M9PO2VsXYwbioFwNpcOjFA", "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus("HBRgPYfrOzTSLdHxv1X7JSHEOVC2M9PO2VsXYwbioFwNpcOjFA", "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBitcoinAddressAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("HBRgPYfrOzTSLdHxv1X7JSHEOVC2M9PO2VsXYwbioFwNpcOjFA", "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus("HBRgPYfrOzTSLdHxv1X7JSHEOVC2M9PO2VsXYwbioFwNpcOjFA", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByTicketAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("c57e5c0e-1946-474a-8775-53cc4c5532d9"), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(UUID.fromString("c57e5c0e-1946-474a-8775-53cc4c5532d9"), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetTicketAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("c57e5c0e-1946-474a-8775-53cc4c5532d9"), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(UUID.fromString("c57e5c0e-1946-474a-8775-53cc4c5532d9"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByDeathDateAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(6854,12,1), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(LocalDate.of(6854,12,1), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetDeathDateAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(6854,12,1), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(LocalDate.of(6854,12,1), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
    }

    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdPunterById() {
        // scenario
        Long idPunterUpdateMock = 185L;
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
        Long idBetObjectUpdateMock = 44176L;
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
        Double betUpdateMock = 261.0;
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
        String bitcoinAddressUpdateMock = "sDyS0JGfyU9uLsvQVLpk05PouiaQpYLGTen20L1HNePiFrKEjX";
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
        UUID ticketUpdateMock = UUID.fromString("1bb22018-07f2-4979-a43d-2e1fda0fca8c");
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
        LocalDate deathDateUpdateMock = LocalDate.of(1037,8,18);
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
        Long idMock = 40273L;
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
        Long idMock = 40273L;
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
        Long idPunterMock = 47770L;
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
        Long idPunterMock = 47770L;
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
        Long idBetObjectMock = 4157L;
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
        Long idBetObjectMock = 4157L;
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
        Double betMock = 708.0;
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
        Double betMock = 708.0;
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
        String bitcoinAddressMock = "8F0r7dADEovTGehxteUBKXOY8yKaaPOL8xH9dxuHqdzjGgzNXO";
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
        String bitcoinAddressMock = "8F0r7dADEovTGehxteUBKXOY8yKaaPOL8xH9dxuHqdzjGgzNXO";
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
        UUID ticketMock = UUID.fromString("d769ae06-3e9c-4981-b246-a80889ac73b0");
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
        UUID ticketMock = UUID.fromString("d769ae06-3e9c-4981-b246-a80889ac73b0");
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
        LocalDate deathDateMock = LocalDate.of(2700,12,7);
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
        LocalDate deathDateMock = LocalDate.of(2700,12,7);
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

