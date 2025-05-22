package com.lovelink.LoveLink_Backend.Services;
import com.lovelink.LoveLink_Backend.Dto.CardPaymentDTO;
import com.lovelink.LoveLink_Backend.Dto.PaginaResponseDto;
import com.lovelink.LoveLink_Backend.Dto.PaymentResponseDTO;
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

    public PaymentResponseDTO processPayment(CardPaymentDTO pagamento) {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            System.out.println("Iniciando pagamento...");
            System.out.println("Token recebido: " + pagamento.token());
            System.out.println("Email do pagador: " + pagamento.payer().email());
            System.out.println("Número do documento: " + pagamento.payer().identification().number());
            PaymentClient paymentClient = new PaymentClient();

            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(BigDecimal.valueOf(pagamento.transactionAmount()))
                            .token(pagamento.token())
                            .description(pagamento.description())
                            .installments(pagamento.installments())
                            .paymentMethodId(pagamento.paymentMethodId())
                            .payer(
                                    PaymentPayerRequest.builder()
                                            .email(pagamento.payer().email())
                                            .identification(
                                                    IdentificationRequest.builder()
                                                            .type(pagamento.payer().identification().type())
                                                            .number(pagamento.payer().identification().number())
                                                            .build())
                                            .build())
                            .build();

            Payment createdPayment = paymentClient.create(paymentCreateRequest);
            System.out.println("Pagamento criado com sucesso! ID: " + createdPayment.getId());
            return new PaymentResponseDTO(
                    createdPayment.getId(),
                    createdPayment.getStatus(),
                    createdPayment.getStatusDetail());
        } catch (MPApiException apiException) {
            System.out.println("Erro da API do Mercado Pago: " + apiException.getApiResponse().getContent());
            throw new RuntimeException(apiException.getApiResponse().getContent());
        } catch (MPException exception) {
            System.out.println("Erro geral do Mercado Pago: " + exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

}
