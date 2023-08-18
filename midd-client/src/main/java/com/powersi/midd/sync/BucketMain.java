package com.powersi.midd.sync;

/**
 * @author sxl
 * @version 1.0
 * @description BucketMain
 * @date 2023/2/27 11:39
 */

public class BucketMain {
    public static void main(String[] args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        Bucket.increase();
                    }
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++) {
                        Bucket.decrease();
                    }
                }
            }).start();
    }
}
