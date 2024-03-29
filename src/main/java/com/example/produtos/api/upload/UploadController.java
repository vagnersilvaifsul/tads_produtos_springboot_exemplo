package com.example.produtos.api.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //indica que essa classe deve ser adicionada ao Contexto do aplicativo como um Bean da camada de controle API REST
@RequestMapping("/api/v1/upload") //Endpoint padrão da classe
public class UploadController {

    @Autowired //indica ao Spring Boot que ele deve injetar essa dependência para a classe funcionar
    private FirebaseStorageService uploadService;

    @PostMapping
    public ResponseEntity<UploadOutput> upload(@RequestBody UploadInput uploadInput) {

        String url = uploadService.upload(uploadInput);

        return ResponseEntity.ok(new UploadOutput(url));
    }
}
