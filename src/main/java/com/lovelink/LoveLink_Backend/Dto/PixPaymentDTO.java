package com.lovelink.LoveLink_Backend.Dto;

public record PixPaymentDTO(
        Double transactionAmount,
        String description,
        String paymentMethodId,
        PayerDto payer
) {
}
