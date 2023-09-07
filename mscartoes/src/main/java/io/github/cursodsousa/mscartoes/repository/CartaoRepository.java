package io.github.cursodsousa.mscartoes.repository;

import io.github.cursodsousa.mscartoes.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    List<Cartao> findByRendaLessThan(Double renda);
}
