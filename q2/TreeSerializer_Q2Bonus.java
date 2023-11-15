package q2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeSerializer_Q2Bonus implements TreeSerializer {
    private Map<Node, Integer> nodeToId = new HashMap<>();
    private int nextId = 0;
    
    public TreeSerializer_Q2Bonus() {}

    /*
     * Uses pre-order traversal and seperates the node numbers by "," if the
     * node is null then "n" will be used to represent the null node. 
     * 
     * In addition to including the node number in the serialization there will 
     * be a unique id for each unique node with a ":" seperating them as well to 
     * help serialize when there are cycles. 
     * 
     * Checks if node has been visited and if it has add "c" to the beginning of 
     * the string to flag as a cycle node.
     */
    public String serialize(Node root) {
        if(root == null) {
            return "n,";
        }
        if(nodeToId.containsKey(root)) {
            return "c" + nodeToId.get(root) + ":" + root.num + ",";
        }
        int id = nextId++;
        nodeToId.put(root, id);
        return id + ":" + root.num + "," + serialize(root.left) + serialize(root.right);
    }
    
    /*
     * Seperates the serializeed string by the "," separator passes it to a helper to deserialize.
     * 
     * Initialize empty id to node mapping to help with deserialization when there are cycles.
     */
    public Node deserialize(String str) {
        Map<Integer, Node> idToNode = new HashMap<>();
        List<String> nodes = new ArrayList<>(Arrays.asList(str.split(",")));
        return deserializerHelper(nodes, idToNode);
    }

    /*
     * Checks and removes first element of the list of strings, if the strings is "n"
     * then it returns null. Otherwise it will perform a preorder traversal and create a 
     * node of the value and then recursively creates the left and right subtrees.
     * 
     * If node is part of cycle it will use the id to get the node from the id to mapping cycle.
     */
    private Node deserializerHelper(List<String> nodes, Map<Integer, Node> idToNode) {
        String curr = nodes.get(0);
        nodes.remove(0);
        if(curr.equals("n")) {
            return null;
        }
        if(curr.startsWith("c")) {
            String[] idAndNum = curr.split(":");
            int id = Integer.parseInt(idAndNum[0].substring(1));
            return idToNode.get(id);
        }
        String[] idAndNum = curr.split(":");
        int id = Integer.parseInt(idAndNum[0]);
        int num = Integer.parseInt(idAndNum[1]);
        Node node = new Node(num);
        idToNode.put(id, node);
        node.left = deserializerHelper(nodes, idToNode);
        node.right = deserializerHelper(nodes, idToNode);
        return node;
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.left.left = new Node(7);
        root.left.right = new Node(5);
        root.left.left.left = new Node(4);
        Node cycleNode = new Node(1);
        root.right = cycleNode;
        root.left.right.right = cycleNode;
        root.right.right = new Node(28);

        TreeSerializer_Q2Bonus treeSerializer = new TreeSerializer_Q2Bonus();
        String serialized = treeSerializer.serialize(root);

        System.out.println(serialized);
        Node deserialized = treeSerializer.deserialize(serialized);
        System.out.println(treeSerializer.serialize(deserialized));
    }
}
