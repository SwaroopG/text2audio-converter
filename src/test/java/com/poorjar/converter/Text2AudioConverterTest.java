package com.poorjar.converter;

import marytts.util.io.FileUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Swaroop
 */
public class Text2AudioConverterTest
{
    Text2AudioConverter converter = new Text2AudioConverter();

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testNullFileNameConversion() throws Exception
    {
        converter.convertText2Audio(null);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFileNameConversion() throws Exception
    {
        converter.convertText2Audio("");
    }

    @Ignore
    @Test
    public void testSingleTextFileConversion() throws Exception
    {
        converter.convertText2Audio("src/test/resources/file1.txt");
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDirectoryConversion() throws Exception
    {
        converter.convertText2AudioFromDir("src/main/resources/");
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDirectoryNameConversion() throws Exception
    {
        converter.convertText2AudioFromDir("");
    }

    @Test
    public void testDirectoryConversion() throws Exception
    {
        converter.convertText2AudioFromDir("src/test/resources/");
    }

    @BeforeClass
    //@AfterClass
    public static void cleanup()
    {
        String[] generatedAudioFiles = FileUtils.getFileList("src/test/resources/", "wav");
        if (generatedAudioFiles != null)
        {
            FileUtils.delete(generatedAudioFiles);
        }
    }
}
