package staff;

import animals.Animal;
import behaviors.Healable;

public class Veterinarian implements Employee, MedicalExaminer {
    private final String name;

    public Veterinarian(String name) {
        this.name = name;
    }

    // === Employee ===
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPosition() {
        return "Ветеринар";
    }

    @Override
    public void performDuty() {
        System.out.printf("%s выполняет ежедневные обязанности%n", name);
    }

    // === MedicalExaminer ===
    @Override
    public String getExaminerName() {
        return name;
    }

    @Override
    public void prepareForExamination() {
        System.out.printf("%s готов к осмотру животных%n", name);
    }

    @Override
    public void examine(Healable healable) {
        System.out.printf("Осмотр %s: %s%n",
                healable.getName(),
                healable.isHealthy() ? "здоров" : "нужно лечение");
    }

    @Override
    public void treat(Healable healable) {
        if (!healable.isHealthy()) {
            healable.heal();
        }
    }
}
