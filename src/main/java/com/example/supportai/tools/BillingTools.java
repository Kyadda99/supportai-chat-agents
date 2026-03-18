package com.example.supportai.tools;

import org.springframework.stereotype.Component;

@Component
public class BillingTools
{
    public String getCustomerPlan() {
        return "Customer plan is Basic - wwww";
    }

    public String openRefundRequest() {

        return "Refund request has been opened. - wwww";
    }

    public String getBillingHistory() {
        return "Last charge: 49PLN on 1 April. - wwww";
    }

}
