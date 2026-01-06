package animals;

import enums.FoodType;
import enums.Species;
import java.util.Set;

public class Snake extends Animal {
    private final double length;
    private final boolean isVenomous;

    public Snake(String name, int age, double length, boolean isVenomous) {
        super(name, age);
        this.length = length;
        this.isVenomous = isVenomous;
    }

    @Override
    public Species getSpecies() {
        return Species.SNAKE;
    }

    @Override
    public Set<FoodType> getDiet() {
        return Set.of(FoodType.RODENTS);
    }

    @Override
    public int getFeedingIntervalHours() {
        return 72;
    }

    public double getLength() {
        return length;
    }

    public boolean isVenomous() {
        return isVenomous;
    }
}
