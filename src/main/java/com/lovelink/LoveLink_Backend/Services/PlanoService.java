package com.lovelink.LoveLink_Backend.Services;

import com.lovelink.LoveLink_Backend.Dto.PlanoRequestDto;
import com.lovelink.LoveLink_Backend.Models.Plano;
import com.lovelink.LoveLink_Backend.Repositorios.PlanosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanoService {

    @Autowired
    PlanosRepository planosRepository;
    public Plano addPlano(PlanoRequestDto planoRequestDto){
        Plano novoPLano = new Plano(planoRequestDto);
        return planosRepository.save(novoPLano);
    }

    public List<Plano> getPlanos(){
        return planosRepository.findAll();
    }
    public Optional<Plano> getPlano(String nome){
        return planosRepository.findByNome(nome);
    }
}
