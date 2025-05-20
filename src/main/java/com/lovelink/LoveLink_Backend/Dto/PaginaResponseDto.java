package com.lovelink.LoveLink_Backend.Dto;

import java.util.Date;
import java.util.List;

public record PaginaResponseDto(
        String nomeCasal,
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
        List<String> playlist
) {

    public record AlbumDto(
            String url,
            String descricao,
            Date data
    ) {}
}

