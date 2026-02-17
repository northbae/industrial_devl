package ru.tigrbank.service.impl;

import org.springframework.stereotype.Service;
import ru.tigrbank.service.TimingService;

import java.util.function.Supplier;

@Service
public class TimingServiceImpl implements TimingService {

    @Override
    public <T> T measureAndReturn(String scenarioName, Supplier<T> action) {
        long start = System.nanoTime();
        T result = action.get();
        long elapsed = System.nanoTime() - start;
        System.out.printf("[%s] - %.3f мс%n", scenarioName, elapsed / 1_000_000.0);
        return result;
    }

    @Override
    public void measure(String scenarioName, Runnable action) {
        long start = System.nanoTime();
        action.run();
        long elapsed = System.nanoTime() - start;
        System.out.printf("[%s] - %.3f мс%n", scenarioName, elapsed / 1_000_000.0);
    }
}
