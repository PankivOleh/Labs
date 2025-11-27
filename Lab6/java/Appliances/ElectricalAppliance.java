package Appliances;
import java.util.Scanner;
import System.SysFunc;

public abstract class ElectricalAppliance  implements java.io.Serializable {
    int ID;
    String name;
    String brand;
    String model;
    double power;
    boolean ison;
    private static final long serialVersionUID = 1L;

    ElectricalAppliance(){
        System.out.println("Створіть прилад:");
        System.out.println("Введіть назву приладу");
        Scanner sc = new Scanner(System.in);
        this.name = sc.nextLine();
        System.out.println("Введіть бренд приладу");
        this.brand = sc.nextLine();
        System.out.println("Введіть модель:");
        this.model = sc.nextLine();
        System.out.println("Введіть потужність приладу (в ватах)");
        this.power = sc.nextDouble();
        SysFunc.Println("Введіть ID приладу(ID повинно бути унікальним, додатним і в межах від 1 до 1000000)");
        ID = SysFunc.get(1,1000000);
    }
    public void turnOn(){
        ison = true;
    }
    public void turnOff(){
        ison = false;
    }
    public boolean isOn(){
        return ison;
    }
    public String getName(){
        return name;
    }
    public String getBrand(){
        return brand;
    }
    public String getModel(){
        return model;
    }
    public double getPower(){
        return power;
    }
    public int getID(){
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Електроприлад{" +
                "ID=" + ID +
                ", Ім'я='" + name + '\'' +
                ", Бренд='" + brand + '\'' +
                ", Модель='" + model + '\'' +
                ", Потужність=" + power +
                ", Ввімкнено: =" + ison +
                '}';
    }
}

