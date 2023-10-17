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
        Long idMock = 18330L;
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
        Long idMock = 7152L;
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
        Long idMock = 36302L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()

                        .id(86248L)
                        .who("03kdJ9Buo4d0XQG6z02CY9pYN56RFB9n7OBaAnQgLBJArON9TT")
                        .externalUUID(UUID.fromString("760a0498-610b-4314-a753-efc82225d56a"))
                        .jackpot(4517.0)
                        .jackpotPending(2016.0)

                        .status("X")
                        .now()
        );
        BetObject betobjectToSaveMock = betobjectModelMock.orElse(null);
        BetObject betobjectSavedMck = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(10500L)
                        .who("CVyNvpH2MwVNMhbn66REfw6lW06Os81WAWXHJkYs2jN0g2PkjA")
                        .externalUUID(UUID.fromString("2a0c1e1b-e4d1-4c8e-a239-41c58995a61d"))
                        .jackpot(184.0)
                        .jackpotPending(7522.0)

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
                .id(54541L)
                .who("0t06kKRS0cVPHMBxmkHfbokSJzNW0fThY8vWsuOmgyem6MQ1Ow")
                .externalUUID(UUID.fromString("a7433551-4823-4437-b11f-552b2e173afe"))
                .jackpot(1544.0)
                .jackpotPending(725.0)

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
                .id(85567L)
                .who("PNHGcaY0U4WM4YrcuEUTz7dVJiGEVWtaJbqy9H0cCDOuia4GWT")
                .externalUUID(UUID.fromString("84cfceb8-3219-4d9d-bd84-39960528290a"))
                .jackpot(7850.0)
                .jackpotPending(1680.0)

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
                .who("GWBIvycfDEK6SlxKLTk90mc1HOxXPYd3z0h0lMxUEFw7uxognq")
                .externalUUID(UUID.fromString("f8a05d84-eff5-4a83-a888-260e5f3a63e2"))
                .jackpot(2805.0)
                .jackpotPending(5376.0)

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"9BC4fAtkU2UwQdB02zxugJcPBY1EhGX11hoOX5l5CmKoywY7J1");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("2bbdb871-f416-4f21-8207-5edbec295579"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,1170.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,6300.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"VWidxlb1RM8vJKwCv7WuO8BkaujmFsxFdDokyIT4zbLf0Mlf34");


        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(65123L)
                        .who("hp5CEl4KQM0bkw2MIdR4EsadQ5z4Qi0Lo0we9JAP9wSwuWM4Hq")
                        .externalUUID(UUID.fromString("6758e9f4-f4d0-4380-b979-feb542c634f0"))
                        .jackpot(222.0)
                        .jackpotPending(7233.0)
                        .status("eS0HmD34OA6t2u77VePNCoWaTrCCxuRLminjVbXPwslN6pBbuW")

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"e1L6hFxQxeg9Bjr1XWmdqNo73Vrs0x3dqWB0AvSI0pb31hoNQy");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("d098219e-5d15-4f8d-86b8-de3862159314"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,6443.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,7175.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"9R4rhoMIhhsYVLynGoC0mfediK0b3VsgoXSeezzC8AkfVSyfj0");


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

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(26500L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(26500L, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("mKOdIWtqwl91IWKek1mKnGXoTGE0AfcNCDLewmOTBtOoqDByLK", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("mKOdIWtqwl91IWKek1mKnGXoTGE0AfcNCDLewmOTBtOoqDByLK", "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("820a87b3-5e36-4bc4-b10d-187617d6af84"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("820a87b3-5e36-4bc4-b10d-187617d6af84"), "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotAndStatus(522.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotAndStatus(522.0, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotPendingAndStatus(5177.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotPendingAndStatus(5177.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByIdAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(40413L, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(40413L, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectIdAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(40413L, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(40413L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByWhoAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("6I64rAl0dslEXUoXBHr7FzcMf0JLTUtAVe0zFCx0cbSq0TExD5", "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus("6I64rAl0dslEXUoXBHr7FzcMf0JLTUtAVe0zFCx0cbSq0TExD5", "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectWhoAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("6I64rAl0dslEXUoXBHr7FzcMf0JLTUtAVe0zFCx0cbSq0TExD5", "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus("6I64rAl0dslEXUoXBHr7FzcMf0JLTUtAVe0zFCx0cbSq0TExD5", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("5a402419-8c55-4b4a-8ba0-81d95d6d89bb"), "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("5a402419-8c55-4b4a-8ba0-81d95d6d89bb"), "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectExternalUUIDAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("5a402419-8c55-4b4a-8ba0-81d95d6d89bb"), "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("5a402419-8c55-4b4a-8ba0-81d95d6d89bb"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(1630.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(1630.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(1630.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(1630.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(6046.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(6046.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotPendingAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(6046.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(6046.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
    }

    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingWhoById() {
        // scenario
        String whoUpdateMock = "3fJfW6JLHq33EeXkq6DnkjJgXKdzWVufazehvPjrSWjcmPypse";
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
        UUID externalUUIDUpdateMock = UUID.fromString("40269733-6850-4b5e-ab8a-28669d276e35");
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
        Double jackpotUpdateMock = 6008.0;
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
        Double jackpotPendingUpdateMock = 5080.0;
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

}

