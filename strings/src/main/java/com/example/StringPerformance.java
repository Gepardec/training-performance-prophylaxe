package com.example;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Measurement(batchSize = 100000, iterations = 10)
@Warmup(batchSize = 100000, iterations = 10)
public class StringPerformance {

    @Benchmark
    public String benchmarkStringConstructor() {
        return new String("Hello World");
    }

    @Benchmark
    public String benchmarkStringLiteral() {
        return "Hello World";
    }

}
