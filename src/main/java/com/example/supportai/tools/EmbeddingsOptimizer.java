package com.example.supportai.tools;
import org.springframework.stereotype.Component;
import smile.feature.extraction.PCA;

@Component
public class EmbeddingsOptimizer
{

    public float[] reduceTo1024(float[] vector)
    {
        float[] truncated = new float[1024];
        int copyLen = Math.min(vector.length, 1024);
        System.arraycopy(vector, 0, truncated, 0, copyLen);

        double norm = 0.0;
        for (float v : truncated) norm += v * v;
        norm = Math.sqrt(norm);
        if (norm > 0)
        {
            for (int i = 0; i < truncated.length; i++)
            {
                truncated[i] /= (float) norm;
            }
        }
        return truncated;
    }
}
