package com.mc.test;

/**
 * @author ymj
 * @Date： 2021/3/4 9:43
 * @description: https://www.cnblogs.com/drizzlewithwind/p/6100164.html
 */
public class Test5 {
    public static void main(String[] args) {
//        Plate<Fruit> p = new Plate<Apple>(new Apple());
//        上界通配符
        Plate<? extends Fruit> p = new Plate<Apple>(new Apple());

        System.out.println(p.toString());
    }
}
class Fruit {
    int a = 5;
    String s = "fruit";

    @Override
    public String toString() {
        return "Fruit{" +
                "a=" + a +
                ", s='" + s + '\'' +
                '}';
    }
}
class Apple extends Fruit {
    int b = 4;
    String s = "Apple";

    @Override
    public String toString() {
        return "Apple{" +
                "b=" + b +
                ", s='" + s + '\'' +
                '}';
    }
}
class Plate<T>{
    private T item;
    public Plate(T t){item=t;}
    public void set(T t){item=t;}
    public T get(){return item;}

    @Override
    public String toString() {
        return "Plate{" +
                "item=" + item +
                '}';
    }
}
