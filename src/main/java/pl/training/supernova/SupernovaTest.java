package pl.training.supernova;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class SupernovaTest {

    private final Path filePath = Paths.get("persons.data");
    private final Random random = new Random();
    private long id;
    private Supernova<PersonRow, Long> supernova;
    private PersonRow nextPerson;

    @Setup(Level.Trial)
    public void beforeAll() throws IOException {
        Files.deleteIfExists(filePath);
        supernova = new FlatFileSupernova<>(filePath, new PersonRow(), new HashMap<>());
    }

    @TearDown
    public void afterAll() throws Exception {
        supernova.close();
    }

    @Setup(Level.Invocation)
    public void create() {
        nextPerson = PersonRow.builder()
                .id(++id)
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .age(random.nextInt(40) + 5)
                .isActive(random.nextBoolean())
                .build();
    }

    @Benchmark
    public void supernovaInsert() {
        supernova.insert(nextPerson);
    }

    public static void main(String[] args) throws RunnerException {
      var options = new OptionsBuilder()
                .include("SupernovaTest")
                .warmupIterations(1)
                .measurementIterations(1)
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
