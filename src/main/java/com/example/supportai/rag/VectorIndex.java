package com.example.supportai.rag;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VectorIndex
{
    private final Directory directory = new ByteBuffersDirectory();
    private final IndexWriterConfig config = new IndexWriterConfig();
    private final List<String> texts = new ArrayList<>();
    private final IndexWriter writer;

    public VectorIndex() {
        try {
            writer = new IndexWriter(directory, config);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void add(float[] vector, String text) {
        try {
            Document doc = new Document();
            doc.add(new KnnVectorField("vector", vector));
            doc.add(new StoredField("text", text));

            writer.addDocument(doc);
            writer.commit();
            texts.add(text);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> search(float[] queryVector, int k) {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            Query query = new KnnVectorQuery("vector", queryVector, k);

            TopDocs docs = searcher.search(query, k);

            List<String> result = new ArrayList<>();
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                result.add(doc.get("text"));
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}