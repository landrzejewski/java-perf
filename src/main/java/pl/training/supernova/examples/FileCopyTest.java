package pl.training.supernova.examples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@BenchmarkMode(Mode.SingleShotTime)
public class FileCopyTest {

    private static final Path INPUT_PATH = Paths.get("random.data");
    private static final Path OUTPUT_PATH = Paths.get("random-copy.data");

    @Benchmark
    public void copyByteByByte() throws IOException {
        try (InputStream inputStream = Files.newInputStream(INPUT_PATH);
             OutputStream outputStream = Files.newOutputStream(OUTPUT_PATH)) {
            int data;
            while ((data = inputStream.read()) != -1) {
                outputStream.write(data);
            }
        }
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .warmupIterations(3)
                .measurementIterations(3)
                .threads(1)
                .build();
        new Runner(options).run();
    }


}
