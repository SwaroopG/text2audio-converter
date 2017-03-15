package com.poorjar.converter;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

import java.io.File;
import java.io.IOException;

public class WaveToMp3Converter {

    private static String pathToFFmpegBin = "C:\\Users\\csdavidson\\projects\\text2audio-converter\\ffmpeg-20170312-58f0bbc-win64-static\\bin\\";

    public static void convertWavToMp3(final String filePath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(pathToFFmpegBin + "ffmpeg.exe");
        FFprobe ffprobe = new FFprobe(pathToFFmpegBin + "ffprobe.exe");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(filePath + ".wav")
                .overrideOutputFiles(true)
                .addOutput(filePath + ".mp3")
                .setFormat("mp3")
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        FFmpegJob job = executor.createJob(builder, new ProgressListener() {
            @Override
            public void progress(Progress progress) {
                if (progress.isEnd()) {
                    File oldWav = new File(filePath + ".wav");
                    oldWav.delete();
                }
            }
        });
        job.run();
    }
}
