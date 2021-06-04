package com.powersi.midd.conf;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

/**
 * @author: sxl
 * @date: 2021/6/3 19:41
 * @description:
 */
public class SparkConfUtil {
    public static SparkConf local() {
        System.setProperty("hadoop.home.dir", "D:/software/");
        return new SparkConf().setAppName("FP_growth" + System.currentTimeMillis())
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .setMaster("local[*]")
                .set("spark.executor.heartbeatInterval", "200000")
                .set("spark.network.timeout", "300000")
                .set("spark.executor.memoryOverhead", "8192")
                .set("spark.executor-memory", "8g");
    }

    public static SparkConf remote() {
        System.setProperty("hadoop.home.dir", "D:/software/");
        return new SparkConf().setAppName("FP_growth" + System.currentTimeMillis())
                .setMaster("spark://172.18.100.135:7077")
                .setJars(new String[]{"midd-client/target/midd-client-0.0.1-SNAPSHOT.jar"})
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .set("spark.executor.heartbeatInterval", "200000")
                .set("spark.network.timeout", "300000")
                .set("spark.executor.memoryOverhead", "8192")
                .set("spark.executor-memory", "8g");
    }


}
