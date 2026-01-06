package services;

import animals.Animal;
import java.util.List;

public interface AnimalCare {
    void performService(List<Animal> animals);
    String getServiceName();
}
