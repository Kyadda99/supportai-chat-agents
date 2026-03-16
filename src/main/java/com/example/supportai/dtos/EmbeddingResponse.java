package com.example.supportai.dtos;

import java.util.List;

public class EmbeddingResponse
{
    public Embedding embedding;

    public static class Embedding {
        public List<Float> values;
    }
}
