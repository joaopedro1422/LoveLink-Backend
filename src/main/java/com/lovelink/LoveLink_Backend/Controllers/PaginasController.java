package com.lovelink.LoveLink_Backend.Controllers;

import com.lovelink.LoveLink_Backend.Dto.FotoAlbumRequestDto;
import com.lovelink.LoveLink_Backend.Dto.PaginaRequestDto;
import com.lovelink.LoveLink_Backend.Models.Pagina;
import com.lovelink.LoveLink_Backend.Services.PaginaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/paginas",  produces = MediaType.APPLICATION_JSON_VALUE)
public class PaginasController {

    @Autowired
    PaginaService paginaService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<?> registrarPagina(@RequestBody @Valid PaginaRequestDto dados){
        return ResponseEntity.status(HttpStatus.CREATED).body(paginaService.registraNovaPagina(dados));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{slug}/{id}/adc-album")
    public ResponseEntity<?> adicionarFotoAlbum(@PathVariable("slug") String slug,
                                                @PathVariable("id") Long id,@RequestBody @Valid FotoAlbumRequestDto dados){
        Optional<Pagina> opPagina = paginaService.getPagina(slug,id);
        if(opPagina.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Pagina pagina = opPagina.get();
        Pagina.Album novoAlbum = new Pagina.Album();
        novoAlbum.setData(dados.data());
        novoAlbum.setDescricao(dados.descricao());
        novoAlbum.setUrl(dados.imagem());
        pagina.getAlbum().add(novoAlbum);
        return ResponseEntity.ok().body(this.paginaService.salvaPagina(pagina));

    }
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{slug}/{id}/update")
    public ResponseEntity<?> atualizarPagina(@PathVariable ("slug") String slug,
                                            @PathVariable ("id") Long id, @RequestBody PaginaRequestDto dados){
        Optional<Pagina> opPagina = paginaService.getPagina(slug,id);
       if(opPagina.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagina nao encontrada");
       }
        Pagina pagina = opPagina.get();
        Pagina.Album novoAlbum = new Pagina.Album();

        pagina.setAlbum(dados.album().stream().map(a -> {
            Pagina.Album alb = new Pagina.Album();
            alb.setUrl(a.url());
            alb.setDescricao(a.descricao());
            alb.setData(a.data());
            return alb;
        }).collect(Collectors.toList()));
        pagina.setMensagem(dados.mensagem());
        pagina.setVideoId(dados.videoId());
        pagina.setImagens(dados.imagens());
        pagina.setNomeCasal(dados.nomeCasal());
        pagina.setAutor(dados.autor());
        pagina.setData(dados.data());
        pagina.setTitulo(dados.titulo());
        return ResponseEntity.ok().body(this.paginaService.salvaPagina(pagina));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPaginas(){
        return ResponseEntity.ok(paginaService.getAll());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}/paginasParceiro")
    public ResponseEntity<?> getPaginasPorParceiro(@PathVariable("id") UUID id){
        return ResponseEntity.ok(paginaService.getPaginasPorParceiro(id));
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/deleta/{slug}/{id}")
    public ResponseEntity<?> deletaPagina(@PathVariable("slug") String slug,
                                          @PathVariable("id") Long id){
        Optional<Pagina> paginaEncontrada = paginaService.getPagina(slug,id);
        if(paginaEncontrada.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagina não encontrada");
        }
        Pagina pagina = paginaEncontrada.get();
        paginaService.deletaPagina(pagina);
        return ResponseEntity.status(HttpStatus.OK).body("Pagina deletada com sucesso");
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{slug}/{id}")
    public ResponseEntity<?> getPagina( @PathVariable("slug") String slug,
                                        @PathVariable("id") Long id){
        Optional<Pagina> paginaEncontrada = paginaService.getPagina(slug,id);
        if(paginaEncontrada.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagina não encontrada");
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(paginaEncontrada.get());
        }
    }
}
