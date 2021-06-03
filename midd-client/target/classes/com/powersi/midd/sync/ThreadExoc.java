package com.powersi.midd.sync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: PowerSI
 * @date: 2021/4/20 16:00
 * @description:
 */
public class ThreadExoc {
    public static void main(String[] args) {
        Executor executor=new ThreadPoolExecutor(5,10,200, TimeUnit.SECONDS, new ArrayBlockingQueue(5));
        Thread thread=new Thread(new Worker());
        Thread thread2=new Thread(new Worker());
        executor.execute(thread);
        executor.execute(thread2);
        executor=null;
    }
}
