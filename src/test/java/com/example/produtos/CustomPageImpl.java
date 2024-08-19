package com.example.produtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

/*
    Classe Utilitária
    Esta classe instrui o Jackson (o conversor de JSON em objeto Java do Spring Boot) como converter um Page
    em objeto de modelo Java.
 */
public class CustomPageImpl <T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPageImpl(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
                          @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
                          @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                          @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                          @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, 1), 50);
    }

    public CustomPageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CustomPageImpl(List<T> content) {
        super(content);
    }

    public CustomPageImpl() {
        super(new ArrayList<>());
    }
}
