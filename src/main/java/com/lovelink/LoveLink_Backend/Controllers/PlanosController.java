package com.lovelink.LoveLink_Backend.Controllers;

import com.lovelink.LoveLink_Backend.Dto.PlanoRequestDto;
import com.lovelink.LoveLink_Backend.Models.Plano;
import com.lovelink.LoveLink_Backend.Services.PlanoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/planos",  produces = MediaType.APPLICATION_JSON_VALUE)
public class PlanosController {
    @Autowired
    PlanoService planoService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<?> adcPlano(@RequestBody @Valid PlanoRequestDto dados){
        return ResponseEntity.status(HttpStatus.CREATED).body(planoService.addPlano(dados));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{nome}")
    public ResponseEntity<?> getPlano( @PathVariable("nome") String nome){
        Optional<Plano> planoEncontrado = planoService.getPlano(nome);
        if(planoEncontrado.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plano não Encontrado");
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(planoEncontrado.get());
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<?> getPlanos(){
        return ResponseEntity.status((HttpStatus.OK)).body(planoService.getPlanos());
    }



}
