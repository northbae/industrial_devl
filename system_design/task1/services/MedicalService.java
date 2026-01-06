package services;

import animals.Animal;
import staff.MedicalExaminer;
import staff.Veterinarian;
import java.util.List;

public class MedicalService implements AnimalCare {
    private final MedicalExaminer medicalExaminer;

    public MedicalService(MedicalExaminer medicalExaminer) {
        this.medicalExaminer = medicalExaminer;
    }

    @Override
    public void performService(List<Animal> animals) {
        System.out.println("Медицинский осмотр:");
        medicalExaminer.prepareForExamination();
        for (Animal animal : animals) {
            medicalExaminer.examine(animal);
            if (!animal.isHealthy()) {
                medicalExaminer.treat(animal);
            }
        }
    }

    @Override
    public String getServiceName() {
        return "Ветеринарная служба";
    }
}
