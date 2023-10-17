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
import com.jwick.continental.deathagreement.constantes.UserPunterConstantes;
import com.jwick.continental.deathagreement.dto.UserPunterDTO;
import com.jwick.continental.deathagreement.exception.UserPunterNotFoundException;
import com.jwick.continental.deathagreement.model.UserPunter;
import com.jwick.continental.deathagreement.repository.UserPunterRepository;
import com.jwick.continental.deathagreement.service.UserPunterService;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        Long idMock = 74136L;
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
        Long idMock = 35016L;
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
        Long idMock = 40712L;
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(42185L)
                        .nickname("FJ01USJgtFCbJQhxmot5qfMtOQ434BB6oFUPIJzxzv1qqNhqv3")
                        .btcAddress("ESxGPzx5qWmRSiyABOtXm9JL8Kmiy8lMTg4sceOwuXrDcCBEGz")
                        .status("X")
                        .now()
        );
        UserPunter userpunterToSaveMock = userpunterModelMock.orElse(null);
        UserPunter userpunterSavedMck = UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(4331L)
                        .nickname("V4hlpq57e1T6G7CA3GxSdayGPvlcS81YwoBAvWAnEtAFxyi2m8")
                        .btcAddress("hSgfKL09fk3XyNE0xhwC2QLSuumyiJy0ShwQDBPYkmgOVurbul")
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
                .id(63208L)
                .nickname("d0pYvMzusN7SAV8bKALbVa0GOOmK6Ajy60Cbdp4tFp6x16zWR6")
                .btcAddress("LvGeH9M3sI0CG8Vm2P0plRk34e6IqwJEBf4Ua0v0m8vj2Fygp1")

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
                .id(17283L)
                .nickname("z7aOshMPEavQKmvcXLn7qU3Dj2LnumtcK8OFSbzeAXcFEveU9g")
                .btcAddress("5UaaCj0eAN4OUwlDSAb0egn074ETG8utlcWQJiEiT8OzItN37e")

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
                .nickname("bOYCmDrmcDyipaRvOfc3QModlC1JxBR4qBAOcBSo5UsPMlbOFA")
                .btcAddress("fRGLMW1mcy2E3mPEVzgJpa04RvzeVhBQDHWVQv0eUI1IVTqsPv")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"35WB7zkoYGH7QMBQxdpKmMm49i6mqLRVi73hIOfjIS1wetie0q");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"OU25Tsmmg0594bCxMe0Y4cj0rbssAPeJleTIKUIwotF9ncRstN");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"Xva1BtOCQq4yK8r0rlAYgjVncqkqc8WCUxe0DsEg0CnLi1mIy0");


        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(
                UserPunterModelBuilder.newUserPunterModelTestBuilder()
                        .id(13770L)
                        .nickname("h1jnX12vrNHWCYFlhyLNTbA5o7JCUzDAXXnAkB1oLLwDgffYdB")
                        .btcAddress("FyPlmufF0HRYIJd4p8mLkrWzdoN9KXX8doo9ivUYyMoRYYf3Bt")
                        .status("V2w5CMGqOl8l1uFsPurRBqquatDQktzxMHGNARLr7TxaXtlQCB")

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
        mapUserPunterDTOMock.put(UserPunterConstantes.NICKNAME,"sfePisdIt0tBrY8n9J69j2xBgMxdwyhqWYuuVO2lw8wPO6NLxm");
        mapUserPunterDTOMock.put(UserPunterConstantes.BTCADDRESS,"foFk0LdUV2RU2GceS3Plqkmw85LzI5aeJa4l4LE4jHJ88klyt6");
        mapUserPunterDTOMock.put(UserPunterConstantes.STATUS,"WwqtLTNHvAaJqex0hhccl2kBYwvzJMhSbPmoCsz7lHr2SF0B0S");


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

        Mockito.when(userpunterRepositoryMock.findAllByIdAndStatus(66380L, "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByIdAndStatus(66380L, "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByNicknameAndStatus("F96dEH3COmuAXKQn10OxvPDD3B8TOmMMX43wcyQF0GizAgGeJX", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByNicknameAndStatus("F96dEH3COmuAXKQn10OxvPDD3B8TOmMMX43wcyQF0GizAgGeJX", "A");

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

        Mockito.when(userpunterRepositoryMock.findAllByBtcAddressAndStatus("2kMng2CahXbAST8wxKrTHNDAvzLJBwTqqKPIQOC8CmtBj8ASV8", "A")).thenReturn(userpunters);

        // action
        List<UserPunterDTO> result = userpunterService.findAllUserPunterByBtcAddressAndStatus("2kMng2CahXbAST8wxKrTHNDAvzLJBwTqqKPIQOC8CmtBj8ASV8", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByIdAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(83688L, "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByIdAndStatus(83688L, "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterIdAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByIdAndStatus(83688L, "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByIdAndStatus(83688L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByNicknameAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("t8Rq3dY7OjUJ19oxaKjbh0rcatDtsSio3MGOaE0ieljUS5Ecvy", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByNicknameAndStatus("t8Rq3dY7OjUJ19oxaKjbh0rcatDtsSio3MGOaE0ieljUS5Ecvy", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterNicknameAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByNicknameAndStatus("t8Rq3dY7OjUJ19oxaKjbh0rcatDtsSio3MGOaE0ieljUS5Ecvy", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByNicknameAndStatus("t8Rq3dY7OjUJ19oxaKjbh0rcatDtsSio3MGOaE0ieljUS5Ecvy", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_NICKNAME));
    }
    @Test
    public void shouldReturnExistentUserPunterDTOWhenFindUserPunterByBtcAddressAndStatus() {
        // scenario
        Optional<UserPunter> userpunterModelMock = Optional.ofNullable(UserPunterModelBuilder.newUserPunterModelTestBuilder().now());
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("KhQlBiFMKLwPNEuhCv0HuPMiS7KAgnit6zYJs1qltDR2hSirWg", "A")).thenReturn(1L);
        Mockito.when(userpunterRepositoryMock.findById(1L)).thenReturn(userpunterModelMock);

        // action
        UserPunterDTO result = userpunterService.findUserPunterByBtcAddressAndStatus("KhQlBiFMKLwPNEuhCv0HuPMiS7KAgnit6zYJs1qltDR2hSirWg", "A");

        // validate
        Assertions.assertInstanceOf(UserPunterDTO.class,result);
    }
    @Test
    public void shouldReturnUserPunterNotFoundExceptionWhenNonExistenceUserPunterBtcAddressAndStatus() {
        // scenario
        Mockito.when(userpunterRepositoryMock.loadMaxIdByBtcAddressAndStatus("KhQlBiFMKLwPNEuhCv0HuPMiS7KAgnit6zYJs1qltDR2hSirWg", "A")).thenReturn(0L);
        Mockito.when(userpunterRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        UserPunterNotFoundException exception = Assertions.assertThrows(UserPunterNotFoundException.class,
                ()->userpunterService.findUserPunterByBtcAddressAndStatus("KhQlBiFMKLwPNEuhCv0HuPMiS7KAgnit6zYJs1qltDR2hSirWg", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(USERPUNTER_NOTFOUND_WITH_BTCADDRESS));
    }

    @Test
    public void shouldReturnUserPunterDTOWhenUpdateExistingNicknameById() {
        // scenario
        String nicknameUpdateMock = "qIsmqKn9c49cQNi680pqMdqziT79DoT5QvTp72PcfDcMryPWUN";
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
        String btcAddressUpdateMock = "uG1s1wYdOKmQdHEsAJpOsk0uRzb64fAicFgJ55cPyHHfNnOixb";
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

