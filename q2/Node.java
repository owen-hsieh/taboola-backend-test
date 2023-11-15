package q2;

public class Node {
    Node left;
    Node right;
    int num;

    public Node(int num) {
        this.num = num;
    }

    public static boolean equals(Node p, Node q) {
        if(p == null && q == null) {
            return true;
        } else if(p == null && q != null) {
            return false;
        } else if(p != null && q == null) {
            return false;
        }
        if(p.num != q.num) {
            return false;
        }
        return equals(p.left, q.left) && equals(p.right, q.right);
    }
}