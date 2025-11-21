package Home;

import Appliances.*;
import System.SysFunc;
import java.util.ArrayList;

public class Room {
    String name;
    int countOfSocket;
    ArrayList<ElectricalAppliance> appliances;
    ArrayList<ElectricalAppliance> appliancesOn;
    ArrayList<PluggedAppliance> pluggedAppliances;

    public Room(String name, int countOfSocket){

        this.name = name;
        this.countOfSocket = countOfSocket;
        appliances = new ArrayList<>();
        appliancesOn = new ArrayList<>();
        pluggedAppliances = new ArrayList<>();
        if(countOfSocket < 0){
            SysFunc.Println("кількість розеток повинна бути >= 0\nВстановлено автоматичне значення 0");
        this.countOfSocket = 0;
        }
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", countOfSocket=" + countOfSocket +
                 ", countOfAppliancesOn=" + appliances.size() +
                ", countOfPluggedAppliances="+pluggedAppliances.size()+
                '}';
    }
    public String getName(){
        return name;
    }
    public int getCountOfSocket(){
        return countOfSocket;
    }
    public ArrayList<ElectricalAppliance> getAppliances(){
        return appliances;
    }
    public ArrayList<ElectricalAppliance> getAppliancesOn(){
        return appliancesOn;
    }
    public void setNames(String name){
    this.name = name;}
    public void setCountOfSocket(int countOfSocket){
        if(countOfSocket < 0){
            SysFunc.Println("кількість розеток повинна бути >= 0 \nВстановлено автоматичне значення 0");
            this.countOfSocket = 0;
        }
        else{
            this.countOfSocket = countOfSocket;
        }
    }
    public int CheckSocket(){
        int dif =  pluggedAppliances.size() - countOfSocket;
        ElectricalAppliance app;
        if(pluggedAppliances.size() > countOfSocket){
            do{
                pluggedAppliances.get(pluggedAppliances.size()-1).PlugOut();
                app = pluggedAppliances.get(pluggedAppliances.size() - 1);
                pluggedAppliances.remove(pluggedAppliances.size() - 1);
                appliancesOn.remove(app);
            }while(pluggedAppliances.size() > countOfSocket);
        }
        return dif;
    }
    public ArrayList<PluggedAppliance> getPluggedAppliances(){return pluggedAppliances;}
    public void showAllAppliances(){
        for(ElectricalAppliance appliance : appliances){
            System.out.println(appliance.toString());
        }
    }
    public void showAllPluggedAppliances(){
        for(PluggedAppliance appliance : pluggedAppliances){
            System.out.println(appliance.toString());
        }
    }
    public void showAllAppliancesOn(){
        for(ElectricalAppliance appliance : appliancesOn){
            System.out.println(appliance.toString());
        }
    }
}
