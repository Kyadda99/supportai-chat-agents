package com.example.supportai.dtos;

import java.util.List;

public class EmbeddingRequest
{
    public String model;
    public Content content;

    public EmbeddingRequest(String model, Content content)
    {
        this.model = model;
        this.content = content;
    }

    public static class Content
    {
        public List<Part> parts;

        public Content(List<Part> parts)
        {
            this.parts = parts;
        }
    }

    public static class Part
    {
        public String text;

        public Part(String text)
        {
            this.text = text;
        }
    }
}
