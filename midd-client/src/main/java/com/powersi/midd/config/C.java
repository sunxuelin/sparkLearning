package com.powersi.midd.config;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: sxl
 * @date: 2021/6/1 23:36
 * @description:
 */
public class C extends B{
    public void aa(){
        System.out.println("C++");
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
       List<String> list=new ArrayList<>();
       list.add("1");
       list.add("2");
       list.add("3");
       list.add("4");
        for (int i = 0; i <list.size() ; i++) {
            if (list.get(i).equals("2")){
                list.remove(i);
                list.add("5");
            }

        }
        for (String st:list
             ) {
            System.out.println(st);

        }
    }
}
