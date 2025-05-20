package com.lovelink.LoveLink_Backend.Services;
import com.lovelink.LoveLink_Backend.Dto.PaginaResponseDto;
import com.lovelink.LoveLink_Backend.Dto.PaymentDtos;
import com.lovelink.LoveLink_Backend.Models.Pagina;

import com.lovelink.LoveLink_Backend.Dto.PaginaRequestDto;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PagamentoServiceCartao {
    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;

    public PaymentDtos.PaymentResponseDTO processPayment(PaymentDtos.CardPaymentDTO pagamento) {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PaymentClient paymentClient = new PaymentClient();

            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(BigDecimal.valueOf(pagamento.getTransactionAmount()))
                            .token(pagamento.getToken())
                            .description(pagamento.getDescription())
                            .installments(pagamento.getInstallments())
                            .paymentMethodId(pagamento.getPaymentMethodId())
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email(pagamento.getPayer().getEmail())
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type(pagamento.getPayer().getIdentification().getType())
                                                            .number(pagamento.getPayer().getIdentification().getNumber())
                                                            .build())
                                            .build())
                            .build();

            Payment createdPayment = paymentClient.create(paymentCreateRequest);

            return new PaymentDtos.PaymentResponseDTO(
                    createdPayment.getId(),
                    createdPayment.getStatus(),
                    createdPayment.getStatusDetail());
        } catch (MPApiException apiException) {
            throw new RuntimeException(apiException.getApiResponse().getContent());
        } catch (MPException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

}
