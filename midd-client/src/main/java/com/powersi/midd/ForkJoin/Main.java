package com.powersi.midd.ForkJoin;

public class Main {

    public static void main(String[] args) {
        Data data = new Data();
        new Thread(() -> {

            data.setI(100);
        }, "t1").start();


        while (data.i == 0) {
        }

    }


}

class Data {
    int i = 0;
//    volatile int i = 0;

    void setI(int i) {
        this.i = i;
    }

    int getI() {
        return i;
    }

    void addOne() {
        i++;
    }
}
