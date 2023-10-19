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
    }

    @AfterAll
    public void tearDown() {
        uuidMockedStatic.close();
        dateUtilityMockedStatic.close();
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
        Long idMock = 4005L;
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
        Long idMock = 1745L;
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
        Long idMock = 37746L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(idMock)
                        .nickname("zw4LcQVLURObtoYQE7XIFX0I9eFsHp62nX0qvUi0mO33CT1b0q")
                        .btcAddress("7hWRJVXOULeuCliQgvLz8OSwYrHVlgWnL84Uf5AyHJ5FAi8Emg")

                        .status("X")
                        .now()
        );
        UserPunter userpunterToSaveMock = userpunterModelMock.orElse(null);
        UserPunter userpunterSavedMck = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(81715L)
                        .nickname("5AoOJDlqJUBNd6uzCNhtJSdF3mDigxbIyY108fyrLp1HjhLr10")
                        .btcAddress("ML4deXmtDOlfRpvd0AElord94TXQbjP5OXkqHq2lPk9RSUnIUA")

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
                .id(5484L)
                .nickname("vooO6kJ8gW3KSHEMpodgUuDwKCOTFwO9eOHRwsPe9paVLXmdSf")
                .btcAddress("d0iXwvKrq0IEp63DnrWTKRzelj8I7TD4WJORlFUkA7nkVamO4O")

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
                .id(75608L)
                .nickname("xt9aES7lnV076VptWGoweQjk2wLawfrtiyCLGUow9T8iC6GX6q")
                .btcAddress("yHC5ESUO0m0XPxKb0RUD8FqN1JSsGkM2dncO0yqBSS0ldOzCA0")

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
                .nickname("6APtYhqEBn9XWHdcweGaFxfJydiTvdBEsCltEqfKYdmHUlKPXI")
                .btcAddress("YJvo2COpYr93SD006Ycy10YU2YOChbOQHQCvaaWVOjoz741Dvw")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"2hFewtU6ms4aKS5VGhqndVdQk0ryGaQIOIYcco1uteJepsgAXQ");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"Ofn1xg9gjRlCGpVnzSV3yzW3i4lruCP0FhRUo7upd1G2Hga85f");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"xm4dTx5y7Y3yJnFRFphrELRNrRux2fb13Cs8kYBWX2rrWxNXhW");


        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(44423L)
                        .nickname("2ry2JFBHhe6wyC4SHc8YkBpT0dNg0IOwFJJlBOePWKax7wudQN")
                        .btcAddress("iNL3oEVbFyr2su9QegpFsyIuW43pqSvBE6zk26ihmpHd8B0d1a")
                        .status("66P2hfk9WstKvyb9NaETJ72r9n9tp4uKG9EOuX5aE3nYictfj1")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"e7sREs2OTrBXqyA0gQCT3kYcENaiOwRI0HDlsc6WeyI8yl3gr5");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"QDo9qOJ0skC1uxbF85fqWo7N30mm7s0tjUAgtmPVN48UwW7uPc");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"otMb6obm8R9xS6kas5eKkbL0dObnnFpaQonxhFEb4B2vVo9FRz");


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

        Mockito.when(userpunterRepositoryMock.findAllByIdAndStatus(55328L, "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByIdAndStatus(55328L, "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByNicknameAndStatus("gXfNmDPNgP8U2wxYAasnfDGVwieNyXwzcsQINaYvgTGbjHajcn", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByNicknameAndStatus("gXfNmDPNgP8U2wxYAasnfDGVwieNyXwzcsQINaYvgTGbjHajcn", "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByBtcAddressAndStatus("oRWLdphejH3t4XPIqhRpdmQdvJIBercwzG0UX7Xyew8qBzRqXF", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByBtcAddressAndStatus("oRWLdphejH3t4XPIqhRpdmQdvJIBercwzG0UX7Xyew8qBzRqXF", "A");

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
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(64182L, "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByIdAndStatus(64182L, "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterIdAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(64182L, "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByIdAndStatus(64182L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByNicknameAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("fwCuYbunWQPBOUTXl1vDDgMxtTLo232KmKQtrCDgr83yGjYHTR", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByNicknameAndStatus("fwCuYbunWQPBOUTXl1vDDgMxtTLo232KmKQtrCDgr83yGjYHTR", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterNicknameAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("fwCuYbunWQPBOUTXl1vDDgMxtTLo232KmKQtrCDgr83yGjYHTR", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByNicknameAndStatus("fwCuYbunWQPBOUTXl1vDDgMxtTLo232KmKQtrCDgr83yGjYHTR", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_NICKNAME));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByBtcAddressAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("eg0M6goFGHa2SmwbcrqjG4uwwg2TgeKRyMWQnnHcubg03hevvz", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByBtcAddressAndStatus("eg0M6goFGHa2SmwbcrqjG4uwwg2TgeKRyMWQnnHcubg03hevvz", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterBtcAddressAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("eg0M6goFGHa2SmwbcrqjG4uwwg2TgeKRyMWQnnHcubg03hevvz", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByBtcAddressAndStatus("eg0M6goFGHa2SmwbcrqjG4uwwg2TgeKRyMWQnnHcubg03hevvz", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_BTCADDRESS));
    }

    @Test
    public void shouldReturnUserPunterDTOWhenUpdateExistingNicknameById() {
        // scenario
        String nicknameUpdateMock = "Bn1F4in9qDzErHfazbF5UAX48LJeTATDp8eY7iq693MNDC2nTT";
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
        String btcAddressUpdateMock = "epAeHs3F9frPDS7ao36xEXPvuWWP7oSAvPRdEyaLGnESNTC7uz";
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
        Long idMock = 0L;
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
        Long idMock = 0L;
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
        String nicknameMock = "qomV9syDbx1nQLHx063OMMvc25ttL67PfrWMC27gmNjEoMXY30";
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
        String nicknameMock = "qomV9syDbx1nQLHx063OMMvc25ttL67PfrWMC27gmNjEoMXY30";
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
        String btcAddressMock = "vC1NK1mJi8YupVOKdFJDFpM38bCNSvpXXP4rAdUyaE4OehoK6e";
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
        String btcAddressMock = "vC1NK1mJi8YupVOKdFJDFpM38bCNSvpXXP4rAdUyaE4OehoK6e";
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

