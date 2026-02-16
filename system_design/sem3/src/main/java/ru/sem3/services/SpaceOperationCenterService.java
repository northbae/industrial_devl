package ru.sem3.services;

import ru.sem3.model.Satellite;
import ru.sem3.model.SatelliteConstellation;
import ru.sem3.repository.ConstellationRepository;

public class SpaceOperationCenterService {
    private final ConstellationRepository repository;

    public SpaceOperationCenterService(ConstellationRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveConstellation(String name) {
        SatelliteConstellation constellation = new SatelliteConstellation(name);
        repository.addConstellation(constellation);
        System.out.printf("Создана спутниковая группировка: %s%n", constellation.getConstellationName());
    }

    public void addSatelliteToConstellation(String constellationName, Satellite satellite) {
        SatelliteConstellation constellation = repository.getConstellation(constellationName);
        constellation.addSatellite(satellite);
        System.out.printf("%s добавлен в группировку %s%n", satellite.getName(), constellationName);
    }

    public void executeConstellationMission(String constellationName) {
        SatelliteConstellation constellation = repository.getConstellation(constellationName);
        constellation.executeAllMissions();
    }

    public void activateAllSatellites(String constellationName) {
        SatelliteConstellation constellation = repository.getConstellation(constellationName);
        for (Satellite satellite : constellation.getSatellites()) {
            if (satellite.activate()) {
                System.out.printf("✅ %s: Активация успешна%n", satellite.getName());
            }
            else {
                System.out.printf("\uD83D\uDED1 %s: Ошибка активации (заряд %.0f%%)%n", satellite.getName(), satellite.getEnergy().getBatteryLevel() * 100);
            }
        }
    }

    public void showConstellationStatus(String constellationName) {
        SatelliteConstellation constellation = repository.getConstellation(constellationName);
        System.out.println("\n=== СТАТУС ГРУППИРОВКИ " + constellationName + " ===");
        System.out.println("Количество спутников: " + constellation.getSatellites().size());
        for (Satellite satellite : constellation.getSatellites()) {
            System.out.println(satellite.getState());
        }
    }

    public void printSatelliteList(String constellationName){
        SatelliteConstellation constellation = repository.getConstellation(constellationName);
        System.out.println("[" + String.join(",\n", constellation.getSatellites().stream().map(Object::toString).toList()) + "]");
    }
}
