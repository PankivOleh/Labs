package org.example;
import pack.Product;

import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Product>products = createList();
        System.out.println("Введіть назву за якою буде проводитись пошук:");
        String name = sc.nextLine();
        System.out.println("Введіть максимальну ціну за якою буде проводитись пошук:");
        int zina = sc.nextInt();
        PrintByName(products , name , zina);
        System.out.println("Введіть мінімальний термін придатності за яким буде проводитись пошук:");
        int termin = sc.nextInt();
        PrintByTermin(products , termin);
    }

    public static ArrayList<Product> createList() {
        int id;
        String name;
        String vurobnuk;
        Scanner sc = new Scanner(System.in);
        int zina, termin, kilkist;
        ArrayList<Product> products = new ArrayList<>();
        while (true) {
            System.out.println("Введіть id товару( 0 щоб зупинити ввід): ");
            id = sc.nextInt();
            sc.nextLine();
            if (id == 0) {
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
            Product prod = new Product(id, name, vurobnuk, zina, termin, kilkist);
            products.add(prod);
        }
        return products;
    }

    public static void PrintByName(ArrayList<Product> products, String name, int zina) {
        System.out.println("Список продуктів за назвою " + name + ":");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name))
                System.out.print(products.get(i).toString());
        }
        System.out.println("Список продуктів за назвою " + name + " та ціною не більше " + zina + ":");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getName().equals(name) & products.get(i).getValue() <= zina)
                System.out.print(products.get(i).toString());
        }
    }
    public static void PrintByTermin(ArrayList<Product> products , int termin){
        System.out.println("Список продуктів за терміном придатності більше " + termin + "м:");
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getTermin()>=termin)
                System.out.print(products.get(i).toString());
        }
    }
}

