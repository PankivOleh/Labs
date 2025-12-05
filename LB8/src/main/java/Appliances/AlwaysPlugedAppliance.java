package Appliances;

public class AlwaysPlugedAppliance extends ElectricalAppliance implements java.io.Serializable{
    public AlwaysPlugedAppliance(){
        super();
    }
    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        return "Постійно підключений прилад{" +
                "ID=" + ID +
                ", Ім'я='" + name + '\'' +
                ", Бренд='" + brand + '\'' +
                ", Модель='" + model + '\'' +
                ", Потужність=" + power +
                ", Ввімкнено: =" + ison +
                '}';
        }
    }
