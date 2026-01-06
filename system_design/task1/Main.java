import animals.Lion;
import animals.Parrot;
import animals.Snake;
import fences.Fence;
import services.FeedingService;
import services.MedicalService;
import services.ReportService;
import staff.Veterinarian;
import staff.Zookeeper;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Zoo zoo = new Zoo("Московский зоопарк");

        Fence lionFence = new Fence("Львятник", 500.0);
        Fence birdFence = new Fence("Птичник", 200.0);
        Fence reptileFence = new Fence("Террариум", 100.0);

        zoo.addFence(lionFence);
        zoo.addFence(birdFence);
        zoo.addFence(reptileFence);

        Lion simba = new Lion("Симба", 5, true);
        Lion nala = new Lion("Нала", 4, false);
        Parrot kesha = new Parrot("Кеша", 2, "зелёный");
        Snake kaa = new Snake("Каа", 10, 3.5, false);

        zoo.addAnimal(simba);
        zoo.addAnimal(nala);
        zoo.addAnimal(kesha);
        zoo.addAnimal(kaa);

        lionFence.addAnimal(simba);
        lionFence.addAnimal(nala);
        birdFence.addAnimal(kesha);
        reptileFence.addAnimal(kaa);

        Veterinarian veterinarian = new Veterinarian("Доктор Жаров");
        Zookeeper zookeeper = new Zookeeper("Иван Петров", List.of(lionFence, reptileFence));

        zoo.addEmployee(veterinarian);
        zoo.addEmployee(zookeeper);

        zoo.registerService(new FeedingService());
        zoo.registerService(new MedicalService(veterinarian));

        kaa.setHealthy(false);

        zoo.runAllServices();

        ReportService reportService = new ReportService();
        reportService.generateReport(zoo.getAnimals(), zoo.getFences());
        reportService.printFeedingSchedule(zoo.getAnimals());
    }
}
