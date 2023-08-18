package com.powersi.midd.algorithm;


import java.util.*;

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
        dfsTreeRecursion(tree, objects);
        objects.forEach(System.out::println);
        dfsStack(tree).forEach(System.out::println);
//        ArrayList<Integer> bfsTreeRecursion = new ArrayList<>();
//        bfsTreeRecursion(tree, bfsTreeRecursion);
//        bfsTreeRecursion.forEach(System.out::println);
    }

    public static void dfsTreeRecursion(Tree tree, ArrayList<Integer> da) {
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

    private static List<Integer> dfsStack(Tree tree) {
        List<Integer> arrayList = new ArrayList();
        Stack<Node> stack = new Stack();
        stack.push(tree.root);
        while (!stack.empty()) {
            Node node = stack.pop();
            if (node.right!=null){
                stack.push(node.right);
            }
            if (node.left!=null){
                stack.push(node.left);
            }
            arrayList.add(node.val);
        }
        return arrayList;
    }

    private static void bfsTreeRecursion(Tree tree, ArrayList<Integer> bfsTreeRecursion) {
        Node root = tree.root;
        bfsNodeRecursion(root, 0, bfsTreeRecursion);
    }

    private static void bfsNodeRecursion(Node node, int level, ArrayList<Integer> bfsTreeRecursion) {
        if (node == null) {
            return;
        }
        if (level == 0) {
            bfsTreeRecursion.add(node.val);
        }


    }

}
