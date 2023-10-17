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
        Long idMock = 68871L;
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
        Long idMock = 56764L;
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
        Long idMock = 744L;
        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(idMock)
                        .idPunter(54803L)
                        .idBetObject(20305L)
                        .bet(1614.0)
                        .bitcoinAddress("g00CRmYDkeox20dPxTWYd92qwD6CQ2049WEdsl7s0cRBd3fHdG")
                        .ticket(UUID.fromString("376b6f7a-4a45-49a4-b2b3-ae57e31bd577"))
                        .deathDate(LocalDate.of(2064,12,3))
                        .status("X")
                        .now()
        );
        Bet betToSaveMock = betModelMock.orElse(null);
        Bet betSavedMck = BetModelBuilder.newBetModelTestBuilder()
                        .id(idMock)
                        .idPunter(84816L)
                        .idBetObject(21010L)
                        .bet(8000.0)
                        .bitcoinAddress("mccLg0SHXYGTJ6yccFcriFvknRaTScpiMwQsO1ifb7eWD5BeG4")
                        .ticket(UUID.fromString("730bac12-94e1-4b4a-b559-8df01d4b7bc4"))
                        .deathDate(LocalDate.of(2176,6,27))

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
                .id(72284L)
                .idPunter(50520L)
                .idBetObject(10846L)
                .bet(4872.0)
                .bitcoinAddress("43d0fe4ve8Lh5cSAVYvV9PGB7k0z3QJ8Si9ynoDl3RSsY8BiTh")
                .ticket(UUID.fromString("2ff377d8-2a9f-4044-9164-c4c8635c8e84"))
                .deathDate(LocalDate.of(2252,9,7))

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
                .id(17817L)
                .idPunter(36254L)
                .idBetObject(41037L)
                .bet(1051.0)
                .bitcoinAddress("u8mvK3gQHkMMEritMr9Oii1qSLyyFG9yB3YWFt5hDfkI4kP7YJ")
                .ticket(UUID.fromString("6dd265dd-d347-4b46-a428-7a06035fd0a6"))
                .deathDate(LocalDate.of(2013,5,6))

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
                .idPunter(85503L)
                .idBetObject(7100L)
                .bet(6380.0)
                .bitcoinAddress("oaMoH8MRWPCpynJyKbjzoMFNT0XPmlrL8CmNxCqv5FWW6P2Nay")
                .ticket(UUID.fromString("014e6ad8-8766-4983-b433-b48a55d3fc6a"))
                .deathDate(LocalDate.of(2511,4,22))

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,71084L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,88061L);
        mapBetDTOMock.put(BetConstantes.BET,8025.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"lALs11CfH0MxFrKQ5YO0DowS6SlhH0MM7YC1dJwp8SvPvXHg09");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("982d9d48-80e2-4292-9f3a-cb512387caa1"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(5656,12,13));
        mapBetDTOMock.put(BetConstantes.STATUS,"fIBiPeY56RBG8YTEmjVmc6ObXrjd43t6jNkt6yWV39u8XYCFFo");


        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(52253L)
                        .idPunter(81432L)
                        .idBetObject(40840L)
                        .bet(3547.0)
                        .bitcoinAddress("663VUENRoiPX2zjMlW4iqSoWU5w7cNSxJTMiCRkAuWvGWbjm7A")
                        .ticket(UUID.fromString("c6d0d56a-8e81-43eb-be2b-3e59ecae457d"))
                        .deathDate(LocalDate.of(8224,1,7))
                        .status("Tghe6nnDDYeMrnM6qAFhwI90FeCPUBST2TJJx0QXHuaVjzdsfL")

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
        mapBetDTOMock.put(BetConstantes.IDPUNTER,71216L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,20027L);
        mapBetDTOMock.put(BetConstantes.BET,4778.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"vhDNqSiA6T2fOCNm2i49olTrlb3EFw0LcH3GpQhLUEcLGD2iT2");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("7b0a1f61-585f-4f04-9046-ec2e6bb07829"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(30,4,28));
        mapBetDTOMock.put(BetConstantes.STATUS,"McThR2pc5SJ35xhbxl1607bVgEK7ohFcVahumJrv97G1rAS3Wq");


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

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(43081L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(43081L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(65016L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(65016L, "A");

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

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(65856L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(65856L, "A");

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

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(728.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(728.0, "A");

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

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("zRXynM3uQFhQdk93MpgrV9uTP60TnHYKRow3tJXBlg9bgWKTzd", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("zRXynM3uQFhQdk93MpgrV9uTP60TnHYKRow3tJXBlg9bgWKTzd", "A");

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

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("e1669bb8-cc81-4721-8ac0-393479b4d91a"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("e1669bb8-cc81-4721-8ac0-393479b4d91a"), "A");

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

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(1630,4,16), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(1630,4,16), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(7634L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(7634L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(7634L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(7634L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdPunterAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(57464L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(57464L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdPunterAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(57464L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(57464L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdBetObjectAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(64518L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(64518L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdBetObjectAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(64518L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(64518L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBetAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(3203.0, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(3203.0, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBetAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(3203.0, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(3203.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBitcoinAddressAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("tesslRQToxcg5I3GgYh6nJij900daURlnPrtToUdhaMJHlEyNC", "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus("tesslRQToxcg5I3GgYh6nJij900daURlnPrtToUdhaMJHlEyNC", "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBitcoinAddressAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("tesslRQToxcg5I3GgYh6nJij900daURlnPrtToUdhaMJHlEyNC", "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus("tesslRQToxcg5I3GgYh6nJij900daURlnPrtToUdhaMJHlEyNC", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByTicketAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("ccd54d54-78f3-436f-a32d-6b0ca92ca42c"), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(UUID.fromString("ccd54d54-78f3-436f-a32d-6b0ca92ca42c"), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetTicketAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("ccd54d54-78f3-436f-a32d-6b0ca92ca42c"), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(UUID.fromString("ccd54d54-78f3-436f-a32d-6b0ca92ca42c"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByDeathDateAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(4100,9,10), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(LocalDate.of(4100,9,10), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetDeathDateAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(4100,9,10), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(LocalDate.of(4100,9,10), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
    }

    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdPunterById() {
        // scenario
        Long idPunterUpdateMock = 1057L;
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
        Long idBetObjectUpdateMock = 35152L;
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
        Double betUpdateMock = 6088.0;
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
        String bitcoinAddressUpdateMock = "98JchnudUxt7Rbwtcc0U08BgPuQefb1H0cOwUMGHV5ejJ6CRFw";
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
        UUID ticketUpdateMock = UUID.fromString("d7226510-65cd-44c8-82c8-23e30769ac83");
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
        LocalDate deathDateUpdateMock = LocalDate.of(7341,12,14);
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

