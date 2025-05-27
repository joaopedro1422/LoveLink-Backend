package com.lovelink.LoveLink_Backend.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.lovelink.LoveLink_Backend.Models.Pagina;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PlanoService planoService;

    private String getPrimeiroNomeAutor(Pagina pagina){
        String[] partes = pagina.getAutor().split(" ");
        return partes[0];
    }
    public void enviarPagamentoPendente(Pagina pagina){
        try {
            String primeiroNome = getPrimeiroNomeAutor(pagina);
            Double valorPago = planoService.getPlano(pagina.getPlanoSelecionado()).get().getPreco();
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(pagina.getEmail());
            helper.setSubject("O seu pagamento está sendo processado");
            String url = "https://lovelink-frontenddeploy.vercel.app/"+pagina.getSlug()+"/" + pagina.getId();
            String conteudoHtml = """
                    <!DOCTYPE html>
                    <html>
                             <head>
                               <style>
                                 body {
                                   background-color: #121212;
                                   margin: 0;
                                   padding: 0;
                                   font-family: Arial, sans-serif;
                                 }
                                 .container {
                                   border: 1px solid #333;
                                   padding: 30px 40px;
                                   max-width: 600px;
                                   margin: 40px auto;
                                   background-color: #1c1c1c;
                                   border-radius: 10px;
                                   color: #eee;
                                   box-shadow: 0 0 15px rgba(198, 48, 44, 0.4);
                                 }
                                 .logo {
                                   display: block;
                                   margin: 0 auto 20px;
                                   height: 40px;
                                   width: auto;
                                 }
                                 h2 {
                                   font-size: 26px;
                                   font-weight: 800;
                                   margin-bottom: 15px;
                                   text-align: center;
                                   color: #f44336;
                                 }
                                 p {
                                   font-size: 14px;
                                   line-height: 1.5;
                                   margin-bottom: 10px;
                                   color: #ccc;
                                 }
                                 .codigo {
                                   font-size: 12px;
                                   font-weight: 700;
                                   color: #778FFE;
                                   text-decoration: underline;
                                   word-break: break-all;
                                   margin: 20px 0;
                                   text-align: center;
                                 }
                                 .btn {
                                   display: block;
                                   width: 200px;
                                   margin: 25px auto 35px auto;
                                   padding: 14px 0;
                                   font-size: 16px;
                                   font-weight: 700;
                                   color: white !important;
                                   background-color: #C6302C;
                                   text-decoration: none;
                                   border-radius: 6px;
                                   text-align: center;
                                   box-shadow: 0 4px 10px rgba(198, 48, 44, 0.6);
                                   transition: background-color 0.3s ease;
                                 }
                                 .btn:hover {
                                   background-color: #a12324;
                                 }
                                 .summary {
                                   background-color: #292929;
                                   padding: 15px 20px;
                                   border-radius: 8px;
                                   margin-bottom: 30px;
                                   color: #ddd;
                                 }
                                 .summary h3 {
                                   margin-top: 0;
                                   margin-bottom: 12px;
                                   color: #f44336;
                                   font-weight: 700;
                                   font-size: 18px;
                                 }
                                 .summary p {
                                   margin: 5px 0;
                                   font-size: 13px;
                                 }
                                 .footer {
                                   font-size: 12px;
                                   color: #777;
                                   text-align: center;
                                   margin-top: 25px;
                                   border-top: 1px solid #333;
                                   padding-top: 15px;
                                 }
                               </style>
                             </head>
                             <body>
                               <div class="container">
                                 <img src="https://firebasestorage.googleapis.com/v0/b/lovelink-imagens.firebasestorage.app/o/logotipoll.png?alt=media&token=7edad674-9bd0-4c53-9312-fe92d82a4095" alt="LoveLink" class="logo" />
                                 <h2>Olá, %s!</h2>
                                 <p>⏳ O seu pagamento está sendo processado e em instantes você receberá uma confirmação. <br><br>
                                 Após a finalização da análise do seu pagamento, você receberá automaticamente em seu email as informações de acesso da sua nova página personalizada LoveLink (QrCode e Link)</p>                                                          
                           
                                 <div class="summary">
                                   <h3>Resumo da Compra</h3>
                                   <p><strong>Produto:</strong> Página Digital Personalizada</p>
                                   <p><strong>Quantidade:</strong> 1</p>
                                   <p><strong>Valor: R$ </strong> %.2f</p>
                                   <p><strong>Data da Compra:</strong> 19/05/2025</p>
                                   <p><strong>Status:</strong> Em processo ⏳</p>
                                 </div>
                           
                                 
                           
                                 <div class="footer">
                                   Em instantes você receberá a sua pagina!<br />
                                   Se precisar de ajuda, responda este e-mail ou visite nosso suporte.
                                   loveLinkSuport@gmail.com
                                 </div>
                               </div>
                             </body>
                           </html>
                    """.formatted(primeiroNome, valorPago);

            helper.setText(conteudoHtml, true); // true = HTML

            mailSender.send(mensagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
    public void enviarPagamentoRecusado(Pagina pagina){
        try {
            String primeiroNome = getPrimeiroNomeAutor(pagina);
            Double valorPago = planoService.getPlano(pagina.getPlanoSelecionado()).get().getPreco();
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            helper.setTo(pagina.getEmail());
            helper.setSubject("Pagamento recusado");
            String conteudoHtml = """
                    <!DOCTYPE html>
                    <html>
                             <head>
                               <style>
                                 body {
                                   background-color: #121212;
                                   margin: 0;
                                   padding: 0;
                                   font-family: Arial, sans-serif;
                                 }
                                 .container {
                                   border: 1px solid #333;
                                   padding: 30px 40px;
                                   max-width: 600px;
                                   margin: 40px auto;
                                   background-color: #1c1c1c;
                                   border-radius: 10px;
                                   color: #eee;
                                   box-shadow: 0 0 15px rgba(198, 48, 44, 0.4);
                                 }
                                 .logo {
                                   display: block;
                                   margin: 0 auto 20px;
                                   height: 40px;
                                   width: auto;
                                 }
                                 h2 {
                                   font-size: 26px;
                                   font-weight: 800;
                                   margin-bottom: 15px;
                                   text-align: center;
                                   color: #f44336;
                                 }
                                 p {
                                   font-size: 14px;
                                   line-height: 1.5;
                                   margin-bottom: 10px;
                                   color: #ccc;
                                 }
                                 .codigo {
                                   font-size: 12px;
                                   font-weight: 700;
                                   color: #778FFE;
                                   text-decoration: underline;
                                   word-break: break-all;
                                   margin: 20px 0;
                                   text-align: center;
                                 }
                                 .btn {
                                   display: block;
                                   width: 200px;
                                   margin: 25px auto 35px auto;
                                   padding: 14px 0;
                                   font-size: 16px;
                                   font-weight: 700;
                                   color: white !important;
                                   background-color: #C6302C;
                                   text-decoration: none;
                                   border-radius: 6px;
                                   text-align: center;
                                   box-shadow: 0 4px 10px rgba(198, 48, 44, 0.6);
                                   transition: background-color 0.3s ease;
                                 }
                                 .btn:hover {
                                   background-color: #a12324;
                                 }
                                 .summary {
                                   background-color: #292929;
                                   padding: 15px 20px;
                                   border-radius: 8px;
                                   margin-bottom: 30px;
                                   color: #ddd;
                                 }
                                 .summary h3 {
                                   margin-top: 0;
                                   margin-bottom: 12px;
                                   color: #f44336;
                                   font-weight: 700;
                                   font-size: 18px;
                                 }
                                 .summary p {
                                   margin: 5px 0;
                                   font-size: 13px;
                                 }
                                 .footer {
                                   font-size: 12px;
                                   color: #777;
                                   text-align: center;
                                   margin-top: 25px;
                                   border-top: 1px solid #333;
                                   padding-top: 15px;
                                 }
                               </style>
                             </head>
                             <body>
                               <div class="container">
                                 <img src="https://firebasestorage.googleapis.com/v0/b/lovelink-imagens.firebasestorage.app/o/logotipoll.png?alt=media&token=7edad674-9bd0-4c53-9312-fe92d82a4095" alt="LoveLink" class="logo" />
                                 <h2>Péssimas notícias, %s!</h2>
                                 <p>A sua forma de pagamento foi recusada pelo Mercado Pago, não haverá cobranças e sua página personalizada ainda não foi cadastrada. <br> Tente outra forma de pagamento e não perca a oportunidade de finalizar esse presente tão especial!</p>
                                 <div class="summary">
                                   <h3>Resumo da Compra</h3>
                                   <p><strong>Produto:</strong> Página Digital Personalizada</p>
                                   <p><strong>Quantidade:</strong> 1</p>
                                   <p><strong>Valor: R$ </strong> %.2f</p>
                                   <p><strong>Data da Compra:</strong> 19/05/2025</p>
                                   <p><strong>Status:</strong> Pagamento recusado</p>
                                 </div>                                              
                                 <div class="footer">
                                   
                                   Se precisar de ajuda, responda este e-mail ou visite nosso suporte.
                                   loveLinkSuport@gmail.com
                                 </div>
                               </div>
                             </body>
                           </html>
                    """.formatted(primeiroNome, valorPago);

            helper.setText(conteudoHtml, true); // true = HTML

            mailSender.send(mensagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

    public void enviaMensagemSuporte(String email,String nome, String mensagemEnviada){
        try{
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            helper.setTo("lovelinksuport@gmail.com");
            helper.setSubject("Nova mensagem de duvidas");
            String conteudoHtml = """
                <!DOCTYPE html>
                <html>
                  <body style="font-family: Arial, sans-serif; color: #333;">
                    <p>Nova mensagem de %s,</p>       
                    <p>Email: %s</p>
                    <p>%s </p>
                  
                
                  </body>
                </html>
                """.formatted(nome, email, mensagemEnviada );
        }catch (Exception ex){
            throw new RuntimeException("Erro ao enviar e-mail", ex);
        }
    }
    public void enviarRegistroPagina(Pagina pagina){
        try {
            String primeiroNome = getPrimeiroNomeAutor(pagina);
            Double valorPago = planoService.getPlano(pagina.getPlanoSelecionado()).get().getPreco();
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(pagina.getEmail());
            helper.setSubject("Parabéns! você acabou de adquirir um presente mais do que especial !");
            String url = "https://lovelink-frontenddeploy.vercel.app/"+pagina.getSlug()+"/" + pagina.getId();
            String conteudoHtml = """
                    <!DOCTYPE html>
                    <html>
                             <head>
                               <style>
                                 body {
                                   background-color: #121212;
                                   margin: 0;
                                   padding: 0;
                                   font-family: Arial, sans-serif;
                                 }
                                 .container {
                                   border: 1px solid #333;
                                   padding: 30px 40px;
                                   max-width: 600px;
                                   margin: 40px auto;
                                   background-color: #1c1c1c;
                                   border-radius: 10px;
                                   color: #eee;
                                   box-shadow: 0 0 15px rgba(198, 48, 44, 0.4);
                                 }
                                 .logo {
                                   display: block;
                                   margin: 0 auto 20px;
                                   height: 40px;
                                   width: auto;
                                 }
                                 h2 {
                                   font-size: 26px;
                                   font-weight: 800;
                                   margin-bottom: 15px;
                                   text-align: center;
                                   color: #f44336;
                                 }
                                 p {
                                   font-size: 14px;
                                   line-height: 1.5;
                                   margin-bottom: 10px;
                                   color: #ccc;
                                 }
                                 .codigo {
                                   font-size: 12px;
                                   font-weight: 700;
                                   color: #778FFE;
                                   text-decoration: underline;
                                   word-break: break-all;
                                   margin: 20px 0;
                                   text-align: center;
                                 }
                                 .btn {
                                   display: block;
                                   width: 200px;
                                   margin: 25px auto 35px auto;
                                   padding: 14px 0;
                                   font-size: 16px;
                                   font-weight: 700;
                                   color: white !important;
                                   background-color: #C6302C;
                                   text-decoration: none;
                                   border-radius: 6px;
                                   text-align: center;
                                   box-shadow: 0 4px 10px rgba(198, 48, 44, 0.6);
                                   transition: background-color 0.3s ease;
                                 }
                                 .btn:hover {
                                   background-color: #a12324;
                                 }
                                 .summary {
                                   background-color: #292929;
                                   padding: 15px 20px;
                                   border-radius: 8px;
                                   margin-bottom: 30px;
                                   color: #ddd;
                                 }
                                 .summary h3 {
                                   margin-top: 0;
                                   margin-bottom: 12px;
                                   color: #f44336;
                                   font-weight: 700;
                                   font-size: 18px;
                                 }
                                 .summary p {
                                   margin: 5px 0;
                                   font-size: 13px;
                                 }
                                 .footer {
                                   font-size: 12px;
                                   color: #777;
                                   text-align: center;
                                   margin-top: 25px;
                                   border-top: 1px solid #333;
                                   padding-top: 15px;
                                 }
                               </style>
                             </head>
                             <body>
                               <div class="container">
                                 <img src="https://firebasestorage.googleapis.com/v0/b/lovelink-imagens.firebasestorage.app/o/logotipoll.png?alt=media&token=7edad674-9bd0-4c53-9312-fe92d82a4095" alt="LoveLink" class="logo" />
                                 <h2>Pronto, %s!</h2>
                                 <p>Aqui estão o link de acesso e o QR Code em anexo para a sua nova página personalizada ❤️</p>
                           
                                 <div class="codigo">
                                   <p>https://lovelink-frontenddeploy.vercel.app/%s/%s</p>
                                 </div>
                           
                                 <a href="https://lovelink-frontenddeploy.vercel.app/qrCode/%s/%s" class="btn" target="_blank" rel="noopener noreferrer">Gerar QR Code</a>
                           
                                 <div class="summary">
                                   <h3>Resumo da Compra</h3>
                                   <p><strong>Produto:</strong> Página Digital Personalizada</p>
                                   <p><strong>Quantidade:</strong> 1</p>
                                   <p><strong>Valor: R$ </strong> %.2f</p>
                                   <p><strong>Data da Compra:</strong> 19/05/2025</p>
                                   <p><strong>Plano Selecionado:</strong> %s</p>
                                 </div>
                           
                                 <p><strong>Dicas de uso:</strong></p>
                                 <ul>
                                   <li>Você poderá preencher o seu álbum apertando no botão '+'.</li>
                                   <li>Abra um card do álbum para visualizar a descrição e feche clicando na própria imagem.</li>
                                   <li>Ative a animação de chuva de corações clicando na foto principal da página.</li>
                                 </ul>
                           
                                 <div class="footer">
                                   Agradecemos a confiança e desejamos boa sorte com a surpresa!<br />
                                   Se precisar de ajuda, responda este e-mail ou visite nosso suporte.
                                   loveLinkSuport@gmail.com
                                 </div>
                               </div>
                             </body>
                           </html>
                    """.formatted(primeiroNome, pagina.getSlug(), pagina.getId() , pagina.getSlug(), pagina.getId(), valorPago, pagina.getPlanoSelecionado());

            helper.setText(conteudoHtml, true); // true = HTML

            mailSender.send(mensagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
    public byte[] gerarQrCode(String texto, int largura, int altura) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(texto, BarcodeFormat.QR_CODE, largura, altura);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
    public void enviarCodigoVerificacao(String para, String codigo) {
        try {
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject("Aqui está o seu código de verificação:");

            String conteudoHtml = """
            <!DOCTYPE html>
            <html>
            <head>
              <style>
                .container {
                  font-family: Arial, sans-serif;
                  border: 1px solid #ddd;
                  padding: 20px;
                  max-width: 500px;
                  margin: auto;
                  background-color: black;
                  border-radius: 8px;
                }
                .container p,h2{
                    color: white !important;
                }
                .container img{
                    height: 25px;
                    width: auto
                }
                .codigo {
                  font-size: 32px;
                  font-weight: bold;
                  color: red;
                  margin: 20px 0;
                }
                .footer {
                  font-size: 12px;
                  color: #999;
                  margin-top: 20px;
                }
                
              </style>
            </head>
            <body>
              <div class="container">
               <img src="https://firebasestorage.googleapis.com/v0/b/lovelink-imagens.firebasestorage.app/o/logotipoll.png?alt=media&token=7edad674-9bd0-4c53-9312-fe92d82a4095" alt="LoveLink" class="logo" />
                <h2>Confirmação de E-mail</h2>
                <p>Você está a um passo de finalizar sua página e presentear essa pessoa tão especial ❤\uFE0F</p>
                <div class="codigo">%s</div>
                <p>Insira este código na aplicação para continuar.</p>
                <div class="footer">
                  Se você não solicitou este código, apenas ignore este e-mail.
                </div>
              </div>
            </body>
            </html>
            """.formatted(codigo);

            helper.setText(conteudoHtml, true); // true = HTM
            mailSender.send(mensagem);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

}
