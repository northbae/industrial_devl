package services;

import animals.Animal;
import java.util.List;

public class FeedingService implements AnimalCare {

    @Override
    public void performService(List<Animal> animals) {
        System.out.println("Кормление:");
        for (Animal animal : animals) {
            if (animal.isHungry()) {
                animal.feed();
            }
        }
    }

    @Override
    public String getServiceName() {
        return "Служба кормления";
    }
}
