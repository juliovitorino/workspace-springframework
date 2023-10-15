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
                .id(83473L)
                .who("HL2Bkz0vQugC9FFAakPOiVxij2dm7Uh5f0Rbm6ojuep9I5eTOO")
                .externalUUID(UUID.fromString("35dbdeb3-890c-482c-9467-4b0be4161af8"))
                .jackpot(5228.0)
                .jackpotPending(6568.0)

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
                .id(80007L)
                .who("pgit13jJAKFfqdFhvEg7GVfglX1xVH6pTiwXUW0iENY0W3Drk3")
                .externalUUID(UUID.fromString("8123afe8-a889-4df8-a92e-b102233cd55b"))
                .jackpot(2103.0)
                .jackpotPending(484.0)

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
                .who("n0xjMsdr586KoOBfoArJfkHtbFWwiPLA7ASj3CrOLYJhuFBDv3")
                .externalUUID(UUID.fromString("6cca44a6-0dbc-4b1e-9da7-d9a9e5e9ec20"))
                .jackpot(4663.0)
                .jackpotPending(3080.0)

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
    public void shouldReturnBetObjectListWhenFindAllBetObjectByIdAndStatus() {
        // scenario
        List<BetObject> betobjects = Arrays.asList(
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now(),
            BetObjectModelBuilder.newBetObjectModelTestBuilder().now()
        );

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(36303L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(36303L, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("HFeOR5W9cOfOTGfqVGxUwTuJd2Oysy9sNpAykIibvur9o9CpeQ", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("HFeOR5W9cOfOTGfqVGxUwTuJd2Oysy9sNpAykIibvur9o9CpeQ", "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("a1e85183-a22c-47eb-8c13-f87c2d80ad62"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("a1e85183-a22c-47eb-8c13-f87c2d80ad62"), "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotAndStatus(6003.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotAndStatus(6003.0, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotPendingAndStatus(7775.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotPendingAndStatus(7775.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByIdAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(44751L, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(44751L, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectIdAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(44751L, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(44751L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByWhoAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("WvJEz49Vf0dmJcUsAp5qyCWDoNfLpLaEEAEG9lHQ1xOtfwbYuS", "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus("WvJEz49Vf0dmJcUsAp5qyCWDoNfLpLaEEAEG9lHQ1xOtfwbYuS", "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectWhoAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("WvJEz49Vf0dmJcUsAp5qyCWDoNfLpLaEEAEG9lHQ1xOtfwbYuS", "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus("WvJEz49Vf0dmJcUsAp5qyCWDoNfLpLaEEAEG9lHQ1xOtfwbYuS", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("48c7bb7f-10d1-4b83-a1db-78d5d8556f49"), "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("48c7bb7f-10d1-4b83-a1db-78d5d8556f49"), "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectExternalUUIDAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("48c7bb7f-10d1-4b83-a1db-78d5d8556f49"), "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("48c7bb7f-10d1-4b83-a1db-78d5d8556f49"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(1288.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(1288.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(1288.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(1288.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(7126.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(7126.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotPendingAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(7126.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(7126.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
    }

}

