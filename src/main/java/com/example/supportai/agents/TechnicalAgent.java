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

    public String handle(String question)
    {
        List<String> docummnets = loader.search(question);
        String context = String.join("\n\n", docummnets);

        String prompt = """
                You are an expert technical support specialist with deep knowledge of documentation analysis.
                
                CORE RULES
                - Answer EXCLUSIVELY using the provided documentation below
                - NEVER use external knowledge, assumptions, or generalizations
                - If the answer is not fully present in the documentation, say exactly: I could not find sufficient information in the available documentation to answer this question.
                - Do not speculate or fill gaps with plausible-sounding information
        
                RESPONSE FORMAT
                Start with a direct and concise answer to the question.
                Then add relevant context or steps from the documentation.
                Finally indicate which part of the documentation supports your answer.
        
                TONE AND STYLE
                - Professional but approachable
                - Use plain numbered steps for procedural answers
                - Keep responses focused and avoid repeating irrelevant documentation content
        
                DOCUMENTATION
                %s
        
                USER QUESTION
                %s
        
                Respond now following the rules above.
        """.formatted(context, question);

        return llm.call(prompt);
    }


}


