package pl.training.supernova.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
//@BenchmarkMode({Mode.SampleTime, Mode.Throughput, Mode.AverageTime})
@BenchmarkMode(Mode.Throughput)
//@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JmhExample {

    private long iterationIndex;
    private final Factorial factorial = new Factorial();

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

    int n = 20;

    @Fork(1)
    //@BenchmarkMode({Mode.SampleTime, Mode.Throughput, Mode.AverageTime})
    //@OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Benchmark
    public void testFactorialWithLoop(Blackhole blackhole /*TestInfo testInfo*/) {
        blackhole.consume(factorial.factorialWithLoop(n));
    }

    @Fork(1)
    @Benchmark
    public void testFactorialWithLoopWithoutBlackHole(/*TestInfo testInfo*/) {
        factorial.factorialWithLoop(n);
    }

   /* @Benchmark
    public long testFactorialWithStream(*//*TestInfo testInfo*//*) {
        return new Factorial().factorialWithStreams(15);
    }*/

    // -Djmh.blackhole.autoDetect=false - disables the need to automatically apply blackhole wrappers
    // -rff results.csv - save results to file
    public static void main(String[] args) throws RunnerException {
        var options = new OptionsBuilder()
                //.include(Factorial.class.getSimpleName())
                .warmupIterations(1)
                .measurementIterations(1)
                .threads(1)
                //.forks(1)
                .build();
        new Runner(options).run();
    }

}
