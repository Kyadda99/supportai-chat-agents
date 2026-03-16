package com.example.supportai.service;


import com.example.supportai.agents.BillingAgent;
import com.example.supportai.agents.TechnicalAgent;
import com.example.supportai.model.ChatResponse;
import com.example.supportai.model.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationService
{
    private final RouterService routerService;
    private final TechnicalAgent technicalAgent;
    private final BillingAgent billingAgent;


    private final Map<String, List<Message>> conversations = new HashMap<>();

    public ConversationService(RouterService routerService, TechnicalAgent technicalAgent, BillingAgent billingAgent)
    {
        this.routerService = routerService;
        this.technicalAgent = technicalAgent;
        this.billingAgent = billingAgent;
    }

    public ChatResponse handleMessage(String conversationId, String message)
    {
        conversations.computeIfAbsent(conversationId, k -> new ArrayList<>());

        conversations.get(conversationId).add(new Message(message, "user"));

        String route = routerService.route(message);

        String anserw;
        String agent;

        if(route.equalsIgnoreCase("Technical")){
            anserw = technicalAgent.handle(message);
            agent = "Technical Specialist";
        }
        else if(route.equalsIgnoreCase("Billing")){
            anserw = billingAgent.handle(message);
            agent = "Technical Specialist";
        }
        else {
            anserw = "I’m sorry, but I cannot assist with that request. Please contact our general support team.";
            agent = "default";

        }

        conversations.get(conversationId).add(new Message("assistant",anserw));
        return new ChatResponse(agent, anserw);

    }


}
