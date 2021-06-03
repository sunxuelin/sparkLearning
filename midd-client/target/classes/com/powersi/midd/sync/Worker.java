package com.powersi.midd.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: PowerSI
 * @date: 2021/4/20 16:03
 * @description:
 */
public class Worker implements Runnable{
   static Lock lock=new ReentrantLock();

    public void job()  {
        lock.lock();
        try {
            System.out.println("i'm working");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        this.job();
    }
}
