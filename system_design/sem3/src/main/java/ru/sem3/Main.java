package ru.sem3;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.sem3.model.CommunicationSatellite;
import ru.sem3.model.ImagingSatellite;
import ru.sem3.model.Satellite;
import ru.sem3.model.SatelliteConstellation;
import ru.sem3.repository.ConstellationRepository;
import ru.sem3.services.SpaceOperationCenterService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {
    static final String LINE = "=".repeat(50);
    static final String DASH = "-".repeat(30);

    public static void main(String[] args) {
        System.out.println("ЗАПУСК СИСТЕМЫ УПРАВЛЕНИЯ СПУТНИКОВОЙ ГРУППИРОВКОЙ");
        System.out.println(LINE);

        ConstellationRepository constellationRepository = new ConstellationRepository();
        SpaceOperationCenterService operationCenter = new SpaceOperationCenterService(constellationRepository);

        System.out.println("СОЗДАНИЕ СПЕЦИАЛИЗИРОВАННЫХ СПУТНИКОВ:");
        System.out.println(DASH);

        Satellite connect1 = new CommunicationSatellite("Связь-1", 0.85, 500);
        printCreateSatellite(connect1);
        Satellite connect2 = new CommunicationSatellite("Связь-2", 0.75, 1000);
        printCreateSatellite(connect2);
        Satellite remote1 = new ImagingSatellite("ДЗЗ-1", 0.92, 2.5);
        printCreateSatellite(remote1);
        Satellite remote2 = new ImagingSatellite("ДЗЗ-2", 0.45, 1);
        printCreateSatellite(remote2);
        Satellite remote3 = new ImagingSatellite("ДЗЗ-3", 0.15, 0.5);
        printCreateSatellite(remote3);
        System.out.println(DASH);

        operationCenter.createAndSaveConstellation("Орбита-1");
        operationCenter.createAndSaveConstellation("Орбита-2");

        System.out.println(DASH);
        System.out.println("ФОРМИРОВАНИЕ ГРУППИРОВКИ:");
        System.out.println(DASH);
        operationCenter.addSatelliteToConstellation("Орбита-1", connect1);
        operationCenter.addSatelliteToConstellation("Орбита-1", connect2);
        operationCenter.addSatelliteToConstellation("Орбита-2", remote1);
        operationCenter.addSatelliteToConstellation("Орбита-2", remote2);
        operationCenter.addSatelliteToConstellation("Орбита-2", remote3);

        System.out.println(DASH);
        operationCenter.printSatelliteList("Орбита-1");
        operationCenter.printSatelliteList("Орбита-2");
        System.out.println(DASH);
        System.out.println("АКТИВАЦИЯ СПУТНИКОВ - Орбита-1");
        System.out.println(DASH);
        operationCenter.activateAllSatellites("Орбита-1");
        System.out.println("ВЫПОЛНЕНИЕ МИССИЙ ГРУППИРОВКИ - Орбита-1");
        System.out.println(LINE);
        operationCenter.executeConstellationMission("Орбита-1");
        System.out.println(DASH);
        System.out.println("СТАТУС ГРУППИРОВКИ - Орбита-1");

        operationCenter.showConstellationStatus("Орбита-1");
    }

    public static void printCreateSatellite(Satellite satellite) {
        System.out.printf("Создан спутник: %s (заряд: %.0f%%)%n", satellite.getName(), satellite.getEnergy().getBatteryLevel() * 100);
    }
}
