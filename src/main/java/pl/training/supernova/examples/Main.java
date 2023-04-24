package pl.training.supernova.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
//@BenchmarkMode({Mode.SampleTime, Mode.Throughput, Mode.AverageTime})
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Main {

    long iterationIndex;

    /*@State(Scope.Benchmark)
    public static class TestInfo {
        long iterationIndex = 0;
    }*/

    @Setup(Level.Iteration)
    public void prepareState() {
        iterationIndex = 1;
        System.out.println("State is ready");
    }

    @TearDown(Level.Trial)
    public void cleanupState() {
        System.out.println("Cleaning state");
        iterationIndex = 1;
    }

    //@BenchmarkMode({Mode.SampleTime, Mode.Throughput, Mode.AverageTime})
    //@OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Benchmark
    public void testFactorialWithLoop(Blackhole blackhole /*TestInfo testInfo*/) {
        blackhole.consume(new Factorial().factorialWithLoop(10));
    }

    @Benchmark
    public long testFactorialWithStream(/*TestInfo testInfo*/) {
        return new Factorial().factorialWithStreams(10);
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(Factorial.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
