package com.lovelink.LoveLink_Backend.Dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record FotoAlbumRequestDto(@NotBlank String imagem,
                                  String descricao,
                                  Date data) {


}
