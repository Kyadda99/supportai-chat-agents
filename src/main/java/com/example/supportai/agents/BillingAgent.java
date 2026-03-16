package com.example.supportai.agents;

import com.example.supportai.llm.LlmClient;
import com.example.supportai.tools.BillingTools;
import org.springframework.stereotype.Component;

@Component
public class BillingAgent
{
    private final LlmClient llmClient;
    private final BillingTools billingTools;

    public BillingAgent(LlmClient llmClient, BillingTools billingTools)
    {
        this.llmClient = llmClient;
        this.billingTools = billingTools;
    }

    public String handle(String question){



        String prompt = """
                You are a billing specialist.
                
                Available tools:
                getCustomerPlan
                openRefundRequest
                getBillingHistory
        
                User question:
                %s
        
                Respond with tool name if needed.
                
                """.formatted(question);
        String response = llmClient.call(prompt);

        if(response.contains("getCustomerPlan"))
            return billingTools.getCustomerPlan();
        if(response.contains("openRefundRequest"))
            return billingTools.openRefundRequest();
        if(response.contains("getBillingHistory"))
            return billingTools.getBillingHistory();

        return response;
    }



}
