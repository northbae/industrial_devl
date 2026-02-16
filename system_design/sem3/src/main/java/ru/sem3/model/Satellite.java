package ru.sem3.model;

import ru.sem3.system.EnergySystem;
import ru.sem3.system.SatelliteState;

public abstract class Satellite {
    protected String name;
    protected SatelliteState state;
    protected EnergySystem energy;

    public Satellite(String name, double batteryLevel) {
        this.name = name;
        this.energy = new EnergySystem(batteryLevel);
        this.state = new SatelliteState();
    }

    public String getName() {
        return name;
    }

    public SatelliteState getState() {
        return state;
    }

    public EnergySystem getEnergy() {
        return energy;
    }

    public boolean activate() {
        return state.activate(energy.hasEfficientPower());
    }

    public void deactivate() {
        if (state.isActive()) {
            state.deactivate();
        }
    }

    protected abstract void performMission();
}
