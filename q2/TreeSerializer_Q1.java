package q2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeSerializer_Q1 implements TreeSerializer {
    public TreeSerializer_Q1() {}

    /*
     * Uses pre-order traversal and seperates the node numbers by "," if the
     * node is null then "n" will be used to represent the null node.
     */
    public String serialize(Node root) {   
        if(root == null) {
            return "n,";
        }
        return root.num + "," + serialize(root.left) + serialize(root.right);
    }
    
    /*
     * Seperates the serializeed string by the "," separator passes it to a helper to deserialize.
     */
    public Node deserialize(String str) {
        List<String> nodes = new ArrayList<>(Arrays.asList(str.split(",")));
        return deserializerHelper(nodes);
    }

    /*
     * Checks and removes first element of the list of strings, if the strings is "n"
     * then it returns null. Otherwise it will perform a preorder traversal and create a 
     * node of the value and then recursively creates the left and right subtrees.
     */
    private Node deserializerHelper(List<String> nodes) {
        String curr = nodes.get(0);
        nodes.remove(0);
        if(curr.equals("n")) {
            return null;
        }
        Node node = new Node(Integer.parseInt(curr));
        node.left = deserializerHelper(nodes);
        node.right = deserializerHelper(nodes);
        return node;
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.left.left = new Node(7);
        root.left.right = new Node(5);
        root.left.left.left = new Node(4);
        root.right = new Node(1);
        root.right.right = new Node(28);

        TreeSerializer_Q1 treeSerializer = new TreeSerializer_Q1();
        String serialized = treeSerializer.serialize(root);

        System.out.println(serialized);
        Node deserialized = treeSerializer.deserialize(serialized);
        System.out.println(treeSerializer.serialize(deserialized));
        System.out.println(Node.equals(root, deserialized));
    }
}
