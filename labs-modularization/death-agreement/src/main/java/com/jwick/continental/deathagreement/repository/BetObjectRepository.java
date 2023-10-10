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
import com.jwick.continental.deathagreement.model.BetObject;
import com.jwick.continental.deathagreement.constantes.BetObjectConstantes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.UUID;


/**
*
* BetObjectRepository - Interface dos métodos de acesso aos dados da tabela BET_OBJECT
* Camada de dados BetObject - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor BetObject
* @since Fri Oct 06 12:17:44 BRT 2023
*
*/
@Repository
public interface BetObjectRepository extends JpaRepository<BetObject, Long>
{
    @Query(value = "SELECT * FROM BET_OBJECT WHERE  status = :status", nativeQuery = true)
    List<BetObject> findAllByStatus(@Param(BetObjectConstantes.STATUS) String status);

@Query(value = "SELECT * FROM BET_OBJECT WHERE 1=1 " +
        "AND (:id = '' OR id_bet_object = :id) " +
        "AND (:who = '' OR who = :who) " +
        "AND (:externalUUID = '' OR externalUUID = :externalUUID) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
Page<BetObject> findBetObjectByFilter(Pageable pageable,
        @Param(BetObjectConstantes.ID) Long id,
        @Param(BetObjectConstantes.WHO) String who,
        @Param(BetObjectConstantes.EXTERNALUUID) UUID externalUUID,
        @Param(BetObjectConstantes.STATUS) String status,
        @Param(BetObjectConstantes.DATECREATED) Date dateCreated,
        @Param(BetObjectConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM BET_OBJECT WHERE 1=1 " +
        "AND (:id = '' OR id_bet_object = :id) " +
        "AND (:who = '' OR who = :who) " +
        "AND (:externalUUID = '' OR externalUUID = :externalUUID) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
List<BetObject> findBetObjectByFilter(
        @Param(BetObjectConstantes.ID) Long id,
        @Param(BetObjectConstantes.WHO) String who,
        @Param(BetObjectConstantes.EXTERNALUUID) UUID externalUUID,
        @Param(BetObjectConstantes.STATUS) String status,
        @Param(BetObjectConstantes.DATECREATED) Date dateCreated,
        @Param(BetObjectConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE id_bet_object = :id AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE who = :who AND status = :status ", nativeQuery = true)
     Long loadMaxIdByWhoAndStatus(String who, String status);
     @Query(value = "SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE externalUUID = :externalUUID AND status = :status ", nativeQuery = true)
     Long loadMaxIdByExternalUUIDAndStatus(UUID externalUUID, String status);
     @Query(value = "SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE dateCreated = :dateCreated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE dateUpdated = :dateUpdated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Modifying
     @Query(value = "UPDATE BET_OBJECT SET who = :who, dt_updated = current_timestamp  WHERE id_bet_object = :id", nativeQuery = true)
     void updateWhoById(@Param("id") Long id, @Param(BetObjectConstantes.WHO) String who);
     @Modifying
     @Query(value = "UPDATE BET_OBJECT SET externalUUID = :externalUUID, dt_updated = current_timestamp  WHERE id_bet_object = :id", nativeQuery = true)
     void updateExternalUUIDById(@Param("id") Long id, @Param(BetObjectConstantes.EXTERNALUUID) UUID externalUUID);
     @Modifying
     @Query(value = "UPDATE BET_OBJECT SET status = :status, dt_updated = current_timestamp  WHERE id_bet_object = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(BetObjectConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE BET_OBJECT SET dateCreated = :dateCreated, dt_updated = current_timestamp  WHERE id_bet_object = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(BetObjectConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE BET_OBJECT SET dateUpdated = :dateUpdated, dt_updated = current_timestamp  WHERE id_bet_object = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(BetObjectConstantes.DATEUPDATED) Date dateUpdated);

    @Query(value = "SELECT * FROM BET_OBJECT WHERE id_bet_object = (SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE id_bet_object = :id AND  status = :status) ", nativeQuery = true)
    Optional<BetObject> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM BET_OBJECT WHERE id_bet_object = (SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE who = :who AND  status = :status) ", nativeQuery = true)
    Optional<BetObject> findByWhoAndStatus(String who, String status);
    @Query(value = "SELECT * FROM BET_OBJECT WHERE id_bet_object = (SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE externalUUID = :externalUUID AND  status = :status) ", nativeQuery = true)
    Optional<BetObject> findByExternalUUIDAndStatus(UUID externalUUID, String status);
    @Query(value = "SELECT * FROM BET_OBJECT WHERE id_bet_object = (SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE dateCreated = :dateCreated AND  status = :status) ", nativeQuery = true)
    Optional<BetObject> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM BET_OBJECT WHERE id_bet_object = (SELECT MAX(id_bet_object) AS maxid FROM BET_OBJECT WHERE dateUpdated = :dateUpdated AND  status = :status) ", nativeQuery = true)
    Optional<BetObject> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM BET_OBJECT WHERE id_bet_object = :id AND  status = :status ", nativeQuery = true)
     List<BetObject> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM BET_OBJECT WHERE who = :who AND  status = :status ", nativeQuery = true)
     List<BetObject> findAllByWhoAndStatus(String who, String status);
     @Query(value = "SELECT * FROM BET_OBJECT WHERE externalUUID = :externalUUID AND  status = :status ", nativeQuery = true)
     List<BetObject> findAllByExternalUUIDAndStatus(UUID externalUUID, String status);
     @Query(value = "SELECT * FROM BET_OBJECT WHERE dateCreated = :dateCreated AND  status = :status ", nativeQuery = true)
     List<BetObject> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM BET_OBJECT WHERE dateUpdated = :dateUpdated AND  status = :status ", nativeQuery = true)
     List<BetObject> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM BET_OBJECT WHERE id_bet_object = :id", nativeQuery = true)
    void deleteById(@Param(BetObjectConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM BET_OBJECT WHERE who = :who", nativeQuery = true)
    void deleteByWho(@Param(BetObjectConstantes.WHO) String who);
    @Modifying
    @Query(value = "DELETE FROM BET_OBJECT WHERE externalUUID = :externalUUID", nativeQuery = true)
    void deleteByExternalUUID(@Param(BetObjectConstantes.EXTERNALUUID) UUID externalUUID);
    @Modifying
    @Query(value = "DELETE FROM BET_OBJECT WHERE status = :status", nativeQuery = true)
    void deleteByStatus(@Param(BetObjectConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM BET_OBJECT WHERE dateCreated = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(BetObjectConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM BET_OBJECT WHERE dateUpdated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(BetObjectConstantes.DATEUPDATED) Date dateUpdated);

}
