package com.powersi.midd.sync;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sxl
 * @version 1.0
 * @description Bucket
 * @date 2023/2/27 11:32
 */

public class Bucket {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static Integer count = 0;

    public static void increase() {
        lock.lock();
        try {
            if (count >= 10) {
                condition.await();
            } else {
                count++;
                System.out.println(Thread.currentThread().getName() + ":生产一个馒头");
                condition.signalAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


    }

    public static void decrease() {
        lock.lock();
        try {
            if (count <= 0) {
                condition.await();
            } else {
                count--;
                System.out.println(Thread.currentThread().getName() + ":吃掉一个馒头");
                condition.signalAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
