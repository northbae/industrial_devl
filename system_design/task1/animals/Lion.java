package animals;

import behaviors.Speakable;
import enums.FoodType;
import enums.Species;
import java.util.Set;

public class Lion extends Animal implements Speakable {
    private final boolean isMale;

    public Lion(String name, int age, boolean isMale) {
        super(name, age);
        this.isMale = isMale;
    }

    @Override
    public Species getSpecies() {
        return Species.LION;
    }

    @Override
    public Set<FoodType> getDiet() {
        return Set.of(FoodType.MEAT, FoodType.FISH);
    }

    @Override
    public int getFeedingIntervalHours() {
        return 12;
    }

    @Override
    public void makeSound() {
        System.out.printf("%s: РРРРРР!%n", getName());
    }

    public boolean hasMane() {
        return isMale;
    }
}
