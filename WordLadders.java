import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Class represents a WordLadder
 * @author Vo Linh Chi Dao
 */
public class WordLadders{

    /**
     * Execute the WordLadders program
     * @param args "word graph" representation of the game
     */
    public static void main(String[] args){
        start(args[0]);
    }

    /**
     * Construct a graph from the given file
     * @param filename the text file that will be converted into a graph
     * @return a graph created based on the input file
     */
    public static Graph<Integer,String> readWordGraph(String filename){

        try{
            // Pass the file to the reader
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Store the current line of the text
            String current = reader.readLine();

            // Initialize a graph
            Graph<Integer,String> result = new Graph<>();

            // Trace through each line of the text
            while(current != null){

                // Initialize nodeList storing the name of the node
                ArrayList<Integer> nodeList = new ArrayList<>();

                // Initialize variable that data of the noe
                String dataNode = "";

                // Modify the current line
                current = current.trim();
                String[] line = current.split(" ");

                // Trace through each line to store the node name and its edges
                for(int i = 0; i < line.length; i++){
                    // Index 1 store the data of the node
                    if(i == 1){
                        dataNode = line[i];
                    }
                    // Other index store the name of the node and its adjacent nodes
                    else{
                        nodeList.add(Integer.parseInt(line[i]));
                    }
                }

                // Initialize list of name stored in each node
                Integer[] nodeName = nodeList.toArray(new Integer[0]);

                // Add its edge to the graph
                result.addEdges(nodeName[0],nodeName);
                // Add data of the node
                result.getAdj().get(result.findNode(nodeName[0])).get(0).setValue(dataNode);

                //Update to the next line
                current = reader.readLine();
            }
            return result;
        }
        catch(IOException e){
            throw new RuntimeException();
        }
    }

    /**
     * Create Hashtable for the nodes in the "word graph" file
     * The String data of the node will the key, while the Integer
     * name of the node will be the data
     * @param filename the "word graph" file
     */
    protected static Hashtable<String,Integer> hashGraph(String filename){

        try{
            // Pass the file to the reader
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Store the current line of the text
            String current = reader.readLine();

            // Initialize HashTable storing nodes of graph input
            Hashtable<String,Integer> hashtable = new Hashtable<>();

            // Initialize nameList storing the name of the node
            ArrayList<Integer> nameList = new ArrayList<>();

            // Initialize dataList storing the data of the node
            ArrayList<String> dataList = new ArrayList<>();

            // Trace through each line of the text
            while(current != null){

                // Modify the current line
                current = current.trim();
                String[] line = current.split(" ");

                // Store the node name and data into the two lists
                if(line.length >= 2){
                    nameList.add(Integer.parseInt(line[0]));
                    dataList.add(line[1]);
                }

                // Update to the next line
                current = reader.readLine();
            }

            // Store each node into the hashTable
            for(int i = 0; i < nameList.size(); i++){
                hashtable.put(dataList.get(i), nameList.get(i));
            }

            return hashtable;
        }
        catch(IOException e){
            throw new RuntimeException();
        }
    }

    /**
     * Generate the wordLadders program and queries for the users
     * @param filename the "word graph" representation of the wordLadder,
     *                 where the user inputs will be processed
     */
    private static void start(String filename){

        // The graph representation of the input "word graph" file
        Graph<Integer, String> wordGraph = readWordGraph(filename);

        // The hash table storing the nodes of the graph
        Hashtable<String, Integer> hashtable = hashGraph(filename);

        // Query for the user input
        Scanner scanner1 = new Scanner(System.in);

        System.out.println("WELCOME TO WORD LADDERS!");
        // Ask user if they want to end the program
        System.out.println("Type 'END' to exit the program: ");
        String status = scanner1.nextLine();

        // Query for the user input until they stop the program
        while(!status.equals("END")){

            // Ask for the first node
            System.out.println("Enter the first word: ");
            String word1 = scanner1.nextLine();
            // Ask for the second node
            System.out.println("Enter the second word: ");
            String word2 = scanner1.nextLine();
            // Ask for the search method
            System.out.println("Enter the search method (BFS or DFS): ");
            String search = scanner1.nextLine();

            // Check if user input is not null
            if(word1 != null && word2 != null){

                // Name of the two nodes
                Integer node1 = hashtable.get(word1);
                Integer node2 = hashtable.get(word2);

                ArrayList<String> dataList = new ArrayList<>();

                // Perform appropriate search method: BFS or DFS
                if(search.equals("BFS")){
                    dataList = wordGraph.dataList(wordGraph.BFS(node1,node2));
                }
                else if(search.equals("DFS")){
                    dataList = wordGraph.dataList(wordGraph.DFS(node1,node2));
                }

                if(!dataList.isEmpty()){
                    System.out.println(Arrays.toString(dataList.toArray()));
                }
            }

            // Ask user if they want to end the program
            System.out.println("Type 'END' to exit the program: ");
            status = scanner1.nextLine();
        }
    }

}
