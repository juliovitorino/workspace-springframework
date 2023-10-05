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
* BetRepository - Interface dos métodos de acesso aos dados da tabela com.jwick.continental.deathagreement.model.Bet
* Camada de dados Bet - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor Bet
* @since Thu Oct 05 10:13:31 BRT 2023
*
*/
@Repository
public interface BetRepository extends JpaRepository<Bet, Long>
{
    @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE  IN_STATUS = :status", nativeQuery = true)
    List<Bet> findAllByStatus(@Param(BetConstantes.STATUS) String status);

@Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE 1=1 " +
        "AND (:id = '' OR id = :id) " +
        "AND (:bounty = '' OR bounty = :bounty) " +
        "AND (:status = '' OR in_status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
Page<Bet> findBetByFilter(Pageable pageable,
        @Param(BetConstantes.ID) Long id,
        @Param(BetConstantes.BOUNTY) Double bounty,
        @Param(BetConstantes.STATUS) String status,
        @Param(BetConstantes.DATECREATED) Date dateCreated,
        @Param(BetConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE 1=1 " +
        "AND (:id = '' OR id = :id) " +
        "AND (:bounty = '' OR bounty = :bounty) " +
        "AND (:status = '' OR in_status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
List<Bet> findBetByFilter(
        @Param(BetConstantes.ID) Long id,
        @Param(BetConstantes.BOUNTY) Double bounty,
        @Param(BetConstantes.STATUS) String status,
        @Param(BetConstantes.DATECREATED) Date dateCreated,
        @Param(BetConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE id = :id AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE bounty = :bounty AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByBountyAndStatus(Double bounty, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE dateCreated = :dateCreated AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE dateUpdated = :dateUpdated AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Modifying
     @Query(value = "UPDATE com.jwick.continental.deathagreement.model.Bet SET bounty = :bounty, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateBountyById(@Param("id") Long id, @Param(BetConstantes.BOUNTY) Double bounty);
     @Modifying
     @Query(value = "UPDATE com.jwick.continental.deathagreement.model.Bet SET in_status = :status, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(BetConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE com.jwick.continental.deathagreement.model.Bet SET dateCreated = :dateCreated, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(BetConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE com.jwick.continental.deathagreement.model.Bet SET dateUpdated = :dateUpdated, dt_updated = current_timestamp  WHERE id = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(BetConstantes.DATEUPDATED) Date dateUpdated);


     long countByIdAndStatus(Long id, String status);
     long countByBountyAndStatus(Double bounty, String status);
     long countByDateCreatedAndStatus(Date dateCreated, String status);
     long countByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE id = (SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE id = :id AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Bet> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE id = (SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE bounty = :bounty AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Bet> findByBountyAndStatus(Double bounty, String status);
    @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE id = (SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE dateCreated = :dateCreated AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Bet> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE id = (SELECT MAX(id) AS maxid FROM com.jwick.continental.deathagreement.model.Bet WHERE dateUpdated = :dateUpdated AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Bet> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE id = :id AND  IN_STATUS = :status ", nativeQuery = true)
     List<Bet> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE bounty = :bounty AND  IN_STATUS = :status ", nativeQuery = true)
     List<Bet> findAllByBountyAndStatus(Double bounty, String status);
     @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE dateCreated = :dateCreated AND  IN_STATUS = :status ", nativeQuery = true)
     List<Bet> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM com.jwick.continental.deathagreement.model.Bet WHERE dateUpdated = :dateUpdated AND  IN_STATUS = :status ", nativeQuery = true)
     List<Bet> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM com.jwick.continental.deathagreement.model.Bet WHERE id = :id", nativeQuery = true)
    void deleteById(@Param(BetConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM com.jwick.continental.deathagreement.model.Bet WHERE bounty = :bounty", nativeQuery = true)
    void deleteByBounty(@Param(BetConstantes.BOUNTY) Double bounty);
    @Modifying
    @Query(value = "DELETE FROM com.jwick.continental.deathagreement.model.Bet WHERE in_status = :status", nativeQuery = true)
    void deleteByStatus(@Param(BetConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM com.jwick.continental.deathagreement.model.Bet WHERE dateCreated = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(BetConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM com.jwick.continental.deathagreement.model.Bet WHERE dateUpdated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(BetConstantes.DATEUPDATED) Date dateUpdated);

}
