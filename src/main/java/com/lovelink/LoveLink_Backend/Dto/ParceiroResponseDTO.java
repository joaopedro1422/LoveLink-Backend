package com.lovelink.LoveLink_Backend.Dto;

import java.util.UUID;

public record ParceiroResponseDTO(
        String nome,
        String email,
        String imagem,
        Double precoCompra, UUID id) {
}
