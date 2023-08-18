package com.powersi.midd.algorithm.leetcode;

/**
 * @author sxl
 * @version 1.0
 * @description Main
 * @date 2023/2/13 11:33
 */

public class AddTwoNumbers {
    public static void main(String[] args) {
        ListNode listNode = new ListNode(2);
        listNode.next = new ListNode(4);
        listNode.next.next = new ListNode(3);
        ListNode listNode2 = new ListNode(5);
        listNode2.next = new ListNode(6);
        listNode2.next.next = new ListNode(4);
        ListNode listNode1 = addTwoNumbers(listNode, listNode2);

    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode pre = null;
        ListNode listNode = new ListNode();
        int temp = 0;
        while (l1 != null || l2 != null || temp > 0) {
            if (listNode.next != null) {
                listNode = listNode.next;
            }
            int nVal = (l1 == null ? 0 : l1.val) + (l2 == null ? 0 : l2.val) + temp;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
            if (nVal > 9) {
                listNode.val = nVal % 10;
                temp = (nVal - nVal % 10) / 10;
            } else {
                listNode.val = nVal;
                temp = 0;
            }
            if (pre == null) {
                pre = listNode;
            }
            listNode.next = new ListNode();
        }
        listNode.next = null;
        return pre;
    }

    public static int getVal(ListNode l) {
        if (l == null) {
            return 0;
        } else {
            int val = l.val;
            l = l.next;
            return val;
        }
    }
}

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}