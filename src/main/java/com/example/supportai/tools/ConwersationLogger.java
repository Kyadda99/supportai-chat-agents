package com.example.supportai.tools;

import com.example.supportai.model.Message;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ConwersationLogger
{
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseDirectory = "conwersations";

    public ConwersationLogger()
    {
        File directory = new File(baseDirectory);
        if (!directory.exists()) directory.mkdir();
    }

    public void log(String conversationId, List<Message> history){

        try (FileWriter file = new FileWriter(baseDirectory + "/" + conversationId + ".json")) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, history);

        } catch (IOException e) {
            System.err.println("Failed to log conversation: " + e.getMessage());
        }
    }


}
