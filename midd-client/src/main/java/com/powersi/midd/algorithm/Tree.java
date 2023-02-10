package com.powersi.midd.algorithm;

import java.util.List;

/**
 * @author sxl
 * @version 1.0
 * @description Tree
 * @date 2023/2/10 11:43
 */

public class Tree {
    public Node root;

    public Tree(List<Integer> data) {
        root = new Node(data.get(0));
        for (int i = 1; i < data.size(); i++) {
            root.add(new Node(data.get(i)));
        }
    }
}

class Node {
    Node left;
    Node right;
    int val;

    public Node(int val) {
        this.val = val;
    }

    public void add(Node node) {
        if (node == null) {
            return;
        }
        if (node.val < this.val) {
            if (this.left == null) {
                this.left = node;
            } else {
                this.left.add(node);
            }
        } else {
            if (this.right == null) {
                this.right = node;
            } else {
                this.right.add(node);
            }
        }
    }
}