package org.example;

public class Luce_numb{
    long i = -1 , j =3 , next , prev , n = -1;

    /**
     * Оновлює поточний стан об'єкта, обчислюючи нові значення
     * для внутрішніх полів на основі поточного стану, дотримуючись певної логіки послідовності.
     * Збільшує лічильник послідовності та визначає наступний стан.
     *
     * @return оновлений об'єкт {@code Luce_numb} після обчислення нового значення
     */
    public Luce_numb process(){
    long numb1 ;
    prev = i;
    numb1 = i+j;
    j=i;
    i=numb1;
    next = numb1+j;
    n++;
    return this ;
}

    /**
     * Повертає поточне число Люка
     * @return Число Люка, яке обраховувалося останнім і нразі записане в об'єкті
     */
    public long getNumber(){
    return i;
}

    /**
     * Метод для отримання поточного номеру числа Люка
     * @return номер якому належить поточне число Люка
     */
    public long getN(){
    return n;
}

    /**
     * Метод, що перевіряє, чи вказане число входить в ряд Люка, що лежить позаду поточного числа Люка.
     * @param numb2 Число, наявність якого перевіряють
     * @return повертає true або false в залежності від того, чи задане чило входить в ряд.
     */
    public boolean isPrev( long numb2) {
    long prev2 = prev , i2 = i;
    if (i > numb2) {
        if (numb2 == prev2) {
            return true;
        } else if (numb2 < prev2) {
            do {
                prev2 = i2 - prev2;
                i2 = i2 - prev2;

            } while (numb2 < prev2);
            if (numb2 == prev2) {
                return true;
            }
        }
    }
    return false;
}

    /**
     * Метод, що перевіряє, чи вказане число входить в ряд Люка, що лежить попереду поточного числа Люка.
     * @param numb2 Число, наявність якого перевіряють
     * @return повертає true або false в залежності від того, чи задане чило входить в ряд.
     */
    public boolean isNext(long numb2){
    long i2 = i , next2 = next;
    if(i<numb2) {
        if (numb2 == next) {
            return true;
        } else if (numb2 > next) {
            do {
                next2 = i2 + next2;
                i2 = next2 - i2;
            } while (numb2 > next2);
            if (numb2 == next2) {
                return true;
            }
        }
    }
    return false;
}
}
