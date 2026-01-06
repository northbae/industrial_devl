package staff;

import fences.Fence;

import java.util.List;

public class Zookeeper implements Employee {
    private final String name;
    private final List<Fence> assignedFences;

    public Zookeeper(String name, List<Fence> fences) {
        this.name = name;
        this.assignedFences = fences;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPosition() {
        return "Смотритель";
    }

    @Override
    public void performDuty() {
        System.out.printf("%s проверяет вольеры%n", name);
        for (Fence e : assignedFences) {
            System.out.printf("Вольер \"%s\": %d животных%n", e.getName(), e.getAnimalCount());
        }
    }
}
