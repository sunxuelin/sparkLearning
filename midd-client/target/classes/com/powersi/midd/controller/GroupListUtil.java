package com.powersi.midd.controller;

import com.powersi.midd.api.medicalInsur.pojo.Item;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: PowerSI
 * @date: 2021/5/6 10:29
 * @description:
 */
public class GroupListUtil {
    public static Item[][] getArrayItem(List<List<Item>> list) {
        Item[][] ps = new Item[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            ps[i] = list.get(i).toArray(new Item[list.get(i).size()]);
        }
        return ps;
    }

    /*将每个部门的对象保存到各自的ArrayList<Person>中，再将所有的ArrayList<Person>保存到List<List<Person>>*/
    public static List<List<Item>> getLists(String[] set, List<Item> persons) {
        List<List<Item>> list = new ArrayList<List<Item>>();

        for (int j = 0; j < set.length; j++) {
            List<Item> pp = new ArrayList<Item>();
            for (int x = 0; x < persons.size(); x++) {
                Item p = persons.get(x);
                if (p.getPid().equals(set[j])) {
                    pp.add(p);
                }
            }
            list.add(pp);
        }

        return list;
    }

    /**
     * 按PID进行分组 同一个PID合并在同一个SET
     * @param set
     * @param items
     * @return
     */
    public static List<Set<Item>> getLists2(String[] set, List<Item> items) {
        List<Set<Item>> list = new ArrayList<>();
        //遍历病人SET
        for (int j = 0; j < set.length; j++) {
            final int finalJ = j;
            //如果流string的PID=SET【j】的PID 合并成同一个SET
            Set list1=items.parallelStream().filter(string->
                 set[finalJ].equals(string.getPid())).collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(list1)){
                list.add(list1);
            }
        }

        return list;
    }
//    /*获取PID，将其存在一个String[]中*/
//    public static String[] getPid(List<Item> list) {
//        Set<String> sets =new HashSet<>();
//        list.parallelStream().forEach(item->sets.add(item.getPid()));
//        return sets.toArray(new String[sets.size()]);
//    }
    /*获取部门，将其存在一个String[]中*/
    public static String[] getPid(List<Item> list) {
        Set<String> sets = new HashSet<String>();
        for (Item item : list) {
            sets.add(item.getPid());
        }
        String[] set = new String[sets.size()];
        Iterator<String> it = sets.iterator();
        int i = 0;
        while (it.hasNext()) {
            set[i] = it.next();
            i++;
        }
        return set;
    }
    /**
     * List<Set<Item>>转 List<Row>
     * @param lists
     * @return
     */
    public static List<Row> transRow(List<Set<Item>> lists){
        List<Row> records = new ArrayList();
        for (int i = 0; i < lists.size(); i++) {
            List record = new ArrayList();
            Set set1 = lists.get(i);
            Iterator iterator = set1.iterator();
            while (iterator.hasNext()) {
                record.add(((Item) (iterator.next())).getItem_id());
            }
            records.add(RowFactory.create(record));
        }
        return records;
    }

    public static void main(String[] args) {

        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);


    }
}
