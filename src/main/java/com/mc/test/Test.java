package com.mc.test;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * @author ymj
 * @Date： 2021/3/2 16:26
 * @description:
 */
public class Test {
    static Scanner cin = new Scanner(System.in);
    public static void main(String[] args) {
        String s = cin.nextLine();
        String[] array = s.split(" ");
        int a = Integer.parseInt(array[0]) , g =  Integer.parseInt(array[1]), c = Integer.parseInt(array[2]);
        long k = Long.parseLong(array[3]);
        if(k%2 == 0) {
            System.out.printf("%d", a-g);
        } else {
            System.out.printf("%d", g-a);
        }
    }
}
