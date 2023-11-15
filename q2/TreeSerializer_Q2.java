package q2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeSerializer_Q2 implements TreeSerializer {
    private Set<Node> visited = new HashSet<>();
    public TreeSerializer_Q2() {}

    /*
     * Uses pre-order traversal and seperates the node numbers by "," if the
     * node is null then "n" will be used to represent the null node. Also checks
     * if node has been visited and throws exception if it has.
     */
    public String serialize(Node root) {
        if(root == null) {
            return "n,";
        }
        if(visited.contains(root)) {
            throw new RuntimeException("Cyclic connection found in tree");
        }
        visited.add(root);
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
        // To test for cycle
        Node cycleNode = new Node(1);
        root.right = cycleNode;
        root.left.right.right = cycleNode;
        root.right.right = new Node(28);

        TreeSerializer_Q2 treeSerializer = new TreeSerializer_Q2();
        String serialized = treeSerializer.serialize(root);

        System.out.println(serialized);
        Node deserialized = treeSerializer.deserialize(serialized);
        System.out.println(Node.equals(root, deserialized));
    }
}
