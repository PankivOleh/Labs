package Appliances;

public class PluggedAppliance extends ElectricalAppliance implements java.io.Serializable{
    boolean isplugged;
    private static final long serialVersionUID = 1L;

    public PluggedAppliance(){
        super();
        isplugged = false;
    }

    public void PlugIn(){
        isplugged = true;
    }
    public void PlugOut(){
        isplugged = false;
        super.turnOff();
    }

    @Override
    public void turnOff() {
      super.turnOff();
    }
    @Override
    public void turnOn(){
        if(isplugged) {
            super.turnOn();
        }else{
            System.out.println("Пристрій не ввімкнено в розетку");
        }
    }
    public boolean isPlugged(){
        return isplugged;
    }

    @Override
    public String toString() {
        return "Прилад на розетку{" +
                ", ID=" + ID +
                ", Ім'я='" + name + '\'' +
                ", Бренд='" + brand + '\'' +
                ", Модель='" + model + '\'' +
                ", Потужність=" + power +
                ", В розетці=" + isplugged +
                ", Ввімкнено=" + ison +
                '}';
    }
}
