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


package br.com.jcv.codegen.codegenerator.repository;

import java.util.List;
import br.com.jcv.codegen.codegenerator.model.Usuario;
import br.com.jcv.codegen.codegenerator.constantes.UsuarioConstantes;
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
* UsuarioRepository - Interface dos métodos de acesso aos dados da tabela tb_user
* Camada de dados Usuario - camada responsável SOMENTE pela acesso aos dados do sistema.
* Não é uma camada visível para outros dispositivos, como as camadas de apresentação e aplicação.
*
* Changelog:
*
* @autor Usuario
* @since Wed Oct 04 12:38:51 BRT 2023
*
*/
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
    @Query(value = "SELECT * FROM tb_user WHERE  IN_STATUS = :status", nativeQuery = true)
    List<Usuario> findAllByStatus(@Param(UsuarioConstantes.STATUS) String status);

@Query(value = "SELECT * FROM tb_user WHERE 1=1 " +
        "AND (:id = '' OR id_usuario = :id) " +
        "AND (:nome = '' OR nome = :nome) " +
        "AND (:idade = '' OR idade = :idade) " +
        "AND (:status = '' OR in_status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
Page<Usuario> findUsuarioByFilter(Pageable pageable,
        @Param(UsuarioConstantes.ID) Long id,
        @Param(UsuarioConstantes.NOME) String nome,
        @Param(UsuarioConstantes.IDADE) Long idade,
        @Param(UsuarioConstantes.STATUS) String status,
        @Param(UsuarioConstantes.DATECREATED) Date dateCreated,
        @Param(UsuarioConstantes.DATEUPDATED) Date dateUpdated

        );

@Query(value = "SELECT * FROM tb_user WHERE 1=1 " +
        "AND (:id = '' OR id_usuario = :id) " +
        "AND (:nome = '' OR nome = :nome) " +
        "AND (:idade = '' OR idade = :idade) " +
        "AND (:status = '' OR in_status = :status) " +
        "AND (:dateCreated = '' OR dateCreated = :dateCreated) " +
        "AND (:dateUpdated = '' OR dateUpdated = :dateUpdated) " 

        , nativeQuery = true)
List<Usuario> findUsuarioByFilter(
        @Param(UsuarioConstantes.ID) Long id,
        @Param(UsuarioConstantes.NOME) String nome,
        @Param(UsuarioConstantes.IDADE) Long idade,
        @Param(UsuarioConstantes.STATUS) String status,
        @Param(UsuarioConstantes.DATECREATED) Date dateCreated,
        @Param(UsuarioConstantes.DATEUPDATED) Date dateUpdated

);

     @Query(value = "SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE id_usuario = :id AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByIdAndStatus(Long id, String status);
     @Query(value = "SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE nome = :nome AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByNomeAndStatus(String nome, String status);
     @Query(value = "SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE idade = :idade AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByIdadeAndStatus(Long idade, String status);
     @Query(value = "SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE dateCreated = :dateCreated AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE dateUpdated = :dateUpdated AND IN_STATUS = :status ", nativeQuery = true)
     Long loadMaxIdByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Modifying
     @Query(value = "UPDATE tb_user SET nome = :nome, dt_updated = current_timestamp  WHERE id_usuario = :id", nativeQuery = true)
     void updateNomeById(@Param("id") Long id, @Param(UsuarioConstantes.NOME) String nome);
     @Modifying
     @Query(value = "UPDATE tb_user SET idade = :idade, dt_updated = current_timestamp  WHERE id_usuario = :id", nativeQuery = true)
     void updateIdadeById(@Param("id") Long id, @Param(UsuarioConstantes.IDADE) Long idade);
     @Modifying
     @Query(value = "UPDATE tb_user SET in_status = :status, dt_updated = current_timestamp  WHERE id_usuario = :id", nativeQuery = true)
     void updateStatusById(@Param("id") Long id, @Param(UsuarioConstantes.STATUS) String status);
     @Modifying
     @Query(value = "UPDATE tb_user SET dateCreated = :dateCreated, dt_updated = current_timestamp  WHERE id_usuario = :id", nativeQuery = true)
     void updateDateCreatedById(@Param("id") Long id, @Param(UsuarioConstantes.DATECREATED) Date dateCreated);
     @Modifying
     @Query(value = "UPDATE tb_user SET dateUpdated = :dateUpdated, dt_updated = current_timestamp  WHERE id_usuario = :id", nativeQuery = true)
     void updateDateUpdatedById(@Param("id") Long id, @Param(UsuarioConstantes.DATEUPDATED) Date dateUpdated);


     long countByIdAndStatus(Long id, String status);
     long countByNomeAndStatus(String nome, String status);
     long countByIdadeAndStatus(Long idade, String status);
     long countByDateCreatedAndStatus(Date dateCreated, String status);
     long countByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Query(value = "SELECT * FROM tb_user WHERE id_usuario = (SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE id_usuario = :id AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Usuario> findByIdAndStatus(Long id, String status);
    @Query(value = "SELECT * FROM tb_user WHERE id_usuario = (SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE nome = :nome AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Usuario> findByNomeAndStatus(String nome, String status);
    @Query(value = "SELECT * FROM tb_user WHERE id_usuario = (SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE idade = :idade AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Usuario> findByIdadeAndStatus(Long idade, String status);
    @Query(value = "SELECT * FROM tb_user WHERE id_usuario = (SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE dateCreated = :dateCreated AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Usuario> findByDateCreatedAndStatus(Date dateCreated, String status);
    @Query(value = "SELECT * FROM tb_user WHERE id_usuario = (SELECT MAX(id_usuario) AS maxid FROM tb_user WHERE dateUpdated = :dateUpdated AND  IN_STATUS = :status) ", nativeQuery = true)
    Optional<Usuario> findByDateUpdatedAndStatus(Date dateUpdated, String status);


     @Query(value = "SELECT * FROM tb_user WHERE id_usuario = :id AND  IN_STATUS = :status ", nativeQuery = true)
     List<Usuario> findAllByIdAndStatus(Long id, String status);
     @Query(value = "SELECT * FROM tb_user WHERE nome = :nome AND  IN_STATUS = :status ", nativeQuery = true)
     List<Usuario> findAllByNomeAndStatus(String nome, String status);
     @Query(value = "SELECT * FROM tb_user WHERE idade = :idade AND  IN_STATUS = :status ", nativeQuery = true)
     List<Usuario> findAllByIdadeAndStatus(Long idade, String status);
     @Query(value = "SELECT * FROM tb_user WHERE dateCreated = :dateCreated AND  IN_STATUS = :status ", nativeQuery = true)
     List<Usuario> findAllByDateCreatedAndStatus(Date dateCreated, String status);
     @Query(value = "SELECT * FROM tb_user WHERE dateUpdated = :dateUpdated AND  IN_STATUS = :status ", nativeQuery = true)
     List<Usuario> findAllByDateUpdatedAndStatus(Date dateUpdated, String status);


    @Modifying
    @Query(value = "DELETE FROM tb_user WHERE id_usuario = :id", nativeQuery = true)
    void deleteById(@Param(UsuarioConstantes.ID) Long id);
    @Modifying
    @Query(value = "DELETE FROM tb_user WHERE nome = :nome", nativeQuery = true)
    void deleteByNome(@Param(UsuarioConstantes.NOME) String nome);
    @Modifying
    @Query(value = "DELETE FROM tb_user WHERE idade = :idade", nativeQuery = true)
    void deleteByIdade(@Param(UsuarioConstantes.IDADE) Long idade);
    @Modifying
    @Query(value = "DELETE FROM tb_user WHERE in_status = :status", nativeQuery = true)
    void deleteByStatus(@Param(UsuarioConstantes.STATUS) String status);
    @Modifying
    @Query(value = "DELETE FROM tb_user WHERE dateCreated = :dateCreated", nativeQuery = true)
    void deleteByDateCreated(@Param(UsuarioConstantes.DATECREATED) Date dateCreated);
    @Modifying
    @Query(value = "DELETE FROM tb_user WHERE dateUpdated = :dateUpdated", nativeQuery = true)
    void deleteByDateUpdated(@Param(UsuarioConstantes.DATEUPDATED) Date dateUpdated);

}
