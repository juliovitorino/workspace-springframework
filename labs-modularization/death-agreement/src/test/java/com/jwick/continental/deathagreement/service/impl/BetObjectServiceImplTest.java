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
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
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
    public void shouldReturnBetObjectNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 7023L;
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
        Long idMock = 7288L;
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
        Long idMock = 72666L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(idMock)
                        .who("qspskXgDMRkrAmK6oSjOKlU6un3dIuN3yl9PfxhfQpDnwIr9aR")
                        .externalUUID(UUID.fromString("be0d5f50-3d68-45f4-a7cc-7e7c40528e38"))
                        .jackpot(5815.0)
                        .jackpotPending(4742.0)

                        .status("X")
                        .now()
        );
        BetObject betobjectToSaveMock = betobjectModelMock.orElse(null);
        BetObject betobjectSavedMck = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(645L)
                        .who("EpXU1Bo4IT5hJIMiRN59z7JqfRhyAnhL48Br8r7UW80dvjDoga")
                        .externalUUID(UUID.fromString("2639323f-e5b6-4ec5-82f6-6ec5b450716c"))
                        .jackpot(616.0)
                        .jackpotPending(6402.0)

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
                .id(30833L)
                .who("2GyeK5Th0j7Xp5I2SnUwn456o1z30V9oExcn5yabxeEoPaqhHE")
                .externalUUID(UUID.fromString("e38bdcbe-72e4-4978-895d-55b593c5e341"))
                .jackpot(8040.0)
                .jackpotPending(1601.0)

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
                .id(80303L)
                .who("6wCsewGhpOFEAHVDB6kqw1NyFPc2078H20cFtcLAriMjNMo8T5")
                .externalUUID(UUID.fromString("bd5f2ecb-3d90-4fa2-b229-a4fade63c0fc"))
                .jackpot(6740.0)
                .jackpotPending(5757.0)

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
                .who("HPil2dSH12X6WMCPQiadX0a6UOVj6EtS0ygrpT3Sjb5kBXkvsv")
                .externalUUID(UUID.fromString("449ecd0f-3694-4068-abbe-5d48cddb97b1"))
                .jackpot(4807.0)
                .jackpotPending(850.0)

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"VGRz1NyH84tSug0O479n237oulNhteJND4dYcC5Qg4IK2yD8k3");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("39473a50-b821-4fa2-b2a9-08a51fd2e244"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,8647.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,6512.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"YL8xJezPvHE2hwiMkyefXe6MKDFmhIvlt9PBIU5F18FY2taon4");


        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(2606L)
                        .who("t7zcGFpWygbD7MO9wu6ALmPG31iD8Qjxv4jYwOraHgvfXEKwdA")
                        .externalUUID(UUID.fromString("bb4c640c-79b4-47b7-a05c-74b059f2a076"))
                        .jackpot(51.0)
                        .jackpotPending(7824.0)
                        .status("G0pArNtho519mEes6rQo6lDOzqh13zbHtooqOXtuKxjwkMb9B9")

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"RX9VnOAXkDPIaMTl1bFF81927paBngQoKLOUmjoTUaf4UFHWRz");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("230c61ad-e661-4de9-b42f-9ca4ffbfdcc5"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,2117.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,1088.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"mLWki0uU9soiAXYw08NC8h4hnket50gcf8BJyqUaSUiyxNjvaQ");


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

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(83620L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(83620L, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("3soYbUh0edvvXhPfgElSeHShWgfuXG4RXmyc5VjP2yNJAj3pd7", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("3soYbUh0edvvXhPfgElSeHShWgfuXG4RXmyc5VjP2yNJAj3pd7", "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("00a81439-2d28-45dd-932c-cd0eabd6df8c"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("00a81439-2d28-45dd-932c-cd0eabd6df8c"), "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotAndStatus(6751.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotAndStatus(6751.0, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotPendingAndStatus(4818.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotPendingAndStatus(4818.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByIdAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(23314L, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(23314L, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectIdAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(23314L, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(23314L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByWhoAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("CXL30wSOb4tsXBI9uXF7JeFEouwaWF49SVrxj9cj075eDVIlz0", "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus("CXL30wSOb4tsXBI9uXF7JeFEouwaWF49SVrxj9cj075eDVIlz0", "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectWhoAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("CXL30wSOb4tsXBI9uXF7JeFEouwaWF49SVrxj9cj075eDVIlz0", "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus("CXL30wSOb4tsXBI9uXF7JeFEouwaWF49SVrxj9cj075eDVIlz0", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("0e270f7e-75a8-4738-9cd6-3e3248396ffe"), "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("0e270f7e-75a8-4738-9cd6-3e3248396ffe"), "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectExternalUUIDAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("0e270f7e-75a8-4738-9cd6-3e3248396ffe"), "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("0e270f7e-75a8-4738-9cd6-3e3248396ffe"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(4240.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(4240.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(4240.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(4240.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(3651.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(3651.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotPendingAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(3651.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(3651.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
    }

    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingWhoById() {
        // scenario
        String whoUpdateMock = "cBxAL0RMnWCK6QdCQOhRJEQoOHOyBOxJPBUrk77THGIeIBewR1";
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
        UUID externalUUIDUpdateMock = UUID.fromString("faf58168-f227-4ca9-98b5-f7c4fa43ca45");
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
        Double jackpotUpdateMock = 3013.0;
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
        Double jackpotPendingUpdateMock = 8712.0;
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
        Long idMock = 50320L;
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
        Long idMock = 50320L;
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
        String whoMock = "Px0BbifL15GGxrGNgT5DO5HdvzOr2YopFbu4cU7cW7Ut06iAMX";
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
        String whoMock = "Px0BbifL15GGxrGNgT5DO5HdvzOr2YopFbu4cU7cW7Ut06iAMX";
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
        UUID externalUUIDMock = UUID.fromString("5166eb11-4625-4124-94f6-cde48776102a");
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
        UUID externalUUIDMock = UUID.fromString("5166eb11-4625-4124-94f6-cde48776102a");
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
        Double jackpotMock = 5758.0;
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .jackpot(jackpotMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(jackpotMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(jackpotMock, "A");

        // validate
        Assertions.assertEquals(jackpotMock, result.getJackpot());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByJackpotAndStatusActiveAnonimous() {
        // scenario
        Double jackpotMock = 5758.0;
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(jackpotMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(jackpotMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatusActiveAnonimous() {
        // scenario
        Double jackpotPendingMock = 8155.0;
        Long maxIdMock = 1972L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder()
                .jackpotPending(jackpotPendingMock)
                .now()
        );
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(jackpotPendingMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(maxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(jackpotPendingMock, "A");

        // validate
        Assertions.assertEquals(jackpotPendingMock, result.getJackpotPending());

    }
    @Test
    public void showReturnBetObjectNotFoundExceptionWhenNonExistenceFindBetObjectByJackpotPendingAndStatusActiveAnonimous() {
        // scenario
        Double jackpotPendingMock = 8155.0;
        Long noMaxIdMock = 0L;
        Optional<BetObject> betobjectModelMock = Optional.empty();
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(jackpotPendingMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betobjectRepositoryMock.findById(noMaxIdMock)).thenReturn(betobjectModelMock);

        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(jackpotPendingMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

}

