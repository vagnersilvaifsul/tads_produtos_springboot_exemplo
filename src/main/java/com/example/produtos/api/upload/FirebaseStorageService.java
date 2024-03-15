package com.example.produtos.api.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/*
    Documentação do Firebase Admin: https://firebase.google.com/docs/storage/admin/start

    Para configurar as Rules (Regras de Acesso) do serviço de Storage:
    Esta regra define que para acessar o serviço o usuário deve estar autenticado no Firebase.
    service firebase.storage {
        match /b/{bucket}/o {
        match /{allPaths=**} {
        allow read, write: if request.auth != null;
    }
*/
@Service
public class FirebaseStorageService {

    @PostConstruct
    private void init() throws IOException {
        if(FirebaseApp.getApps().isEmpty()) {
            InputStream in =
                    FirebaseStorageService.class.getResourceAsStream("/serviceAccountKey.json");

            System.out.println(in);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .setStorageBucket("tads-pedidos-springboot.appspot.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    public String upload(UploadInput uploadInput) {


        Bucket bucket = StorageClient.getInstance().bucket();
        System.out.println("bucket no service:");
        System.out.println(bucket);

//        Blob blob = bucket.create("nome.txt","O texto do arquivo.".getBytes(), "text/html");

        byte[] bytes = Base64.getDecoder().decode(uploadInput.getBase64());

        String fileName = uploadInput.getFileName();
        Blob blob = bucket.create(fileName,bytes, uploadInput.getMimeType());

        // Assina URL válida por N dias
//        URL signedUrl = blob.signUrl(3, TimeUnit.DAYS);

        // Deixa URL pública
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s",bucket.getName(),fileName);
    }
}