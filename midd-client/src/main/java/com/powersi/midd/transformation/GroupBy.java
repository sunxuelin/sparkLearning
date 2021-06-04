package com.powersi.midd.transformation;

import com.powersi.midd.conf.SparkConfUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author: sxl
 * @date: 2021/6/4 10:59
 * @description:
 */
public class GroupBy {
    //按Key进行分组  返回key:List<iterator>value
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
        List<Tuple2<String, Iterable<Integer>>> list1=javaPairRDD.groupByKey().collect();
        list1.forEach(action->{
            System.out.println(action._1);
            Iterator iterator=action._2.iterator();
            while (iterator.hasNext()){
                System.out.print(" "+iterator.next());
            }
        });
    }
}
