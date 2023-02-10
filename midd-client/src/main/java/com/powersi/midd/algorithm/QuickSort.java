package com.powersi.midd.algorithm;

/**
 * @author sxl
 * @version 1.0
 * @description QuickSort
 * @date 2023/2/9 16:01
 */

public class QuickSort {
    public static void main(String[] args) {
        int a[] = {3, 3, 4, 2, 5, 4, 1};
        quickSort(a, 0, a.length - 1);
        for (int i : a) {
            System.out.println(i);
        }
    }

    private static void quickSort(int[] a, int l, int h) {
        if (l >= h) {
            return;
        }
        int k = a[l];
        int left = l;
        int right = h;
        while (left < right) {
            while (a[right] >= k && right > left) {
                right--;
            }
            while (a[left] <= k && left < right) {
                left++;
            }
            if(right>left) {
                PopSort.swap(a, left, right);
            }
        }
        a[l] = a[left];
        a[left] = k;
        quickSort(a, l, left - 1);
        quickSort(a, right + 1, h);
    }
}
//3 3 5 2 1
//3
//3 3 5 2 1 4
//3 3 5 2 1 2
//3 3 1 2 5
//
