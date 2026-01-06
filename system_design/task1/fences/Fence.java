package fences;

import animals.Animal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fence {
    private final String name;
    private final double area;
    private final List<Animal> animals = new ArrayList<>();

    public Fence(String name, double area) {
        this.name = name;
        this.area = area;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        System.out.printf("%s помещён в вольер \"%s\"%n", animal.getName(), name);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public String getName() {
        return name;
    }

    public double getArea() {
        return area;
    }

    public List<Animal> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public int getAnimalCount() {
        return animals.size();
    }
}
