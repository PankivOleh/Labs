package System;

import java.util.Scanner;

public class SysFunc {
    public static int get(int i , int j){
        System.out.println("Введіть свій вибір від "+ i +"до"+j);
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        if(t>=i && t<=j){
            return t;
        }
        else{
            System.out.println("Хибний вибір");
            return get(i,j);
        }
    }
}
