package com.example.supportai.dtos;

import java.util.List;

public class GeminiRequest
{
    public List<Content> contents;

    public static class Content {
        public List<Part> parts;

        public Content(List<Part> parts) {
            this.parts = parts;
        }
    }

    public static class Part {
        public String text;

        public Part(String text) {
            this.text = text;
        }
    }
}