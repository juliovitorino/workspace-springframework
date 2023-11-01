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
import com.jwick.continental.deathagreement.builder.BetDTOBuilder;
import com.jwick.continental.deathagreement.builder.BetModelBuilder;
import com.jwick.continental.deathagreement.dto.BetDTO;
import com.jwick.continental.deathagreement.exception.BetNotFoundException;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.repository.BetRepository;
import com.jwick.continental.deathagreement.service.BetService;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
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
public class BetServiceImplTest {
    private static final UUID uuidMock = UUID.fromString("3dc936e6-478e-4d21-b167-67dee8b730af");
    private static MockedStatic<UUID> uuidMockedStatic;
    private static MockedStatic<DateUtility> dateUtilityMockedStatic;

    public static final String BET_NOTFOUND_WITH_ID = "Bet não encontrada com id = ";
    public static final String BET_NOTFOUND_WITH_IDPUNTER = "Bet não encontrada com idPunter = ";
    public static final String BET_NOTFOUND_WITH_IDBETOBJECT = "Bet não encontrada com idBetObject = ";
    public static final String BET_NOTFOUND_WITH_BET = "Bet não encontrada com bet = ";
    public static final String BET_NOTFOUND_WITH_BITCOINADDRESS = "Bet não encontrada com bitcoinAddress = ";
    public static final String BET_NOTFOUND_WITH_TICKET = "Bet não encontrada com ticket = ";
    public static final String BET_NOTFOUND_WITH_DEATHDATE = "Bet não encontrada com deathDate = ";
    public static final String BET_NOTFOUND_WITH_STATUS = "Bet não encontrada com status = ";
    public static final String BET_NOTFOUND_WITH_DATECREATED = "Bet não encontrada com dateCreated = ";
    public static final String BET_NOTFOUND_WITH_DATEUPDATED = "Bet não encontrada com dateUpdated = ";


    @Mock
    private BetRepository betRepositoryMock;

    @InjectMocks
    private BetService betService;
    final DateTime dateTimeMock = Mockito.mock(DateTime.class);

    @BeforeAll
    public void setup() {
        Mockito.when(dateTimeMock.getToday()).thenReturn(DateUtility.getDate(12,10,2023));
        Mockito.when(dateTimeMock.now()).thenReturn(DateUtility.getDate(12,10,2023));

        betService = new BetServiceImpl();
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
    public void shouldReturnMapWithBetListWhenFindPageByFilterIsCalled() {
        // scenario
        Long id = 22300L;
        Long idPunter = 62320L;
        Long idBetObject = 17467L;
        Double bet = 175.0;
        String bitcoinAddress = "wts6Tf0RHgXyRtjpjOByaa37VhQnILyjtxauNvAKUMa14jBimj";
        UUID ticket = UUID.fromString("8b5dff79-b710-4c2f-ad45-672ba608d75f");
        String deathDate = "2025-10-07";
        String status = "Hv5T7WiRc1c7KulFuay3O3ruhpH9x3bInO8xr6pF1x5pOCRmjc";
        String dateCreated = "2025-10-07";
        String dateUpdated = "2025-10-07";


        Map<String, Object> mapFieldsRequestMock = new HashMap<>();
        mapFieldsRequestMock.put("id", id);
        mapFieldsRequestMock.put("idPunter", idPunter);
        mapFieldsRequestMock.put("idBetObject", idBetObject);
        mapFieldsRequestMock.put("bet", bet);
        mapFieldsRequestMock.put("bitcoinAddress", bitcoinAddress);
        mapFieldsRequestMock.put("ticket", ticket);
        mapFieldsRequestMock.put("deathDate", deathDate);
        mapFieldsRequestMock.put("status", status);
        mapFieldsRequestMock.put("dateCreated", dateCreated);
        mapFieldsRequestMock.put("dateUpdated", dateUpdated);


        RequestFilter requestFilterMock = new RequestFilter();
        requestFilterMock.setQtdeRegistrosPorPagina(25);
        requestFilterMock.setOrdemAsc(true);
        requestFilterMock.setPagina(0);
        requestFilterMock.setCamposFiltro(mapFieldsRequestMock);

        List<Bet> betsFromRepository = new ArrayList<>();
        betsFromRepository.add(BetModelBuilder.newBetModelTestBuilder().now());
        betsFromRepository.add(BetModelBuilder.newBetModelTestBuilder().now());
        betsFromRepository.add(BetModelBuilder.newBetModelTestBuilder().now());
        betsFromRepository.add(BetModelBuilder.newBetModelTestBuilder().now());

        List<BetDTO> betsFiltered = betsFromRepository
                .stream()
                .map(m->betService.toDTO(m))
                .collect(Collectors.toList());

        Map<String,Object> mapResponseMock = new HashMap<>();
        mapResponseMock.put("currentPage", 0);
        mapResponseMock.put("totalItems", 4);
        mapResponseMock.put("totalPages", 1);
        mapResponseMock.put("pageBetItems", betsFiltered);

        Pageable pageableMock = PageRequest.of(0,25);

        PageImpl<Bet> pagedResponse =
                new PageImpl<>(betsFromRepository,
                        pageableMock,
                        betsFromRepository.size());

        Mockito.when(betRepositoryMock.findBetByFilter(pageableMock,
            id,
            idPunter,
            idBetObject,
            bet,
            bitcoinAddress,
            ticket,
            deathDate,
            status,
            dateCreated,
            dateUpdated

        )).thenReturn(pagedResponse);

        // action
        Map<String, Object> result = betService.findPageByFilter(requestFilterMock);

        // validate
        Long currentPage = Long.valueOf(result.get("currentPage").toString());
        Long totalItems = Long.valueOf(result.get("totalItems").toString());
        Long totalPages = Long.valueOf(result.get("totalPages").toString());
        List<BetDTO> betsResult = (List<BetDTO>) result.get("pageBetItems");

        Assertions.assertEquals(0L, currentPage);
        Assertions.assertEquals(4L, totalItems);
        Assertions.assertEquals(1L, totalPages);
        Assertions.assertEquals(4L, betsResult.size());
    }


    @Test
    public void showReturnListOfBetWhenAskedForFindAllByStatus() {
        // scenario
        List<Bet> listOfBetModelMock = new ArrayList<>();
        listOfBetModelMock.add(BetModelBuilder.newBetModelTestBuilder().now());
        listOfBetModelMock.add(BetModelBuilder.newBetModelTestBuilder().now());

        Mockito.when(betRepositoryMock.findAllByStatus("A")).thenReturn(listOfBetModelMock);

        // action
        List<BetDTO> listOfBets = betService.findAllByStatus("A");

        // validate
        Assertions.assertTrue(!listOfBets.isEmpty());
        Assertions.assertEquals(2, listOfBets.size());
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 70168L;
        Optional<Bet> betNonExistentMock = Optional.empty();
        Mockito.when(betRepositoryMock.findById(idMock)).thenReturn(betNonExistentMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldThrowBetNotFoundExceptionWhenUpdateStatusByIdForInexistentId() {
        // scenario
        Long idMock = 4565L;
        Mockito.when(betRepositoryMock.findById(idMock))
                .thenThrow(new BetNotFoundException(BET_NOTFOUND_WITH_ID,
                HttpStatus.NOT_FOUND,
                BET_NOTFOUND_WITH_ID ));

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.updateStatusById(idMock, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404,exception.getHttpStatus().value());
    }
    @Test
    public void shouldReturnBetDTOAfterUpdateStatusById() {
        // scenario
        Long idMock = 84336L;
        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(idMock)
                        .idPunter(77738L)
                        .idBetObject(67584L)
                        .bet(114.0)
                        .bitcoinAddress("TAJbbbci1QaM5HQ8GSjhB6mH6Ie71HQjFSqAwUa0deqpx5r32U")
                        .ticket(UUID.fromString("3543d662-a887-4de1-8846-5f481b1e65c4"))
                        .deathDate(LocalDate.of(1004,10,7))

                        .status("X")
                        .now()
        );
        Bet betToSaveMock = betModelMock.orElse(null);
        Bet betSavedMck = BetModelBuilder.newBetModelTestBuilder()
                        .id(23640L)
                        .idPunter(70140L)
                        .idBetObject(87258L)
                        .bet(8448.0)
                        .bitcoinAddress("Ivua6YqL0fzcjelyB9hniyXx9oJXXcqu2OdIEvvmUFxIByf9Y7")
                        .ticket(UUID.fromString("9444feb6-3cb5-44bb-8f06-5af3ec68109b"))
                        .deathDate(LocalDate.of(2351,4,18))

                        .status("A")
                        .now();
        Mockito.when(betRepositoryMock.findById(idMock)).thenReturn(betModelMock);
        Mockito.when(betRepositoryMock.save(betToSaveMock)).thenReturn(betSavedMck);

        // action
        BetDTO result = betService.updateStatusById(idMock, "A");

        // validate
        Assertions.assertEquals("A",result.getStatus());

    }

    @Test
    public void shouldSearchBetByAnyNonExistenceIdAndReturnBetNotFoundException() {
        // scenario
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()-> betService.findById(-1000L));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldSearchBetByIdAndReturnDTO() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .id(46312L)
                .idPunter(35012L)
                .idBetObject(4723L)
                .bet(2054.0)
                .bitcoinAddress("IVLi0n6s3CkVw6YNcre4L7ryI6vMzHAnTEQTTIX853WIsWQbj0")
                .ticket(UUID.fromString("36fffe2a-1f86-405c-aabf-0fcd2c3592ac"))
                .deathDate(LocalDate.of(2653,9,25))

                .status("A")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now());
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findById(1L);

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldDeleteBetByIdWithSucess() {
        // scenario
        Optional<Bet> bet = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().id(1L).now());
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(bet);

        // action
        betService.delete(1L);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void whenTryDeleteNotExistenceBetShouldReturnBetNotFoundException() {
        // scenario
        Mockito.when(betRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(
                BetNotFoundException.class, () -> betService.delete(1L)
        );

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }

    @Test
    public void ShouldSaveUpdateExistingBetWithSucess() {
        // scenario
        BetDTO betDTOMock = BetDTOBuilder.newBetDTOTestBuilder()
                .id(851L)
                .idPunter(18475L)
                .idBetObject(28134L)
                .bet(7.0)
                .bitcoinAddress("pwypIJspMPBcAJJ2xezMNnA0noMwMe3m2svO1G5MSxr2YQWcoN")
                .ticket(UUID.fromString("b28848cf-824c-4ce9-b9a4-3f859d0a27fd"))
                .deathDate(LocalDate.of(2368,4,29))

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betMock = BetModelBuilder.newBetModelTestBuilder()
                .id(betDTOMock.getId())
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betSavedMock = BetModelBuilder.newBetModelTestBuilder()
                .id(betDTOMock.getId())
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betRepositoryMock.save(betMock)).thenReturn(betSavedMock);

        // action
        BetDTO betSaved = betService.salvar(betDTOMock);

        // validate
        Assertions.assertInstanceOf(BetDTO.class, betSaved);
        Assertions.assertNotNull(betSaved.getId());
    }

    @Test
    public void ShouldSaveNewBetWithSucess() {
        // scenario
        BetDTO betDTOMock = BetDTOBuilder.newBetDTOTestBuilder()
                .id(null)
                .idPunter(3008L)
                .idBetObject(14140L)
                .bet(1532.0)
                .bitcoinAddress("5QiwLbaB1rHK3fW0Io7S04DsQjrQWw9eaeJCUtQcxcKr2TlSeB")
                .ticket(UUID.fromString("294ffba3-2f7a-4cf1-9a02-24066633a7f0"))
                .deathDate(LocalDate.of(2607,2,16))

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betModelMock = BetModelBuilder.newBetModelTestBuilder()
                .id(null)
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();
        Bet betSavedMock = BetModelBuilder.newBetModelTestBuilder()
                .id(501L)
                .idPunter(betDTOMock.getIdPunter())
                .idBetObject(betDTOMock.getIdBetObject())
                .bet(betDTOMock.getBet())
                .bitcoinAddress(betDTOMock.getBitcoinAddress())
                .ticket(betDTOMock.getTicket())
                .deathDate(betDTOMock.getDeathDate())

                .status("P")
                .dateCreated(dateTimeMock.getToday())
                .dateUpdated(dateTimeMock.getToday())
                .now();

        Mockito.when(betRepositoryMock.save(betModelMock)).thenReturn(betSavedMock);

        // action
        BetDTO betSaved = betService.salvar(betDTOMock);

        // validate
        Assertions.assertInstanceOf(BetDTO.class, betSaved);
        Assertions.assertNotNull(betSaved.getId());
        Assertions.assertEquals("P",betSaved.getStatus());
    }

    @Test
    public void shouldExecutePartialUpdateWithSucess() {
        // scenario
        Map<String, Object> mapBetDTOMock = new HashMap<>();
        mapBetDTOMock.put(BetConstantes.IDPUNTER,4806L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,10251L);
        mapBetDTOMock.put(BetConstantes.BET,276.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"mN4PMpH8EbfJ6bwbeb22P8dlCHy7G3WKXxTsuyWJCU5oeMJUfH");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("9cf8d1b0-6d76-41d2-954b-87158cdf5d38"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(545,3,18));
        mapBetDTOMock.put(BetConstantes.STATUS,"USerY7gM0bp12d0q7P4RVGR0osTgSSbky3KKuV9JUPb7LYY53b");


        Optional<Bet> betModelMock = Optional.ofNullable(
                BetModelBuilder.newBetModelTestBuilder()
                        .id(26087L)
                        .idPunter(21667L)
                        .idBetObject(44727L)
                        .bet(1766.0)
                        .bitcoinAddress("JyJdT80PCYc5oArUktEP0sPj0zkJmsJlVTeeoqY0YHCqNOhmch")
                        .ticket(UUID.fromString("9577ec04-b896-4275-8c34-11b39cdc784f"))
                        .deathDate(LocalDate.of(8484,12,8))
                        .status("dKJjVlwShoCaxHaG56cpj6DY2RuUJv3XUILz53D03j2HaE2ViF")

                        .now()
        );

        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        boolean executed = betService.partialUpdate(1L, mapBetDTOMock);

        // validate
        Assertions.assertTrue(executed);

    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenTrySearchNotExistentId() {
        // scenario
        Map<String, Object> mapBetDTOMock = new HashMap<>();
        mapBetDTOMock.put(BetConstantes.IDPUNTER,33133L);
        mapBetDTOMock.put(BetConstantes.IDBETOBJECT,82064L);
        mapBetDTOMock.put(BetConstantes.BET,580.0);
        mapBetDTOMock.put(BetConstantes.BITCOINADDRESS,"N8hmxRevSDbatvBeLsdGCjJEHUatgtE1lwJ2GXQlTV59DpkF0R");
        mapBetDTOMock.put(BetConstantes.TICKET,UUID.fromString("fc64231f-3981-4b92-8755-bae6a293414e"));
        mapBetDTOMock.put(BetConstantes.DEATHDATE,LocalDate.of(4173,8,1));
        mapBetDTOMock.put(BetConstantes.STATUS,"wq52B0hnT3fP0xy0gIN3k1RWwXnyPW8TEcRI54CwcMwN5m71A4");


        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.partialUpdate(1L, mapBetDTOMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains("Bet não encontrada com id = "));
        Assertions.assertEquals(404,exception.getHttpStatus().value());

    }

    @Test
    public void shouldReturnBetListWhenFindAllBetByIdAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdAndStatus(70023L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdAndStatus(70023L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByIdPunterAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdPunterAndStatus(15768L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdPunterAndStatus(15768L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByIdBetObjectAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByIdBetObjectAndStatus(84462L, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByIdBetObjectAndStatus(84462L, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByBetAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByBetAndStatus(4606.0, "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBetAndStatus(4606.0, "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByBitcoinAddressAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByBitcoinAddressAndStatus("yieMBku1m09axQdGymn2PR3pfjPUFscMVIzjHRqpUh0kc8iyL3", "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByBitcoinAddressAndStatus("yieMBku1m09axQdGymn2PR3pfjPUFscMVIzjHRqpUh0kc8iyL3", "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByTicketAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByTicketAndStatus(UUID.fromString("d5c4adc4-e4c9-48a1-ba6e-7c69ebcaea5a"), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByTicketAndStatus(UUID.fromString("d5c4adc4-e4c9-48a1-ba6e-7c69ebcaea5a"), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByDeathDateAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByDeathDateAndStatus(LocalDate.of(1030,3,13), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDeathDateAndStatus(LocalDate.of(1030,3,13), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByDateCreatedAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByDateCreatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDateCreatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void shouldReturnBetListWhenFindAllBetByDateUpdatedAndStatus() {
        // scenario
        List<Bet> bets = Arrays.asList(
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now(),
            BetModelBuilder.newBetModelTestBuilder().now()
        );

        Mockito.when(betRepositoryMock.findAllByDateUpdatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A")).thenReturn(bets);

        // action
        List<BetDTO> result = betService.findAllBetByDateUpdatedAndStatus(Date.from(LocalDate.of(2025,10,7).atStartOfDay(ZoneId.systemDefault()).toInstant()), "A");

        // validate
        Assertions.assertInstanceOf(List.class, result);
        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(81506L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(81506L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(81506L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(81506L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdPunterAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(32338L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(32338L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdPunterAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(32338L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(32338L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByIdBetObjectAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(25083L, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(25083L, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetIdBetObjectAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(25083L, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(25083L, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBetAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(3810.0, "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(3810.0, "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBetAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(3810.0, "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(3810.0, "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByBitcoinAddressAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("wWpyycnCnOkIIR14XIwbPmVvt0GnpnWpGQvbzVrr2e3u9febVO", "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus("wWpyycnCnOkIIR14XIwbPmVvt0GnpnWpGQvbzVrr2e3u9febVO", "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetBitcoinAddressAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus("wWpyycnCnOkIIR14XIwbPmVvt0GnpnWpGQvbzVrr2e3u9febVO", "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus("wWpyycnCnOkIIR14XIwbPmVvt0GnpnWpGQvbzVrr2e3u9febVO", "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByTicketAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("585b7a9b-1641-4636-8baa-a6d696d6577c"), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(UUID.fromString("585b7a9b-1641-4636-8baa-a6d696d6577c"), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetTicketAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(UUID.fromString("585b7a9b-1641-4636-8baa-a6d696d6577c"), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(UUID.fromString("585b7a9b-1641-4636-8baa-a6d696d6577c"), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
    }
    @Test
    public void shouldReturnExistentBetDTOWhenFindBetByDeathDateAndStatus() {
        // scenario
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder().now());
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(67,4,13), "A")).thenReturn(1L);
        Mockito.when(betRepositoryMock.findById(1L)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(LocalDate.of(67,4,13), "A");

        // validate
        Assertions.assertInstanceOf(BetDTO.class,result);
    }
    @Test
    public void shouldReturnBetNotFoundExceptionWhenNonExistenceBetDeathDateAndStatus() {
        // scenario
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(LocalDate.of(67,4,13), "A")).thenReturn(0L);
        Mockito.when(betRepositoryMock.findById(0L)).thenReturn(Optional.empty());
        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(LocalDate.of(67,4,13), "A"));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
    }

    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdPunterById() {
        // scenario
        Long idPunterUpdateMock = 36555L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateIdPunterById(420L, idPunterUpdateMock);

        // action
        betService.updateIdPunterById(420L, idPunterUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateIdPunterById(420L, idPunterUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingIdBetObjectById() {
        // scenario
        Long idBetObjectUpdateMock = 42212L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateIdBetObjectById(420L, idBetObjectUpdateMock);

        // action
        betService.updateIdBetObjectById(420L, idBetObjectUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateIdBetObjectById(420L, idBetObjectUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingBetById() {
        // scenario
        Double betUpdateMock = 7784.0;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateBetById(420L, betUpdateMock);

        // action
        betService.updateBetById(420L, betUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateBetById(420L, betUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingBitcoinAddressById() {
        // scenario
        String bitcoinAddressUpdateMock = "W0CawT2j69jrjr9UWHn7obbH2Gf1GS20Pvrn0Hx1kwVPEHyJYm";
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateBitcoinAddressById(420L, bitcoinAddressUpdateMock);

        // action
        betService.updateBitcoinAddressById(420L, bitcoinAddressUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateBitcoinAddressById(420L, bitcoinAddressUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingTicketById() {
        // scenario
        UUID ticketUpdateMock = UUID.fromString("95b4e249-196c-420e-bd38-94270da07f08");
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateTicketById(420L, ticketUpdateMock);

        // action
        betService.updateTicketById(420L, ticketUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateTicketById(420L, ticketUpdateMock);
    }
    @Test
    public void shouldReturnBetDTOWhenUpdateExistingDeathDateById() {
        // scenario
        LocalDate deathDateUpdateMock = LocalDate.of(20,11,23);
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                        .id(420L)
                .now());
        Mockito.when(betRepositoryMock.findById(420L)).thenReturn(betModelMock);
        Mockito.doNothing().when(betRepositoryMock).updateDeathDateById(420L, deathDateUpdateMock);

        // action
        betService.updateDeathDateById(420L, deathDateUpdateMock);

        // validate
        Mockito.verify(betRepositoryMock,Mockito.times(1)).updateDeathDateById(420L, deathDateUpdateMock);
    }



    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 37763L;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .id(idMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdAndStatus(idMock);

        // validate
        Assertions.assertEquals(idMock, result.getId());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdAndStatusActiveAnonimous() {
        // scenario
        Long idMock = 37763L;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdAndStatus(idMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdAndStatus(idMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_ID));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdPunterAndStatusActiveAnonimous() {
        // scenario
        Long idPunterMock = 31510L;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .idPunter(idPunterMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(idPunterMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdPunterAndStatus(idPunterMock);

        // validate
        Assertions.assertEquals(idPunterMock, result.getIdPunter());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdPunterAndStatusActiveAnonimous() {
        // scenario
        Long idPunterMock = 31510L;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdPunterAndStatus(idPunterMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdPunterAndStatus(idPunterMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDPUNTER));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByIdBetObjectAndStatusActiveAnonimous() {
        // scenario
        Long idBetObjectMock = 73415L;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .idBetObject(idBetObjectMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(idBetObjectMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByIdBetObjectAndStatus(idBetObjectMock);

        // validate
        Assertions.assertEquals(idBetObjectMock, result.getIdBetObject());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByIdBetObjectAndStatusActiveAnonimous() {
        // scenario
        Long idBetObjectMock = 73415L;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByIdBetObjectAndStatus(idBetObjectMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByIdBetObjectAndStatus(idBetObjectMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_IDBETOBJECT));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByBetAndStatusActiveAnonimous() {
        // scenario
        Double betMock = 6152.0;
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .bet(betMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(betMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBetAndStatus(betMock);

        // validate
        Assertions.assertEquals(betMock, result.getBet());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByBetAndStatusActiveAnonimous() {
        // scenario
        Double betMock = 6152.0;
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByBetAndStatus(betMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBetAndStatus(betMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BET));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByBitcoinAddressAndStatusActiveAnonimous() {
        // scenario
        String bitcoinAddressMock = "qdy96DLcSlINzBuELYaF3hUfJhiy4MDTTH3qJ1uPlLoGAdymTg";
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .bitcoinAddress(bitcoinAddressMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus(bitcoinAddressMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByBitcoinAddressAndStatus(bitcoinAddressMock);

        // validate
        Assertions.assertEquals(bitcoinAddressMock, result.getBitcoinAddress());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByBitcoinAddressAndStatusActiveAnonimous() {
        // scenario
        String bitcoinAddressMock = "qdy96DLcSlINzBuELYaF3hUfJhiy4MDTTH3qJ1uPlLoGAdymTg";
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByBitcoinAddressAndStatus(bitcoinAddressMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByBitcoinAddressAndStatus(bitcoinAddressMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_BITCOINADDRESS));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByTicketAndStatusActiveAnonimous() {
        // scenario
        UUID ticketMock = UUID.fromString("2cb0761f-61c0-407f-9a6c-185d0bfe548c");
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .ticket(ticketMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(ticketMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByTicketAndStatus(ticketMock);

        // validate
        Assertions.assertEquals(ticketMock, result.getTicket());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByTicketAndStatusActiveAnonimous() {
        // scenario
        UUID ticketMock = UUID.fromString("2cb0761f-61c0-407f-9a6c-185d0bfe548c");
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByTicketAndStatus(ticketMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByTicketAndStatus(ticketMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_TICKET));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

    @Test
    public void showReturnExistingBetDTOWhenFindBetByDeathDateAndStatusActiveAnonimous() {
        // scenario
        LocalDate deathDateMock = LocalDate.of(2421,2,5);
        Long maxIdMock = 1972L;
        Optional<Bet> betModelMock = Optional.ofNullable(BetModelBuilder.newBetModelTestBuilder()
                .deathDate(deathDateMock)
                .now()
        );
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(deathDateMock, "A")).thenReturn(maxIdMock);
        Mockito.when(betRepositoryMock.findById(maxIdMock)).thenReturn(betModelMock);

        // action
        BetDTO result = betService.findBetByDeathDateAndStatus(deathDateMock);

        // validate
        Assertions.assertEquals(deathDateMock, result.getDeathDate());

    }
    @Test
    public void showReturnBetNotFoundExceptionWhenNonExistenceFindBetByDeathDateAndStatusActiveAnonimous() {
        // scenario
        LocalDate deathDateMock = LocalDate.of(6421,2,5);
        Long noMaxIdMock = 0L;
        Optional<Bet> betModelMock = Optional.empty();
        Mockito.when(betRepositoryMock.loadMaxIdByDeathDateAndStatus(deathDateMock, "A")).thenReturn(noMaxIdMock);
        Mockito.when(betRepositoryMock.findById(noMaxIdMock)).thenReturn(betModelMock);

        // action
        BetNotFoundException exception = Assertions.assertThrows(BetNotFoundException.class,
                ()->betService.findBetByDeathDateAndStatus(deathDateMock));

        // validate
        Assertions.assertTrue(exception.getMessage().contains(BET_NOTFOUND_WITH_DEATHDATE));
        Assertions.assertEquals(404, exception.getHttpStatus().value());

    }

}

