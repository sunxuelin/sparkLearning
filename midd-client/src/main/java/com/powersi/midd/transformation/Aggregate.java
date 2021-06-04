package com.powersi.midd.transformation;

import com.powersi.midd.conf.SparkConfUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * 1.zeroValue 初始值用于第二个函数第一个入参
 * 2.zeroValue和每个分区value进行操作
 * 3.将每个分区第二步返回结果进行combine组合
 */
public class Aggregate {

    public static void main(String[] args) {

        SparkConf conf = SparkConfUtil.local();
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<String> list = Arrays.asList("12", "23", "345", "4567");
        JavaRDD<String> lines = sc.parallelize(list, 2);
        lines.foreachPartition(part->{
            String a="";
            while(part.hasNext()) {
               a+=","+part.next();
            }
            System.out.println(a+" next ");
        });
        String u = lines.aggregate("",
                (x, y) -> String.valueOf(Math.max(x.length(), y.length())),// 2|4
                (x, y) -> x +","+ y);//2,4或者4|2
        System.out.println("result:" + u);


    }


}
