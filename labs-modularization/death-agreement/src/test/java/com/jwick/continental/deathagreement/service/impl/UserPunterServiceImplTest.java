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
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;
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
    public void shouldReturnUserPunterNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 47750L;
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
        Long idMock = 1007L;
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
        Long idMock = 5058L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(idMock)
                        .nickname("9R9wE07SrHY6E4GNLypmXB4BG7YJ0RuMqts50zhHuPNi3dT9MX")
                        .btcAddress("0XmOgHvd8RYiPnXP8SqKrSdoPAQU70YQU5vIXqtLQ5hRUs0Agf")

                        .status("X")
                        .now()
        );
        UserPunter userpunterToSaveMock = userpunterModelMock.orElse(null);
        UserPunter userpunterSavedMck = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(2051L)
                        .nickname("NSyNCioNmpIhBnVDYESuKmAMDYi4hUS4S1pt22dcJEXminfFAq")
                        .btcAddress("UqmIdCtEOKDL5O8oXCqBpFfMvP0uPBMDHA6Vpubl12tUHwfRiK")

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
                .id(62800L)
                .nickname("1mdGBsUEni2v1G8rSCj1qC6N0ShqafDobRiNuV2xPndakRWdAs")
                .btcAddress("mch4JyvWi4tsVuKgJxHdlWWh0fHrCfaC825zox0sxtm0pj92GO")

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
                .id(31825L)
                .nickname("D4gnTdIXnSwo68XIg10tJrsvVTW4W93EhLmxs0JnRS4RCBP8OE")
                .btcAddress("h4eALsW9TcUtlluNY0yyqOdjvz0brfrCcAtzprBofFfRWMmWUc")

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
                .nickname("HyfvurixkSr9F3qzbWJbjdupSIKCHUBsnqutrUDkaA8m6GcUvf")
                .btcAddress("ENNQ3vswQuwbLlQQ0JDBte53ViMMEw55xnPSwxgV80Di64rEVG")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"H8x00JLLdrvN0FKLenSzblWc0h4W23l9WlC4PEGcPiOd6nNuVO");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"tBN64haS17k5jpAFYKrYVtBgdeOcROplCzfNCQEBq26awny0u4");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"SoCyX4BzJ8pqG6HvKShzp3Wv9t6gYdf5omFaH952Bx5Adsl1BF");


        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(53328L)
                        .nickname("H0hsL0nVrjdF0odmaLoe6s4gxp7tXWH0fRPY09xcoBbYHykIFz")
                        .btcAddress("1QtbHd8VwnunjCYRszmoCzhEh5nvOQN5nDYA8kWUb5JPHssP2Y")
                        .status("5WgsJvHv9f5rbh7390SdFzPjUMJO4cqXa3Ms0Tbwpn1gYylJnj")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"HQ7V2KBc19QEED0135N1bgb2xSIuBgltrgMrufOc1ms8NfbcEi");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"VPn7OLjrMkdzujAdsS1ODtH6FPDxKnazOgK1Ea7uHeFWH2tebd");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"M4mysEL1TJzblMgnp7OadUr1wH1mR979yvWhavCfKP0IYMNmuJ");


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

        Mockito.when(userpunterRepositoryMock.findAllByIdAndStatus(74414L, "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByIdAndStatus(74414L, "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByNicknameAndStatus("dgjFzwNKlGNuYzYWqCuchPqIdyAWeBjhnisb09CtAMcerxN3dA", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByNicknameAndStatus("dgjFzwNKlGNuYzYWqCuchPqIdyAWeBjhnisb09CtAMcerxN3dA", "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByBtcAddressAndStatus("OW5ilGLjxpA8O67BMCBHS3QAi0H5DhODkfqHWuXIxHo5jNjf1l", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByBtcAddressAndStatus("OW5ilGLjxpA8O67BMCBHS3QAi0H5DhODkfqHWuXIxHo5jNjf1l", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByIdAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(5035L, "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByIdAndStatus(5035L, "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterIdAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(5035L, "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByIdAndStatus(5035L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByNicknameAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("W4pWFUSwancA0qBR38KMHnfQW0g48FbhyUXVuagJKxRCRFpR2O", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByNicknameAndStatus("W4pWFUSwancA0qBR38KMHnfQW0g48FbhyUXVuagJKxRCRFpR2O", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterNicknameAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("W4pWFUSwancA0qBR38KMHnfQW0g48FbhyUXVuagJKxRCRFpR2O", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByNicknameAndStatus("W4pWFUSwancA0qBR38KMHnfQW0g48FbhyUXVuagJKxRCRFpR2O", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_NICKNAME));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByBtcAddressAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("TUzIm0MG6VfGdrRWcCVLhpHpo9oenEyMaQpen9zwfbK00GpNyM", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByBtcAddressAndStatus("TUzIm0MG6VfGdrRWcCVLhpHpo9oenEyMaQpen9zwfbK00GpNyM", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterBtcAddressAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("TUzIm0MG6VfGdrRWcCVLhpHpo9oenEyMaQpen9zwfbK00GpNyM", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByBtcAddressAndStatus("TUzIm0MG6VfGdrRWcCVLhpHpo9oenEyMaQpen9zwfbK00GpNyM", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_BTCADDRESS));
    }

    @Test
    public void shouldReturnUserPunterDTOWhenUpdateExistingNicknameById() {
        // scenario
        String nicknameUpdateMock = "Udj3kxqruatm8hG5gW9fW4xSEb3ldmPTTgtKnhmINwFtjR0HzF";
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
        String btcAddressUpdateMock = "fD34F2g4MwirdrxNG2vg4lmYN0MFlV7D20R129Kiw7tHxIEU1I";
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
        Long idMock = 36213L;
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
        Long idMock = 36213L;
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
        String nicknameMock = "ETzp96tRxemq6BEp6f3hI1U1t3CRsaRgLgSLO8XVr2RlL16YJv";
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
        String nicknameMock = "ETzp96tRxemq6BEp6f3hI1U1t3CRsaRgLgSLO8XVr2RlL16YJv";
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
        String btcAddressMock = "z2dmE4tf563vhGebzsjfQpAyx74bp0yp2hTUEw9XMEgFw0uzEM";
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
        String btcAddressMock = "z2dmE4tf563vhGebzsjfQpAyx74bp0yp2hTUEw9XMEgFw0uzEM";
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

