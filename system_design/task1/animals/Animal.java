package animals;

import behaviors.Feedable;
import behaviors.Healable;
import enums.AnimalCategory;
import enums.FoodType;
import enums.Species;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Animal implements Feedable, Healable {
    private final String name;
    private final int age;
    private boolean isHealthy;
    private boolean isHungry;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
        this.isHealthy = true;
        this.isHungry = true;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isHungry() {
        return isHungry;
    }

    @Override
    public boolean isHealthy() {
        return isHealthy;
    }

    @Override
    public void setHealthy(boolean healthy) {
        this.isHealthy = healthy;
    }

    @Override
    public void heal() {
        this.isHealthy = true;
        System.out.printf("%s вылечен%n", name);
    }

    @Override
    public void feed() {
        this.isHungry = false;
        String food = getDiet().stream()
                .map(FoodType::getDisplayName)
                .collect(Collectors.joining(", "));
        System.out.printf("%s накормлен (%s)%n", name, food);
    }

    public abstract Species getSpecies();

    public String getSpeciesName() {
        return getSpecies().getDisplayName();
    }

    public AnimalCategory getCategory() {
        return getSpecies().getCategory();
    }
}
