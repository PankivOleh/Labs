package pack;

public class Product {
    String id;
    String name;
    String vurobnuk;
    int zina, termin, kilkist;
    boolean p;

    public Product(String id, String name, String vurobnuk, int zina, int termin, int kilkist) {
        this.id = id;
        this.name = name;
        this.vurobnuk = vurobnuk;
        this.zina = zina;
        this.termin = termin;
        this.kilkist = kilkist;
        this.p = true;
    }
    public Product() {
        this.id = "noun";
        this.name = "noun";
        this.vurobnuk = "noun";
        this.zina = 0;
        this.termin = 0;
        this.kilkist = 0;
        this.p = false;
    }
    public void setValue(int zina) {
        this.zina = zina;
    }
    public int getValue() {
        return this.zina;
    }
    public int getTermin() {
        return this.termin;
    }
    public String getName() {
        return this.name;
    }
    public String getId(){
        return this.id;
    }
    public String toString() {
        return "\nПродукт{" + "\n" +
                "id=" + id + "\n" +
                "Назва='" + name + '\n' +
                "Виробник='" + vurobnuk + '\n' +
                "Ціна=" + zina + "\n" +
                "Термін придатності=" + termin + "м \n" +
                "кількість=" + kilkist + "\n" +
                '}';
    }
    public boolean getStan() {
        return p;
    }
    public void setStan(boolean p) {
        this.p = p;
    }
}
