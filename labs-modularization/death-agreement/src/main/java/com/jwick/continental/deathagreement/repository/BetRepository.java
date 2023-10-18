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


package com.jwick.continental.deathagreement.repository;

import com.jwick.continental.deathagreement.constantes.BetConstantes;
import com.jwick.continental.deathagreement.model.Bet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
*
* BetRepository - Interface dos métodos de acesso aos dados da tabela Bet
* Camada de dados Bet - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor Bet
* @since Fri Oct 06 17:19:07 BRT 2023
*
*/
@Repository
public interface BetRepository extends JpaRepository<Bet, Long>
{
    @Query(value = "SELECT * FROM Bet WHERE  status = :status", nativeQuery = true)
    List<Bet> findAllByStatus(@Param(BetConstantes.STATUS) String status);

@Query(value = "SELECT * FROM Bet WHERE 1=1 " +
        "AND (:id = '' OR id_bet = :id) " +
        "AND (:idPunter = '' OR id_punter = :idPunter) " +
        "AND (:idBetObject = '' OR id_bet_object = :idBetObject) " +
        "AND (:bet = '' OR bet = :bet) " +
        "AND (:bitcoinAddress = '' OR btc_address = :bitcoinAddress) " +
        "AND (:ticket = '' OR ticket = :ticket) " +
        "AND (:deathDate = '' OR death_date = :deathDate) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR date_created = :dateCreated) " +
        "AND (:dateUpdated = '' OR date_updated = :dateUpdated) " 

        , nativeQuery = true)
Page<Bet> findBetByFilter(Pageable pageable,
        @Param(BetConstantes.ID) Long id,
        @Param(BetConstantes.IDPUNTER) Long idPunter,
        @Param(BetConstantes.IDBETOBJECT) Long idBetObject,
        @Param(BetConstantes.BET) Double bet,
        @Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress,
        @Param(BetConstantes.TICKET) UUID ticket,
        @Param(BetConstantes.DEATHDATE) Date deathDate,
        @Param(BetConstantes.STATUS) String status,
        @Param(BetConstantes.DATECREATED) Date dateCreated,
        @Param(BetConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM Bet WHERE 1=1 " +
        "AND (:id = '' OR id_bet = :id) " +
        "AND (:idPunter = '' OR id_punter = :idPunter) " +
        "AND (:idBetObject = '' OR id_bet_object = :idBetObject) " +
        "AND (:bet = '' OR bet = :bet) " +
        "AND (:bitcoinAddress = '' OR btc_address = :bitcoinAddress) " +
        "AND (:ticket = '' OR ticket = :ticket) " +
        "AND (:deathDate = '' OR death_date = :deathDate) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR date_created = :dateCreated) " +
        "AND (:dateUpdated = '' OR date_updated = :dateUpdated) " 

        , nativeQuery = true)
List<Bet> findBetByFilter(
        @Param(BetConstantes.ID) Long id,
        @Param(BetConstantes.IDPUNTER) Long idPunter,
        @Param(BetConstantes.IDBETOBJECT) Long idBetObject,
        @Param(BetConstantes.BET) Double bet,
        @Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress,
        @Param(BetConstantes.TICKET) UUID ticket,
        @Param(BetConstantes.DEATHDATE) Date deathDate,
        @Param(BetConstantes.STATUS) String status,
        @Param(BetConstantes.DATECREATED) Date dateCreated,
        @Param(BetConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_bet = :id AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_punter = :idPunter AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdPunterAndStatus(Long idPunter, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_punter = :idPunter AND id_bet_object = :idBetObject AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdPunterAndIdBetObjectAndStatus(Long idPunter, Long idBetObject, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_bet_object = :idBetObject AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdBetObjectAndStatus(Long idBetObject, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE bet = :bet AND status = :status ", nativeQuery = true)
     Long loadMaxIdByBetAndStatus(Double bet, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE btc_address = :bitcoinAddress AND status = :status ", nativeQuery = true)
     Long loadMaxIdByBitcoinAddressAndStatus(String bitcoinAddress, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE ticket = :ticket AND status = :status ", nativeQuery = true)
     Long loadMaxIdByTicketAndStatus(UUID ticket, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE death_date = :deathDate AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDeathDateAndStatus(LocalDate deathDate, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE date_created = :dateCreated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id_bet) AS maxid FROM Bet WHERE date_updated = :dateUpdated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Modifying
     @Query(value = "UPDATE Bet SET id_punter = :idPunter, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateIdPunterById(@Param("id") Long id, @Param(BetConstantes.IDPUNTER) Long idPunter);
     @Modifying
     @Query(value = "UPDATE Bet SET id_bet_object = :idBetObject, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateIdBetObjectById(@Param("id") Long id, @Param(BetConstantes.IDBETOBJECT) Long idBetObject);
     @Modifying
     @Query(value = "UPDATE Bet SET bet = :bet, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateBetById(@Param("id") Long id, @Param(BetConstantes.BET) Double bet);
     @Modifying
     @Query(value = "UPDATE Bet SET btc_address = :bitcoinAddress, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateBitcoinAddressById(@Param("id") Long id, @Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress);
     @Modifying
     @Query(value = "UPDATE Bet SET ticket = :ticket, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateTicketById(@Param("id") Long id, @Param(BetConstantes.TICKET) UUID ticket);
     @Modifying
     @Query(value = "UPDATE Bet SET death_date = :deathDate, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateDeathDateById(@Param("id") Long id, @Param(BetConstantes.DEATHDATE) LocalDate deathDate);
     @Modifying
     @Query(value = "UPDATE Bet SET status = :status, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(BetConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE Bet SET date_created = :dateCreated, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(BetConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE Bet SET date_updated = :dateUpdated, dt_updated = current_timestamp  WHERE id_bet = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(BetConstantes.DATEUPDATED) Date dateUpdated);

    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_bet = :id AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_punter = :idPunter AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByIdPunterAndStatus(Long idPunter, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE id_bet_object = :idBetObject AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByIdBetObjectAndStatus(Long idBetObject, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE bet = :bet AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByBetAndStatus(Double bet, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE btc_address = :bitcoinAddress AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByBitcoinAddressAndStatus(String bitcoinAddress, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE ticket = :ticket AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByTicketAndStatus(UUID ticket, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE death_date = :deathDate AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByDeathDateAndStatus(Date deathDate, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE date_created = :dateCreated AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM Bet WHERE id_bet = (SELECT MAX(id_bet) AS maxid FROM Bet WHERE date_updated = :dateUpdated AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM Bet WHERE id_bet = :id AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM Bet WHERE id_punter = :idPunter AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByIdPunterAndStatus(Long idPunter, String status);
     @Query(value = "SELECT * FROM Bet WHERE id_bet_object = :idBetObject AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByIdBetObjectAndStatus(Long idBetObject, String status);
     @Query(value = "SELECT * FROM Bet WHERE bet = :bet AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByBetAndStatus(Double bet, String status);
     @Query(value = "SELECT * FROM Bet WHERE btc_address = :bitcoinAddress AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByBitcoinAddressAndStatus(String bitcoinAddress, String status);
     @Query(value = "SELECT * FROM Bet WHERE ticket = :ticket AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByTicketAndStatus(UUID ticket, String status);
     @Query(value = "SELECT * FROM Bet WHERE id_punter = :idPunter AND id_bet_object = :idBetObject AND date_part('YEAR',death_date) = :yearBet AND date_part('MONTH',death_date) = :monthBet  AND  status = :status ORDER BY id_bet", nativeQuery = true)
     List<Bet> findAllBetByIdPunterAndIdBetObjectAndYearMonthAndStatus(Long idPunter, Long idBetObject, int yearBet, int monthBet, String status);
     @Query(value = "SELECT * FROM Bet WHERE death_date = :deathDate AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByDeathDateAndStatus(LocalDate deathDate, String status);
     @Query(value = "SELECT * FROM Bet WHERE date_created = :dateCreated AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM Bet WHERE date_updated = :dateUpdated AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM Bet WHERE id_bet = :id", nativeQuery = true)
    void deleteById(@Param(BetConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE id_punter = :idPunter", nativeQuery = true)
    void deleteByIdPunter(@Param(BetConstantes.IDPUNTER) Long idPunter);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE id_bet_object = :idBetObject", nativeQuery = true)
    void deleteByIdBetObject(@Param(BetConstantes.IDBETOBJECT) Long idBetObject);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE bet = :bet", nativeQuery = true)
    void deleteByBet(@Param(BetConstantes.BET) Double bet);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE btc_address = :bitcoinAddress", nativeQuery = true)
    void deleteByBitcoinAddress(@Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE ticket = :ticket", nativeQuery = true)
    void deleteByTicket(@Param(BetConstantes.TICKET) UUID ticket);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE death_date = :deathDate", nativeQuery = true)
    void deleteByDeathDate(@Param(BetConstantes.DEATHDATE) LocalDate deathDate);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE status = :status", nativeQuery = true)
    void deleteByStatus(@Param(BetConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE date_created = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(BetConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE date_updated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(BetConstantes.DATEUPDATED) Date dateUpdated);

    @Query(value = "select count(*) from bet b where to_char(b.death_date, 'YYYY-MM-DD') = :deathDateBet " +
            "and b.id_bet_object = :idBetObject " +
            "and b.status = 'A'", nativeQuery = true)
    Long countBetsAtDayForBetObject(String deathDateBet, Long idBetObject);
}
