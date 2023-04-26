package pl.training.supernova;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@State(Scope.Benchmark)
public class SupernovaTest {

    private final Supernova<PersonRow, Long> supernova = new Supernova<>(Paths.get("persons3.data"), new PersonRow(), new HashMap<>());
    private final Random random = new Random();
    private long id;
    private PersonRow nextPerson;

    @Setup(Level.Iteration)
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
                .include("supernova")
                .warmupIterations(1)
                .measurementIterations(1)
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
