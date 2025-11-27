package System;
import Home.Home;

import java.io.*;

public class FileManager implements java.io.Serializable{
    Home home;
    private static final long serialVersionUID = 1L;

    public FileManager(Home home){
        this.home = home;
    }
    public static void downloadToFile(Home home) throws IOException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Oleg\\IdeaProject\\java\\LB4\\Home.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(home);
        oos.close();

        System.out.println("Дані збережено в файл");


    }
    public static Home uploadFromFile() throws IOException {
        FileInputStream fis = new FileInputStream("C:\\Users\\Oleg\\IdeaProject\\java\\LB4\\Home.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);

        Home newHome = null;
        try {
            newHome = (Home) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Дані завантажено з файлу");
        return newHome;
    }
}
