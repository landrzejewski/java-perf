package pl.training.supernova.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
//@BenchmarkMode({Mode.SampleTime, Mode.Throughput, Mode.AverageTime})
@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Main {

    private long iterationIndex;
    private Factorial factorial = new Factorial();

    /*@State(Scope.Benchmark)
    public static class TestInfo {
        long iterationIndex = 0;
    }*/

    @Setup(Level.Trial)
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
        blackhole.consume(factorial.factorialWithLoop(20));
    }

    @Benchmark
    public void testFactorialWithLoopWithoutBlackHole(/*TestInfo testInfo*/) {
        factorial.factorialWithLoop(20);
    }

   /* @Benchmark
    public long testFactorialWithStream(*//*TestInfo testInfo*//*) {
        return new Factorial().factorialWithStreams(15);
    }*/

    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                //.include(Factorial.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .threads(1)
                .forks(1)
                .build();
        new Runner(options).run();
    }

}
