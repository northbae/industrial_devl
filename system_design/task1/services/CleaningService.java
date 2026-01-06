package services;

import animals.Animal;

import java.util.List;

public class CleaningService implements AnimalCare {

    @Override
    public void performService(List<Animal> animals) {
        System.out.println("Уборка вольеров выполнена");
    }

    @Override
    public String getServiceName() {
        return "Служба уборки";
    }
}
