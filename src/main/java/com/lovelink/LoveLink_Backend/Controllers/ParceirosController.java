package com.lovelink.LoveLink_Backend.Controllers;

import com.lovelink.LoveLink_Backend.Dto.*;
import com.lovelink.LoveLink_Backend.Models.Parceiro;
import com.lovelink.LoveLink_Backend.Services.ParceiroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/parceiros" ,  produces = MediaType.APPLICATION_JSON_VALUE)
public class ParceirosController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ParceiroService parceiroService;
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<?> registraParceiro(@RequestBody @Valid ParceiroRequestDTO parceiroRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(parceiroService.criaParceiro(parceiroRequestDTO));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/loginParceiro")
    public ResponseEntity<?> logaParceiro(@RequestBody @Valid LoginDTO loginDTO){
        try{
            Optional<Parceiro> parceiroEncontrado = parceiroService.getParceiroByEmail(loginDTO.email().toLowerCase());
            if(parceiroEncontrado.isEmpty()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos1");
            }
            Parceiro parceiro = parceiroEncontrado.get();
            if(!passwordEncoder.matches(loginDTO.senha(), parceiro.getSenha())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos");
            }
            return ResponseEntity.ok(new ParceiroResponseDTO(parceiro.getNome(), parceiro.getEmail(), parceiro.getImagem(), parceiro.getPrecoCompra(), parceiro.getId()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar login: " + e.getMessage());
        }
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}/alteraSenha")
    public ResponseEntity<?> alteraSenhaParceiro(@PathVariable("id")UUID id, @RequestBody NewSenhaDTO newSenhaDTO){
        Optional<Parceiro> parceiroEncontrado = parceiroService.getParceiro(id);
        if(parceiroEncontrado.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parceiro não encontrado");
        }
        Parceiro parceiro = parceiroEncontrado.get();
        parceiro.setSenha(passwordEncoder.encode(newSenhaDTO.novaSenha()));
        parceiroService.salvaParceiro(parceiro);
        return ResponseEntity.status(HttpStatus.OK).body(new ParceiroResponseDTO(parceiro.getNome(), parceiro.getEmail(), parceiro.getImagem(), parceiro.getPrecoCompra(), parceiro.getId()));
    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateParceiro(@PathVariable("id")UUID id, @RequestBody ParceiroRequestDTO novoParceiro){
        Optional<Parceiro> parceiroEncontrado = parceiroService.getParceiro(id);
        if(parceiroEncontrado.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parceiro não encontrado");
        }
        Parceiro parceiro = parceiroEncontrado.get();
        parceiro.setImagem(novoParceiro.imagem());
        parceiro.setEmail(novoParceiro.email());
        parceiro.setNome(novoParceiro.nome());
        parceiroService.salvaParceiro(parceiro);
        return ResponseEntity.status(HttpStatus.OK).body(new ParceiroResponseDTO(parceiro.getNome(), parceiro.getEmail(), parceiro.getImagem(), parceiro.getPrecoCompra(), parceiro.getId()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}/alteraValor")
    public ResponseEntity<?> alteraValorParceiro(@PathVariable("id")UUID id, @RequestBody NewValorParceiro newValorParceiro){
        Optional<Parceiro> parceiroEncontrado = parceiroService.getParceiro(id);
        if(parceiroEncontrado.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parceiro não encontrado");
        }
        Parceiro parceiro = parceiroEncontrado.get();
        parceiro.setPrecoCompra(newValorParceiro.novoValor());
        parceiroService.salvaParceiro(parceiro);
        return ResponseEntity.status(HttpStatus.OK).body(new ParceiroResponseDTO(parceiro.getNome(), parceiro.getEmail(),parceiro.getImagem(), parceiro.getPrecoCompra(), parceiro.getId()));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getParceiro/{idParceiro}")
    public ResponseEntity<?> getParceiro(@PathVariable("idParceiro") UUID id){
        Optional<Parceiro> parceiroEncontrado = parceiroService.getParceiro(id);
        if(parceiroEncontrado.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("parceiro não encontrado");
        }
        Parceiro parceiro = parceiroEncontrado.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ParceiroResponseDTO(parceiro.getNome(), parceiro.getEmail(), parceiro.getImagem(), parceiro.getPrecoCompra(), parceiro.getId()));
    }

}
