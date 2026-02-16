package ru.satellite.model;

import java.util.ArrayList;
import java.util.List;

public class SatelliteConstellation {
    private final String constellationName;
    private final List<Satellite> satellites = new ArrayList<>();

    public SatelliteConstellation(String constellationName) {
        this.constellationName = constellationName;
    }

    public String getConstellationName() {
        return constellationName;
    }

    public void addSatellite(Satellite satellite) {
        if (satellite != null && !satellites.contains(satellite)) {
            satellites.add(satellite);
        }
    }

    public void executeAllMissions() {
        for (Satellite s : satellites) {
            s.performMission();
        }
    }

    public List<Satellite> getSatellites() {
        return satellites;
    }
}
