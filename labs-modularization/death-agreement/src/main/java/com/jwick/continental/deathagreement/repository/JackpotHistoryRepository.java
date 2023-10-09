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

import com.jwick.continental.deathagreement.constantes.JackpotHistoryConstantes;
import com.jwick.continental.deathagreement.model.JackpotHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
*
* JackpotHistoryRepository - Interface dos métodos de acesso aos dados da tabela jackpot_history
* Camada de dados JackpotHistory - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor JackpotHistory
* @since Mon Oct 09 08:35:31 BRT 2023
*
*/
@Repository
public interface JackpotHistoryRepository extends JpaRepository<JackpotHistory, Long>
{
    @Query(value = "SELECT * FROM jackpot_history WHERE  status = :status", nativeQuery = true)
    List<JackpotHistory> findAllByStatus(@Param(JackpotHistoryConstantes.STATUS) String status);

@Query(value = "SELECT * FROM jackpot_history WHERE 1=1 " +
        "AND (:id = '' OR id_jackpot = :id) " +
        "AND (:description = '' OR description = :description) " +
        "AND (:type = '' OR type = :type) " +
        "AND (:betValue = '' OR bet_value = :betValue) " +
        "AND (:ticket = '' OR ticket = :ticket) " +
        "AND (:idPunter = '' OR id_punter = :idPunter) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR date_created = :dateCreated) " +
        "AND (:dateUpdated = '' OR date_updated = :dateUpdated) " 

        , nativeQuery = true)
Page<JackpotHistory> findJackpotHistoryByFilter(Pageable pageable,
        @Param(JackpotHistoryConstantes.ID) Long id,
        @Param(JackpotHistoryConstantes.DESCRIPTION) String description,
        @Param(JackpotHistoryConstantes.TYPE) String type,
        @Param(JackpotHistoryConstantes.BETVALUE) String betValue,
        @Param(JackpotHistoryConstantes.TICKET) UUID ticket,
        @Param(JackpotHistoryConstantes.IDPUNTER) Long idPunter,
        @Param(JackpotHistoryConstantes.STATUS) String status,
        @Param(JackpotHistoryConstantes.DATECREATED) Date dateCreated,
        @Param(JackpotHistoryConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM jackpot_history WHERE 1=1 " +
        "AND (:id = '' OR id_jackpot = :id) " +
        "AND (:description = '' OR description = :description) " +
        "AND (:type = '' OR type = :type) " +
        "AND (:betValue = '' OR bet_value = :betValue) " +
        "AND (:ticket = '' OR ticket = :ticket) " +
        "AND (:idPunter = '' OR id_punter = :idPunter) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR date_created = :dateCreated) " +
        "AND (:dateUpdated = '' OR date_updated = :dateUpdated) " 

        , nativeQuery = true)
List<JackpotHistory> findJackpotHistoryByFilter(
        @Param(JackpotHistoryConstantes.ID) Long id,
        @Param(JackpotHistoryConstantes.DESCRIPTION) String description,
        @Param(JackpotHistoryConstantes.TYPE) String type,
        @Param(JackpotHistoryConstantes.BETVALUE) String betValue,
        @Param(JackpotHistoryConstantes.TICKET) UUID ticket,
        @Param(JackpotHistoryConstantes.IDPUNTER) Long idPunter,
        @Param(JackpotHistoryConstantes.STATUS) String status,
        @Param(JackpotHistoryConstantes.DATECREATED) Date dateCreated,
        @Param(JackpotHistoryConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE id_jackpot = :id AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE description = :description AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDescriptionAndStatus(String description, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE type = :type AND status = :status ", nativeQuery = true)
     Long loadMaxIdByTypeAndStatus(String type, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE bet_value = :betValue AND status = :status ", nativeQuery = true)
     Long loadMaxIdByBetValueAndStatus(String betValue, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE ticket = :ticket AND status = :status ", nativeQuery = true)
     Long loadMaxIdByTicketAndStatus(UUID ticket, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE id_punter = :idPunter AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdPunterAndStatus(Long idPunter, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE date_created = :dateCreated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE date_updated = :dateUpdated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Modifying
     @Query(value = "UPDATE jackpot_history SET description = :description, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateDescriptionById(@Param("id") Long id, @Param(JackpotHistoryConstantes.DESCRIPTION) String description);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET type = :type, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateTypeById(@Param("id") Long id, @Param(JackpotHistoryConstantes.TYPE) String type);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET bet_value = :betValue, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateBetValueById(@Param("id") Long id, @Param(JackpotHistoryConstantes.BETVALUE) String betValue);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET ticket = :ticket, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateTicketById(@Param("id") Long id, @Param(JackpotHistoryConstantes.TICKET) UUID ticket);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET id_punter = :idPunter, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateIdPunterById(@Param("id") Long id, @Param(JackpotHistoryConstantes.IDPUNTER) Long idPunter);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET status = :status, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(JackpotHistoryConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET date_created = :dateCreated, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(JackpotHistoryConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE jackpot_history SET date_updated = :dateUpdated, dt_updated = current_timestamp  WHERE id_jackpot = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(JackpotHistoryConstantes.DATEUPDATED) Date dateUpdated);


     long countByIdAndStatus(Long id, String status);
     long countByDescriptionAndStatus(String description, String status);
     long countByTypeAndStatus(String type, String status);
     long countByBetValueAndStatus(String betValue, String status);
     long countByTicketAndStatus(UUID ticket, String status);
     long countByIdPunterAndStatus(Long idPunter, String status);
     long countByDateCreatedAndStatus(Date dateCreated, String status);
     long countByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE id_jackpot = :id AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE description = :description AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByDescriptionAndStatus(String description, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE type = :type AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByTypeAndStatus(String type, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE bet_value = :betValue AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByBetValueAndStatus(String betValue, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE ticket = :ticket AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByTicketAndStatus(UUID ticket, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE id_punter = :idPunter AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByIdPunterAndStatus(Long idPunter, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE date_created = :dateCreated AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = (SELECT MAX(id_jackpot) AS maxid FROM jackpot_history WHERE date_updated = :dateUpdated AND  status = :status) ", nativeQuery = true)
    Optional<JackpotHistory> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM jackpot_history WHERE id_jackpot = :id AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE description = :description AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByDescriptionAndStatus(String description, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE type = :type AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByTypeAndStatus(String type, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE bet_value = :betValue AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByBetValueAndStatus(String betValue, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE ticket = :ticket AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByTicketAndStatus(UUID ticket, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE id_punter = :idPunter AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByIdPunterAndStatus(Long idPunter, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE date_created = :dateCreated AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM jackpot_history WHERE date_updated = :dateUpdated AND  status = :status ", nativeQuery = true)
     List<JackpotHistory> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE id_jackpot = :id", nativeQuery = true)
    void deleteById(@Param(JackpotHistoryConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE description = :description", nativeQuery = true)
    void deleteByDescription(@Param(JackpotHistoryConstantes.DESCRIPTION) String description);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE type = :type", nativeQuery = true)
    void deleteByType(@Param(JackpotHistoryConstantes.TYPE) String type);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE bet_value = :betValue", nativeQuery = true)
    void deleteByBetValue(@Param(JackpotHistoryConstantes.BETVALUE) String betValue);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE ticket = :ticket", nativeQuery = true)
    void deleteByTicket(@Param(JackpotHistoryConstantes.TICKET) UUID ticket);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE id_punter = :idPunter", nativeQuery = true)
    void deleteByIdPunter(@Param(JackpotHistoryConstantes.IDPUNTER) Long idPunter);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE status = :status", nativeQuery = true)
    void deleteByStatus(@Param(JackpotHistoryConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE date_created = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(JackpotHistoryConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM jackpot_history WHERE date_updated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(JackpotHistoryConstantes.DATEUPDATED) Date dateUpdated);

}
