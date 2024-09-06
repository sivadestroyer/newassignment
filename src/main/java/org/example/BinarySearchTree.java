package org.example;

public class BinarySearchTree< T extends Comparable<T>> {

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

        this.root= null;
    }

    public void insert(T data) {
        Node newnode = new Node(data);
        if(root== null){
            root=newnode;
            return;
        }
        Node current=root;
        Node parent= null;
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

    public void inorder() {
        inorderhelp(root);
        System.out.println();
    }
    private void inorderhelp(Node current) {
        if (current!= null) {
            inorderhelp(current.left);
            System.out.print(current.data + " ");
            inorderhelp(current.right);
        }
    }

    public void preorder() {
        preorderhelp(root);
        System.out.println();
    }
 private void preorderhelp(Node current) {
        if (current!= null) {
            System.out.print(current.data + " ");
            preorderhelp(current.left);
            preorderhelp(current.right);
        }

 }

    public void postorder() {
        postorderhelp(root);
        System.out.println();
    }
    private void postorderhelp(Node current) {
        if (current!= null) {
            postorderhelp(current.left);
            postorderhelp(current.right);
            System.out.print(current.data + " ");
        }
    }

    public void find(T data){
        Node current = root;
        while (current!= null) {
            int comparison = data.compareTo(current.data);
            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                System.out.println(data + " found");
                return;
            }
        }
        System.out.println(data + " not found");
    }
    public void delete(T data){
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
            if (root.left == null){
                return root.right;}
            else if (root.right == null)
                return root.left;

            root.data = minValue(root.right);
            root.right = deleteNode(root.right, root.data);
        }
        return root;
    }
    private T minValue(Node node) {
        T minValue = node.data;
        while (node.left!= null) {
            minValue = node.left.data;
            node = node.left;
        }
        return minValue;
    }
    public void findmin(){
        Node current =root;
        T minvalue= current.data;
        while (current.left!=null){
            minvalue = current.left.data;
            current=current.left;
        }
        System.out.println("Minimum value is " + minvalue);
    }

    public void findmax(){
        Node current=root;
        T maxvalue= current.data;
        while(current.right!=null){
            maxvalue=current.right.data;
            current=current.right;
        }
        System.out.println("maximum value is " + maxvalue);
    }
    public void height(){
        System.out.println("Height of the tree is " + height(root));
    }
    private int height(Node root){
        if(root==null){
            return 0;
        }
        int leftHeight=height(root.left);
        int rightHeight=height(root.right);
        return Math.max(leftHeight,rightHeight)+1;
    }
    public void balance(){
        int left=leftHeight(root);
        int right=rightHeight(root);
        if (Math.abs(left-right)<=1){
            System.out.println("balanced");
        } else {
            System.out.println("unbalanced");
        }
    }
    private int leftHeight(Node current){
        if(current==null){
            return 0;
        }
        return leftHeight(current.left)+1;
    }
    private int rightHeight(Node current){
        if (current==null){
            return 0;
        }
        return rightHeight(current.right)+1;
    }
    public void isBst(){
        isBst(root);
    }
    private void isBst(Node node){
        T current=node.data;
        while(node.left!= null){
            int comparison=current.compareTo(node.left.data);
            if (comparison<0){
                System.out.println("not balanced");
            }
            node=node.left;
        }
        while(node.right!=null){
            int comparison=current.compareTo(node.right.data);
            if (comparison>0){
                System.out.println("not balanced");
            }
            node=node.right;
        }
    }
  public static void main(String[] args) {
        int[] nums = {14, 2, 40, 9, 31, 25, 18,5};

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for(int n : nums ) {
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
