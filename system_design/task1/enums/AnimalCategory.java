package enums;

public enum AnimalCategory {
    MAMMAL("Млекопитающее"),
    BIRD("Птица"),
    REPTILE("Рептилия"),
    AMPHIBIAN("Земноводное"),
    FISH("Рыба");

    private final String displayName;

    AnimalCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
