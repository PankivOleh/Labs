package Appliances;

public class Charger extends PluggedAppliance{
    boolean isCharging;
    BatteryAppliance appliance;
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
        return "Charger{" +
                ", ID=" + ID +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", power=" + power +
                ", isplugged=" + isplugged +
                ", ison=" + ison +
                ", isCharging=" + isCharging +
                ", appliance=" + appliance.getID() +
                '}';
    }
}
