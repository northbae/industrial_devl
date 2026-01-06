package behaviors;

import enums.FoodType;
import java.util.Set;

public interface Feedable {
    void feed();
    Set<FoodType> getDiet();
    int getFeedingIntervalHours();
}
