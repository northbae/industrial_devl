package ru.sem3.repository;

import ru.sem3.model.SatelliteConstellation;

import java.util.HashMap;
import java.util.Map;

public class ConstellationRepository {
    private final Map<String, SatelliteConstellation> constellations = new HashMap<>();

    public void addConstellation(SatelliteConstellation constellation) {
        constellations.put(constellation.getConstellationName(), constellation);
    }

    public SatelliteConstellation getConstellation(String name) {
        SatelliteConstellation constellation = constellations.get(name);
        if (constellation == null) {
            throw new RuntimeException("Группировка не найдена" + name);
        }
        return constellation;
    }

    public Map<String, SatelliteConstellation> getAllConstellations() {
        return new HashMap<>(constellations);
    }

    public boolean containsConstellation(String name) {
        return constellations.containsKey(name);
    }

    public void removeConstellation(String name) {
        constellations.remove(name);
    }
}
