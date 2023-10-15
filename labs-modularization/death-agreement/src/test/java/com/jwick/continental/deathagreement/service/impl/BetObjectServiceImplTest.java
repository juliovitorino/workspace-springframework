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
                .id(54740L)
                .who("3v39dzSeH8DQIFSBL4fCnOq1vOYUyWT2YKHdaInHGpjdAD1mE8")
                .externalUUID(UUID.fromString("9ee00433-1dbd-47e4-99db-54beb824da07"))
                .jackpot(4000.0)
                .jackpotPending(424.0)

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
                .id(82107L)
                .who("R4oKwuDnk9OpSaKvU8T0g5aHePh3eXCNiy7j6b7n1uszp73Hk2")
                .externalUUID(UUID.fromString("3ecda1ea-c696-452a-b51c-ff1e87e5f8f3"))
                .jackpot(1285.0)
                .jackpotPending(2571.0)

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
                .who("kyKC0CuxdGBiMFAwquHE4LKDO0Ly7oa6cA9FaY6ACP7jMb3wcQ")
                .externalUUID(UUID.fromString("9a6abd3a-e37a-4267-a93c-8fc9ae23a253"))
                .jackpot(214.0)
                .jackpotPending(45.0)

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

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(3305L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(3305L, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("7pk3r39do9qKm04MtYrsPyc6Bgk9K9mgBybyzqHrGgODEW7iap", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("7pk3r39do9qKm04MtYrsPyc6Bgk9K9mgBybyzqHrGgODEW7iap", "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("d035e7f1-8ae7-4176-adb1-b810dbae30ff"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("d035e7f1-8ae7-4176-adb1-b810dbae30ff"), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }


}

