package com.lovelink.LoveLink_Backend.Repositorios;

import com.lovelink.LoveLink_Backend.Models.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanosRepository extends JpaRepository<Plano, Long> {
    Optional<Plano> findByNome(String nome);
}
