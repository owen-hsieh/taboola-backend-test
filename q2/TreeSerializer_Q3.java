package q2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/*
 * Generic Node class
 */
class NodeG<T> {
    NodeG<T> left;
    NodeG<T> right;
    T value;

    NodeG(T value) {
        this.value = value;
    }

    public static <T> boolean equals(NodeG<T> p, NodeG<T> q) {
        if(p == null && q == null) {
            return true;
        } else if(p == null && q != null) {
            return false;
        } else if(p != null && q == null) {
            return false;
        }
        if(!p.value.equals(q.value)) {
            return false;
        }
        return equals(p.left, q.left) && equals(p.right, q.right);
    }
}

public class TreeSerializer_Q3<T> {
    private Map<NodeG<T>, Integer> nodeToId = new HashMap<>();
    private int nextId = 0;
    
    public TreeSerializer_Q3() {}

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
     * 
     * Make sure that the object type T type has a .toString() method for serialization.
     */
    public String serialize(NodeG<T> root) {
        if(root == null) {
            return "n,";
        }
        if(nodeToId.containsKey(root)) {
            return "c" + nodeToId.get(root) + ":" + root.value + ",";
        }
        int id = nextId++;
        nodeToId.put(root, id);
        return id + ":" + root.value + "," + serialize(root.left) + serialize(root.right);
    }
    
    /*
     * Seperates the serializeed string by the "," separator passes it to a helper to deserialize.
     * 
     * Initialize empty id to node mapping to help with deserialization when there are cycles.
     * 
     * Have method take in deserializer parameter to convert string back into object type T
     */
    public NodeG<T> deserialize(String str, Function<String, T> deserializer) {
        Map<Integer, NodeG<T>> idToNode = new HashMap<>();
        List<String> nodes = new ArrayList<>(Arrays.asList(str.split(",")));
        return deserializerHelper(nodes, idToNode, deserializer);
    }

    /*
     * Checks and removes first element of the list of strings, if the strings is "n"
     * then it returns null. Otherwise it will perform a preorder traversal and create a 
     * node of the value and then recursively creates the left and right subtrees.
     * 
     * If node is part of cycle it will use the id to get the node from the id to mapping cycle.
     */
    private NodeG<T> deserializerHelper(List<String> nodes, Map<Integer, NodeG<T>> idToNode, Function<String, T> deserializer) {
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
        T value = deserializer.apply(idAndNum[1]);
        NodeG<T> node = new NodeG<T>(value);
        idToNode.put(id, node);
        node.left = deserializerHelper(nodes, idToNode, deserializer);
        node.right = deserializerHelper(nodes, idToNode, deserializer);
        return node;
    }

    public static void main(String[] args) {
        NodeG<String> root = new NodeG<String>("1");
        root.left = new NodeG<String>("2");
        root.left.left = new NodeG<String>("7");
        root.left.right = new NodeG<String>("5");
        root.left.left.left = new NodeG<String>("4");
        root.right = new NodeG<String>("1");
        root.right.right = new NodeG<String>("28");
        
        TreeSerializer_Q3<String> treeSerializer = new TreeSerializer_Q3<>();
        String serialized = treeSerializer.serialize(root);

        System.out.println(serialized);
        Function<String, String> deserializer = (str) -> {return str;} ;
        NodeG<String> deserialized = treeSerializer.deserialize(serialized, deserializer);
        System.out.println(treeSerializer.serialize(deserialized));
        System.out.println(NodeG.equals(root,deserialized));
    }
}