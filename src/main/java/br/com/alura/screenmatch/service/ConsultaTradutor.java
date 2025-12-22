package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsultaTradutor {

    private static final String URL_BASE =
            "https://api.mymemory.translated.net/get?q=%s&langpair=en%%7Cpt-BR";

    public static String obterTraducao(String texto) {

        try {
            String textoCodificado =
                    URLEncoder.encode(texto, StandardCharsets.UTF_8);

            String url = String.format(URL_BASE, textoCodificado);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.body());

            return json
                    .path("responseData")
                    .path("translatedText")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao traduzir com MyMemory", e);
        }
    }
}
