package pl.training.supernova.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.SampleTime, Mode.Throughput, Mode.AverageTime})
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
    public void testFactorialWithLoop(/*TestInfo testInfo*/) {
        //System.out.println("Iteration: " + iterationIndex++ /*testInfo.iterationIndex++*/);
        new Factorial().factorialWithStreams(10);
    }

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                .include(Factorial.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(1)
                .threads(5)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
