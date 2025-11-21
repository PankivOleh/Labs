package System;

import java.util.Scanner;

public class SysFunc {
    public static int get(int i , int j){
        System.out.println("Введіть число від "+ i +" до "+j);
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        sc.nextLine();
        if(t>=i && t<=j){
            return t;
        }
        else{
            System.out.println("Хибний ввід");
            return get(i,j);
        }
    }
    public static void Println(String s){
        System.out.println(s);
    }
}
