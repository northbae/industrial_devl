package enums;

public enum Species {
    LION("Лев", AnimalCategory.MAMMAL),
    TIGER("Тигр", AnimalCategory.MAMMAL),
    ELEPHANT("Слон", AnimalCategory.MAMMAL),
    MONKEY("Обезьяна", AnimalCategory.MAMMAL),

    PARROT("Попугай", AnimalCategory.BIRD),
    EAGLE("Орёл", AnimalCategory.BIRD),
    PENGUIN("Пингвин", AnimalCategory.BIRD),

    SNAKE("Змея", AnimalCategory.REPTILE),
    CROCODILE("Крокодил", AnimalCategory.REPTILE),
    TURTLE("Черепаха", AnimalCategory.REPTILE);

    private final String displayName;
    private final AnimalCategory category;

    Species(String displayName, AnimalCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AnimalCategory getCategory() {
        return category;
    }
}
