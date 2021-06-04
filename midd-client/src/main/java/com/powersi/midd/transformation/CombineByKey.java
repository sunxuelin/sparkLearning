package com.powersi.midd.transformation;

import com.powersi.midd.conf.SparkConfUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.springframework.stereotype.Controller;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

/**
 * createCombiner: V => C ，这个函数把当前的值作为参数，此时我们可以对其做些附加操作(类型转换)并把它返回 (这一步类似于初始化操作)
 * mergeValue: (C, V) => C，该函数把元素V合并到之前的元素C(createCombiner)上 (这个操作在每个分区内单独进行)
 * mergeCombiners: (C, C) => C，该函数把不同分区的相同key元素C合并
 */
public class CombineByKey {

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
        List list1 = javaPairRDD.combineByKey(
                //初始化操作 ByKey分组 选取相同的key只挑选一个（一般是第一个）设置累加器 v是value
                (v) -> new Tuple2<>(v, 1)// a:(94):1 90  b:(94):1 | a:(96):1  b:(92):1 90
                //acc为上一步初始化返回的内容 这里是相同的key的value相加 并且累加器按相同key出现的次数+1
                , (acc, v) -> new Tuple2<>(acc._1 + v, acc._2 + 1)//a:(184):2 b:(94):1 | a:(96):1   b:(182):2
                , (acc1, acc2) -> new Tuple2<>(acc1._1 + acc2._1, acc1._2 + acc2._2)).map(
                        //分区融合 acc1 acc2 是上一步两个分区相同key的结果 a:(184):2  a:(96):1
                stringTuple2Tuple2 -> new Tuple2<>(stringTuple2Tuple2._1, stringTuple2Tuple2._2._1 / stringTuple2Tuple2._2._2)
                //a:(184+96):3  b:(94+182):3
        ).collect();
        list1.forEach(System.out::println);

    }


}
