import java.util.ArrayList;
import java.util.List;

public class Main {
    static final String LINE = "=".repeat(50);
    static final String DASH = "-".repeat(30);

    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println(LINE);
        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println(DASH);
        List<Satellite> satellites = new ArrayList<>();
        satellites.add(new CommunicationSatellite("Связь-1", 0.85, 500));
        satellites.add(new CommunicationSatellite("Связь-2", 0.75, 1000));
        satellites.add(new ImagingSatellite("ДЗЗ-1", 0.92, 2.5));
        satellites.add(new ImagingSatellite("ДЗЗ-2", 0.45, 1));
        satellites.add(new ImagingSatellite("ДЗЗ-3", 0.15, 0.5));
        for (Satellite s : satellites) {
            printCreateSatellite(s);
        }
        System.out.println(DASH);
        SatelliteConstellation constellationRuBasic = new SatelliteConstellation("RU Basic");
        System.out.printf("Создана спутниковая группировка: %s%n", constellationRuBasic.getConstellationName());
        System.out.println(DASH);
        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ:");
        System.out.println(DASH);
        for (Satellite s : satellites) {
            addSatellite(s, constellationRuBasic);
        }
        System.out.println(DASH);
        printSatelliteList(constellationRuBasic);
        System.out.println(DASH);
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ");
        System.out.println(DASH);
        for (Satellite s : constellationRuBasic.getSatellites()) {
            activateSatellite(s);
        }
        System.out.printf("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ %s%n", constellationRuBasic.getConstellationName());
        System.out.println(LINE);
        constellationRuBasic.executeAllMissions();
        printSatelliteList(constellationRuBasic);
    }

    public static void printCreateSatellite(Satellite satellite) {
        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", satellite.getName(), satellite.getBatteryLevel() * 100);
    }

    public static void addSatellite(Satellite satellite, SatelliteConstellation constellation) {
        constellation.addSatellite(satellite);
        System.out.printf("%s добавлен в группировку '%s'%n", satellite.getName(), constellation.getConstellationName());
    }

    public static void activateSatellite(Satellite satellite) {
        if (satellite.activate()) {
            System.out.printf("✅ %s: Активация успешна%n", satellite.getName());
        }
        else {
            System.out.printf("\uD83D\uDED1 %s: Ошибка активации (заряд %.0f%%)%n", satellite.getName(), satellite.getBatteryLevel() * 100);
        }
    }

    public static void printSatelliteList(SatelliteConstellation constellation) {
        System.out.println("[" + String.join(",\n", constellation.getSatellites().stream().map(Object::toString).toList()) + "]");
    }
}
