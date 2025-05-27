package com.lovelink.LoveLink_Backend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record PaginaRequestDto(
        @NotBlank String nomeCasal,
        UUID idParceiro,
        String musica,
        String planoSelecionado,
        String email,
        Long pagamentoId,
        String status,
        String mensagem,
        String autor,
        String planoSelecionadoForm,
        LocalDate data,
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
            @NotNull LocalDate data
    ) {}
}
