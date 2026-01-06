package services;

import animals.Animal;
import fences.Fence;

import java.util.List;

public interface ReportGenerator {
    void generateReport(List<Animal> animals, List<Fence> fences);
}
