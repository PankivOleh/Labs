package Appliances;

public class Charger extends PluggedAppliance implements java.io.Serializable{
    boolean isCharging;
    BatteryAppliance appliance;
    private static final long serialVersionUID = 1L;

    public void Charging(){
        if(isplugged && appliance != null) {
            isCharging = true;
            ison = true;
            this.appliance.setCharging(true);
        }else {
            System.out.println("Зарядку не ввімкнений в розетку або не під'єднано д опристрою");
        }
    }
    public void StopCharging(){
        isCharging = false;
        ison = false;
        this.appliance.setCharging(false);
    }
    @Override
    public void turnOff() {
        super.turnOff();
        this.StopCharging();

    }
    @Override
    public void turnOn(){
        super.turnOn();
        this.Charging();
    }
    public void setAppliance(BatteryAppliance appliance) {
        this.appliance = appliance;
    }

    @Override
    public String toString() {
        return "Зарядка{" +
                ", ID=" + ID +
                ", Ім'я='" + name + '\'' +
                ", Бренд='" + brand + '\'' +
                ", Модель='" + model + '\'' +
                ", Потужність=" + power +
                ", Ввімкнено в розетку=" + isplugged +
                ", Ввімкнено=" + ison +
                ", Заряджає=" + isCharging +
                ", Прилад=" + appliance.getID() +
                '}';
    }
}
