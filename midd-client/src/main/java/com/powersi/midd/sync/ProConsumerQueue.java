package com.powersi.midd.sync;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author sxl
 * @version 1.0
 * @description ProConsumerQueue  阻塞队列实现生产者消费者模式
 * @date 2023/8/18 16:20
 */

public class ProConsumerQueue {

    public static void main(String[] args) {
        ArrayBlockingQueue<Object> bucket = new ArrayBlockingQueue<>(10);
        Thread thread = new Thread(new Producer(bucket));
        Thread thread2 = new Thread(new Consumer(bucket));
        Thread thread3 = new Thread(new Consumer(bucket));
        thread.start();
        thread2.start();
        thread3.start();
    }
}

class Producer implements Runnable {
    private BlockingQueue bucket;

    public Producer(BlockingQueue bucket) {
        this.bucket = bucket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                bucket.put(produce());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Object produce() {
        return new Object();
    }

}

class Consumer implements Runnable {
    private BlockingQueue bucket;

    public Consumer(BlockingQueue bucket) {
        this.bucket = bucket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume(bucket.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void consume(Object o) {
        System.out.println("consume " + o.hashCode());
    }

}