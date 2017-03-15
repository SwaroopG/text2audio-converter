package com.poorjar.converter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.StringTokenizer;

import com.google.common.base.Stopwatch;
import marytts.client.MaryClient;
import marytts.util.http.Address;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Sets;

/**
 * This Class converts a given text file into audio file (wave format) using the MARY TTS API.
 * <p/>
 * <b>Note</b>: MARY TTS Server should be running locally. <br/>
 * <ul>
 * <li>Step 1: Download the MARY TTS Standalone server</li>
 * <li>Step 2: Start the MARY TTS Server at (installDir)/bin/maryserver.</li>
 * <li>Step 3: Verify the MARY TTS Server status at http://localhost:59125/</li>
 * </ul>
 * <p/>
 * @see <a href="http://mary.dfki.de/download/index.html">http://mary.dfki.de/download/index.html</a>
 * @see <a href="https://github.com/marytts/marytts/releases/tag/v4.3.1">https://github.com/marytts/marytts/releases/tag/v4.3.1</a> <p/>
 * @author Swaroop
 */
public class Text2AudioConverter {
    static final Logger LOGGER = Logger.getLogger(Text2AudioConverter.class);

    private static String maryHostname = "localhost";
    private static int maryPort = 59125;

    private static String locale = "en_US";
    private static String inputType = "TEXT";
    private static String outputType = "AUDIO";
    private static String audioType = "WAVE";
    private static String voiceName = "cmu-slt-hsmm";

    private String outputPath = "";

    /**
     * Converts a given text file into a audio file of WAVE format.
     * @param inputFile The input file path. Cannot be null or empty.
     * @throws Exception
     */
    public void convertText2Audio(String inputFile) throws IOException {
        LOGGER.debug("Starting conversion of file: " + inputFile);
        if (StringUtils.isEmpty(inputFile)) {
            throw new IllegalArgumentException("Invalid file path for file: " + inputFile);
        }
        StringTokenizer tokenizer = new StringTokenizer(inputFile, ".");
        outputPath = tokenizer.nextToken();

        String inputText = readFileToString(inputFile);
        convertToWav(inputText, outputPath);
        WaveToMp3Converter.convertWavToMp3(outputPath);
    }

    // TODO: This is very memory inefficient.
    private static String readFileToString(String inputFile) throws IOException {
        return FileUtils.readFileToString(new File(inputFile));
    }

    private static void convertToWav(String inputText, String outputPath) throws IOException {
        ByteArrayOutputStream audioOutputStream = new ByteArrayOutputStream();
        MaryClient mary = MaryClient.getMaryClient(new Address(maryHostname, maryPort));
        //TODO voiceName throws error if voice is not installe I only had cmu-slt locally
        mary.process(inputText, inputType, outputType, locale, audioType, voiceName, audioOutputStream);
        FileOutputStream fileOut = new FileOutputStream(new File(outputPath + ".wav"));
        audioOutputStream.writeTo(fileOut);
        audioOutputStream.flush();
        fileOut.close();
    }

    /**
     * Goes through the "TXT" files in the folder and converts them to WAV files. The audio files will have the same name as the
     * corresponding text file but with a .WAV extension.
     * @param directory The directory path. Cannot be null or empty.
     * @throws Exception
     */
    public void convertText2AudioFromDir(String directory) throws Exception {
        // Verify the directory name
        if (StringUtils.isEmpty(directory)) {
            throw new IllegalArgumentException("Invalid directory path");
        }

        for (String inputFile : getFiles(directory)) {
            Stopwatch timer = Stopwatch.createStarted();
            convertText2Audio(inputFile);
            LOGGER.info("Conversion time for file - " + inputFile + " - took " + timer.stop());
        }
    }

    /**
     * Files have to be text files with a *.txt extension.
     * <p/>
     * TODO: BUG: FileUtils gets all .txt files from main and sub directories as well.
     * @param directory
     * @return A set of file names.
     */
    private Set<String> getFiles(String directoryPath) {
        try {
            return Sets.newHashSet(marytts.util.io.FileUtils.getFileList(directoryPath, "txt"));
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid directory path : " + directoryPath);
        }
    }

    // TODO: Experiment with Codahale Metrics later
    // static final MetricRegistry metrics = new MetricRegistry();
    // private Timer audioConversionTimer = metrics.timer("audioConversionTime");
    // final Timer.Context context = audioConversionTimer.time();
    // context.stop();
}
