package com.example.supportai.service;


import com.example.supportai.agents.BillingAgent;
import com.example.supportai.agents.TechnicalAgent;
import com.example.supportai.llm.LlmClient;
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
    private final LlmClient llmClient;


    private final Map<String, List<Message>> conversations = new HashMap<>();
    private final Map<String, String> activeAgent = new HashMap<>();
    private final Map<String, String> conversationSummary = new HashMap<>();

    public ConversationService(RouterService routerService, TechnicalAgent technicalAgent, BillingAgent billingAgent, LlmClient llmClient)
    {
        this.routerService = routerService;
        this.technicalAgent = technicalAgent;
        this.billingAgent = billingAgent;
        this.llmClient = llmClient;
    }

    public ChatResponse handleMessage(String conversationId, String message)
    {
        conversations.computeIfAbsent(conversationId, k -> new ArrayList<>());

        List<Message> history = conversations.get(conversationId);
        history.add(new Message(message, "user"));

        if (history.size()>20){

            String summary = summarizeConversation(history.subList(0, history.size() - 5));

            conversationSummary.put(conversationId, summary);

            List<Message> lastMessages =new ArrayList<>(history.subList(history.size() - 5, history.size()));

            conversations.put(conversationId, lastMessages);
            history = lastMessages;

        }
        String historyText = buildConversationHistory(conversationId, history);



        String route;

        if (!activeAgent.containsKey(conversationId)) {
            route = routerService.route(historyText);
        } else {
            route = activeAgent.get(conversationId);

            String newRoute = routerService.route(historyText);

            if (!newRoute.equalsIgnoreCase("OTHER")
                    && !newRoute.equalsIgnoreCase(route)) {
                route = newRoute;
            }
        }

        activeAgent.put(conversationId, route);

        String anserw;
        String agent;





        if(route.equalsIgnoreCase("Technical")){
            anserw = technicalAgent.handle(message,historyText);
            agent = "Technical Specialist";
        }
        else if(route.equalsIgnoreCase("Billing")){
            anserw = billingAgent.handle(message, historyText);
            agent = "Billing Agent";
        }
        else if(route.startsWith("ERROR-ERROR-ERROR")){
            anserw = route.substring(18);
            agent = "error";
        }
        else {
            anserw = "I’m sorry, but I cannot assist with that request. Please contact our general support team.";
            agent = "default";

        }

        history.add(new Message(anserw, "assistant"));


        return new ChatResponse(agent, anserw);

    }

    private String buildConversationHistory(String conversationId, List<Message> messages) {

        StringBuilder sb = new StringBuilder();

        if (conversationSummary.containsKey(conversationId)) {
            sb.append("SUMMARY:\n")
                    .append(conversationSummary.get(conversationId))
                    .append("\n\n");
        }

        int maxMessages = 10;
        int start = Math.max(0, messages.size() - maxMessages);

        for (int i = start; i < messages.size(); i++) {
            Message m = messages.get(i);
            if (m.role.equals("user")) {
                sb.append("User: ").append(m.content).append("\n");
            } else {
                sb.append("Assistant: ").append(m.content).append("\n");
            }
        }

        return sb.toString();

    }


    private String summarizeConversation(List<Message> messages){
        StringBuilder sb = new StringBuilder();

        for (Message m : messages)
        {
            if (m.role.equals("user"))
            {
                sb.append("User: ").append(m.content).append("\n");
            } else
            {
                sb.append("Assistant: ").append(m.content).append("\n");
            }
        }
            String prompt = """
                Summarize the conversation briefly.
                
                Keep:
                - key problem
                - important context
                - user intent
                
                Conversation:
                %s
                """.formatted(sb.toString());


        return llmClient.call(prompt);


    }

}
