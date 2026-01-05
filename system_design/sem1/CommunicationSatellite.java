public class CommunicationSatellite extends Satellite{
    private final double bandwidth;

    CommunicationSatellite(String name, double batteryLevel, double bandwidth) {
        super(name, batteryLevel);
        this.bandwidth = bandwidth;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    @Override
    public void performMission() {
        if (isActive) {
            System.out.printf("%s: Передача данных со скоростью %.2f Мбит/с%n", name, bandwidth);
            sendData(bandwidth);
            consumeButtery(0.05);
        }
        else {
            System.out.printf("%s: Не может отправить данные - не активен%n", name);
        }
    }

    private void sendData(double value) {
        if (isActive) {
            System.out.printf("%s: Отправил %.2f Мбит данных!%n", name, value);
        }
    }

    @Override
    public String toString() {
        return String.format("CommunicationSatellite(bandwidth=%.2f, name='%s', isActive=%b, " +
                "batteryLevel=%.2f)", bandwidth, name, isActive, batteryLevel);
    }
}
