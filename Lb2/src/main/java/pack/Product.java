package pack;

public class Product {
    int id;
    String name;
    String vurobnuk;
    int zina , termin , kilkist;
    public Product( int id , String name , String vurobnuk , int zina , int termin , int kilkist){
        this.id = id;
        this.name = name;
        this.vurobnuk = vurobnuk;
        this.zina = zina;
        this.termin = termin;
        this.kilkist = kilkist;
    }
    public void setValue(int zina){
        this.zina = zina;
    }
    public int getValue(){
        return this.zina;
    }
    public int getTermin(){return this.termin;}
    public String getName(){return this.name;}
    public String toString(){
        return "\nПродукт{" +"\n"+
                "id=" + id +"\n"+
                "Назва='" + name + '\n' +
                "Виробник='" + vurobnuk + '\n' +
                "Ціна=" + zina +"\n"+
                "Термін придатності=" + termin +"м \n"+
                "кількість=" + kilkist +"\n"+
                '}';
    };
}
