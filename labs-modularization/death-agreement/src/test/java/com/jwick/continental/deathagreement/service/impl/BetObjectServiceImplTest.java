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
        Long idMock = 17610L;
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
        Long idMock = 30388L;
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
        Long idMock = 23121L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(idMock)
                        .who("yWfYKGGixbjyGv5Cpdw4zTn1WOIVLtOIzC0RvTtHM44WYXaGO8")
                        .externalUUID(UUID.fromString("e6b99ba2-32a3-4cbb-9df6-2dbc8b202cec"))
                        .jackpot(6057.0)
                        .jackpotPending(130.0)

                        .status("X")
                        .now()
        );
        BetObject betobjectToSaveMock = betobjectModelMock.orElse(null);
        BetObject betobjectSavedMck = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(44807L)
                        .who("QNuVg4C1k0L6iYxWW26dg68sV3E408N4kEEKBe8W5rXhSwoE3h")
                        .externalUUID(UUID.fromString("c4aec220-2726-4eaa-bccb-ca57d097c0c8"))
                        .jackpot(3758.0)
                        .jackpotPending(8737.0)

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
                .id(3451L)
                .who("0YtOBfwHBTr5Mb6VKTI6PTGPKL1vKw07bSAN06kzsxG19eGqs8")
                .externalUUID(UUID.fromString("89dd2365-98f2-4ced-816e-c8f3eae7a2a7"))
                .jackpot(352.0)
                .jackpotPending(6360.0)

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
                .id(21634L)
                .who("LNr5ok0S2gFzGfLwn7vKWHPUOovDEMtW9RB3emY1qqs0igUxlX")
                .externalUUID(UUID.fromString("a8dcedc9-7897-47b8-80ac-d2cf4a90eac2"))
                .jackpot(6562.0)
                .jackpotPending(2007.0)

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
                .who("a2yhKIQzg4Ojf0KFg50mKXceTuIWXtbefWcmOrVsOfnzvzLQk0")
                .externalUUID(UUID.fromString("ee5140d1-88cd-414e-aff7-3c412333395d"))
                .jackpot(4683.0)
                .jackpotPending(201.0)

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"4v80cUp7l21PTET337F0GcIRqXe2yUAbC6m1dPo1UiBkOMuVuh");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("42b0d53e-20ee-4122-9fb5-edb787625047"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,5180.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,8154.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"x0bs0GdlVlpfHVwxclGmbwhNfJVmYuHF7T5hJmt8WxPrcQ73iS");


        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(68630L)
                        .who("7EYJOQllHE3mHxehk7LMWM2aaj1bmrWN6upmdXIddHM73ghSa4")
                        .externalUUID(UUID.fromString("cd6aeb5e-e436-4275-a15d-c12b0dc2e438"))
                        .jackpot(357.0)
                        .jackpotPending(833.0)
                        .status("Pfh38Yur0WzGHf5PKH1tFGwQd2Y34JsWkvUAqUA7BohyzjoO99")

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"ODHN0P10fvmItXEMA6tLTthiXJ5iCjLsHuyRwigvUmiMzaXvp6");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("8e96a717-e35c-4393-9beb-cb991e1890f7"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,3062.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,4870.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"KKgkmOQXysyKU3EcTU5Cg0EBCQoCVYwTbkSmjIXsg95hpHMirm");


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

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(24500L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(24500L, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("Wwze4PJ4pcX60tCyMJbmtHhVNRxDSOUe49ayPhrKIKvu9tfO54", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("Wwze4PJ4pcX60tCyMJbmtHhVNRxDSOUe49ayPhrKIKvu9tfO54", "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("4ff7d82b-afe5-4cf8-b21d-9b4cddbcd741"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("4ff7d82b-afe5-4cf8-b21d-9b4cddbcd741"), "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotAndStatus(8552.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotAndStatus(8552.0, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotPendingAndStatus(5527.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotPendingAndStatus(5527.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByIdAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(35504L, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(35504L, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectIdAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(35504L, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(35504L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByWhoAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("F54t5twMRVcibzcIltrOMilC2zBOEBdG6gqiUwI56DogGOQor0", "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus("F54t5twMRVcibzcIltrOMilC2zBOEBdG6gqiUwI56DogGOQor0", "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectWhoAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("F54t5twMRVcibzcIltrOMilC2zBOEBdG6gqiUwI56DogGOQor0", "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus("F54t5twMRVcibzcIltrOMilC2zBOEBdG6gqiUwI56DogGOQor0", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("e68e4d28-1dd2-42a6-a478-a237fb0c202a"), "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("e68e4d28-1dd2-42a6-a478-a237fb0c202a"), "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectExternalUUIDAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("e68e4d28-1dd2-42a6-a478-a237fb0c202a"), "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("e68e4d28-1dd2-42a6-a478-a237fb0c202a"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(1734.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(1734.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(1734.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(1734.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(1801.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(1801.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotPendingAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(1801.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(1801.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
    }

    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingWhoById() {
        // scenario
        String whoUpdateMock = "34UeVLCXb5MTh4kW5g4LpDXB81CyzJXXCk2AgurUxylvMNCgT9";
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
        UUID externalUUIDUpdateMock = UUID.fromString("88ac72c8-884a-42bd-b82a-c43f73b69193");
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
        Double jackpotUpdateMock = 721.0;
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
        Double jackpotPendingUpdateMock = 4268.0;
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
        Long idMock = 58405L;
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
        Long idMock = 58405L;
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
        String whoMock = "1vOzdtHiw7iwqAw03r3dSQTVwrU53BRAWJLVS8F0G1p347ORhh";
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
        String whoMock = "1vOzdtHiw7iwqAw03r3dSQTVwrU53BRAWJLVS8F0G1p347ORhh";
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
        UUID externalUUIDMock = UUID.fromString("3f76dd66-21d9-4193-a1d3-04f7c3f696e8");
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
        UUID externalUUIDMock = UUID.fromString("3f76dd66-21d9-4193-a1d3-04f7c3f696e8");
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
        Double jackpotMock = 100.0;
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
        Double jackpotMock = 100.0;
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
        Double jackpotPendingMock = 4582.0;
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
        Double jackpotPendingMock = 4582.0;
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

