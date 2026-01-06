package animals;

import behaviors.Speakable;
import enums.FoodType;
import enums.Species;
import java.util.Set;

public class Parrot extends Animal implements Speakable {
    private final String color;

    public Parrot(String name, int age, String color) {
        super(name, age);
        this.color = color;
    }

    @Override
    public Species getSpecies() {
        return Species.PARROT;
    }

    @Override
    public Set<FoodType> getDiet() {
        return Set.of(FoodType.GRAIN, FoodType.FRUITS);
    }

    @Override
    public int getFeedingIntervalHours() {
        return 6;
    }

    @Override
    public void makeSound() {
        System.out.printf("%s: Привет, я попугай!%n", getName());
    }

    public String getColor() {
        return color;
    }
}
