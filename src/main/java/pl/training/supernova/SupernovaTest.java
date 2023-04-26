package pl.training.supernova;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
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
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class SupernovaTest {

    private final Path filePath = Paths.get("persons.data");
    private final Random random = new Random();
    private long id;
    private long readId;
    private Supernova<PersonRow, Long> supernova;
    private PersonRow nextPerson;

    @State(Scope.Thread)
    @AuxCounters(AuxCounters.Type.EVENTS)
    public static class OperationCounters {

        public long read;
        public long insert;

        public long total() {
            return read + insert;
        }

    }

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
        readId = random.nextLong(id + 1);
        nextPerson = PersonRow.builder()
                .id(id)
                .firstName(UUID.randomUUID().toString())
                .lastName(UUID.randomUUID().toString())
                .age(random.nextInt(40) + 5)
                .isActive(random.nextBoolean())
                .build();
    }

    @Group("a")
    @GroupThreads(1)
    @Benchmark
    public void supernovaInsert(OperationCounters counters, Blackhole blackhole) {
        ++id;
        supernova.insert(nextPerson);
        counters.insert++;
    }

    @Group("a")
    @GroupThreads(1)
    @Benchmark
    public void supernovaRead(OperationCounters counters, Blackhole blackhole) {
        blackhole.consume(supernova.getById(readId));
        counters.read++;
    }

    public static void main(String[] args) throws RunnerException {
      var options = new OptionsBuilder()
                .include("SupernovaTest")
                .warmupIterations(0)
                .measurementIterations(1_000_000)
                .threads(2)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}

