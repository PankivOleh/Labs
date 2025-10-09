package org.example;
import pack.Product;

import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ArrayList<Product> products = new ArrayList<>();
        int i = 0 ;
        while(i==0){
            menu();
            switch(choise(products)){
                case 1: createList(products);break;
                case 8: i = 1 ;
            }
        }
    }

    public static void createList(ArrayList<Product> products) {
        String id;
        String name;
        String vurobnuk;
        Scanner sc = new Scanner(System.in);
        int zina, termin, kilkist;
        while (true) {
            System.out.println("Введіть id товару( 0 щоб зупинити ввід): ");
            id = sc.nextLine();
            if (id.trim().equals("0")) {
                break;
            }
            System.out.println("Введіть назву товару: ");
            name = sc.nextLine();
            System.out.println("Введіть виробника товару: ");
            vurobnuk = sc.nextLine();
            System.out.println("Введіть ціну товару: ");
            zina = sc.nextInt();
            System.out.println("Введіть термін придатності товару(в місяцях): ");
            termin = sc.nextInt();
            System.out.println("Введіть кількість товару: ");
            kilkist = sc.nextInt();
            sc.nextLine();
            Product prod = new Product(id, name, vurobnuk, zina, termin, kilkist);
            products.add(prod);
        }
        return ;
    }
    public static void menu(){
        System.out.println("Меню:");
        System.out.println("1.Створити список продуктів.");
        System.out.println("2.Вивести весь список.");
        System.out.println("3.Додати новий продукт до списку.");
        System.out.println("4.Вивести список продуктів за назвою.");
        System.out.println("5.Вивести список продуктів за назвою і ціною не більше заданої.");
        System.out.println("6.Вивести список продуктів з терміном придатності більше заданого.");
        System.out.println("7.Видалити продукт з списку.");
        System.out.println("8.Вийти з програми.");
    }
    public static int choise(ArrayList<Product>products){
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        switch(n){
            case 1: return 1;
            case 2: PrintList(products);break;
            case 3: AddToList(products);break;
            case 4: PrintByName(products);break;
            case 5: PrintByZina(products);break;
            case 6: PrintByTermin(products);break;
            case 7: Delete(products);break;
            case 8: return n;
        }
        return n;
    }
    public static void PrintList(ArrayList<Product> products){
        System.out.println("Список продуктів:");
        for (int i = 0; i < products.size(); i++) {
            if(products.get(i).getStan()) {
                System.out.println(products.get(i).toString());
            }
        }
        System.out.println(products.size());
    }
    public static void PrintByZina(ArrayList<Product> products) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введіть назву:");
        String name = sc.nextLine();
        System.out.println("Введіть максимальну ціну:");
        int zina = sc.nextInt();
        System.out.println("Список продуктів за назвою " + name + ":");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)&products.get(i).getStan())
                System.out.print(products.get(i).toString());
        }
        System.out.println("Список продуктів за назвою " + name + " та ціною не більше " + zina + ":");
        for (int i = 0; i < products.size(); i++) {
            if ((products.get(i).getName().equals(name) && products.get(i).getValue() <= zina)&&products.get(i).getStan())
                System.out.print(products.get(i).toString());
        }
    }
    public static void PrintByName(ArrayList<Product> products) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введіть назву:");
        String name = sc.nextLine();
        System.out.println("Список продуктів за назвою " + name + ":");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name)&&products.get(i).getStan())
                System.out.print(products.get(i).toString());
        }
    }
    public static void PrintByTermin(ArrayList<Product> products){
        Scanner sc = new Scanner(System.in);
        System.out.println("Введіть мінімальний термін придатності: ");
        int termin = sc.nextInt();
        System.out.println("Список продуктів за терміном придатності більше " + termin + "м:");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getTermin()>=termin && products.get(i).getStan())
                System.out.print(products.get(i).toString());
        }
    }
    public static void AddToList(ArrayList<Product> products){
        String id;
        String name;
        String vurobnuk;
        Scanner sc = new Scanner(System.in);
        int zina, termin, kilkist;
            System.out.println("Введіть id товару: ");
            id = sc.nextLine();
            System.out.println("Введіть назву товару: ");
            name = sc.nextLine();
            System.out.println("Введіть виробника товару: ");
            vurobnuk = sc.nextLine();
            System.out.println("Введіть ціну товару: ");
            zina = sc.nextInt();
            System.out.println("Введіть термін придатності товару(в місяцях): ");
            termin = sc.nextInt();
            System.out.println("Введіть кількість товару: ");
            kilkist = sc.nextInt();
            Product prod = new Product(id, name, vurobnuk, zina, termin, kilkist);
            products.add(prod);
    }
    public static void Delete(ArrayList<Product> products){
        Scanner sc = new Scanner(System.in);
        System.out.println("Введіть id продукта для видалення:");
        String id  = sc.nextLine();
        for(int i = 0 ; i< products.size();i++){
            if(products.get(i).getId().equals(id)){
                products.get(i).setStan(false);
            }
        }

    }
}

