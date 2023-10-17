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
                .id(84200L)
                .nickname("MTXOpNwAiu3c0rnRdyD9QQCa32V4O0j4HoJJgwC0sepfpLnDlt")
                .btcAddress("RFroNMX17PrdKqJik5EqMDskLr0wtEG0WA7f2xsMODrDnjv43A")

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
                .id(27046L)
                .nickname("q7lb2rwV0efgna0IuvgpwVHSoLjNNk1q8JnVMhG5jikRI8Ynmc")
                .btcAddress("965jkkI1pGV6Dya9KIaJtWgaKEr00DMtORG8D0Bk5eAkzyDW0e")

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
                .nickname("d7IRiKClysuKiylwIVWkPpA1AstgrsbMLM79mFJ1fO9N2iPl14")
                .btcAddress("NVRKE7IdSqwGs0CajfCJThv04FqzWKheULNFejNJDx3ON4k92o")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"zaFVLPbt6B2RKj7CgC4qAxngsPLjwJmL9MtNBtL4PykTAzbTdk");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"MfvzImRK7RuAyfsOWRT8nhFYIQaBwe80QFiy4A6QnLqcINAM4B");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"JwheOQq3uTrmwMm9WSsAExefzcOjFRSLn0M0F1gJdVvokasn93");


        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(84310L)
                        .nickname("1NK5ptopAmN6mwv2HtQC3QrlbD5wQDjcJXCqmLBWN53URsMR2f")
                        .btcAddress("vbmv9b0j9N5602qCSwj0Uo5CWEvUi6kubebyI7wPLnVt0GcHYI")
                        .status("605UgIbOSwhWUM4G1GGwjF1eHk369ixuYmwlvDfQrelBd11I41")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"U21YD35VgR6kE8215eBGovoGICm1KTkhOOSxAlwJNR8f07k6RB");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"fr2uqVC0XbwM84QhiebDrtJnT7wb0FhnfnxWq3zIv0i74IjWhx");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"97cOu1R702l4JHtDlQ1SP8TRm6BONisoz9YzdAqA27uLxHHLGL");


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

        Mockito.when(userpunterRepositoryMock.findAllByIdAndStatus(1705L, "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByIdAndStatus(1705L, "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByNicknameAndStatus("T8uocs8lEX32IkAHcDeYxDslN2Jnmdg4YWbeB0rPwWfI9bflfz", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByNicknameAndStatus("T8uocs8lEX32IkAHcDeYxDslN2Jnmdg4YWbeB0rPwWfI9bflfz", "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByBtcAddressAndStatus("CrOG11FsnA9EHD2b4rI55fW9W5jv2XXC028gXlKNrgXmNnyvTx", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByBtcAddressAndStatus("CrOG11FsnA9EHD2b4rI55fW9W5jv2XXC028gXlKNrgXmNnyvTx", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByIdAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(5153L, "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByIdAndStatus(5153L, "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterIdAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(5153L, "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByIdAndStatus(5153L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByNicknameAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("8IHFo4NHNW6Ryo7XiJcRrp0iWShRzOxLbQeeYkO0xBqd9wIM6g", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByNicknameAndStatus("8IHFo4NHNW6Ryo7XiJcRrp0iWShRzOxLbQeeYkO0xBqd9wIM6g", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterNicknameAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("8IHFo4NHNW6Ryo7XiJcRrp0iWShRzOxLbQeeYkO0xBqd9wIM6g", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByNicknameAndStatus("8IHFo4NHNW6Ryo7XiJcRrp0iWShRzOxLbQeeYkO0xBqd9wIM6g", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_NICKNAME));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByBtcAddressAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("BjClvVprv9m31dBsUFi9c7QQIGf9Sld0fBwQLNx0US9sH9sPsA", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByBtcAddressAndStatus("BjClvVprv9m31dBsUFi9c7QQIGf9Sld0fBwQLNx0US9sH9sPsA", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterBtcAddressAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("BjClvVprv9m31dBsUFi9c7QQIGf9Sld0fBwQLNx0US9sH9sPsA", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByBtcAddressAndStatus("BjClvVprv9m31dBsUFi9c7QQIGf9Sld0fBwQLNx0US9sH9sPsA", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_BTCADDRESS));
    }

    @Test
    public void shouldReturnUserPunterDTOWhenUpdateExistingNicknameById() {
        // scenario
        String nicknameUpdateMock = "l74l705r40eXsIDf7MbIIKscowXoVlFIquRQ1W7nKzJqEvuWHT";
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
        String btcAddressUpdateMock = "RWuPzNLjdLjA50Ok40uxVpYLNMMHr9NTc4wgYYAsBYejcC6Qgs";
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

}

