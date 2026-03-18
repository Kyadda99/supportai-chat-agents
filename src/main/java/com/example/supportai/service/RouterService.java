package com.example.supportai.service;

import com.example.supportai.llm.LlmClient;
import org.springframework.stereotype.Service;

@Service
public class RouterService
{
    private final LlmClient llm;

    public RouterService(LlmClient llm)
    {
        this.llm = llm;
    }

    public String route(String conversation){



        String prompt=  """
        Classify the user message into one category:
        
        TECHNICAL
        BILLING
        OTHER
        
        Consider full context:
        
        Conversation
        %s

        Return only category name.
        """.formatted(conversation).trim();

        return llm.call(prompt).trim();

    }



}
