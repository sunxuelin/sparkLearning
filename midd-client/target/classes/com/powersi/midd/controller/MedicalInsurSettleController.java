package com.powersi.midd.controller;

import com.powersi.midd.api.medicalInsur.pojo.Item;
import com.powersi.midd.apriori.Aprori;
import com.powersi.midd.mapper.ItemMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.ml.fpm.FPGrowth;
import org.apache.spark.ml.fpm.FPGrowthModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.apache.spark.sql.types.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scala.collection.mutable.WrappedArray;
import scala.reflect.ClassTag;

import javax.annotation.Resource;
import java.beans.Encoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.sort_array;

/**
 * @description: 医保数据抽取调用执行Controller
 * @author: sunxuelin
 * @date: 2021/01/22
 **/
@RestController
public class MedicalInsurSettleController {

    @Resource
    ItemMapper itemMapper;

    @RequestMapping("VF")
    public String VF() {
        List list = itemMapper.queryAll(null);
        System.out.println("基础数据生成完成");
        String[] set = GroupListUtil.getPid(list);
        System.out.println("获取所有PID");
        List<Set<Item>> list2 = GroupListUtil.getLists2(set, list);
        System.out.println("PID分组完成");
        List records = new ArrayList();
        for (int i = 0; i < list2.size(); i++) {
            List record = new ArrayList();
            Set set1 = list2.get(i);
            Iterator iterator = set1.iterator();
            while (iterator.hasNext()) {
                record.add(((Item) (iterator.next())).getItem_id());
            }
            records.add(record);
        }
        System.out.println("二维数组完成");
        Aprori aprori = new Aprori();
        aprori.calc(records);
        return "success";
    }

    @RequestMapping("spark")
    public String spark() throws IOException {
//        System.out.println("开始获取数据");
//        List list = new ArrayList(20000000);
//        for (int i = 0; i < 1; i++) {
//            list.addAll(itemMapper.queryAll(500000L * i));
//            System.out.println("遍历第"+i+"次");
//        }
//        System.out.println("基础数据生成完成:"+list.size());
//        String[] set = GroupListUtil.getPid(list);
//        System.out.println("获取所有PID完成:"+set.length);
//        List<Set<Item>> list2 = GroupListUtil.getLists2(set, list);
//        System.out.println("PID分组完成");
//        List<Row> records = GroupListUtil.transRow(list2);
        System.setProperty("hadoop.home.dir", "D:\\software\\");
        StructType schema = new StructType(new StructField[]{new StructField(
                "items", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });
        SparkConf conf = new SparkConf().setAppName("FP_growth" + System.currentTimeMillis())
//                .setMaster("spark://172.18.100.135:7077")
                .setMaster("local[*]")
                .set("spark.executor.heartbeatInterval", "200000")
                .set("spark.network.timeout", "300000")
                .set("spark.executor.memoryOverhead", "8192")
                .set("spark.executor.memory", "8g");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        Map<String, String> options = new HashMap<String, String>();
        options.put("url", "jdbc:postgresql://172.18.20.87:5432/everest?currentSchema=public");
        options.put("user", "drg");
        options.put("password","drg_87");
        options.put("dbtable", "item");
        options.put("driver", "org.postgresql.Driver");
        Dataset<Row> df = spark.read().format("jdbc").options(options).load();
        df.createOrReplaceTempView("item");
//        collect_set去重
        String filter =" where item_id  not in(4424) ";
        Dataset<Row> ds1 = spark.sql(" select collect_set(item_id) as item_id from item"+filter+" group by pid");
        Dataset<Row> itemsDF = spark.createDataFrame(ds1.collectAsList(), schema);
        FPGrowthModel model = new FPGrowth()
                .setItemsCol("items")
                .setMinSupport(0.2)
                .setMinConfidence(0.6)
                .fit(itemsDF);

// Display frequent itemsets.
//        Dataset dataset = model.freqItemsets();


// Display generated association rules.
        Dataset dataset=model.associationRules().sort(col("confidence").desc()).limit(5000);
        List<GenericRowWithSchema> datasetList = dataset.collectAsList();
        FileOutputStream fileOutputStream=new FileOutputStream("data.txt");
        fileOutputStream.write(("支持度(表示数据集D中，事件A和事件B共同出现的概率) \n" +
                "置信度(表示数据集D中，出现事件A的事件中出现事件B的概率)\n" +
                "提升度(表示数据集D中，出现A的条件下出现事件B的概率和没有条件A出现B的概率)\n").getBytes());
        for (GenericRowWithSchema data:datasetList
        ) {
            String line="[";
            for (int i = 0; i <data.values().length ; i++) {

                if (i==0){
                    line += ((String[]) ((WrappedArray) data.values()[i]).array())[i]+"-->";
                }else if (i==1){
                    line += ((String[]) ((WrappedArray) data.values()[i]).array())[i];
                }else if (i==2){
                    line+="] 置信度:"+(double)data.values()[i];
                }else if (i==3){
                    line+=" 提升度:"+(double)data.values()[i];
                }else {
                    line+=" 支持度:"+(double)data.values()[i];
                }
            }
            fileOutputStream.write((line+"\n").getBytes());

        }
        fileOutputStream.close();
        dataset.show(5000);
// transform examines the input items against all the association rules and summarize the
// consequents as prediction
        model.transform(itemsDF).show();
        spark.close();
        return "sss";
    }

    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir", "D:\\software\\");
//        StructType schema = new StructType(new StructField[]{new StructField(
//                "items", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
//        });
        SparkConf conf = new SparkConf().setAppName("FP_growth" + System.currentTimeMillis())
//                .setMaster("spark://172.18.100.135:7077")
                .setMaster("local[*]")
                .set("spark.executor.heartbeatInterval", "200000")
                .set("spark.network.timeout", "300000")
                .set("spark.executor.memoryOverhead", "8192")
                .set("spark.executor.memory", "8g");
//        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        JavaSparkContext sc=new JavaSparkContext(conf);
        JavaRDD dataset=sc.parallelize(Arrays.asList(new int[]{1,2,3,4,5,6},3));
        dataset.saveAsTextFile("output");
    }

}
