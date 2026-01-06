package enums;

public enum FoodType {
    MEAT("мясо"),
    FISH("рыба"),
    GRAIN("зерно"),
    FRUITS("фрукты"),
    VEGETABLES("овощи"),
    RODENTS("грызуны"),
    INSECTS("насекомые");

    private final String displayName;

    FoodType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
