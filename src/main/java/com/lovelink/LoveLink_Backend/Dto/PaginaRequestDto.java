package com.lovelink.LoveLink_Backend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public record PaginaRequestDto(
        @NotBlank String nomeCasal,
        String musica,
        String planoSelecionado,
        String email,

        String mensagem,
        String autor,
        String planoSelecionadoForm,
        Date data,
        String titulo,
        String videoId,
        List<String> imagens,
        List<AlbumDto> album,
        String token,
        List<String> playlist
) {

    public record AlbumDto(
            @NotBlank String url,
            String descricao,
            @NotNull Date data
    ) {}
}
