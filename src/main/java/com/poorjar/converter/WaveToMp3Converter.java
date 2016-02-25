package com.poorjar.converter;

import java.io.File;
import java.util.StringTokenizer;

public class WaveToMp3Converter
{
    public void convertWavetoMp3(String filePath)
    {

    }

    private File getOutputFile(String fileName)
    {
        StringTokenizer tokenizer = new StringTokenizer(fileName, ".");
        return new File(tokenizer.nextToken() + ".wav");
    }
}
