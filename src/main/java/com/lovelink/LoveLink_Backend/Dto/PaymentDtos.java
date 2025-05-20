package com.lovelink.LoveLink_Backend.Dto;

import java.math.BigDecimal;

public class PaymentDtos {
    public class PayerIdentificationDTO {
        private String type;
        private String number;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    // PayerDTO.java
    public class PayerDTO {
        private String email;
        private PayerIdentificationDTO identification;
        // getters e setters

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public PayerIdentificationDTO getIdentification() {
            return identification;
        }

        public void setIdentification(PayerIdentificationDTO identification) {
            this.identification = identification;
        }
    }

    // CardPaymentDTO.java (para cartão)
    public class CardPaymentDTO {
        private Double transactionAmount;
        private String token; // token do cartão gerado pelo front
        private String description;
        private Integer installments;
        private String paymentMethodId;
        private PayerDTO payer;
        // getters e setters

        public Double getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(Double transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getInstallments() {
            return installments;
        }

        public void setInstallments(Integer installments) {
            this.installments = installments;
        }

        public String getPaymentMethodId() {
            return paymentMethodId;
        }

        public void setPaymentMethodId(String paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
        }

        public PayerDTO getPayer() {
            return payer;
        }

        public void setPayer(PayerDTO payer) {
            this.payer = payer;
        }
    }

    // PixPaymentDTO.java (para PIX)
    public class PixPaymentDTO {
        private Double transactionAmount;
        private String description;
        private PayerDTO payer;
        // getters e setters
    }

    // PaymentResponseDTO.java
    public static class PaymentResponseDTO {
        private Long id;
        private String status;
        private String statusDetail;

        public PaymentResponseDTO(Long id, String status, String statusDetail) {
            this.id = id;
            this.status = status;
            this.statusDetail = statusDetail;
        }
        // getters e setters
    }
}
