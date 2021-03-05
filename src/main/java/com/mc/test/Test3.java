package com.mc.test;


import java.net.BindException;
import java.util.Scanner;

/**
 * @author ymj
 * @Date： 2021/3/3 11:20
 * @description:
 */
public class Test3 {
    /** 1 10 1[0]0 1000 10000 100000 … …
     *  可以用公式
     *  可以用bit方式
     */
    static Scanner cin = new Scanner(System.in);
    public static void main(String[] args) {

        int n = cin.nextInt();

        for (int i = 0; i < n ; i++) {
            int item = cin.nextInt();
            int index = sum(item)-1;
            int sub = item - (index+ 1) * index / 2; // 第几个位置
            if(sub == 1) {
                System.out.println(1);
            }else  {
                System.out.println(0);
            }
        }

    }

    public static int  sum(int sum) {
        return (int) Math.ceil((Math.sqrt(sum*8 + 1) - 1 )/ 2);
    }

}
