package com.powersi.midd.transformation;

import com.powersi.midd.conf.SparkConfUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * @author: sxl
 * @date: 2021/6/4 11:27
 * @description:
 */
public class Reduce {
    //将两个对象进行计算 a b c ->a+b+c
    public static void main(String[] args) {
        SparkConf conf = SparkConfUtil.local();
        JavaSparkContext sc = new JavaSparkContext(conf);
        Tuple2<String, Integer> t1 = new Tuple2("a", 94);
        Tuple2<String, Integer> t2 = new Tuple2("a", 90);
        Tuple2<String, Integer> t3 = new Tuple2("b", 94);
        Tuple2<String, Integer> t4 = new Tuple2("a", 96);
        Tuple2<String, Integer> t5 = new Tuple2("b", 92);
        Tuple2<String, Integer> t6 = new Tuple2("b", 90);
        List<Tuple2<String, Integer>> list = Arrays.asList(t1, t2, t3, t4, t5, t6);
        JavaPairRDD<String, Integer> javaPairRDD = sc.parallelizePairs(list, 2);
        Tuple2<String, Integer> tuple2 = javaPairRDD.reduce(
                (Tuple2<String, Integer> stringIntegerTuple2, Tuple2<String, Integer> stringIntegerTuple22) ->
                        new Tuple2<>(stringIntegerTuple2._1 + stringIntegerTuple22._1, stringIntegerTuple2._2 + stringIntegerTuple22._2));
        System.out.println(tuple2._1 + " " + tuple2._2);
    }
}
