package services;

import animals.Animal;
import behaviors.Speakable;
import enums.FoodType;
import fences.Fence;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReportService implements ReportGenerator{

    private final String DASH = "=".repeat(50);
    private final String LINE = "-".repeat(30);

    @Override
    public void generateReport(List<Animal> animals, List<Fence> fences) {
        System.out.println(DASH);
        System.out.println("ОТЧЁТ ПО ЗООПАРКУ");
        System.out.println(DASH);

        System.out.printf("Всего животных: %d%n", animals.size());
        System.out.printf("Всего вольеров: %d%n", fences.size());

        long healthy = animals.stream().filter(Animal::isHealthy).count();
        long sick = animals.size() - healthy;
        System.out.printf("Здоровых животных: %d | Больных животных: %d%n", healthy, sick);

        System.out.println(LINE);
        System.out.println("СПИСОК ЖИВОТНЫХ:");
        for (Animal a : animals) {
            System.out.printf("  - %s (%s, %d лет) - кормить каждые %d ч.%n",
                    a.getName(), a.getSpecies(), a.getAge(), a.getFeedingIntervalHours());
        }

        System.out.println(LINE);
        System.out.println("ВОЛЬЕРЫ:");
        for (Fence e : fences) {
            System.out.printf("  - \"%s\" (%.1f м²) - %d животных%n",
                    e.getName(), e.getArea(), e.getAnimalCount());
        }

        System.out.println(DASH);
    }

    public void printFeedingSchedule(List<Animal> animals) {
        System.out.println("РАСПИСАНИЕ КОРМЛЕНИЯ:");
        animals.stream()
                .sorted(Comparator.comparingInt(Animal::getFeedingIntervalHours))
                .forEach(a -> {
                    String diet = a.getDiet().stream()
                            .map(FoodType::getDisplayName)
                            .collect(Collectors.joining(", "));
                    System.out.printf("  %s (%s): каждые %d ч. - %s%n",
                            a.getName(),
                            a.getSpeciesName(),
                            a.getFeedingIntervalHours(),
                            diet);
                });
    }
}
