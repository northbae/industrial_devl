package ru.tigrbank.service;

import java.util.function.Supplier;

public interface TimingService {
    <T> T measureAndReturn(String scenarioName, Supplier<T> action);
    void measure(String scenarioName, Runnable action);
}
