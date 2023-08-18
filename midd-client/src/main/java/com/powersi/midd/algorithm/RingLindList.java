package com.powersi.midd.algorithm;

import java.util.LinkedList;

/**
 * @author sxl
 * @version 1.0
 * @description ringLindList
 * @date 2023/6/1 17:00
 */

public class RingLindList {
    int val;
    RingLindList next;
    private RingLindList(){

    }
    public RingLindList(int arr[]){
        int i=0;
        val=arr[i];
        next=new RingLindList(arr);
    }
    public static void main(String[] args) {
       int arr[]={1,2,3};
        RingLindList ringLindList = new RingLindList(arr);

        System.out.println(1);
    }
}
