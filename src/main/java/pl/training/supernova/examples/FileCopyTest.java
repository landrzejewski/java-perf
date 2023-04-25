package pl.training.supernova.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class FileCopyTest {

    private static final Path INPUT_PATH = Paths.get("random.data");
    private static final Path OUTPUT_PATH = Paths.get("random-copy.data");
    private static final int BUFFER_SIZE = 512 * 1024;
    private static final ByteBuffer BUFFER = ByteBuffer.allocate(BUFFER_SIZE);

    @Setup(Level.Iteration)
    public void setup() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
    }

    @Benchmark
    public void copyBytes() throws IOException {
        try (InputStream inputStream = Files.newInputStream(INPUT_PATH);
             OutputStream outputStream = Files.newOutputStream(OUTPUT_PATH)) {
            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
            }
        }
    }

    @Benchmark
    public void copyBytesWithBufferedStream() throws IOException {
        try (InputStream inputStream = Files.newInputStream(INPUT_PATH);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, BUFFER_SIZE);
             OutputStream outputStream = Files.newOutputStream(OUTPUT_PATH);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
            int data;
            while ((data = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(data);
            }
        }
    }

    @Benchmark
    public void copyBytesWithChannels() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(INPUT_PATH.toFile());
             FileChannel inputChannel = fileInputStream.getChannel();
             FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_PATH.toFile());
             FileChannel outputChannel = fileOutputStream.getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            //outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

            /*while ((inputChannel.read(BUFFER)) != -1) {
                BUFFER.flip();
                while (BUFFER.hasRemaining()) {
                    outputChannel.write(BUFFER);
                }
                BUFFER.clear();
            }*/
        }
    }

    @Benchmark
    public void copyBytesWithMappedFile() throws IOException {
        var buffer = new byte[BUFFER_SIZE];
        try (RandomAccessFile inputFile = new RandomAccessFile(INPUT_PATH.toFile(), "r");
             FileChannel fileChannel = inputFile.getChannel();
             RandomAccessFile outputFile = new RandomAccessFile(OUTPUT_PATH.toFile(), "rw");
             FileChannel outputChannel = outputFile.getChannel();) {
            MappedByteBuffer inputMappedFile = fileChannel.map(READ_ONLY, 0, fileChannel.size());
            MappedByteBuffer outputMappedFile = outputChannel.map(READ_WRITE, 0, fileChannel.size());
            while (inputMappedFile.hasRemaining()) {
                outputMappedFile.put(inputMappedFile.get(buffer));
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include("copyBytesWith")
                .forks(1)
                .threads(1)
                .build();
        new Runner(options).run();
    }


}
