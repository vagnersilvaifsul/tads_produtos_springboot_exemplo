package com.example.produtos.api.servicos.upload;

public record UploadInput(
    String fileName,
    String base64,
    String mimeType
) {
}
