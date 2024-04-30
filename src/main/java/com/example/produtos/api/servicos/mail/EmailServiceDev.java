package com.example.produtos.api.servicos.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*
    Para configurar uma conta para envio de emails, crie uma conta no Google (este projeto está baseado na
    conta Google). Vá em >Gerenciar a sua Conta do Google > Seguraça e adicione >Verificação em Duas Etapas.
    Feito isso, volte em Seguraça, vá na barra de pesquisa da janela (na parte superior da janela) e pesquise por
    Senhas de Aplicativos. Na janela de geração de senhas de aplicativos, adicione o seu app, pegue o nome dele
    no pom.xml, em <name>. Siga os passos da janela, pegue a senha de 16 dígitos gerada, a armazene com segurança
    e adicione ao seu projeto (o indicado é adicionar por variáveis de ambiente, mas, para testes em dev, você
    pode adicionar o application.properties. Lembre de retirar antes de subir em um servidor na web).

    Link da documentação do Google: https://support.google.com/accounts/answer/185833 (título: Fazer login com senhas de app)
 */

@Service
@Profile("dev")
public class EmailServiceDev implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void enviarEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("<enderecoDaContaGoogle>@gmail.com");
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        emailSender.send(email);
    }

}
