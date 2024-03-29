package com.example.produtos.api.upload;

public record UploadInput(
    String fileName,
    String base64,
    String mimeType
) {
}
