package Utils;
import Home.Home;

import java.io.*;

import static org.example.Main.logger;

public class FileManager implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    public static void downloadToFile(Home home , String filePath) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(home);
            oos.close();
            logger.info("Успішний бекап даних у файл: " + filePath);
        }catch (IOException e){
            logger.severe("Помилка запису у файл " + filePath + ": " + e.getMessage());
            throw e;
        }

        System.out.println("Дані збережено в файл");


    }
    public static Home uploadFromFile(String filePath) throws IOException {
        try{
        FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Home newHome = null;
        try {
            newHome = (Home) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
            System.out.println("Дані завантажено з файлу");
            logger.info("Дані успішно завантажені з файлу: " + filePath );
            return newHome;
        }catch (IOException e){
            logger.severe("Помилка зчитування з файлу " + filePath + ": " + e.getMessage());
            throw e;
        }

    }
}
