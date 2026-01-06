import animals.Animal;
import fences.Fence;
import services.AnimalCare;
import staff.Employee;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Zoo {
    private final String name;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Fence> fences = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<AnimalCare> services = new ArrayList<>();

    public Zoo(String name) {
        this.name = name;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        System.out.printf("В зоопарк прибыл: %s (%s)%n", animal.getName(), animal.getSpecies().getDisplayName());
    }

    public void addFence(Fence fence) {
        fences.add(fence);
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        System.out.printf("Принят сотрудник: %s (%s)%n", employee.getName(), employee.getPosition());
    }

    public void registerService(AnimalCare service) {
        services.add(service);
        System.out.printf("Зарегистрирован сервис: %s%n", service.getServiceName());
    }

    public void runAllServices() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ВЫПОЛНЕНИЕ СЕРВИСОВ УХОДА:");
        System.out.println("=".repeat(50));
        for (AnimalCare service : services) {
            service.performService(animals);
            System.out.println();
        }
    }

    public String getName() {
        return name;
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public List<Fence> getFences() {
        return Collections.unmodifiableList(fences);
    }

    public List<Employee> getEmployees() {
        return Collections.unmodifiableList(employees);
    }
}
