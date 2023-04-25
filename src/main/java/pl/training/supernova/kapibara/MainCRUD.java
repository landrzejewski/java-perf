package pl.training.supernova.kapibara;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

@Warmup(iterations = 1)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class MainCRUD {

  private final int ITERATIONS = 1000;

  @Benchmark
  public void testCRUD1Create() throws IOException {
    KapibaraService service = new KapibaraService();
    for (long i = 0; i < ITERATIONS; i++) {
      service.create(new Kapibara(i, generate(50), generate(70), new Random().nextInt(), new Random().nextBoolean()));
    }
  }
  @Benchmark
  public void testCRUD2Edit() throws IOException {
    KapibaraService service = new KapibaraService();
    for (long i = 0; i < ITERATIONS; i++) {
      service.update(new Kapibara(i, generate(50), generate(70), new Random().nextInt(), new Random().nextBoolean()));
    }
  }
  @Benchmark
  public void testCRUD22Edit() throws IOException {
    KapibaraService service = new KapibaraService();
    for (long i = 0; i < ITERATIONS; i++) {
      service.update(new Kapibara(i, "Janek"+i, "Kowalski"+i, (int) i, i%2>1));
    }
  }
  @Benchmark
  public void testCRUD3Get() throws IOException {
    KapibaraService service = new KapibaraService();
    for (long i = 0; i < ITERATIONS; i++) {
      service.get(new Random().nextInt(0,ITERATIONS));
    }
  }
  @Benchmark
  public void testCRUD4Delete() throws IOException {
    KapibaraService service = new KapibaraService();
    for (long i = 0; i < ITERATIONS; i++) {
      service.delete(new Random().nextInt(0,ITERATIONS));
    }
  }
  public static void main(String[] args) throws RunnerException, IOException {
    Files.deleteIfExists(KapibaraService.FILE_PATH);
    Files.createFile(KapibaraService.FILE_PATH);
    var options = new OptionsBuilder()
      .include("testCRUD")
      .forks(1)
      .threads(1)
      .build();
    new Runner(options).run();
  }

  private String generate(int length){
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1)
      .limit(length)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
      .toString();
  }
}
