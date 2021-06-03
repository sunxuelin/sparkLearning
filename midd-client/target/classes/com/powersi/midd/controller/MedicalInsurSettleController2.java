package com.powersi.midd.controller;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Controller;
import scala.Tuple2;

import java.util.*;

/**
 * @description: 医保数据抽取调用执行Controller
 * @author: sunxuelin
 * @date: 2021/01/22
 **/
@Controller
public class MedicalInsurSettleController2 {


    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "D:/software/");
        SparkConf conf = new SparkConf().setAppName("FP_growth" + System.currentTimeMillis())
//                .setMaster("spark://172.18.100.135:7077")
//                .setJars(new String[]{"midd-client/target/midd-client-0.0.1-SNAPSHOT.jar"})
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .setMaster("local[*]")
                .set("spark.executor.heartbeatInterval", "200000")
                .set("spark.network.timeout", "300000")
                .set("spark.executor.memoryOverhead", "8192")
                .set("spark.executor-memory", "8g");
        JavaSparkContext sc = new JavaSparkContext(conf);
//        SparkSession sparkSession=SparkSession.builder().config(conf).getOrCreate();
        Tuple2<String, Integer> tuple21 = new Tuple2("a", 94);
        Tuple2<String, Integer> tuple22 = new Tuple2("a", 96);
        Tuple2<String, Integer> tuple23 = new Tuple2("b", 92);
        Tuple2<String, Integer> tuple24 = new Tuple2("b", 94);
        List<Tuple2<String, Integer>> list = Arrays.asList(tuple21, tuple22, tuple23, tuple24);
        JavaPairRDD<String, Integer> javaPairRDD = sc.parallelizePairs(list, 2);
        List list1 = javaPairRDD.combineByKey(
                (o) -> new Tuple2<>(o, 1)
                , (acc, v) -> new Tuple2<>(acc._1 + v, acc._2 + 1)
                , (acc1, acc2) -> new Tuple2<>(acc1._1 + acc2._1, acc1._2 + acc2._2)).map(new Function<Tuple2<String, Tuple2<Integer, Integer>>, Object>() {
            @Override
            public Object call(Tuple2<String, Tuple2<Integer, Integer>> stringTuple2Tuple2) throws Exception {
                return new Tuple2<>(stringTuple2Tuple2._1,stringTuple2Tuple2._2._1/stringTuple2Tuple2._2._2);
            }
        }).collect();
        list1.forEach(System.out::println);
//       Dataset dataset= sparkSession.createDataFrame(list,Map.class);
//       dataset
//        List<Integer> list = Arrays.asList(1, 1, 2, 3, 4, 5);
//        JavaRDD lines = sc.parallelize(list);
//        JavaPairRDD<Integer, Integer> pairs = lines.mapToPair(s -> new Tuple2(s, 1));
//        JavaPairRDD<Integer, Integer> counts = pairs.reduceByKey((a, b) -> a + b);
//        List list1 = counts.collect();
//        list1.forEach(System.out::println);

//        List<String> list = Arrays.asList("12","23","345","4567");
//        JavaRDD<String> lines = sc.parallelize(list,3);
//       String u= lines.aggregate("",(x,y)-> String.valueOf(Math.max(x.length(),y.length())),(x, y)-> x+y);
//        System.out.println("result2:"+u);

//        JavaRDD<Integer> lines = sc.parallelize(list,3);
////        //flatMap算子lambda形式：求从0到javaRDD元素的数字组成的RDD
//        JavaRDD<Integer> reduce3 = lines.flatMap(integer -> {
//            List<Integer> list2 = new ArrayList<>();
//                list2.add(integer);
//            return list2.iterator();
//        });
//        System.out.println(reduce3.collect());

//        List rdd=lines.flatMap(new FlatMapFunction<Integer, Object>() {
//            @Override
//            public Iterator<Object> call(Integer integer) throws Exception {
//                return new ArrayList<>(integer).iterator();
//            }
//        }).collect();
//        System.out.println("result:");
//        for (int i = 0; i <list.size() ; i++) {
//            list.get(i)
//        }

    }


}
