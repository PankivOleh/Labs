package org.example;
import java.util.Scanner;


class Main{
    public static void main(String[] args) {
        System.out.println("enter M max(91): ");
        Scanner scan = new Scanner(System.in);
        int M , count =0;
        long numb1 , numb2 , n;
        M = scan.nextInt();
        Luce_numb luce = new Luce_numb();
        n = luce.getN();
        System.out.println("n Числа люка - n(n+1)/2");
        while(n<M){
            luce=luce.process();
            n = luce.getN();
            numb1 = luce.getNumber();
            numb2 = n*(n+1)/2;
            if(numb1 == numb2){
                count++;
            }
            else if(luce.isNext(numb2)|luce.isPrev(numb2)){
                count++;
            }
            System.out.println(n+" "+numb1+"-"+numb2+" ");
        }
        System.out.println("M - "+M);
        System.out.println("Чисел які можна подати у вигляді n(n+1)/2 - " + count);
        scan.close();
    }


}