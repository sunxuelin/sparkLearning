package com.powersi.midd.ForkJoin;

import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author: PowerSI
 * @date: 2021/4/26 14:28
 * @description:
 */
public class ForkWorker extends RecursiveTask {
    List<Character> list;

    ForkWorker(List<Character> list) {
        this.list = list;
    }

    @Override
    protected StringBuffer compute() {
        StringBuffer stringBuffer = new StringBuffer();
        if (list.size() <= 100) {
            for (Character c :
                    list) {
                stringBuffer.append(c);
            }
            return stringBuffer;
        }
        int numberBatch=100;
        double number = list.size() * 1.0 /numberBatch;
        int n = ((Double) Math.ceil(number)).intValue(); //向上取整
        ForkJoinTask[] forkJoinTasks = new ForkJoinTask[n];
        for (int i = 0; i < n; i++) {
            int end = numberBatch * (i + 1);
            if (end > list.size()) {
                end = list.size(); //如果end不能超过最大索引值
            }
            forkJoinTasks[i]=new ForkWorker(list.subList(numberBatch * i, end));
        }
            invokeAll(forkJoinTasks);
        for (ForkJoinTask forkJoinTask :
                forkJoinTasks) {
            stringBuffer.append(forkJoinTask.join());
        }
        return stringBuffer;
        }
    }
