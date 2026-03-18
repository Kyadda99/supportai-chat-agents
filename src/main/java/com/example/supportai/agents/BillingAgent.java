package com.example.supportai.agents;

import com.example.supportai.llm.LlmClient;
import com.example.supportai.tools.BillingTools;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class BillingAgent
{
    private final LlmClient llmClient;
    private final BillingTools billingTools;
    private final ObjectMapper mapper = new ObjectMapper();

    public BillingAgent(LlmClient llmClient, BillingTools billingTools)
    {
        this.llmClient = llmClient;
        this.billingTools = billingTools;
    }

    public String handle(String question, String history){



        String prompt = """
                You are a billing specialist.
                
                Available tools:
                getCustomerPlan - returns customer's plan and pricing
                openRefundRequest - opens a refund request
                getBillingHistory - provides customer's billing history
                clarify - use if you need to ask any followup questions, use parameter "question" 
        
                Rules:
                - Always decide if a tool is needed for the answer
                - If no tool is needed, set "tool": "none" and provide your answer in "parameters.answer"
                - Respond ONLY with a JSON object in the format:
                  {
                      "tool": "<tool_name or none>",
                      "parameters": { ... }
                  }

                Conversation history:
                %s

                User question:
                %s
                """.formatted(history, question);

        String response = llmClient.call(prompt);

        try {
            JsonNode node = mapper.readTree(response);
            String tool = node.get("tool").asText();

            switch (tool) {
                case "getCustomerPlan":
                    return billingTools.getCustomerPlan();
                case "openRefundRequest":
                    return billingTools.openRefundRequest();
                case "getBillingHistory":
                    return billingTools.getBillingHistory();
                case "clarify":
                    return node.get("parameters").get("question").asText();
                case "none":
                    return node.get("parameters").get("answer").asText();
                default:
                    return response;
            }

        } catch (Exception e) {
            return "Could not parse response, response: " + response;
        }
    }



}
