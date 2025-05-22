package com.lovelink.LoveLink_Backend.Dto;

public record PixPaymentResponseDTO(  Long id,
                                      String status,
                                      String statusDetail,
                                      String qrCode,
                                      String qrCodeBase64) {
}
