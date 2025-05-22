package com.lovelink.LoveLink_Backend.Controllers;

import com.lovelink.LoveLink_Backend.Dto.CardPaymentDTO;
import com.lovelink.LoveLink_Backend.Dto.PaymentResponseDTO;
import com.lovelink.LoveLink_Backend.Dto.PixPaymentDTO;
import com.lovelink.LoveLink_Backend.Dto.PixPaymentResponseDTO;
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
    public ResponseEntity<PaymentResponseDTO> payWithCard(@RequestBody CardPaymentDTO cardPaymentDTO) {
        PaymentResponseDTO response = pagamentoServiceCartao.processPayment(cardPaymentDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pix")
    public ResponseEntity<PixPaymentResponseDTO> payWithPix(@RequestBody PixPaymentDTO pixPaymentDTO) {
        PixPaymentResponseDTO response = pagamentoServiceCartao.processPaymentPix(pixPaymentDTO);
        return ResponseEntity.ok(response);
    }
}


