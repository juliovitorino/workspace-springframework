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

import java.util.List;
import com.jwick.continental.deathagreement.model.Bet;
import com.jwick.continental.deathagreement.constantes.BetConstantes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;


/**
*
* BetRepository - Interface dos métodos de acesso aos dados da tabela Bet
* Camada de dados Bet - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor Bet
* @since Fri Oct 06 10:33:15 BRT 2023
*
*/
@Repository
public interface BetRepository extends JpaRepository<Bet, Long>
{
    @Query(value = "SELECT * FROM Bet WHERE  status = :status", nativeQuery = true)
    List<Bet> findAllByStatus(@Param(BetConstantes.STATUS) String status);

@Query(value = "SELECT * FROM Bet WHERE 1=1 " +
        "AND (:id = '' OR id = :id) " +
        "AND (:idPunter = '' OR idPunter = :idPunter) " +
        "AND (:idBetObject = '' OR idBetObject = :idBetObject) " +
        "AND (:bet = '' OR bet = :bet) " +
        "AND (:bitcoinAddress = '' OR bitcoinAddress = :bitcoinAddress) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
Page<Bet> findBetByFilter(Pageable pageable,
        @Param(BetConstantes.ID) Long id,
        @Param(BetConstantes.IDPUNTER) Long idPunter,
        @Param(BetConstantes.IDBETOBJECT) Long idBetObject,
        @Param(BetConstantes.BET) Double bet,
        @Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress,
        @Param(BetConstantes.STATUS) String status,
        @Param(BetConstantes.DATECREATED) Date dateCreated,
        @Param(BetConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM Bet WHERE 1=1 " +
        "AND (:id = '' OR id = :id) " +
        "AND (:idPunter = '' OR idPunter = :idPunter) " +
        "AND (:idBetObject = '' OR idBetObject = :idBetObject) " +
        "AND (:bet = '' OR bet = :bet) " +
        "AND (:bitcoinAddress = '' OR bitcoinAddress = :bitcoinAddress) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
List<Bet> findBetByFilter(
        @Param(BetConstantes.ID) Long id,
        @Param(BetConstantes.IDPUNTER) Long idPunter,
        @Param(BetConstantes.IDBETOBJECT) Long idBetObject,
        @Param(BetConstantes.BET) Double bet,
        @Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress,
        @Param(BetConstantes.STATUS) String status,
        @Param(BetConstantes.DATECREATED) Date dateCreated,
        @Param(BetConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE id = :id AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE idPunter = :idPunter AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdPunterAndStatus(Long idPunter, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE idBetObject = :idBetObject AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdBetObjectAndStatus(Long idBetObject, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE bet = :bet AND status = :status ", nativeQuery = true)
     Long loadMaxIdByBetAndStatus(Double bet, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE bitcoinAddress = :bitcoinAddress AND status = :status ", nativeQuery = true)
     Long loadMaxIdByBitcoinAddressAndStatus(String bitcoinAddress, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE dateCreated = :dateCreated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM Bet WHERE dateUpdated = :dateUpdated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Modifying
     @Query(value = "UPDATE Bet SET idPunter = :idPunter, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateIdPunterById(@Param("id") Long id, @Param(BetConstantes.IDPUNTER) Long idPunter);
     @Modifying
     @Query(value = "UPDATE Bet SET idBetObject = :idBetObject, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateIdBetObjectById(@Param("id") Long id, @Param(BetConstantes.IDBETOBJECT) Long idBetObject);
     @Modifying
     @Query(value = "UPDATE Bet SET bet = :bet, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateBetById(@Param("id") Long id, @Param(BetConstantes.BET) Double bet);
     @Modifying
     @Query(value = "UPDATE Bet SET bitcoinAddress = :bitcoinAddress, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateBitcoinAddressById(@Param("id") Long id, @Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress);
     @Modifying
     @Query(value = "UPDATE Bet SET status = :status, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(BetConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE Bet SET dateCreated = :dateCreated, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(BetConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE Bet SET dateUpdated = :dateUpdated, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(BetConstantes.DATEUPDATED) Date dateUpdated);


     long countByIdAndStatus(Long id, String status);
     long countByIdPunterAndStatus(Long idPunter, String status);
     long countByIdBetObjectAndStatus(Long idBetObject, String status);
     long countByBetAndStatus(Double bet, String status);
     long countByBitcoinAddressAndStatus(String bitcoinAddress, String status);
     long countByDateCreatedAndStatus(Date dateCreated, String status);
     long countByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE id = :id AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE idPunter = :idPunter AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByIdPunterAndStatus(Long idPunter, String status);
    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE idBetObject = :idBetObject AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByIdBetObjectAndStatus(Long idBetObject, String status);
    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE bet = :bet AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByBetAndStatus(Double bet, String status);
    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE bitcoinAddress = :bitcoinAddress AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByBitcoinAddressAndStatus(String bitcoinAddress, String status);
    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE dateCreated = :dateCreated AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM Bet WHERE id = (SELECT MAX(id) AS maxid FROM Bet WHERE dateUpdated = :dateUpdated AND  status = :status) ", nativeQuery = true)
    Optional<Bet> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM Bet WHERE id = :id AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM Bet WHERE idPunter = :idPunter AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByIdPunterAndStatus(Long idPunter, String status);
     @Query(value = "SELECT * FROM Bet WHERE idBetObject = :idBetObject AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByIdBetObjectAndStatus(Long idBetObject, String status);
     @Query(value = "SELECT * FROM Bet WHERE bet = :bet AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByBetAndStatus(Double bet, String status);
     @Query(value = "SELECT * FROM Bet WHERE bitcoinAddress = :bitcoinAddress AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByBitcoinAddressAndStatus(String bitcoinAddress, String status);
     @Query(value = "SELECT * FROM Bet WHERE dateCreated = :dateCreated AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM Bet WHERE dateUpdated = :dateUpdated AND  status = :status ", nativeQuery = true)
     List<Bet> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM Bet WHERE id = :id", nativeQuery = true)
    void deleteById(@Param(BetConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE idPunter = :idPunter", nativeQuery = true)
    void deleteByIdPunter(@Param(BetConstantes.IDPUNTER) Long idPunter);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE idBetObject = :idBetObject", nativeQuery = true)
    void deleteByIdBetObject(@Param(BetConstantes.IDBETOBJECT) Long idBetObject);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE bet = :bet", nativeQuery = true)
    void deleteByBet(@Param(BetConstantes.BET) Double bet);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE bitcoinAddress = :bitcoinAddress", nativeQuery = true)
    void deleteByBitcoinAddress(@Param(BetConstantes.BITCOINADDRESS) String bitcoinAddress);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE status = :status", nativeQuery = true)
    void deleteByStatus(@Param(BetConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE dateCreated = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(BetConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM Bet WHERE dateUpdated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(BetConstantes.DATEUPDATED) Date dateUpdated);

}
