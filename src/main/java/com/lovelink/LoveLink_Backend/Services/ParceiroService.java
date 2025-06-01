package com.lovelink.LoveLink_Backend.Services;

import com.lovelink.LoveLink_Backend.Dto.ParceiroRequestDTO;
import com.lovelink.LoveLink_Backend.Dto.ParceiroResponseDTO;
import com.lovelink.LoveLink_Backend.Models.Parceiro;
import com.lovelink.LoveLink_Backend.Repositorios.ParceirosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ParceiroService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    ParceirosRepository parceirosRepository;

    public ParceiroResponseDTO criaParceiro(ParceiroRequestDTO parceiroRequestDTO){
        Parceiro novoParceiro = new Parceiro(parceiroRequestDTO);
        novoParceiro.setSenha(passwordEncoder.encode(parceiroRequestDTO.senha()));
        parceirosRepository.save(novoParceiro);
        return new ParceiroResponseDTO(novoParceiro.getNome(), novoParceiro.getEmail(), novoParceiro.getImagem(), novoParceiro.getPrecoCompra(), novoParceiro.getId());
    }
    public Parceiro salvaParceiro(Parceiro parceiro){
        return parceirosRepository.save(parceiro);
    }

    public Optional<Parceiro> getParceiroByEmail(String email){
        return parceirosRepository.findByEmail(email);
    }
    public Optional<Parceiro> getParceiro(UUID id){
        return parceirosRepository.findById(id);
    }
}
