package com.example.supportai.rag;


import com.example.supportai.tools.EmbeddingsOptimizer;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class DocumentLoader
{
    private final VectorIndex index = new VectorIndex();
    private final EmbeddingClient embeddingClient;
    private final EmbeddingsOptimizer embeddingsOptimizer;

    public DocumentLoader(EmbeddingClient embeddingClient, EmbeddingsOptimizer embeddingsOptimizer) throws Exception {

        this.embeddingClient = embeddingClient;
        this.embeddingsOptimizer = embeddingsOptimizer;

        Path dir = Paths.get("src/main/resources/docs");

        Files.list(dir).forEach(path -> {

            try {

                String text = readPdf(path);

                var chunks =
                        TextChunker.split(text, 800);

                for(String chunk : chunks){

                    float[] vector =
                            embeddingClient.embed(chunk);

                    if(vector.length > 1024){
                        vector = embeddingsOptimizer.reduceTo1024(vector);
                    }

                    index.add(vector, chunk);
                }

            } catch(Exception e){
                e.printStackTrace();
            }

        });
    }

        public java.util.List<String> search(String question){

        float[] queryVector =
                embeddingClient.embed(question);

            if(queryVector.length > 1024){
                queryVector = embeddingsOptimizer.reduceTo1024(queryVector);
            }

        return index.search(queryVector, 4);
    }

        private String readPdf(Path path){

        try(PDDocument doc = Loader.loadPDF(path.toFile())){

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
