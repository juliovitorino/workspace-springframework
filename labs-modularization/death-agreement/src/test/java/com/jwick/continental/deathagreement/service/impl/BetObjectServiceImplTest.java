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
import com.jwick.continental.deathagreement.builder.BetObjectDTOBuilder;
import com.jwick.continental.deathagreement.builder.BetObjectModelBuilder;
import com.jwick.continental.deathagreement.dto.BetObjectDTO;
import com.jwick.continental.deathagreement.exception.BetObjectNotFoundException;
import com.jwick.continental.deathagreement.model.BetObject;
import com.jwick.continental.deathagreement.repository.BetObjectRepository;
import com.jwick.continental.deathagreement.service.BetObjectService;
import com.jwick.continental.deathagreement.constantes.BetObjectConstantes;
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
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class BetObjectServiceImplTest {
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;

    public static final String BETOBJECT_NOTFOUND_WITH_ID = "BetObject não encontrada com id = ";
    public static final String BETOBJECT_NOTFOUND_WITH_WHO = "BetObject não encontrada com who = ";
    public static final String BETOBJECT_NOTFOUND_WITH_EXTERNALUUID = "BetObject não encontrada com externalUUID = ";
    public static final String BETOBJECT_NOTFOUND_WITH_JACKPOT = "BetObject não encontrada com jackpot = ";
    public static final String BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING = "BetObject não encontrada com jackpotPending = ";
    public static final String BETOBJECT_NOTFOUND_WITH_STATUS = "BetObject não encontrada com status = ";
    public static final String BETOBJECT_NOTFOUND_WITH_DATECREATED = "BetObject não encontrada com dateCreated = ";
    public static final String BETOBJECT_NOTFOUND_WITH_DATEUPDATED = "BetObject não encontrada com dateUpdated = ";


    @Mock
    private BetObjectRepository betobjectRepositoryMock;

    @InjectMocks
    private BetObjectService betobjectService;
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);

    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));
        Mockito.when(dateTimeMock.now()).thenReturn(DateUtility.getDate(12,10,2023));

        betobjectService = new BetObjectServiceImpl();
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
    public void showReturnListOfBetObjectWhenAskedForFindAllByStatus() {
        // scenario
        List<BetObject> listOfBetObjectModelMock = new ArrayList<>();
        listOfBetObjectModelMock.add(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        listOfBetObjectModelMock.add(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());

        Mockito.when(betobjectRepositoryMock.findAllByStatus("A")).thenReturn(listOfBetObjectModelMock);

        // action
        List<BetObjectDTO> listOfBetObjects = betobjectService.findAllByStatus("A");

        // validate
        Assertions.assertTrue(!listOfBetObjects.isEmpty());
        Assertions.assertEquals(2, listOfBetObjects.size());
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 81332L;
        Optional<BetObject> betobjectNonExistentMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.findById(idMock)).thenReturn(betobjectNonExistentMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldThrowBetObjectNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 23024L;
        Mockito.when(betobjectRepositoryMock.findById(idMock))
                .thenThrow(new BetObjectNotFoundException(BETOBJECT_NOTFOUND_WITH_ID,
                HttpStatus.NOT_FOUND,
                BETOBJECT_NOTFOUND_WITH_ID ));

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldReturnBetObjectDTOAfterUpdateStatusById() {
        // scenario
        Long idMock = 48001L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(idMock)
                        .who("v0SIxiz2n0aGfHmxsemBH4jtsa19r5GzxEKxrGKrOV6XxV03mu")
                        .externalUUID(UUID.fromString("6e0cb7dc-58f6-4085-a865-3328d4cf440f"))
                        .jackpot(1365.0)
                        .jackpotPending(6734.0)

                        .status("X")
                        .now()
        );
        BetObject betobjectToSaveMock = betobjectModelMock.orElse(null);
        BetObject betobjectSavedMck = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(54372L)
                        .who("hPeR8jdjP7ogF21n31IHawckirxjNXoP6SuOdS7M0ebdPq54o4")
                        .externalUUID(UUID.fromString("136e3ba9-c4bf-4e86-a9e8-1538c28358e7"))
                        .jackpot(1222.0)
                        .jackpotPending(8150.0)

                        .status("A")
                        .now();
        Mockito.when(betobjectRepositoryMock.findById(idMock)).thenReturn(betobjectModelMock);
        Mockito.when(betobjectRepositoryMock.save(betobjectToSaveMock)).thenReturn(betobjectSavedMck);

        // action
        BetObjectDTO result = betobjectService.updateStatusById(idMock, "A");

        // validate
        Assertions.assertEquals("A",result.getStatus());

    }

    @Test
    public void shouldSearchBetObjectByAnyNonExistenceIdAndReturnBetObjectNotFoundException() {
        // scenario
        Mockito.when(betobjectRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()-> betobjectService.findById(-1000L));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldSearchBetObjectByIdAndReturnDTO() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .id(38385L)
                .who("2FFQSoPMmlEYVTU3CRlxKJy17AC9EHaaJnkioQDr0qO0R9AQtN")
                .externalUUID(UUID.fromString("33132804-05ac-45ee-9912-9194064e8f4d"))
                .jackpot(6000.0)
                .jackpotPending(3226.0)

                .status("A")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now());
        Mockito.when(betobjectRepositoryMock.findById(Mockito.anyLong())).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findById(1L);

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldDeleteBetObjectByIdWithSucess() {
        // scenario
        Optional<BetObject> betobject = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().id(1L).now());
        Mockito.when(betobjectRepositoryMock.findById(Mockito.anyLong())).thenReturn(betobject);

        // action
        betobjectService.delete(1L);

        // validate
        Mockito.verify(betobjectRepositoryMock,Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenTryDeleteNotExistenceBetObjectShouldReturnBetObjectNotFoundException() {
        // scenario
        Mockito.when(betobjectRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(
                BetObjectNotFoundException.class, () -> betobjectService.delete(1L)
        );

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }

    @Test
    public void ShouldSaveUpdateExistingBetObjectWithSucess() {
        // scenario
        BetObjectDTO betobjectDTOMock = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .id(7104L)
                .who("G0xAKRWoPpcUhJLfWQqGPmbmTXwF5DsTf8OaFg4Kt4lyEKRNu4")
                .externalUUID(UUID.fromString("d3069515-c584-4013-935e-40e3988e8d22"))
                .jackpot(7553.0)
                .jackpotPending(188.0)

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        BetObject betobjectMock = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .id(betobjectDTOMock.getId())
                .who(betobjectDTOMock.getWho())
                .externalUUID(betobjectDTOMock.getExternalUUID())
                .jackpot(betobjectDTOMock.getJackpot())
                .jackpotPending(betobjectDTOMock.getJackpotPending())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        BetObject betobjectSavedMock = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .id(betobjectDTOMock.getId())
                .who(betobjectDTOMock.getWho())
                .externalUUID(betobjectDTOMock.getExternalUUID())
                .jackpot(betobjectDTOMock.getJackpot())
                .jackpotPending(betobjectDTOMock.getJackpotPending())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betobjectRepositoryMock.save(betobjectMock)).thenReturn(betobjectSavedMock);

        // action
        BetObjectDTO betobjectSaved = betobjectService.salvar(betobjectDTOMock);

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class, betobjectSaved);
        Assertions.assertNotNull(betobjectSaved.getId());
    }

    @Test
    public void ShouldSaveNewBetObjectWithSucess() {
        // scenario
        BetObjectDTO betobjectDTOMock = BetObjectDTOBuilder.newBetObjectDTOTestBuilder()
                .id(null)
                .who("S0ozj7X5ukUqtxlHCVxEo0rYOJXkmxPfbiPtzf0Hpt2agoAwaK")
                .externalUUID(UUID.fromString("80e5f432-47ac-4576-8030-f0ce88b7f148"))
                .jackpot(6040.0)
                .jackpotPending(7623.0)

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        BetObject betobjectModelMock = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .id(null)
                .who(betobjectDTOMock.getWho())
                .externalUUID(betobjectDTOMock.getExternalUUID())
                .jackpot(betobjectDTOMock.getJackpot())
                .jackpotPending(betobjectDTOMock.getJackpotPending())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        BetObject betobjectSavedMock = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .id(501L)
                .who(betobjectDTOMock.getWho())
                .externalUUID(betobjectDTOMock.getExternalUUID())
                .jackpot(betobjectDTOMock.getJackpot())
                .jackpotPending(betobjectDTOMock.getJackpotPending())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betobjectRepositoryMock.save(betobjectModelMock)).thenReturn(betobjectSavedMock);

        // action
        BetObjectDTO betobjectSaved = betobjectService.salvar(betobjectDTOMock);

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class, betobjectSaved);
        Assertions.assertNotNull(betobjectSaved.getId());
        Assertions.assertEquals("P",betobjectSaved.getStatus());
    }

    @Test
    public void shouldExecutePartialUpdateWithSucess() {
        // scenario
        Map<String, Object> mapBetObjectDTOMock = new HashMap<>();
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"pB8l9iJLXiJUq4V3OfE35sngDKjvxOQ8WH4Jmq2pNr68viK7oY");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("53cc871e-f8dd-4024-9682-97ef00386ad1"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,6846.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,3103.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"IKKa1WXB8KNgylB0hYc4wcHiPQEoyjXB6QMeQuuB80QiYSPv0x");


        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(63221L)
                        .who("YqsYAA9bbcn4o6uEXzJjJvbCXaFPtuTrWE2mI5FVVp0cI5OEON")
                        .externalUUID(UUID.fromString("26c8335a-328f-4037-9ddf-9c46480a2b0b"))
                        .jackpot(80.0)
                        .jackpotPending(6176.0)
                        .status("5V13Kfw8uNBVthwIl6DnKbQ6y12VsMBsPuVl9ggc80qmSdY55y")

                        .now()
        );

        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        boolean executed = betobjectService.partialUpdate(1L, mapBetObjectDTOMock);

        // validate
        Assertions.assertTrue(executed);

    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenTrySearchNotExistentId() {
        // scenario
        Map<String, Object> mapBetObjectDTOMock = new HashMap<>();
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"cYvJAOITr0A4zUfYMy8dH7FgbKgW5GE5oYuFj7KueFkuzyF2dh");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("cd19ab81-869e-42db-91ed-c3c1d872e66c"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,8101.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,7371.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"XCs3P2xntQVvkP5Mhy8eOpHOhgVUpJ8XcCUPbQERyfvhsqEJK0");


        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.partialUpdate(1L, mapBetObjectDTOMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains("BetObject não encontrada com id = "));
        Assertions.assertEquals(404,exception.getHttpStatus().value());

    }

    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByIdAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(40356L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(40356L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByWhoAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("bwtXeqkz3Sg6KB9zK4noPxyQlnbGJ9oeLbcgyfTJ8zI9GOTpd4", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("bwtXeqkz3Sg6KB9zK4noPxyQlnbGJ9oeLbcgyfTJ8zI9GOTpd4", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByExternalUUIDAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("84a7a3b9-2ef5-4e29-84ba-ccf6c787b822"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("84a7a3b9-2ef5-4e29-84ba-ccf6c787b822"), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByJackpotAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByJackpotAndStatus(8802.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotAndStatus(8802.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByJackpotPendingAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByJackpotPendingAndStatus(2574.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotPendingAndStatus(2574.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByDateCreatedAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByDateCreatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByDateCreatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetObjectListWhenFindAllBetObjectByDateUpdatedAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByDateUpdatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByDateUpdatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByIdAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(46586L, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(46586L, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectIdAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(46586L, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(46586L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByWhoAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("ahWYhgMFNNqpWvbB227TgPgyj5sPqWor8Nhl3rgdDNw0qmOXgN", "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus("ahWYhgMFNNqpWvbB227TgPgyj5sPqWor8Nhl3rgdDNw0qmOXgN", "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectWhoAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("ahWYhgMFNNqpWvbB227TgPgyj5sPqWor8Nhl3rgdDNw0qmOXgN", "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus("ahWYhgMFNNqpWvbB227TgPgyj5sPqWor8Nhl3rgdDNw0qmOXgN", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("ad9495a0-55bc-43e3-ada3-1dbdee673476"), "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("ad9495a0-55bc-43e3-ada3-1dbdee673476"), "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectExternalUUIDAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("ad9495a0-55bc-43e3-ada3-1dbdee673476"), "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("ad9495a0-55bc-43e3-ada3-1dbdee673476"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(128.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(128.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(128.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(128.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(2120.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(2120.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotPendingAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(2120.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(2120.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
    }

    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingWhoById() {
        // scenario
        String whoUpdateMock = "Xa09havARPHv5z8V0bUfHAMUpsNvMb3IzSiEJL9Kir1pBfuTWE";
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betobjectRepositoryMock.findById(420L)).thenReturn(betobjectModelMock);
        Mockito.doNothing().when(betobjectRepositoryMock).updateWhoById(420L, whoUpdateMock);

        // action
        betobjectService.updateWhoById(420L, whoUpdateMock);

        // validate
        Mockito.verify(betobjectRepositoryMock,Mockito.times(1)).updateWhoById(420L, whoUpdateMock);
    }
    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingExternalUUIDById() {
        // scenario
        UUID externalUUIDUpdateMock = UUID.fromString("5c008273-eae6-4618-991d-351f4e4c8605");
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betobjectRepositoryMock.findById(420L)).thenReturn(betobjectModelMock);
        Mockito.doNothing().when(betobjectRepositoryMock).updateExternalUUIDById(420L, externalUUIDUpdateMock);

        // action
        betobjectService.updateExternalUUIDById(420L, externalUUIDUpdateMock);

        // validate
        Mockito.verify(betobjectRepositoryMock,Mockito.times(1)).updateExternalUUIDById(420L, externalUUIDUpdateMock);
    }
    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingJackpotById() {
        // scenario
        Double jackpotUpdateMock = 1623.0;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betobjectRepositoryMock.findById(420L)).thenReturn(betobjectModelMock);
        Mockito.doNothing().when(betobjectRepositoryMock).updateJackpotById(420L, jackpotUpdateMock);

        // action
        betobjectService.updateJackpotById(420L, jackpotUpdateMock);

        // validate
        Mockito.verify(betobjectRepositoryMock,Mockito.times(1)).updateJackpotById(420L, jackpotUpdateMock);
    }
    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingJackpotPendingById() {
        // scenario
        Double jackpotPendingUpdateMock = 7814.0;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betobjectRepositoryMock.findById(420L)).thenReturn(betobjectModelMock);
        Mockito.doNothing().when(betobjectRepositoryMock).updateJackpotPendingById(420L, jackpotPendingUpdateMock);

        // action
        betobjectService.updateJackpotPendingById(420L, jackpotPendingUpdateMock);

        // validate
        Mockito.verify(betobjectRepositoryMock,Mockito.times(1)).updateJackpotPendingById(420L, jackpotPendingUpdateMock);
    }



    @Test
    public void showReturnExistingBetObjectDTOWhenFindBetObjectByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 40802L;
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .id(idMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(idMock);

        // validate
        Assertions.assertEquals(idMock, result.getId());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 40802L;
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(idMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetObjectDTOWhenFindBetObjectByWhoAndStatusActiveAnonimous() {
        // scenario
        String whoMock = "mBLKWPxwNaFQnldRnvtWng8abtdfkWSbdm11jWXAX1SwMC80rl";
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .who(whoMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus(whoMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus(whoMock);

        // validate
        Assertions.assertEquals(whoMock, result.getWho());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByWhoAndStatusActiveAnonimous() {
        // scenario
        String whoMock = "mBLKWPxwNaFQnldRnvtWng8abtdfkWSbdm11jWXAX1SwMC80rl";
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus(whoMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus(whoMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatusActiveAnonimous() {
        // scenario
        UUID externalUUIDMock = UUID.fromString("51307038-d059-41c1-9e1c-0efaa1650c58");
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .externalUUID(externalUUIDMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(externalUUIDMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(externalUUIDMock);

        // validate
        Assertions.assertEquals(externalUUIDMock, result.getExternalUUID());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByExternalUUIDAndStatusActiveAnonimous() {
        // scenario
        UUID externalUUIDMock = UUID.fromString("51307038-d059-41c1-9e1c-0efaa1650c58");
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(externalUUIDMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(externalUUIDMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetObjectDTOWhenFindBetObjectByJackpotAndStatusActiveAnonimous() {
        // scenario
        Double jackpotMock = 284.0;
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .jackpot(jackpotMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(jackpotMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(jackpotMock,"A");

        // validate
        Assertions.assertEquals(jackpotMock, result.getJackpot());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByJackpotAndStatusActiveAnonimous() {
        // scenario
        Double jackpotMock = 284.0;
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(jackpotMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(jackpotMock,"A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatusActiveAnonimous() {
        // scenario
        Double jackpotPendingMock = 1320.0;
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .jackpotPending(jackpotPendingMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(jackpotPendingMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(jackpotPendingMock,"A");

        // validate
        Assertions.assertEquals(jackpotPendingMock, result.getJackpotPending());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByJackpotPendingAndStatusActiveAnonimous() {
        // scenario
        Double jackpotPendingMock = 1320.0;
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(jackpotPendingMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(jackpotPendingMock,"A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

}

