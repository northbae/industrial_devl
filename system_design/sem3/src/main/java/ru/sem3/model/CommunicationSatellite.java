package ru.sem3.model;

public class CommunicationSatellite extends Satellite{
    private final double bandwidth;

    public CommunicationSatellite(String name, double batteryLevel, double bandwidth) {
        super(name, batteryLevel);
        this.bandwidth = bandwidth;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    @Override
    public void performMission() {
        if (state.isActive()) {
            System.out.printf("%s: Передача данных со скоростью %.2f Мбит/с%n", name, bandwidth);
            sendData(bandwidth);
            energy.consumeBattery(0.05);
        }
        else {
            System.out.printf("%s: Не может отправить данные - не активен%n", name);
        }
    }

    private void sendData(double value) {
        if (state.isActive()) {
            System.out.printf("%s: Отправил %.2f Мбит данных!%n", name, value);
        }
    }

    @Override
    public String toString() {
        return String.format("CommunicationSatellite(bandwidth=%.2f, name='%s', isActive=%b, " +
                "batteryLevel=%s)", bandwidth, name, state, energy);
    }
}
