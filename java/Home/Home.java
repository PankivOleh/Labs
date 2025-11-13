package Home;
import Appliances.*;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Home {
    ArrayList<Room> rooms;
    ArrayList<ElectricalAppliance> allAppliances;
    ArrayList<ElectricalAppliance> allAppliancesOn;
    int allSocket;
    Logger logger;
    public void ShowHome(){}
    public void ConfigAppInRoom(){}
    public void ConfigRoom(){}
    public void ConfigHome(){}
    public void CalculateGeneralPower(){}
    public void ShowAllAppliancesInRoom(){}
    public void SetAppliances(){}
    public void SortAppliancesByPower(){}
    public void FindAppliancesByParams(){}
    public double CalculateConsumptionPerHour(){
        return 0;
    }
    public void ShowLogs(){}
    public void DownloadToLogFromFile(){}
    public void UploadFromLogToFile(){}

}
