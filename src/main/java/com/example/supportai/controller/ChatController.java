package com.example.supportai.controller;


import com.example.supportai.model.ChatRequest;
import com.example.supportai.model.ChatResponse;
import com.example.supportai.service.ConversationService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/chat")
public class ChatController
{
    private final ConversationService conversationService;

    public ChatController(ConversationService conversationService)
    {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request)
    {
        String conversationId = request.conversationId();
        if (conversationId == null || conversationId.isBlank()) {
            conversationId = UUID.randomUUID().toString();
        }
        return conversationService.handleMessage(request.conversationId(),request.message());
    }
}
