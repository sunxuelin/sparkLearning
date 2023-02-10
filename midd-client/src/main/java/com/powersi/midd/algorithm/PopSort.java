package com.powersi.midd.algorithm;

/**
 * @author sxl
 * @version 1.0
 * @description PopSort
 * @date 2023/2/9 16:01
 */

public class PopSort {
    public static void main(String[] args) {
        int a[] = {5, 3, 7, 8, 6, 2, 1, 4, 9};
        popSort(a);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    private static void popSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = i; j < a.length - 1; j++) {
                if (a[i] > a[j + 1]) {
                    swap(a, i, j + 1);
                }
            }
        }
    }

    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
