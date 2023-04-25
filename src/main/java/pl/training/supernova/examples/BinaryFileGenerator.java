package pl.training.supernova.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class BinaryFileGenerator {

    private static final Path FILE_PATH = Paths.get("random.data");
    private static final long FILE_SIZE = 1024L * 1024L;
    private static final byte[] buffer = new byte[1024];

    public static void main(String[] args) throws IOException {
        var random = new Random();
        Files.deleteIfExists(FILE_PATH);
        try (var outputStream = Files.newOutputStream(FILE_PATH)) {
            for (long i = 0; i < FILE_SIZE / buffer.length; i++) {
                random.nextBytes(buffer);
                outputStream.write(buffer);
            }
            int remainingBytes = (int) (FILE_SIZE % buffer.length);
            if (remainingBytes > 0) {
                var remainingBuffer = new byte[remainingBytes];
                random.nextBytes(remainingBuffer);
                outputStream.write(remainingBuffer);
            }
        }
    }

}
