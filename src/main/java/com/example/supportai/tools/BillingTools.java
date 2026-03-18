package com.example.supportai.tools;

import org.springframework.stereotype.Component;

@Component
public class BillingTools
{
    public String getCustomerPlan()
    {
        return "Customer plan is Basic, cost: 30$ per month";
    }

    public String openRefundRequest()
    {

        return "Refund request has been opened. you will receive answer in 30 days";
    }

    public String getBillingHistory()
    {
        return "Last charge was on 49PLN on 1 April 2026";
    }

}
