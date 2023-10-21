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

import br.com.jcv.commons.library.commodities.dto.RequestFilter;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
import java.util.stream.Collectors;

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

        uuidMockedStatic.when(() -> UUID.fromString(Mockito.anyString())).thenReturn(uuidMock);
    }

    @AfterAll
    public void tearDown() {
        uuidMockedStatic.close();
        dateUtilityMockedStatic.close();
    }

    @Test
    public void shouldReturnMapWithBetObjectListWhenFindPageByFilterIsCalled() {
        // scenario
        Long id = 30084L;
        String who = "4COh74Hb2RJ0bEUtmAelWT0ptz9E6S4rVcMAmxLeaUjOXWwEwc";
        UUID externalUUID = UUID.fromString("a1d6a5c2-18f8-4b45-a57c-6490aa1f52a3");
        Double jackpot = 7667.0;
        Double jackpotPending = 46.0;
        String status = "6Hnqx0KVFSHHgOUIUKMl9ruAmp39E60F2zSjL48hslGRMEKILq";
        String dateCreated = "2025-10-07";
        String dateUpdated = "2025-10-07";


        Map<String, Object> mapFieldsRequestMock = new HashMap<>();
        mapFieldsRequestMock.put("id", id);
        mapFieldsRequestMock.put("who", who);
        mapFieldsRequestMock.put("externalUUID", externalUUID);
        mapFieldsRequestMock.put("jackpot", jackpot);
        mapFieldsRequestMock.put("jackpotPending", jackpotPending);
        mapFieldsRequestMock.put("status", status);
        mapFieldsRequestMock.put("dateCreated", dateCreated);
        mapFieldsRequestMock.put("dateUpdated", dateUpdated);


        RequestFilter requestFilterMock = new RequestFilter();
        requestFilterMock.setQtdeRegistrosPorPagina(25);
        requestFilterMock.setOrdemAsc(true);
        requestFilterMock.setPagina(0);
        requestFilterMock.setCamposFiltro(mapFieldsRequestMock);

        List<BetObject> betobjectsFromRepository = new ArrayList<>();
        betobjectsFromRepository.add(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        betobjectsFromRepository.add(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        betobjectsFromRepository.add(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        betobjectsFromRepository.add(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());

        List<BetObjectDTO> betobjectsFiltered = betobjectsFromRepository
                .stream()
                .map(m->betobjectService.toDTO(m))
                .collect(Collectors.toList());

        Map<String,Object> mapResponseMock = new HashMap<>();
        mapResponseMock.put("currentPage", 0);
        mapResponseMock.put("totalItems", 4);
        mapResponseMock.put("totalPages", 1);
        mapResponseMock.put("pageBetObjectItems", betobjectsFiltered);

        Pageable pageableMock = PageRequest.of(0,25);

        PageImpl<BetObject> pagedResponse =
                new PageImpl<>(betobjectsFromRepository,
                        pageableMock,
                        betobjectsFromRepository.size());

        Mockito.when(betobjectRepositoryMock.findBetObjectByFilter(pageableMock,
            id,
            who,
            externalUUID,
            jackpot,
            jackpotPending,
            status,
            dateCreated,
            dateUpdated

        )).thenReturn(pagedResponse);

        // action
        Map<String, Object> result = betobjectService.findPageByFilter(requestFilterMock);

        // validate
        Long currentPage = Long.valueOf(result.get("currentPage").toString());
        Long totalItems = Long.valueOf(result.get("totalItems").toString());
        Long totalPages = Long.valueOf(result.get("totalPages").toString());
        List<BetObjectDTO> betobjectsResult = (List<BetObjectDTO>) result.get("pageBetObjectItems");

        Assertions.assertEquals(0L, currentPage);
        Assertions.assertEquals(4L, totalItems);
        Assertions.assertEquals(1L, totalPages);
        Assertions.assertEquals(4L, betobjectsResult.size());
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
        Long idMock = 20260L;
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
        Long idMock = 20280L;
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
        Long idMock = 83330L;
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(idMock)
                        .who("u0P8PC5tLcz7yYJFacuKiYjAcKJVeaiaoN2OTdEfdOeqe6GNOa")
                        .externalUUID(UUID.fromString("cf9c78d6-8e08-47d4-a7e5-8fc7d059dd80"))
                        .jackpot(400.0)
                        .jackpotPending(4072.0)

                        .status("X")
                        .now()
        );
        BetObject betobjectToSaveMock = betobjectModelMock.orElse(null);
        BetObject betobjectSavedMck = BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(62501L)
                        .who("sOi5z2DdyB9yvLE0bGR4QLw0TKaN9XkluEb6JtMUv8ACztsoUN")
                        .externalUUID(UUID.fromString("e9273e4b-f2ed-49d8-8a02-9a6176937fd1"))
                        .jackpot(7274.0)
                        .jackpotPending(2444.0)

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
                .id(67476L)
                .who("0c8cKgjkRdPT8mmtKeqRvlQpqdJHVxSYkrXXJ0qAoEaI1vyk2x")
                .externalUUID(UUID.fromString("481386f2-83e6-4eba-a17a-ce89662e864f"))
                .jackpot(4605.0)
                .jackpotPending(1204.0)

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
                .id(16835L)
                .who("i0mBsm0ea0lagQsXTD8FXhvBm3L7p3wP5aMPnrLGCw5VYHE86S")
                .externalUUID(UUID.fromString("eefa2143-1170-4e64-bf4f-f0aa4d39ab40"))
                .jackpot(2012.0)
                .jackpotPending(4050.0)

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
                .who("SSDraxq00AwrJDGVifE10McLRJrXVL6SX84G9jFs49NuJUPzGF")
                .externalUUID(UUID.fromString("afea8051-19ab-4c07-ada9-27f6e9d43f95"))
                .jackpot(726.0)
                .jackpotPending(814.0)

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"nboSz18oP7qPR5ReyFs6Io046QeGPfzoAUGxJ3Nb1FnFrebXoA");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("bff07a61-cc66-42e3-bbf9-34b297ff9240"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,4724.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,4512.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"te8clzW3iLpQaQppJWGos2zs3pi70pkwLfMWXMyRkdXTmavtSh");


        Optional<BetObject> betobjectModelMock = Optional.ofNullable(
                BetObjectModelBuilder.newBetObjectModelTestBuilder()
                        .id(73400L)
                        .who("3uzsq2DYdxVWW10DMkjN6j09lpp2vpDMpE0tHI073PCN0kuG8T")
                        .externalUUID(UUID.fromString("4ce85a2e-aa7b-41ab-a5dc-77480bae84d7"))
                        .jackpot(6123.0)
                        .jackpotPending(6588.0)
                        .status("RCfL7AXNtOhy0topLnOpvyByF7cvNVzPy09ToNyewMRTtM1YkU")

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
        mapBetObjectDTOMock.put(BetObjectConstantes.WHO,"LSjxukNjVRf1dc0AvDpLYKBSCdTu4psaXbSrtBTjRx4mLLnlX6");
        mapBetObjectDTOMock.put(BetObjectConstantes.EXTERNALUUID,UUID.fromString("1d8438e2-8971-4787-9ac0-f28515d8a7b2"));
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOT,688.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.JACKPOTPENDING,8060.0);
        mapBetObjectDTOMock.put(BetObjectConstantes.STATUS,"T0lRf9XUBFEdVWFpWtI8e93DuhbCz2RnzGO7m0FuFq0kzGNiE7");


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

        Mockito.when(betobjectRepositoryMock.findAllByIdAndStatus(10011L, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByIdAndStatus(10011L, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByWhoAndStatus("p7mtIqmmblKlqk4Ygahqe8J5z5xmDlRqXzY3xx5aR552N4hsyl", "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByWhoAndStatus("p7mtIqmmblKlqk4Ygahqe8J5z5xmDlRqXzY3xx5aR552N4hsyl", "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByExternalUUIDAndStatus(UUID.fromString("2f759d1c-eb79-4e90-88ae-a97e8b28c927"), "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByExternalUUIDAndStatus(UUID.fromString("2f759d1c-eb79-4e90-88ae-a97e8b28c927"), "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotAndStatus(2501.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotAndStatus(2501.0, "A");

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

        Mockito.when(betobjectRepositoryMock.findAllByJackpotPendingAndStatus(7503.0, "A")).thenReturn(betobjects);

        // action
        List<BetObjectDTO> result = betobjectService.findAllBetObjectByJackpotPendingAndStatus(7503.0, "A");

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
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(6315L, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByIdAndStatus(6315L, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectIdAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByIdAndStatus(6315L, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByIdAndStatus(6315L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByWhoAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("WR9Nf5XTaOO7F0T0PwRAlNJPyAajwT6nGGP0M1qOqambQr3sIE", "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByWhoAndStatus("WR9Nf5XTaOO7F0T0PwRAlNJPyAajwT6nGGP0M1qOqambQr3sIE", "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectWhoAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByWhoAndStatus("WR9Nf5XTaOO7F0T0PwRAlNJPyAajwT6nGGP0M1qOqambQr3sIE", "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByWhoAndStatus("WR9Nf5XTaOO7F0T0PwRAlNJPyAajwT6nGGP0M1qOqambQr3sIE", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_WHO));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByExternalUUIDAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("a6c0630e-1cd7-4309-a02e-7bc2b2d9accd"), "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("a6c0630e-1cd7-4309-a02e-7bc2b2d9accd"), "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectExternalUUIDAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByExternalUUIDAndStatus(UUID.fromString("a6c0630e-1cd7-4309-a02e-7bc2b2d9accd"), "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByExternalUUIDAndStatus(UUID.fromString("a6c0630e-1cd7-4309-a02e-7bc2b2d9accd"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_EXTERNALUUID));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(7007.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotAndStatus(7007.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotAndStatus(7007.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotAndStatus(7007.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOT));
    }
    @Test
    public void shouldReturnExistentBetObjectDTOWhenFindBetObjectByJackpotPendingAndStatus() {
        // scenario
        Optional<BetObject> betobjectModelMock = Optional.ofNullable(BetObjectModelBuilder.newBetObjectModelTestBuilder().now());
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(330.0, "A")).thenReturn(1L);
        Mockito.when(betobjectRepositoryMock.findById(1L)).thenReturn(betobjectModelMock);

        // action
        BetObjectDTO result = betobjectService.findBetObjectByJackpotPendingAndStatus(330.0, "A");

        // validate
        Assertions.assertInstanceOf(BetObjectDTO.class,result);
    }
    @Test
    public void shouldReturnBetObjectNotFoundExceptionWhenNonExistenceBetObjectJackpotPendingAndStatus() {
        // scenario
        Mockito.when(betobjectRepositoryMock.loadMaxIdByJackpotPendingAndStatus(330.0, "A")).thenReturn(0L);
        Mockito.when(betobjectRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetObjectNotFoundException exception = Assertions.assertThrows(BetObjectNotFoundException.class,
                ()->betobjectService.findBetObjectByJackpotPendingAndStatus(330.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BETOBJECT_NOTFOUND_WITH_JACKPOTPENDING));
    }

    @Test
    public void shouldReturnBetObjectDTOWhenUpdateExistingWhoById() {
        // scenario
        String whoUpdateMock = "mABBVrEmgh9rAyPjCxhPcUgrTzePBkmT02zzzL53eUf9y8yFfH";
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
        UUID externalUUIDUpdateMock = UUID.fromString("9b76a9e1-505b-4991-a686-96afe93f0aa3");
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
        Double jackpotUpdateMock = 3160.0;
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
        Double jackpotPendingUpdateMock = 8355.0;
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
        Long idMock = 1105L;
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
        Long idMock = 1105L;
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
        String whoMock = "hoF10UFBo4GXt5PYhMhesnNLC0kXifQxOSDMWcaCht1lj7pHKb";
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
        String whoMock = "hoF10UFBo4GXt5PYhMhesnNLC0kXifQxOSDMWcaCht1lj7pHKb";
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
        UUID externalUUIDMock = UUID.fromString("5f1439c3-9184-4e8e-8073-7535076233db");
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
        UUID externalUUIDMock = UUID.fromString("5f1439c3-9184-4e8e-8073-7535076233db");
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
        Double jackpotMock = 8.0;
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
        Double jackpotMock = 8.0;
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
        Double jackpotPendingMock = 5301.0;
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
        Double jackpotPendingMock = 5301.0;
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

