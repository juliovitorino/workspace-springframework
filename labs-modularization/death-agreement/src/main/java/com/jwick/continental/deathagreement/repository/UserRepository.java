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
import com.jwick.continental.deathagreement.model.UserPunter;
import com.jwick.continental.deathagreement.constantes.UserConstantes;
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
* UserRepository - Interface dos métodos de acesso aos dados da tabela User
* Camada de dados User - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor User
* @since Fri Oct 06 17:47:30 BRT 2023
*
*/
@Repository
public interface UserRepository extends JpaRepository<UserPunter, Long>
{
    @Query(value = "SELECT * FROM user_punter WHERE  status = :status", nativeQuery = true)
    List<UserPunter> findAllByStatus(@Param(UserConstantes.STATUS) String status);

@Query(value = "SELECT * FROM user_punter WHERE 1=1 " +
        "AND (:id = '' OR id_user_punter = :id) " +
        "AND (:nickname = '' OR nickname = :nickname) " +
        "AND (:btcAddress = '' OR btc_address = :btcAddress) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR date_created = :dateCreated) " +
        "AND (:dateUpdated = '' OR date_updated = :dateUpdated) " 

        , nativeQuery = true)
Page<UserPunter> findUserByFilter(Pageable pageable,
                                  @Param(UserConstantes.ID) Long id,
                                  @Param(UserConstantes.NICKNAME) String nickname,
                                  @Param(UserConstantes.BTCADDRESS) String btcAddress,
                                  @Param(UserConstantes.STATUS) String status,
                                  @Param(UserConstantes.DATECREATED) Date dateCreated,
                                  @Param(UserConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM user_punter WHERE 1=1 " +
        "AND (:id = '' OR id_user_punter = :id) " +
        "AND (:nickname = '' OR nickname = :nickname) " +
        "AND (:btcAddress = '' OR btc_address = :btcAddress) " +
        "AND (:status = '' OR status = :status) " +
        "AND (:dateCreated = '' OR date_created = :dateCreated) " +
        "AND (:dateUpdated = '' OR date_updated = :dateUpdated) " 

        , nativeQuery = true)
List<UserPunter> findUserByFilter(
        @Param(UserConstantes.ID) Long id,
        @Param(UserConstantes.NICKNAME) String nickname,
        @Param(UserConstantes.BTCADDRESS) String btcAddress,
        @Param(UserConstantes.STATUS) String status,
        @Param(UserConstantes.DATECREATED) Date dateCreated,
        @Param(UserConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE id_user_punter = :id AND status = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE nickname = :nickname AND status = :status ", nativeQuery = true)
     Long loadMaxIdByNicknameAndStatus(String nickname, String status);
     @Query(value = "SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE btc_address = :btcAddress AND status = :status ", nativeQuery = true)
     Long loadMaxIdByBtcAddressAndStatus(String btcAddress, String status);
     @Query(value = "SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE date_created = :dateCreated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE date_updated = :dateUpdated AND status = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);

     @Modifying
     @Query(value = "UPDATE user_punter SET nickname = :nickname, dt_updated = current_timestamp  WHERE id_user_punter = :id", nativeQuery = true)
     void updateNicknameById(@Param("id") Long id, @Param(UserConstantes.NICKNAME) String nickname);
     @Modifying
     @Query(value = "UPDATE user_punter SET btc_address = :btcAddress, dt_updated = current_timestamp  WHERE id_user_punter = :id", nativeQuery = true)
     void updateBtcAddressById(@Param("id") Long id, @Param(UserConstantes.BTCADDRESS) String btcAddress);
     @Modifying
     @Query(value = "UPDATE user_punter SET status = :status, dt_updated = current_timestamp  WHERE id_user_punter = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(UserConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE user_punter SET date_created = :dateCreated, dt_updated = current_timestamp  WHERE id_user_punter = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(UserConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE user_punter SET date_updated = :dateUpdated, dt_updated = current_timestamp  WHERE id_user_punter = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(UserConstantes.DATEUPDATED) Date dateUpdated);

    @Query(value = "SELECT * FROM user_punter WHERE id_user_punter = (SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE id_user_punter = :id AND  status = :status) ", nativeQuery = true)
    Optional<UserPunter> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM user_punter WHERE id_user_punter = (SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE nickname = :nickname AND  status = :status) ", nativeQuery = true)
    Optional<UserPunter> findByNicknameAndStatus(String nickname, String status);
    @Query(value = "SELECT * FROM user_punter WHERE id_user_punter = (SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE btc_address = :btcAddress AND  status = :status) ", nativeQuery = true)
    Optional<UserPunter> findByBtcAddressAndStatus(String btcAddress, String status);
    @Query(value = "SELECT * FROM user_punter WHERE id_user_punter = (SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE date_created = :dateCreated AND  status = :status) ", nativeQuery = true)
    Optional<UserPunter> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM user_punter WHERE id_user_punter = (SELECT MAX(id_user_punter) AS maxid FROM user_punter WHERE date_updated = :dateUpdated AND  status = :status) ", nativeQuery = true)
    Optional<UserPunter> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM user_punter WHERE id_user_punter = :id AND  status = :status ", nativeQuery = true)
     List<UserPunter> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM user_punter WHERE nickname = :nickname AND  status = :status ", nativeQuery = true)
     List<UserPunter> findAllByNicknameAndStatus(String nickname, String status);
     @Query(value = "SELECT * FROM user_punter WHERE btc_address = :btcAddress AND  status = :status ", nativeQuery = true)
     List<UserPunter> findAllByBtcAddressAndStatus(String btcAddress, String status);
     @Query(value = "SELECT * FROM user_punter WHERE date_created = :dateCreated AND  status = :status ", nativeQuery = true)
     List<UserPunter> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM user_punter WHERE date_updated = :dateUpdated AND  status = :status ", nativeQuery = true)
     List<UserPunter> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM user_punter WHERE id_user_punter = :id", nativeQuery = true)
    void deleteById(@Param(UserConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM user_punter WHERE nickname = :nickname", nativeQuery = true)
    void deleteByNickname(@Param(UserConstantes.NICKNAME) String nickname);
    @Modifying
    @Query(value = "DELETE FROM user_punter WHERE btc_address = :btcAddress", nativeQuery = true)
    void deleteByBtcAddress(@Param(UserConstantes.BTCADDRESS) String btcAddress);
    @Modifying
    @Query(value = "DELETE FROM user_punter WHERE status = :status", nativeQuery = true)
    void deleteByStatus(@Param(UserConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM user_punter WHERE date_created = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(UserConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM user_punter WHERE date_updated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(UserConstantes.DATEUPDATED) Date dateUpdated);

}
