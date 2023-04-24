package pl.training.supernova.examples;

import java.util.stream.LongStream;

public class Factorial {

    public long factorialWithLoop(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result = result * i;
        }
        return result;
    }

    public long factorialWithRecursion(int n) {
        if (n <= 2) {
            return n;
        }
        return n * factorialWithRecursion(n - 1);
    }

    public long factorialWithStreams(int n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (long x, long y) -> x * y);
    }

}
