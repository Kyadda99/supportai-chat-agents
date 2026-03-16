package com.example.supportai.rag;

import com.example.supportai.dtos.EmbeddingRequest;
import com.example.supportai.dtos.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.*;
import java.util.List;

@Component
public class EmbeddingClient
{
     @Value("${llm.api-key}")
     private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();
    public float[] embed(String text)
    {
        try
        {

            EmbeddingRequest.Part part = new EmbeddingRequest.Part(text);
            EmbeddingRequest.Content content = new EmbeddingRequest.Content(List.of(part));
            EmbeddingRequest requestObj = new EmbeddingRequest("models/gemini-embedding-001", content);

            String body = mapper.writeValueAsString(requestObj);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(
                            "https://generativelanguage.googleapis.com/v1beta/models/gemini-embedding-001:embedContent"
                    ))
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

//            System.out.println("Embedding response: " + response.body());

            EmbeddingResponse res =
                    mapper.readValue(response.body(), EmbeddingResponse.class);

            if(res == null || res.embedding == null || res.embedding.values == null){
                throw new RuntimeException("Embedding API returned null: " + response.body());
            }

            float[] vector = new float[res.embedding.values.size()];

            for(int i=0;i<vector.length;i++)
                vector[i] = res.embedding.values.get(i);

            return vector;

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
