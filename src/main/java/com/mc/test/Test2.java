package com.mc.test;

import java.util.Scanner;

/**
 * @author ymj
 * @Date： 2021/3/2 17:23
 * @description:
 */
public class Test2 {
    static Scanner cin = new Scanner(System.in);
    public static void main(String[] args) {
        int n = cin.nextInt();
        cin.nextLine();
        String[] s = cin.nextLine().split(" ");
        // 根据 最高高位 排序  569 569
        int max = 0;
//        for (int i = 0; i < n; i++) {
//            if(max < s[i].length()) {
//                max = s[i].length();
//            }
//        }
//        System.out.println(max);
        // 冒泡排序
        // 956 97
        // 97 956
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if(s[j].charAt(0) - '0' < s[j+1].charAt(0) - '0'){
                    String temp =  s[j];
                    s[j] = s[j+1];
                    s[j+1] = temp;
                }else if (s[j].charAt(0) - '0' == s[j+1].charAt(0) - '0'){
                    int k;
                    for (k = 1; k < Math.min(s[j].length(), s[j+1].length()); k++) {
                        if(s[j].charAt(k) - '0' < s[j+1].charAt(k) - '0'){
                            String temp =  s[j];
                            s[j] = s[j+1];
                            s[j+1] = temp;
                            break;
                        }
                    }
                    if (k >= Math.min(s[j].length(), s[j+1].length())) {
                        if(s[j].length() != Math.min(s[j].length(), s[j+1].length())){
                            String temp =  s[j];
                            s[j] = s[j+1];
                            s[j+1] = temp;
                        }
                    }

                }
            }
        }
        for (String te : s) {
            System.out.print(te);
        }
    }
}
