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
import com.jwick.continental.deathagreement.builder.UserPunterDTOBuilder;
import com.jwick.continental.deathagreement.builder.UserPunterModelBuilder;
import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.exception.UserPunterNotFoundException;
import com.jwick.continental.deathagreement.model.UserPunter;
import com.jwick.continental.deathagreement.repository.UserPunterRepository;
import com.jwick.continental.deathagreement.service.UserPunterService;
import com.jwick.continental.deathagreement.constantes.UserPunterConstantes;
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
public class UserPunterServiceImplTest {
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;

    public static final String USERPUNTER_NOTFOUND_WITH_ID = "UserPunter não encontrada com id = ";
    public static final String USERPUNTER_NOTFOUND_WITH_NICKNAME = "UserPunter não encontrada com nickname = ";
    public static final String USERPUNTER_NOTFOUND_WITH_BTCADDRESS = "UserPunter não encontrada com btcAddress = ";
    public static final String USERPUNTER_NOTFOUND_WITH_STATUS = "UserPunter não encontrada com status = ";
    public static final String USERPUNTER_NOTFOUND_WITH_DATECREATED = "UserPunter não encontrada com dateCreated = ";
    public static final String USERPUNTER_NOTFOUND_WITH_DATEUPDATED = "UserPunter não encontrada com dateUpdated = ";


    @Mock
    private UserPunterRepository userpunterRepositoryMock;

    @InjectMocks
    private UserPunterService userpunterService;
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);

    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));
        Mockito.when(dateTimeMock.now()).thenReturn(DateUtility.getDate(12,10,2023));

        userpunterService = new UserPunterServiceImpl();
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
    public void shouldReturnMapWithUserPunterListWhenFindPageByFilterIsCalled() {
        // scenario
        Long id = 71628L;
        String nickname = "MTab8wLIo3AmzgY3YIlNU7g55SjkRATMnqma9hidwcx8LXcIVm";
        String btcAddress = "1ENnyiObVrcQ065iYlmRbpCCBpRYYUcICU0CwKg7PAicVzu7d9";
        String status = "RoToTtsEw6THPqq30MiGjep3MwDarTvbYiQ7Kz9YjLRwWK0k3V";
        String dateCreated = "2025-10-07";
        String dateUpdated = "2025-10-07";


        Map<String, Object> mapFieldsRequestMock = new HashMap<>();
        mapFieldsRequestMock.put("id", id);
        mapFieldsRequestMock.put("nickname", nickname);
        mapFieldsRequestMock.put("btcAddress", btcAddress);
        mapFieldsRequestMock.put("status", status);
        mapFieldsRequestMock.put("dateCreated", dateCreated);
        mapFieldsRequestMock.put("dateUpdated", dateUpdated);


        RequestFilter requestFilterMock = new RequestFilter();
        requestFilterMock.setQtdeRegistrosPorPagina(25);
        requestFilterMock.setOrdemAsc(true);
        requestFilterMock.setPagina(0);
        requestFilterMock.setCamposFiltro(mapFieldsRequestMock);

        List<UserPunter> userpuntersFromRepository = new ArrayList<>();
        userpuntersFromRepository.add(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        userpuntersFromRepository.add(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        userpuntersFromRepository.add(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        userpuntersFromRepository.add(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());

        List<UserPunterDTO> userpuntersFiltered = userpuntersFromRepository
                .stream()
                .map(m->userpunterService.toDTO(m))
                .collect(Collectors.toList());

        Map<String,Object> mapResponseMock = new HashMap<>();
        mapResponseMock.put("currentPage", 0);
        mapResponseMock.put("totalItems", 4);
        mapResponseMock.put("totalPages", 1);
        mapResponseMock.put("pageUserPunterItems", userpuntersFiltered);

        Pageable pageableMock = PageRequest.of(0,25);

        PageImpl<UserPunter> pagedResponse =
                new PageImpl<>(userpuntersFromRepository,
                        pageableMock,
                        userpuntersFromRepository.size());

        Mockito.when(userpunterRepositoryMock.findUserPunterByFilter(pageableMock,
            id,
            nickname,
            btcAddress,
            status,
            dateCreated,
            dateUpdated

        )).thenReturn(pagedResponse);

        // action
        Map<String, Object> result = userpunterService.findPageByFilter(requestFilterMock);

        // validate
        Long currentPage = Long.valueOf(result.get("currentPage").toString());
        Long totalItems = Long.valueOf(result.get("totalItems").toString());
        Long totalPages = Long.valueOf(result.get("totalPages").toString());
        List<UserPunterDTO> userpuntersResult = (List<UserPunterDTO>) result.get("pageUserPunterItems");

        Assertions.assertEquals(0L, currentPage);
        Assertions.assertEquals(4L, totalItems);
        Assertions.assertEquals(1L, totalPages);
        Assertions.assertEquals(4L, userpuntersResult.size());
    }


    @Test
    public void showReturnListOfUserPunterWhenAskedForFindAllByStatus() {
        // scenario
        List<UserPunter> listOfUserPunterModelMock = new ArrayList<>();
        listOfUserPunterModelMock.add(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        listOfUserPunterModelMock.add(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());

        Mockito.when(userpunterRepositoryMock.findAllByStatus("A")).thenReturn(listOfUserPunterModelMock);

        // action
        List<UserPunterDTO> listOfUserPunters = userpunterService.findAllByStatus("A");

        // validate
        Assertions.assertTrue(!listOfUserPunters.isEmpty());
        Assertions.assertEquals(2, listOfUserPunters.size());
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 55053L;
        Optional<UserPunter> userpunterNonExistentMock = Optional.empty();
        Mockito.when(userpunterRepositoryMock.findById(idMock)).thenReturn(userpunterNonExistentMock);

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldThrowUserPunterNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 36370L;
        Mockito.when(userpunterRepositoryMock.findById(idMock))
                .thenThrow(new UserPunterNotFoundException(USERPUNTER_NOTFOUND_WITH_ID,
                HttpStatus.NOT_FOUND,
                USERPUNTER_NOTFOUND_WITH_ID ));

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldReturnUserPunterDTOAfterUpdateStatusById() {
        // scenario
        Long idMock = 7630L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(idMock)
                        .nickname("6IKIEEaHW23RJW8rc76rbLBKoUCoBfzXksgFB04VefcKfOXsOc")
                        .btcAddress("t8d1Tr5rst3oxory12SDJXVTYlsQDFSHUHrABtGSdJvcLaFEi9")

                        .status("X")
                        .now()
        );
        UserPunter userpunterToSaveMock = userpunterModelMock.orElse(null);
        UserPunter userpunterSavedMck = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(70081L)
                        .nickname("qeFXXGUw7GUu9v9j9m66VEtyGD5xvEWoY6ca0yoYRNveysxsc4")
                        .btcAddress("YVO1o3yzj6o3mNFNliQ0K5E6xuzdodikJRrgV7Be0yTsXI5dGC")

                        .status("A")
                        .now();
        Mockito.when(userpunterRepositoryMock.findById(idMock)).thenReturn(userpunterModelMock);
        Mockito.when(userpunterRepositoryMock.save(userpunterToSaveMock)).thenReturn(userpunterSavedMck);

        // action
        UserPunterDTO result = userpunterService.updateStatusById(idMock, "A");

        // validate
        Assertions.assertEquals("A",result.getStatus());

    }

    @Test
    public void shouldSearchUserPunterByAnyNonExistenceIdAndReturnUserPunterNotFoundException() {
        // scenario
        Mockito.when(userpunterRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()-> userpunterService.findById(-1000L));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldSearchUserPunterByIdAndReturnDTO() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .id(55761L)
                .nickname("SOsB7df0jsEswjpblfEqDXxmaegJDy141zoINUmSTwjfW0g8Vl")
                .btcAddress("qiQAR7uiUHgSxmLeUb3pd9RHV3WTVtS9fgOTIQQktrOmDWmDVY")

                .status("A")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now());
        Mockito.when(userpunterRepositoryMock.findById(Mockito.anyLong())).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findById(1L);

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldDeleteUserPunterByIdWithSucess() {
        // scenario
        Optional<UserPunter> userpunter = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().id(1L).now());
        Mockito.when(userpunterRepositoryMock.findById(Mockito.anyLong())).thenReturn(userpunter);

        // action
        userpunterService.delete(1L);

        // validate
        Mockito.verify(userpunterRepositoryMock,Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenTryDeleteNotExistenceUserPunterShouldReturnUserPunterNotFoundException() {
        // scenario
        Mockito.when(userpunterRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(
                UserPunterNotFoundException.class, () -> userpunterService.delete(1L)
        );

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }

    @Test
    public void ShouldSaveUpdateExistingUserPunterWithSucess() {
        // scenario
        UserPunterDTO userpunterDTOMock = UserPunterDTOBuilder.newUserPunterDTOTestBuilder()
                .id(522L)
                .nickname("IIlMdlnprQsIGv5Sq0ofxpUrJm7HPN7DfIus03gDOOr4s06GyD")
                .btcAddress("v7tavKfjJYrTm8AKpjTrK9sHqdvVmDYv3R5uNxCJ0VX8dgF3qV")

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        UserPunter userpunterMock = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .id(userpunterDTOMock.getId())
                .nickname(userpunterDTOMock.getNickname())
                .btcAddress(userpunterDTOMock.getBtcAddress())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        UserPunter userpunterSavedMock = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .id(userpunterDTOMock.getId())
                .nickname(userpunterDTOMock.getNickname())
                .btcAddress(userpunterDTOMock.getBtcAddress())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(userpunterRepositoryMock.save(userpunterMock)).thenReturn(userpunterSavedMock);

        // action
        UserPunterDTO userpunterSaved = userpunterService.salvar(userpunterDTOMock);

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class, userpunterSaved);
        Assertions.assertNotNull(userpunterSaved.getId());
    }

    @Test
    public void ShouldSaveNewUserPunterWithSucess() {
        // scenario
        UserPunterDTO userpunterDTOMock = UserPunterDTOBuilder.newUserPunterDTOTestBuilder()
                .id(null)
                .nickname("M8Iv8DsC4mLGB6xu0rQpngE0XF55ebYzzngoax3GAqElGEdYPF")
                .btcAddress("sz3crSSHuCYBJSHa1lB9sBMJQjEW2fPt0Fmuut2z2DHDsoVvPx")

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        UserPunter userpunterModelMock = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .id(null)
                .nickname(userpunterDTOMock.getNickname())
                .btcAddress(userpunterDTOMock.getBtcAddress())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        UserPunter userpunterSavedMock = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .id(501L)
                .nickname(userpunterDTOMock.getNickname())
                .btcAddress(userpunterDTOMock.getBtcAddress())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(userpunterRepositoryMock.save(userpunterModelMock)).thenReturn(userpunterSavedMock);

        // action
        UserPunterDTO userpunterSaved = userpunterService.salvar(userpunterDTOMock);

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class, userpunterSaved);
        Assertions.assertNotNull(userpunterSaved.getId());
        Assertions.assertEquals("P",userpunterSaved.getStatus());
    }

    @Test
    public void shouldExecutePartialUpdateWithSucess() {
        // scenario
        Map<String, Object> mapUserPunterDTOMock = new HashMap<>();
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"mCQ86gnIJsSeQXXY0zI4TJEbfcQnkpPoMhVSQSoSGm779xr3OK");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"E86XRBJkvMXe0yjSqwAY8XTnHbeRSDqITpQxPDMvbycM1sKgnp");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"PfGMn3R3L7xzM4lbriaPXa0tDxvnRn32JddTJYCwPbiIWIHRSY");


        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(4774L)
                        .nickname("vnXxwgbCfRAPKYbxd489u0oIqiAnWxUfDNvNtBsdlvRSnyrQAp")
                        .btcAddress("RKsl38nDyLyW5xXoFWMvd753CLguPXC3qRTtpjpKfi3ai72QeQ")
                        .status("Evsp8upySTKhn5SuH9aNyLwCIhHCPp6jPV3atl7sihmOPJp5WR")

                        .now()
        );

        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        boolean executed = userpunterService.partialUpdate(1L, mapUserPunterDTOMock);

        // validate
        Assertions.assertTrue(executed);

    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenTrySearchNotExistentId() {
        // scenario
        Map<String, Object> mapUserPunterDTOMock = new HashMap<>();
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"vGqbyhrUvpIDI0kmcAmPkMb65kXTnnBSnM7Q3DrwcwVWObvu1Q");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"VfQxr35zYlwwSsQwhd0Xg3SGg4xsWf0x3Qze39Ip27FnVi7kJY");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"LzCiruJB0yUolsPTqXBGiK47Q3GlbvBzA1eP9SbE02cPG75gqS");


        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.partialUpdate(1L, mapUserPunterDTOMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains("UserPunter não encontrada com id = "));
        Assertions.assertEquals(404,exception.getHttpStatus().value());

    }

    @Test
    public void shouldReturnUserPunterListWhenFindAllUserPunterByIdAndStatus() {
        // scenario
        List<UserPunter> userpunters = Arrays.asList(
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now()
        );

        Mockito.when(userpunterRepositoryMock.findAllByIdAndStatus(18377L, "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByIdAndStatus(18377L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnUserPunterListWhenFindAllUserPunterByNicknameAndStatus() {
        // scenario
        List<UserPunter> userpunters = Arrays.asList(
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now()
        );

        Mockito.when(userpunterRepositoryMock.findAllByNicknameAndStatus("mGs2W1cwuXTWx722zdOg9aPkohUCBKRvv3ngCeSxebKxPqnTmD", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByNicknameAndStatus("mGs2W1cwuXTWx722zdOg9aPkohUCBKRvv3ngCeSxebKxPqnTmD", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnUserPunterListWhenFindAllUserPunterByBtcAddressAndStatus() {
        // scenario
        List<UserPunter> userpunters = Arrays.asList(
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now()
        );

        Mockito.when(userpunterRepositoryMock.findAllByBtcAddressAndStatus("7NORJSodQR08v9AaJkFgBVgRpuhV9O701QVwT6BxyjbRXLUSDN", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByBtcAddressAndStatus("7NORJSodQR08v9AaJkFgBVgRpuhV9O701QVwT6BxyjbRXLUSDN", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnUserPunterListWhenFindAllUserPunterByDateCreatedAndStatus() {
        // scenario
        List<UserPunter> userpunters = Arrays.asList(
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now()
        );

        Mockito.when(userpunterRepositoryMock.findAllByDateCreatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByDateCreatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnUserPunterListWhenFindAllUserPunterByDateUpdatedAndStatus() {
        // scenario
        List<UserPunter> userpunters = Arrays.asList(
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now(),
            UserPunterModelBuilder.newUserPunterModelTestBuilder().now()
        );

        Mockito.when(userpunterRepositoryMock.findAllByDateUpdatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByDateUpdatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByIdAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(32742L, "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByIdAndStatus(32742L, "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterIdAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(32742L, "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByIdAndStatus(32742L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByNicknameAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("LQD1mF0NpC1TPYMKBCfWp0E1r1o4Wf1U0lF30UiXBsrnG1Elho", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByNicknameAndStatus("LQD1mF0NpC1TPYMKBCfWp0E1r1o4Wf1U0lF30UiXBsrnG1Elho", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterNicknameAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("LQD1mF0NpC1TPYMKBCfWp0E1r1o4Wf1U0lF30UiXBsrnG1Elho", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByNicknameAndStatus("LQD1mF0NpC1TPYMKBCfWp0E1r1o4Wf1U0lF30UiXBsrnG1Elho", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_NICKNAME));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByBtcAddressAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("DXDJmvbdnTJrMt5j0LkjL6KRjQRpzqTcyuvCm0xK5bVLPqAsAw", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByBtcAddressAndStatus("DXDJmvbdnTJrMt5j0LkjL6KRjQRpzqTcyuvCm0xK5bVLPqAsAw", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterBtcAddressAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("DXDJmvbdnTJrMt5j0LkjL6KRjQRpzqTcyuvCm0xK5bVLPqAsAw", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByBtcAddressAndStatus("DXDJmvbdnTJrMt5j0LkjL6KRjQRpzqTcyuvCm0xK5bVLPqAsAw", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_BTCADDRESS));
    }

    @Test
    public void shouldReturnUserPunterDTOWhenUpdateExistingNicknameById() {
        // scenario
        String nicknameUpdateMock = "TXtrzxiqHNXtd5w3l7jpCBP6NjDx0ywWrbtGHgG7N1TueO00vF";
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(userpunterRepositoryMock.findById(420L)).thenReturn(userpunterModelMock);
        Mockito.doNothing().when(userpunterRepositoryMock).updateNicknameById(420L, nicknameUpdateMock);

        // action
        userpunterService.updateNicknameById(420L, nicknameUpdateMock);

        // validate
        Mockito.verify(userpunterRepositoryMock,Mockito.times(1)).updateNicknameById(420L, nicknameUpdateMock);
    }
    @Test
    public void shouldReturnUserPunterDTOWhenUpdateExistingBtcAddressById() {
        // scenario
        String btcAddressUpdateMock = "0HwshI2lxQdSuYodDO8rcNBNsnQ8XreiKKFxKD8dHV1A6Hk3Gx";
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(userpunterRepositoryMock.findById(420L)).thenReturn(userpunterModelMock);
        Mockito.doNothing().when(userpunterRepositoryMock).updateBtcAddressById(420L, btcAddressUpdateMock);

        // action
        userpunterService.updateBtcAddressById(420L, btcAddressUpdateMock);

        // validate
        Mockito.verify(userpunterRepositoryMock,Mockito.times(1)).updateBtcAddressById(420L, btcAddressUpdateMock);
    }



    @Test
    public void showReturnExistingUserPunterDTOWhenFindUserPunterByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 82160L;
        Long maxIdMock = 1972L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .id(idMock)
                .now()
        );
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(maxIdMock);
        Mockito.when(userpunterRepositoryMock.findById(maxIdMock)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByIdAndStatus(idMock);

        // validate
        Assertions.assertEquals(idMock, result.getId());

    }
    @Test
    public void showReturnUserPunterNotFoundExceptionWhenNonExistenceFindUserPunterByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 82160L;
        Long noMaxIdMock = 0L;
        Optional<UserPunter> userpunterModelMock = Optional.empty();
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(userpunterRepositoryMock.findById(noMaxIdMock)).thenReturn(userpunterModelMock);

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByIdAndStatus(idMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingUserPunterDTOWhenFindUserPunterByNicknameAndStatusActiveAnonimous() {
        // scenario
        String nicknameMock = "GLaj2jICYpd0aJjQbvQ1jNptckSvEki9LpiRU2zbnr0Ux7aPmg";
        Long maxIdMock = 1972L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .nickname(nicknameMock)
                .now()
        );
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus(nicknameMock, "A")).thenReturn(maxIdMock);
        Mockito.when(userpunterRepositoryMock.findById(maxIdMock)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByNicknameAndStatus(nicknameMock);

        // validate
        Assertions.assertEquals(nicknameMock, result.getNickname());

    }
    @Test
    public void showReturnUserPunterNotFoundExceptionWhenNonExistenceFindUserPunterByNicknameAndStatusActiveAnonimous() {
        // scenario
        String nicknameMock = "GLaj2jICYpd0aJjQbvQ1jNptckSvEki9LpiRU2zbnr0Ux7aPmg";
        Long noMaxIdMock = 0L;
        Optional<UserPunter> userpunterModelMock = Optional.empty();
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus(nicknameMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(userpunterRepositoryMock.findById(noMaxIdMock)).thenReturn(userpunterModelMock);

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByNicknameAndStatus(nicknameMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_NICKNAME));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingUserPunterDTOWhenFindUserPunterByBtcAddressAndStatusActiveAnonimous() {
        // scenario
        String btcAddressMock = "pNRAMqbKSpLkXctxW80bF2lHkxu9PvgmOCvU9egIuoEvqgBjiw";
        Long maxIdMock = 1972L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder()
                .btcAddress(btcAddressMock)
                .now()
        );
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus(btcAddressMock, "A")).thenReturn(maxIdMock);
        Mockito.when(userpunterRepositoryMock.findById(maxIdMock)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByBtcAddressAndStatus(btcAddressMock);

        // validate
        Assertions.assertEquals(btcAddressMock, result.getBtcAddress());

    }
    @Test
    public void showReturnUserPunterNotFoundExceptionWhenNonExistenceFindUserPunterByBtcAddressAndStatusActiveAnonimous() {
        // scenario
        String btcAddressMock = "pNRAMqbKSpLkXctxW80bF2lHkxu9PvgmOCvU9egIuoEvqgBjiw";
        Long noMaxIdMock = 0L;
        Optional<UserPunter> userpunterModelMock = Optional.empty();
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus(btcAddressMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(userpunterRepositoryMock.findById(noMaxIdMock)).thenReturn(userpunterModelMock);

        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByBtcAddressAndStatus(btcAddressMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_BTCADDRESS));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

}

