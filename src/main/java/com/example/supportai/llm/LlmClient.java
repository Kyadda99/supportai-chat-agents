package com.example.supportai.llm;

import com.example.supportai.dtos.GeminiRequest;
import com.example.supportai.dtos.GeminiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class LlmClient
{
    private final String apiKey;
    private final String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    public LlmClient(@Value("${llm.api-key}") String apiKey, @Value("${llm.api-url}") String apiUrl)
    {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    public String call(String prompt)
    {
        try
        {
            GeminiRequest geminiRequest = new GeminiRequest();
            geminiRequest.contents = List.of(new GeminiRequest.Content(
                    List.of(new GeminiRequest.Part(prompt))
            ));


            String body = mapper.writeValueAsString(geminiRequest);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

            GeminiResponse gemini =
                    mapper.readValue(response.body(), GeminiResponse.class);

            return gemini.candidates
                    .get(0)
                    .content
                    .parts
                    .get(0)
                    .text;

        } catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

    }


}
