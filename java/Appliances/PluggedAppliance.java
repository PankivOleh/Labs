package Appliances;

public class PluggedAppliance extends ElectricalAppliance{
    boolean isplugged;

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
        return "PluggedAppliance{" +
                ", ID=" + ID +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", power=" + power +
                ", isplugged=" + isplugged +
                ", ison=" + ison +
                '}';
    }
}
