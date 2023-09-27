import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Class representing undirected, unweighted graph
 * @param <K> reference type to the node in the graph
 * @param <V> type of data stored in the node
 * @author Vo Linh Chi Dao
 */
public class Graph<K,V>{

    /**
     * Class represents a graph node
     */
    protected class Node{

        /**
         * Reference to the node
         */
        private final K key;

        /**
         * Value of the node
         */
        private V value;

        /**
         * Constructor initializes a node
         * @param key reference to the node
         * @param value value of the node
         */
        public Node(K key, V value){
            this.key = key;
            this.value = value;
        }

        /**
         *
         * @return the reference name of the node
         */
        protected K getKey(){
            return key;
        }

        /**
         *
         * @return the value of the node
         */
        protected V getValue(){
            return value;
        }

        /**
         * Change the value of the node to the input
         * @param value the new value of the node
         */
        protected void setValue(V value){
            this.value = value;
        }
    }

    /**
     * Number of nodes in the graph
     */
    private int numNodes;

    /**
     * Adjacency list contains nodes and edges associated with the nodes
     */
    private final ArrayList<ArrayList<Node>> adj;

    /**
     * Constructor initialize an empty graph
     */
    public Graph(){
        this.numNodes = 0;
        this.adj = new ArrayList<>();
    }

    /**
     * Add a node to the graph and check for duplicates
     * Duplicated node can be defined as node with same name/reference.
     * @param name reference to the new node
     * @param data value stored in the new node
     * @return true if the node is added to the graph
     */
    public boolean addNode(K name, V data){

        // If the graph is currently empty, add the specified node
        if(getAdj().isEmpty()){
           getAdj().add(new ArrayList<>());
           getAdj().get(0).add(new Node(name,data));
           // Update number of nodes
           updateNumNodes();
           return true;
        }

        // Return the index of the pre-existing node
        int i = findNode(name);

        // If the node does not exist in the graph, add it
        if(i < 0){
            // Initialize the new node
            Node newNode = new Node(name,data);
            getAdj().add(new ArrayList<>());
            getAdj().get(getAdj().size() - 1).add(newNode);
            // Update number of nodes
            updateNumNodes();
            return true;
        }

        // If node already existed in the graph, ignore
        return false;
    }

    /**
     * Add a list of nodes to the graph and check for duplicates
     * @param names list of names of the nodes
     * @param data list of values of the nodes
     * @return true if all nodes in the list are added to the graph
     * @throws IllegalArgumentException if the length of two input arrays are not equal
     */
    public boolean addNodes(K[] names,V[] data){

        // If the lists of names and values do not have equal length, throw an exception
        if(names.length != data.length){
            throw new IllegalArgumentException("The length of names and data lists does not match");
        }

        // Indicate number of nodes in the list has been added to the graph
        int numAdded = 0;

        // Trace through the list of names and data
        for(int i = 0; i < names.length; i++){
            // Increment numAdded if a node is successfully added
            if(addNode(names[i],data[i])){
                numAdded++;
            }
        }

        return numAdded == names.length;
    }

    /**
     * Add undirected edge between two specified nodes.
     * If specified node does not exist, add it to the graph, then
     * add the edge between them
     * @param from the source node
     * @param to the destination node
     * @return true if an edge is added between two nodes
     */
    public boolean addEdge(K from,K to){

        // If source node does not exist, add it to the graph
        if(findNode(from) < 0){
            addNode(from,null);
        }
        // If destination node does not exist, add it to the graph
        if(findNode(to) < 0){
            addNode(to,null);
        }

        // Store index of node from
        int i = findNode(from);
        // Store index of node to
        int j = findNode(to);

        // Add the edge between two existing nodes in the graph
        // If there is already an edge between two nodes, return false
        if(checkEdges(getAdj().get(i),to)){
            return false;
        }
        // Node from
        Node source = getAdj().get(i).get(0);
        // Node to
        Node destination = getAdj().get(j).get(0);
        // Add edge between the two nodes
        getAdj().get(i).add(destination);
        getAdj().get(j).add(source);
        return true;
    }

    /**
     * Add undirected edges between node from and a list of "to" nodes
     * @param from the source node
     * @param toList list contain destination nodes
     * @return true if source node is connected to all destination nodes in the list
     */
    @SafeVarargs
    public final boolean addEdges(K from, K... toList){

        // Indicate number of nodes in the list that has been connected to source node
        int numAdded = 0;

        // Connect each value with the source node
        for(K value : toList){
            // Increment numAdded if an edge is successfully formed
            if(addEdge(from,value)){
                numAdded++;
            }
        }
        return numAdded == toList.length;
    }

    /**
     * Remove a node along all connected edges
     * @param name reference of the removed node
     * @return true is the node is removed
     */
    public boolean removeNode(K name){

        // Index of the removed node
        int i = findNode(name);

        // If the node exist in the graph
        if(i > -1){
           // Store the removed node and its edge
           ArrayList<Node> removed = getAdj().remove(i);
           // Update number of nodes
           updateNumNodes();
           // Number of adjacent node with removed node, excluding the node itself
           int numAdj = removed.size() - 1;
           // Trace through adjacency list to remove the edge associated with removed node
           for(ArrayList<Node> current : getAdj()){
               if(current.remove(removed.get(0))){
                   numAdj--;
               }
           }
           return numAdj == 0;
        }

        return false;
    }

    /**
     * Remove each node in specified list of nodes and their edges from the graph
     * @param nodeList specified list of node to be removed
     * @return true when all nodes in the list and their edges are removed from the graph
     */
    @SafeVarargs
    public final boolean removeNodes(K... nodeList){

        // Number of node in the list
        int numNode = 0;

        // Remove each node in the nodeList and its edge from the graph
        for(K k : nodeList){
            // Increment numNode if the node is successfully removed to the graph
            if(removeNode(k)){
                numNode++;
            }
        }

        return numNode == nodeList.length;
    }

    /**
     * Print the adjacency list of node names and their neighbors
     */
    public void printGraph(){

        // Trace through each node in the graph
        for(ArrayList<Node> nodes : getAdj()){
            // Print the node and its edge
            for(Node node : nodes){
                System.out.print(node.getKey());
                System.out.print(" ");
            }
            System.out.print('\n');
        }
        System.out.print('\n');
    }

    /**
     * Depth-first search between nodes from and to
     * @param from the source node
     * @param to the destination node
     * @return an array representing the path between source node and
     * destination node. Return an empty array if one of the two nodes does
     * not exist in the graph or there is no path between two nodes
     */
    public K[] DFS(K from,K to){

        // Check if the two nodes exist in the graph
        if(findNode(from) < 0 || findNode(to) < 0){
            System.out.println("One of the node does not exist in the graph");
            return (K[]) new Object[0];
        }

        // Store the path between two nodes, in reverse
        ArrayList<K> result = new ArrayList<>();
        // Store the current path
        Stack<K> stack = new Stack<>();
        // Indicate if each node is visited
        boolean[] visited = new boolean[getNumNodes()];

        dfsHelper(result,stack,visited,from,to);

        // Store the result path
        K[] path = (K[]) new Object[result.size()];
        for(int i = 0; i < path.length; i++){
            path[i] = result.get(result.size() - i - 1);
        }

        // If the destination node is not reached
        if(path.length == 0){
            System.out.println("There is no path between " + from + " and " + to);
        }

        return path;
    }

    /**
     * Breadth-first search between node from and to
     * @param from the source node
     * @param to the destination node
     * @return an array representing one of the path between source node
     * and destination node. Return an empty array if one of the two nodes does
     * not exist in the graph or there is no path between two nodes
     */
    public K[] BFS(K from,K to){

        // Check if the two nodes exist in the graph
        if(findNode(from) < 0 || findNode(to) < 0){
            System.out.println("One of the node does not exist in the graph");
            return (K[]) new Object[0];
        }

        // Store the path between two nodes
        ArrayList<K> result = new ArrayList<>();
        // Store the current path
        Queue<K> queue = new LinkedList<>();
        // Indicate if each node is visited
        boolean[] visited = new boolean[getNumNodes()];

        bfsHelper(result,queue,visited,from,to);

        // If the destination node is not reached
        if(!result.get(result.size() - 1).equals(to)){
            System.out.println("There is no path between " + from + " and " + to);
            return (K[]) new Object[0];
        }

        return result.toArray((K[]) new Object[result.size()]);
    }

    /**
     * Construct a graph from the specified text file
     * @param filename the corresponding adjacency list
     * @return a graph object based on the specified input file
     * @param <V> type of the data stored in each node of the graph
     */
    public static <V> Graph<String,V> read(String filename){

        try{
            // Pass the file to the reader
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Store the current line of the text
            String current = reader.readLine();

            // Initialize a graph
            Graph<String,V> result = new Graph<>();

            // Trace through each line of the text
            while(current != null){

                // Initialize nodeList
                ArrayList<String> nodeList = new ArrayList<>();

                // Modify the current line
                current = current.trim();
                String[] line = current.split(" ");

                // Add each alphanumeric word in the line into the nodeList
                for(String s : line){
                    addAlphaNumeric(nodeList,s);
                }

                // Initialize list of name of each node
                String[] nodeName = nodeList.toArray(new String[0]);

                // Add nodes and edge in the associated nodeList
                result.addEdges(nodeName[0],nodeName);

                // Update to the next line
                current = reader.readLine();
            }
            return result;
        }
        // Throw an error if the input file is inappropriate
        catch(IOException e){
            throw new RuntimeException();
        }
    }

    /**
     * Retrieve the adjacency list of the graph
     * @return the adjacency list of the graph
     */
    protected ArrayList<ArrayList<Node>> getAdj(){
        return this.adj;
    }

    /**
     * Retrieve number of nodes in the graph
     * @return number of nodes in the graph
     */
    protected int getNumNodes(){
        return numNodes;
    }

    /**
     * Update the number of nodes in the graph
     */
    protected void updateNumNodes(){
        this.numNodes = adj.size();
    }

    /**
     * Search for the node with specified name in the adjacency list
     * @param name reference of the node
     * @return the index where the node is located.
     * Return -1 if the node does not exist in the graph
     */
    protected int findNode(K name){

        // Store the index of the node
        int i;

        // Trace through the adjacency list
        for(i = 0; i < getAdj().size(); i++){
            // Return the index where the node is found
            if(getAdj().get(i).get(0).getKey().equals(name)){
                return i;
            }
        }

        // Return -1 if node does not exist
        return -1;
    }

    /**
     * Check if an edge already existed between two nodes
     * @param list store the source node
     * @param name the node that form edge with the source node
     * @return true if the edge already existed between the two nodes
     */
    protected boolean checkEdges(ArrayList<Node> list, K name){

       // Compare each connected node with the specified node
       for(Node node : list){
           // If an edge already existed, return true
           if(node.getKey().equals(name)){
               return true;
           }
       }

       // If the edge does not exist, return false
       return false;
    }

    /**
     * Retrieve list of data stored in each node of the graph
     * @return list of data stored in each node of the graph
     */
    protected ArrayList<V> dataList(K[] nameList){

        // Initialize the dataList
        ArrayList<V> data = new ArrayList<>();

        // Paste each data of the node to the list
        for(K name : nameList){
            data.add(getAdj().get(findNode(name)).get(0).getValue());
        }

        return data;
    }

    /**
     * Helper method for DFS
     * @param result list representing the path between two nodes
     * @param stack store the current path
     * @param visited indicate if each node is visited
     * @param from the source node
     * @param to the destination node
     * @return true there is a path between two nodes
     */
    private boolean dfsHelper(ArrayList<K> result, Stack<K> stack, boolean[] visited, K from, K to){

        // Indicate the source node has been visited
        visited[findNode(from)] = true;
        // Add source node to the stack
        stack.add(from);

        // Base case: If the destination node is reached, return true
        if(from.equals(to)){
            result.add(stack.pop());
            return true;
        }

        // Trace through the unvisited adjacent node to the source node
        ArrayList<Node> current = getAdj().get(findNode(from));
        for(Node node : current){
            if(!visited[findNode(node.getKey())]){
                // Recursively perform DFS on each adjacent node of the source node
                if(dfsHelper(result,stack, visited,node.getKey(),to)){
                    // Return true when the path between the two nodes have been found
                    result.add(stack.pop());
                    return true;
                }
            }
        }

        // If the node does not provide path to the destination, return false
        stack.pop();
        return false;
    }

    /**
     * Helper method for BFS
     * @param result list representing the path between two nodes
     * @param queue store the current path
     * @param visited indicate if each node is visited
     * @param from the source node
     * @param to the destination node
     */
    private void bfsHelper(ArrayList<K> result, Queue<K> queue, boolean[] visited, K from, K to){

        // Indicate the source node has been visited
        visited[findNode(from)] = true;
        // Add the source node to the queue
        queue.add(from);

        // Trace through the graph until the queue is empty
        while(!queue.isEmpty()){
            // Add current node to the path
            K current = queue.poll();
            result.add(current);

            // Return if the reaches the destination node
            if(current.equals(to)){
                return;
            }

            // The nodeList contain the current node and its adjacent nodes
            ArrayList<Node> list = getAdj().get(findNode(current));
            // Add unvisited adjacent node of the current node to the queue
            for (Node node : list){
                if(!visited[findNode(node.getKey())]){
                    visited[findNode(node.getKey())] = true;
                    queue.add(node.getKey());
                }
            }
        }
    }

    /**
     * Decompose a line of string sequence into set of alphaNumeric words
     * and add those words to the list. Alphanumeric word is defined as a word without
     * non-numeric and non-alphabetic characters
     * @param list the list where the alphanumeric words will be added to
     * @param word the sequence that contains alphanumeric words to be
     *             added to the list
     */
    private static void addAlphaNumeric(ArrayList<String> list,String word){

        // Store the result normalized word
        StringBuilder result = new StringBuilder();

        // Trace through each character of the word
        for(int i = 0; i < word.length(); i++){
            // Current character of the word
            char c = word.charAt(i);

            // Only add alphabetic or numeric character
            if(Character.isLetterOrDigit(c)){
                result.append(c);
            }

            // When approach the end of the line or a space, add the alphaNumeric word
            // to the list
            if(i + 1 == word.length() || !Character.isLetterOrDigit(c)){
                if(result.length() != 0){
                    list.add(result.toString());
                    result = new StringBuilder();
                }
            }
        }
    }

}
