package com.example.supportai.model;

public record ChatResponse(

        String conversationID,
        String agent,
        String answer
) {}
