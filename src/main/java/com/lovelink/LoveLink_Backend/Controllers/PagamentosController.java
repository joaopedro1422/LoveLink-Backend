package com.lovelink.LoveLink_Backend.Controllers;

import com.lovelink.LoveLink_Backend.Dto.PaymentDtos;
import com.lovelink.LoveLink_Backend.Services.PagamentoServiceCartao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PagamentosController {

    @Autowired
    PagamentoServiceCartao pagamentoServiceCartao;
    @PostMapping("/card")
    public ResponseEntity<PaymentDtos.PaymentResponseDTO> payWithCard(@RequestBody PaymentDtos.CardPaymentDTO cardPaymentDTO) {
        PaymentDtos.PaymentResponseDTO response = pagamentoServiceCartao.processPayment(cardPaymentDTO);
        return ResponseEntity.ok(response);
    }
}
