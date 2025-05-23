package com.lovelink.LoveLink_Backend.Services;
import com.lovelink.LoveLink_Backend.Dto.*;
import com.lovelink.LoveLink_Backend.Models.Pagina;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PagamentoServiceCartao {
    @Value("${mercado_pago_sample_access_token}")
    private String mercadoPagoAccessToken;
    @Autowired
    private PaginaService paginaService;
    @Autowired
    private EmailService emailService;

    private final ScheduledExecutorService scheduler;
    public PagamentoServiceCartao(){
        int nThreads = Runtime.getRuntime().availableProcessors() * 2;
        this.scheduler = Executors.newScheduledThreadPool(nThreads);

    }

    public PixPaymentResponseDTO processPaymentPix(PixPaymentDTO pagamento){
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);
            System.out.println("Iniciando pagamento via PIX...");
            System.out.println("Email do pagador: " + pagamento.payer().email());

            PaymentClient paymentClient = new PaymentClient();

            PaymentCreateRequest paymentCreateRequest =
                    PaymentCreateRequest.builder()
                            .transactionAmount(BigDecimal.valueOf(pagamento.transactionAmount()))
                            .description(pagamento.description())
                            .paymentMethodId("pix")
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
            System.out.println("Pagamento PIX criado com sucesso! ID: " + createdPayment.getId());

            return new PixPaymentResponseDTO(
                    createdPayment.getId(),
                    createdPayment.getStatus(),
                    createdPayment.getStatusDetail(),
                    createdPayment.getPointOfInteraction().getTransactionData().getQrCode(),
                    createdPayment.getPointOfInteraction().getTransactionData().getQrCodeBase64());

        } catch (MPApiException apiException) {
            System.out.println("Erro da API do Mercado Pago: " + apiException.getApiResponse().getContent());
            throw new RuntimeException(apiException.getApiResponse().getContent());
        } catch (MPException exception) {
            System.out.println("Erro geral do Mercado Pago: " + exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void processarWebhook(Long paymentId, int tentativaAtual) {
        final int MAX_TENTATIVAS = 5;
        final int DELAY_SEGUNDOS = 5;

        try {
            System.out.println("🔍 Consultando pagamento ID: " + paymentId);
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PaymentClient paymentClient = new PaymentClient();
            Payment payment = paymentClient.get(paymentId);
            Optional<Pagina> opPagina = paginaService.getPaginaByPagamentoId(paymentId);
            if(opPagina.isEmpty()){
                if(tentativaAtual < MAX_TENTATIVAS){
                    System.out.println("⏳ Página ainda não encontrada. Tentando novamente em " + DELAY_SEGUNDOS + "s...");
                    scheduler.schedule(() -> processarWebhook(paymentId, tentativaAtual + 1),
                            DELAY_SEGUNDOS, TimeUnit.SECONDS);
                }
                else {
                    System.out.println("🚫 Página não encontrada após " + MAX_TENTATIVAS + " tentativas. Abortando.");
                }
                return;
            }
            Pagina paginaEncontrada = opPagina.get();
            System.out.println("📊 Status: " + payment.getStatus());
            System.out.println("Nome do casal da pagina: " + paginaEncontrada.getNomeCasal());

            switch (payment.getStatus()) {
                case "approved":
                    System.out.println("✅ Pagamento aprovado. Criar página agora.");
                    paginaEncontrada.setStatus("aprovado");
                    paginaService.salvaPagina(paginaEncontrada);
                    emailService.enviarRegistroPagina(paginaEncontrada);
                    break;

                case "rejected":
                    System.out.println("❌ Pagamento recusado. Remover dados temporários.");
                    paginaService.deletaPagina(paginaEncontrada);
                    emailService.enviarPagamentoRecusado(paginaEncontrada);
                    break;

                case "pending":
                    System.out.println("⌛ Pagamento pendente. Aguardando confirmação.");
                    break;

                case "inc_process":
                    System.out.println("⌛ Pagamento pendente. Aguardando confirmação.");
                    emailService.enviarPagamentoPendente((paginaEncontrada));
                    break;

                default:
                    System.out.println("⚠️ Status não tratado: " + payment.getStatus());
            }

        } catch (MPException | MPApiException e) {
            System.err.println("Erro ao processar webhook: " + e.getMessage());
        }
    }

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
