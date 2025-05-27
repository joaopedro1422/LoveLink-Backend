package com.lovelink.LoveLink_Backend.Repositorios;

import com.lovelink.LoveLink_Backend.Models.Parceiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParceirosRepository extends JpaRepository<Parceiro, UUID> {
    Optional<Parceiro> findByEmail(String email);
}
