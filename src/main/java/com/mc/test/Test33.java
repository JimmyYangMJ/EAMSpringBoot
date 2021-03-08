package com.mc.test;

import java.util.HashSet;
import java.util.Scanner;

public class Test33 {

    static Scanner cin = new Scanner(System.in);

    /**
     * 楼梯一共有  层，你一次能上一阶、两阶或三阶台阶，请问，你从楼梯底部 ( 开始) 走到楼梯顶部，共有多少种走法。
     *
     * 输入格式
     * 输入数据共两行，第一行包含两个自然数  () 和  ()，第二行包含  个自然数  ()，数字之间用一个空格隔开，表示损坏的台阶的序号（从楼梯底部到楼梯顶部，台阶序号依次为  到 ）。
     *
     * 输出格式
     * 输出数据仅包含一个整数，表示所有可行走法的总数。
     */
    public static void main(String[] args) {
        int n = cin.nextInt();
        int k = cin.nextInt();
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < k; i++) {
            set.add(cin.nextInt());
        }
         // 0 1 1 2 4
        int a1 = 0, a2 = 0, a3 = 0, a4 = 1;
        for (int i = 1; i <= n; i++) {
           a1 = a2;
           a2 = a3;
           a3 = a4;
           a4 = a3 + a2 + a1;
           if (set.contains(i)) { // 这一阶的 台阶坏了，不能走
               a4 = 0;
           }
            // * * 1 2 3 4
            // 0 0 0   1 （原始数据）
            // 《--- 左移
            // 0 0 1（ 1 第一阶
            // 0 1 1（ 2 第二阶
            // 1 1 2（ 4 第三阶
            // 1 2 4（ 7 第四阶
        }
        System.out.println(a4);
    }
}