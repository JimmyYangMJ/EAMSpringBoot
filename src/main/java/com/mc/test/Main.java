package com.mc.test;

import java.math.BigInteger;
import java.util.*;

public class Main {
    static Scanner cin = new Scanner(System.in);

    // 桶排序
    public static void main(String[] args) {

        int n = cin.nextInt();
        cin.nextLine();
        String[] s = cin.nextLine().split(" ");
        LinkedList<String> linkedList = new LinkedList<>();
        // 根据 最高高位 排序  569 569 69 99
        int maxLength = 0;
        for (int i = 0; i < n; i++) {
            linkedList.add(s[i]);
            if(maxLength < s[i].length()) {
                maxLength = s[i].length();
            }
        }
        // 桶排序  9 8 7 ··· ···3 2 1 0
        // 5
        // 12 3 31 2 11
//        LinkedList<String>[] arr = new LinkedList[5];
        LinkedList<String>[] linked = new LinkedList[11];
        for (int i = 0; i < maxLength; i++) {
            for (int j = 0; j < 11; j++) {
                linked[j] = new LinkedList();
            }
//            Arrays.fill(linked, new LinkedList<>());
            for (int j = 0; j < s.length; j++) {
                // 遍历数组
                int index;
                if (i < linkedList.get(j).length()) {
                   index = linkedList.get(j).charAt(i) - '0';
                } else { index = 10;
                }
                linked[index].add(s[j]);
            }
            // 遍历所有桶
            linkedList = new LinkedList<>();
            for (List list: linked) {
                if (!list.isEmpty()) {
                    for (Object s1: list) {
                        linkedList.add(s1.toString());
                    }
                }
            }


        }
        for (String list : linkedList) {

            System.out.println(list);
        }
    }
}
