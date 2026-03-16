package com.example.supportai.rag;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TextChunker
{
    public static List<String> split(String text, int maxSize) {

        try {

            InputStream modelStream =
                    TextChunker.class.getResourceAsStream("/models/en-sent.bin");

            SentenceModel model = new SentenceModel(modelStream);
            SentenceDetectorME detector = new SentenceDetectorME(model);

            String[] sentences = detector.sentDetect(text);

            List<String> chunks = new ArrayList<>();

            StringBuilder current = new StringBuilder();

            for(String sentence : sentences){

                if(current.length() + sentence.length() > maxSize){

                    chunks.add(current.toString());
                    current = new StringBuilder();
                }

                current.append(sentence).append(" ");
            }

            if(!current.isEmpty())
                chunks.add(current.toString());

            return chunks;

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
