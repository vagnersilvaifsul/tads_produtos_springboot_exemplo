package com.example.produtos.api.servicos.mail;

public interface EmailService {

    void enviarEmail(String to, String subject, String message);

}
