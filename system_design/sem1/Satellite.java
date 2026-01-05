public abstract class Satellite {
    protected String name;
    protected boolean isActive;
    protected double batteryLevel;

    Satellite(String name, double batteryLevel) {
        this.name = name;
        this.isActive = false;
        this.batteryLevel = Math.max(0.0, Math.min(1.0, batteryLevel));
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public boolean activate() {
        if (batteryLevel <= 0.2 || isActive) {
            return false;
        }
        isActive = true;
        return true;
    }

    public void deactivate() {
        if (isActive) {
            isActive = false;
        }
    }

    public void consumeButtery(double batteryValue) {
        if (batteryValue > 0) {
            batteryLevel = Math.max(0.0, batteryLevel - batteryValue);
            if (batteryLevel <= 0.2 && isActive) {
                deactivate();
            }
        }
    }

    protected abstract void performMission();
}
