package Appliances;


public class BatteryAppliance extends ElectricalAppliance implements java.io.Serializable {
    boolean isCharging;
    Charger charger;
    private static final long serialVersionUID = 1L;


    public BatteryAppliance(){
    super();
    isCharging = false;
    charger = null;
 }

 public void Charging(){
     if(charger == null){
         System.out.println("Цей пристрій не має визначеної зарядки");
         return;
     }
     isCharging = true;
     this.charger.Charging();
 }
 public void Discharging(){
     isCharging = false;
     if(charger != null) {
         this.charger.StopCharging();
     }
 }
 public void setCharging(boolean charging){
     isCharging = charging;
 }
    public void setCharger(Charger charger) {
        if (this.charger != null) {
            this.charger.setAppliance(null);
        }

        this.charger = charger;

        if (charger != null) {
            charger.setAppliance(this);
        }
    }


    @Override
    public String toString() {
        return "Акумуляторний прилад{" +
                "ID=" + ID +
                ", Ім'я='" + name + '\'' +
                ", Бренд='" + brand + '\'' +
                ", Модель='" + model + '\'' +
                ", Потужність=" + power +
                ", Ввімкнено=" + ison +
                ", Заряджається=" + isCharging +
                ", Зарядка=" + charger.getID() +
                '}';
    }
}
