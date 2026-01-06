package behaviors;

public interface Huntable {
    String getPreyType();
    int getHuntingSkillLevel(); // 1-10

    default void hunt() {
        System.out.println("Охотится на " + getPreyType());
    }
}
