package com.powersi.midd.ForkJoin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: PowerSI
 * @date: 2021/4/26 15:12
 * @description:
 */
public class Main2 {
    public static void main(String[] args) {
        CP cp=new CP("a","b");
        cp.comp();
    }


}

class CP<E> {
    E a, b;
    public  CP(E a,E b){
        this.a=a;
        this.b=b;
    }
    public void comp() {
        Comparable<? super E> ca= (Comparable<? super E>) a;
        System.out.println(   ((Comparable<? super E>) a).compareTo(b));
    }
}
