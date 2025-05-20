package com.lovelink.LoveLink_Backend.Services;

import com.lovelink.LoveLink_Backend.Dto.PaginaRequestDto;
import com.lovelink.LoveLink_Backend.Dto.PaginaResponseDto;
import com.lovelink.LoveLink_Backend.Models.Pagina;
import com.lovelink.LoveLink_Backend.Repositorios.PaginaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PaginaService {
    @Autowired
    PaginaRepository paginaRepository;

    @Autowired
    EmailService emailService;
    public Pagina registraNovaPagina(PaginaRequestDto pagina){
        Pagina novaPagina = new Pagina(pagina);
        BeanUtils.copyProperties(pagina, novaPagina);
        Pagina paginaRetorno = paginaRepository.save(novaPagina);
        if(paginaRetorno != null){
            emailService.enviarRegistroPagina(paginaRetorno);
        }
        return paginaRetorno;
    }

    public Pagina salvaPagina(Pagina pagina){
        return this.paginaRepository.save(pagina);
    }

    public Optional<Pagina> getPagina(String slug, Long id){

        return paginaRepository.findByIdAndSlug(id,slug);
    }
}
