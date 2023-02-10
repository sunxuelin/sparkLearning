package com.powersi.midd.algorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author sxl
 * @version 1.0
 * @description DFS
 * @date 2023/2/10 11:42
 */

public class DFSandBFS {
    public static void main(String[] args) {
        List<Integer> data = Arrays.asList(5, 4, 3, 2, 1, 6, 7, 8, 9);
        Tree tree = new Tree(data);
        ArrayList<Integer> objects = new ArrayList<>();
        dfsTree(tree, objects);
        objects.forEach(System.out::println);
    }

    public static void dfsTree(Tree tree, ArrayList<Integer> da) {
        Node root = tree.root;
        dfsNodeRecursion(root, da);
    }

    private static void dfsNodeRecursion(Node node, ArrayList<Integer> da) {
        if (node == null) {
            return;
        }
        da.add(node.val);
        if (node.left != null) {
            dfsNodeRecursion(node.left, da);
        }
        if (node.right != null) {
            dfsNodeRecursion(node.right, da);
        }
    }

}
