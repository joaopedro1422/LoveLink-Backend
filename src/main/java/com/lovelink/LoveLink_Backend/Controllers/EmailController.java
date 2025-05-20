package com.lovelink.LoveLink_Backend.Controllers;

import com.lovelink.LoveLink_Backend.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/confirmacao-email")
@CrossOrigin(origins = "*")
public class EmailController {
    @Autowired
    private EmailService emailService;
    private Map<String, String> codigosPorEmail = new ConcurrentHashMap<>();

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/enviar-codigo")
    public ResponseEntity<?> enviarCodigo(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email é obrigatório");
        }

        String codigo = gerarCodigo();
        codigosPorEmail.put(email, codigo);

        try {
            emailService.enviarCodigoVerificacao(email, codigo);
            return ResponseEntity.ok(Map.of("mensagem", "Código enviado para " + email));
        } catch (Exception e) {
            e.printStackTrace(); // Mostra o erro no console/log
            return ResponseEntity.status(500).body("Erro ao enviar o e-mail: " + e.getMessage());
        }
    }

    // Endpoint para validar código
    @PostMapping("/validar-codigo")
    public ResponseEntity<?> validarCodigo(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigoRecebido = request.get("codigo");

        if(email == null || codigoRecebido == null) {
            return ResponseEntity.badRequest().body("Email e código são obrigatórios");
        }

        String codigoEsperado = codigosPorEmail.get(email);

        if(codigoEsperado != null && codigoEsperado.equals(codigoRecebido)) {
            // Código correto, pode remover para não reutilizar
            codigosPorEmail.remove(email);
            return ResponseEntity.ok(Map.of("mensagem", "Código verificado com sucesso para " + email));
        } else {
            return ResponseEntity.status(400).body("Código inválido");
        }
    }

    private String gerarCodigo() {
        return String.valueOf((int)(100000 + Math.random() * 900000)); // 6 dígitos
    }
}
