package com.lovelink.LoveLink_Backend.Dto;

public record CardPaymentDTO(
        Double transactionAmount,
        String token,
        String description,
        Integer installments,
        String paymentMethodId,
        PayerDto payer
) {
}
