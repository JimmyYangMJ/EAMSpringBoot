package com.mc.test;

import java.util.*;

/**
 * @author ymj
 * @Date： 2021/3/3 17:38
 * @description:
 */
public class Test4 {
    static Scanner cin = new Scanner(System.in);
    public static void main(String[] args) {
        int n = cin.nextInt();
        cin.nextLine();
        String[] s = cin.nextLine().split(" ");
        Arrays.sort(s, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                char[] chars1=o1.toCharArray();
                char[] chars2=o2.toCharArray();
                int i=0;
                while(i<chars1.length && i<chars2.length){
                    if(chars1[i]>chars2[i]){
                        return -1;
                    }else if(chars1[i]<chars2[i]){
                        return 1;
                    }else{
                        i++;
                    }
                }
                if(i==chars1.length){  //o1到头
                    return -1;
                }
                if(i== chars2.length){ //o2到头
                    return 1;
                }
                return 0;
            }
        });


        for (String s2 :
                s) {
            System.out.print(s2);
        }
    }
}
