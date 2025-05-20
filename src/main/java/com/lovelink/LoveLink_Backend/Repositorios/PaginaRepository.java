package com.lovelink.LoveLink_Backend.Repositorios;

import com.lovelink.LoveLink_Backend.Models.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaginaRepository extends JpaRepository<Pagina, Long> {

    Optional<Pagina> findByIdAndSlug(Long id, String slug);

}
