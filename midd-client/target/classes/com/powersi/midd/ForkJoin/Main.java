package com.powersi.midd.ForkJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @author: PowerSI
 * @date: 2021/4/26 14:30
 * @description:
 */
public class Main {
    public static void main(String[] args) {
        List list=new ArrayList();
        for (int i = 0; i <500 ; i++) {
            list.add((char)i);
        }
        Long l=System.currentTimeMillis();
        ForkJoinPool forkJoinPool=new ForkJoinPool();
        String  s=  forkJoinPool.invoke(new ForkWorker(list)).toString();
        System.out.println(s);
        System.out.println(System.currentTimeMillis()-l);
    }
}
