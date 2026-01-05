public class ImagingSatellite extends Satellite{
    private final double resolution;
    private int photosTaken;

    ImagingSatellite(String name, double batteryLevel, double resolution) {
        super(name, batteryLevel);
        this.resolution = resolution;
        this.photosTaken = 0;
    }

    public double getResolution() {
        return resolution;
    }

    public int getPhotosTaken() {
        return photosTaken;
    }

    @Override
    public void performMission() {
        if (isActive) {
            System.out.printf("%s: Съемка территории с разрешением %.2f м/пиксель%n", name, resolution);
            takePhoto();
            consumeButtery(0.08);
        }
        else {
            System.out.printf("%s: Не может выполнить съемку - неактивен%n", name);
        }
    }

    private void takePhoto() {
        if (isActive) {
            photosTaken++;
            System.out.printf("%s: Снимок #%d сделан!%n", name, photosTaken);
        }
    }

    @Override
    public String toString() {
        return String.format("ImagingSatellite(resolution=%.2f, photosTaken=%d, name='%s', " +
                "isActive=%b, batteryLevel=%.2f)", resolution, photosTaken, name, isActive, batteryLevel);
    }
}
