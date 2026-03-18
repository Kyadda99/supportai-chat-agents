package com.example.supportai.agents;

import com.example.supportai.llm.LlmClient;

import com.example.supportai.rag.DocumentLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TechnicalAgent
{
    private final DocumentLoader loader;
    private final LlmClient llm;

    public TechnicalAgent(DocumentLoader loader, LlmClient llm)
    {
        this.loader = loader;
        this.llm = llm;
    }

    public String handle(String question, String history)
    {



        if(history.length()>4000) history = history.substring(history.length()-4000);

        List<String> docummnets = loader.search(history + question);
        String context = String.join("\n\n", docummnets);


        if(context.length()>2000) context = context.substring(context.length()-2000);


        String prompt = """
              You are an expert technical support specialist.

              RULES:
              - Answer ONLY from documentation
              - If not found say: I could not find sufficient information in the available documentation to answer this question.

              CONVERSATION HISTORY:
              %s

              DOCUMENTATION:
              %s

              QUESTION:
              %s
        """.formatted(history, context, question);

        return llm.call(prompt);
    }


}


