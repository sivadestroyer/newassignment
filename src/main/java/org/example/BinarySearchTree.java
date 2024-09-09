
package org.example;
import java.util.List;
import java.util.ArrayList;
public class BinarySearchTree<T extends Comparable<T>> {

    private class Node {
        T data;
        Node left;
        Node right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        Node(Node left, T data, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    Node root;

    public BinarySearchTree() {

        this.root = null;
    }

    public void insert(T data) {
        Node newnode = new Node(data);

        if (root == null) {
            root = newnode;
            return;
        }

        Node current = root;
        Node parent = null;

        while (true) {
            parent = current;
            int comparison = data.compareTo(current.data);
            if (comparison < 0) {
                current = current.left;
                if (current == null) {
                    parent.left = newnode;
                    return;
                }
            } else if (comparison > 0) {
                current = current.right;
                if (current == null) {
                    parent.right = newnode;
                    return;
                }
            } else {
                // Handle duplicates (if needed)
                return;
            }
        }
    }

    public List<T> inorder() {
        List<T> result = new ArrayList<>();
        inorderHelp(root,result);
        return result;
    }

    private void inorderHelp(Node current, List<T> result) {

        if (current == null) return;
        inorderHelp(current.left,result);
        result.add(current.data);
        inorderHelp(current.right,result);

    }

    public List<T> preorder() {
        List<T> result = new ArrayList<>();
        preorderhelp(root,result);
        return result;
    }

    private void preorderhelp(Node current, List<T> result) {
        if (current != null) {
            result.add(current.data);
            preorderhelp(current.left,result);
            preorderhelp(current.right,result);
        }

    }

    public List<T> postorder() {
        List<T> result = new ArrayList<>();
        postorderhelp(root,result);
        return result;
    }

    private void postorderhelp(Node current,List<T> result) {
        if (current != null) {
            postorderhelp(current.left,result);
            postorderhelp(current.right,result);
            result.add(current.data);
        }
    }

    public Boolean find(T data) {
        Node current = root;
        while (current != null) {
            int comparison = data.compareTo(current.data);
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {

                return true;
            }
        }
        return false;
    }

    public void delete(T data) {
        root = deleteNode(root, data);
    }

    private Node deleteNode(Node root, T data) {
        if (root == null) return root;
        int comparison = data.compareTo(root.data);
        if (comparison < 0) {
            root.left = deleteNode(root.left, data);
        } else if (comparison > 0) {
            root.right = deleteNode(root.right, data);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null)
                return root.left;

            root.data = minValue(root.right);
            root.right = deleteNode(root.right, root.data);
        }
        return root;
    }

    private T minValue(Node node) {
        T minValue = node.data;
        while (node.left != null) {
            minValue = node.left.data;
            node = node.left;
        }
        return minValue;
    }

    public T findmin() {
        Node current = root;
        T minvalue = current.data;
        while (current.left != null) {
            minvalue = current.left.data;
            current = current.left;
        }
        return minvalue;
    }

    public T findmax() {
        Node current = root;
        T maxvalue = current.data;
        while (current.right != null) {
            maxvalue = current.right.data;
            current = current.right;
        }
        return maxvalue;
    }

    public int height() {
        return height(root);
    }

    private int height(Node root) {
        if (root == null) {
            return 0;
        }
        int leftHeight = height(root.left);
        int rightHeight = height(root.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public Boolean balance() {
        int left = leftHeight(root);
        int right = rightHeight(root);
        return Math.abs(left - right) <= 1;
    }

    private int leftHeight(Node current) {
        if (current == null) {
            return 0;
        }
        return leftHeight(current.left) + 1;
    }

    private int rightHeight(Node current) {
        if (current == null) {
            return 0;
        }
        return rightHeight(current.right) + 1;
    }

    public boolean isBst() {
        return isBst(root);
    }

    private boolean isBst(Node node) {
        T current = node.data;
        while (node.left != null) {
            int comparison = current.compareTo(node.left.data);
            if (comparison < 0) {
                return false;
            }
            node = node.left;
        }
        while (node.right != null) {
            int comparison = current.compareTo(node.right.data);
            if (comparison > 0) {
                return false;
            }
            node = node.right;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int n : nums) {
            bst.insert(n);
        }

        //prints sorted numbers
        //inorder traversing , prints number in sorted order
        bst.inorder();
        bst.postorder();
        bst.preorder();
        bst.find(25);
        bst.findmin();
        bst.findmax();
        bst.height();
        bst.balance();
        bst.delete(40);
    }
}
