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

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        Pagina.Album novoAlbum = new Pagina.Album(dados.imagem(), dados.descricao(), dados.data());
        pagina.getAlbum().add(novoAlbum);
        return ResponseEntity.ok().body(this.paginaService.salvaPagina(pagina));

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
