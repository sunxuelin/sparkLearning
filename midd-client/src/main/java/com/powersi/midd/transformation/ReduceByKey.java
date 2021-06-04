package com.powersi.midd.transformation;

import com.powersi.midd.conf.SparkConfUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * @author: sxl
 * @date: 2021/6/4 11:42
 * @description:
 */
public class ReduceByKey {
    public static void main(String[] args) {
        SparkConf conf = SparkConfUtil.local();
        JavaSparkContext sc = new JavaSparkContext(conf);
        //求a b的平均值
        Tuple2<String, Integer> t1 = new Tuple2("a", 94);
        Tuple2<String, Integer> t2 = new Tuple2("a", 90);
        Tuple2<String, Integer> t3 = new Tuple2("b", 94);
        Tuple2<String, Integer> t4 = new Tuple2("a", 96);
        Tuple2<String, Integer> t5 = new Tuple2("b", 92);
        Tuple2<String, Integer> t6 = new Tuple2("b", 90);
        List<Tuple2<String, Integer>> list = Arrays.asList(t1, t2, t3, t4, t5, t6);
        JavaPairRDD<String, Integer> javaPairRDD = sc.parallelizePairs(list, 2);
        List<Tuple2<String, Integer>> list2=javaPairRDD.reduceByKey((v1,v2)->v1+v2
        ).collect();
        list2.forEach(System.out::println);
    }
}
