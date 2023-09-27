import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

/**
 * Testing class for Graph
 * @author Vo Linh Chi Dao
 */
public class GraphTest{

    String[] s1 = {"aaa","bbb","cdf","ahs","bbb","csds"};
    String[] s2 = {"233","csds","ta","ahs","abcd-ef","chi","stats"};
    Double[] d = {10.0,57.0,45.6,44.3,300.2,20.97};
    Integer[] t1 = {5,1,20,14,35,77,100};

    /**
     * Test addNode method
     */
    @Test
    public void addNode(){

        Graph<String,Double> g1 = new Graph<>();
        Graph<Integer,String> g2 = new Graph<>();

        // Add nodes to the graph
        assertTrue(g1.addNode("aaa",5.5));
        assertTrue(g1.addNode("bbb",10.0));
        assertTrue(g1.addNode("cdf",32.0));
        assertTrue(g1.addNode("ahs",100.3));
        // Add duplicate node into graph
        assertFalse(g1.addNode("bbb",5.5));
        assertEquals(4,g1.getNumNodes());
        System.out.println("Nodes in graph 1: ");
        g1.printGraph();

        // Add nodes to the graph
        assertTrue(g2.addNode(5,"233"));
        assertTrue(g2.addNode(1,"CSDS"));
        assertTrue(g2.addNode(20,"TA"));
        assertTrue(g2.addNode(14,"ahs"));
        // Add duplicate node into graph
        assertFalse(g2.addNode(20,"abc-def"));
        assertEquals(4,g2.getNumNodes());
        System.out.println("Nodes in graph 2: ");
        g2.printGraph();
    }

    /**
     * Test addNodes method
     */
    @Test (expected = IllegalArgumentException.class)
    public void addNodes(){

        Graph<String,Double> g1 = new Graph<>();
        Graph<Integer,String> g2 = new Graph<>();

        // Some nodes are duplicated in the list
        assertFalse(g1.addNodes(s1,d));
        assertEquals(5,g1.getNumNodes());
        System.out.println("Nodes in graph 1:");
        g1.printGraph();

        // All nodes are unique in the list
        assertTrue(g2.addNodes(t1,s2));
        assertEquals(7,g2.getNumNodes());
        System.out.println("Nodes in graph 2:");
        g2.printGraph();

        // List do not have the same number of reference and data
        g1.addNodes(s2,d);
    }

    /**
     * Test addEdge method
     */
    @Test
    public void addEdge(){

        Graph<String,Double> g1 = new Graph<>();

        g1.addNodes(s1,d);
        // Add edge between two existing nodes
        assertTrue(g1.addEdge("aaa","cdf"));
        assertTrue(g1.addEdge("aaa","csds"));
        assertTrue(g1.addEdge("ahd","bbb"));
        // Add edge between existing and non-existing nodes
        assertTrue(g1.addEdge("bbb","eee"));
        // Add edge between two non-existing nodes
        assertTrue(g1.addEdge("pho","bunbo"));
        assertTrue(g1.addEdge("huy","pool"));
        // Add duplicated edge between two nodes
        assertFalse(g1.addEdge("aaa","csds"));
        assertFalse(g1.addEdge("bbb","ahd"));
        assertEquals(11,g1.getNumNodes());
        System.out.println("Nodes in graph 1:");
        g1.printGraph();
    }

    /**
     * Test addEdges method
     */
    @Test
    public void addEdges(){

        Graph<String,Double> g1 = new Graph<>();
        g1.addNodes(s1,d);

        // Add edges between unconnected nodes
        assertTrue(g1.addEdges("pho",s2));
        // Add edges between connected and unconnected nodes
        assertFalse(g1.addEdges("ahd",s1));

        assertEquals(12,g1.getNumNodes());
        System.out.println("Nodes in graph 1:");
        g1.printGraph();
    }

    /**
     * Test removeNode method
     */
    @Test
    public void removeNode(){

        Graph<String,Double> g1 = new Graph<>();
        g1.addNodes(s1,d);

        g1.addEdges("pho",s2);
        g1.addEdges("ahd",s1);
        g1.addEdges("bme",s2);
        g1.addEdge("bbb","book");
        g1.addEdge("csds","ahihi");
        g1.addEdge("ta","pure");

        assertEquals(16,g1.getNumNodes());
        System.out.println("Original nodes in graph 1:");
        g1.printGraph();

        // Remove existing nodes in the graph
        assertTrue(g1.removeNode("pure"));
        assertTrue(g1.removeNode("chi"));
        assertTrue(g1.removeNode("bbb"));
        // Remove non-existing node in the graph
        assertFalse(g1.removeNode("agustD"));
        assertFalse(g1.removeNode("PLA"));

        assertEquals(13,g1.getNumNodes());
        System.out.println("Modified nodes in graph 1:");
        g1.printGraph();
    }

    /**
     * Test removeNodes method
     */
    @Test
    public void removeNodes(){

        Graph<String,Double> g1 = new Graph<>();
        g1.addNodes(s1,d);

        String[] r1 = {"233","CSDS","chi","heal","abcd-ef","chi"};
        String[] r2 = {"csds","bbb","stats"};

        g1.addEdges("pho",s2);
        g1.addEdges("ahd",s1);
        g1.addEdges("bme",s2);
        g1.addEdge("bbb","book");
        g1.addEdge("csds","ahihi");
        g1.addEdge("ta","pure");

        assertEquals(16,g1.getNumNodes());
        System.out.println("Original nodes in graph 1:");
        g1.printGraph();

        // Remove existing and non-existing nodes in the graph
        assertFalse(g1.removeNodes(r1));
        // Remove existing nodes in the graph
        assertTrue(g1.removeNodes(r2));
        assertEquals(10,g1.getNumNodes());
        System.out.println("Modified nodes in graph 1:");
        g1.printGraph();
    }

    /**
     * Test read method
     */
    @Test
    public void read(){

        String file0 = "text";
        String file1 = "src/text1.txt";
        String file2 = "src/text2.txt";
        String file3 = "src/text3.txt";

        // Read appropriate text file and create graph
        Graph<String,Integer> g1 = Graph.read(file1);
        Graph<String,Integer> g2 = Graph.read(file2);
        Graph<String,Integer> g3 = Graph.read(file3);

        System.out.println("Nodes in Graph 1: ");
        g1.printGraph();
        System.out.println("Nodes in Graph 2: ");
        g2.printGraph();
        System.out.println("Nodes in Graph 3: ");
        g3.printGraph();

        // Add non-existing file to the graph
        System.out.println("Nodes in Graph 4: ");
        try{
            Graph.read(file0);
        }
        catch(RuntimeException e){
            System.out.println("This is an error file");
        }
    }

    /**
     * Test DFS method
     */
    @Test
    public void DFS(){

        Graph<Integer,String> g2 = new Graph<>();
        g2.addNodes(t1,s2);
        g2.addNode(47,"thi");
        g2.addEdge(20,1);
        g2.addEdge(20,5);
        g2.addEdge(20,100);
        g2.addEdge(14,20);
        g2.addEdge(14,35);
        g2.addEdge(14,77);
        g2.addEdge(93,5);
        g2.addEdge(93,0);
        g2.addEdge(93,11);

        System.out.println("Nodes in graph 2:");
        g2.printGraph();

        // Find path between two connected nodes
        System.out.println("Path between 14 and 77: " + Arrays.toString(g2.DFS(14, 77)));
        // Find path between two unconnected nodes
        System.out.println("Path between 0 and 5: " + Arrays.toString(g2.DFS(0, 5)));
        System.out.println("Path between 11 and 100: " + Arrays.toString(g2.DFS(11, 100)));
        // Find path between two non-existing nodes
        System.out.println("Path between 8 and 49: " + Arrays.toString(g2.DFS(8, 49)));
        // No path that link the two nodes
        System.out.println("Path between 11 and 47: " + Arrays.toString(g2.DFS(11, 47)));
    }

    /**
     * Test BFS method
     */
    @Test
    public void BFS(){

        Graph<Integer,String> g2 = new Graph<>();
        g2.addNodes(t1,s2);
        g2.addNode(47,"thi");
        g2.addEdge(20,1);
        g2.addEdge(20,5);
        g2.addEdge(20,100);
        g2.addEdge(14,20);
        g2.addEdge(14,35);
        g2.addEdge(14,77);
        g2.addEdge(93,5);
        g2.addEdge(93,0);
        g2.addEdge(93,11);

        System.out.println("Nodes in graph 2:");
        g2.printGraph();

        // Find path between two connected nodes
        System.out.println("Path between 14 and 77: " + Arrays.toString(g2.BFS(14, 77)));
        // Find path between two unconnected nodes
        System.out.println("Path between 0 and 5: " + Arrays.toString(g2.BFS(0, 5)));
        System.out.println("Path between 11 and 100: " + Arrays.toString(g2.BFS(11, 100)));
        // Find path between two non-existing nodes
        System.out.println("Path between 8 and 49: " + Arrays.toString(g2.BFS(8, 49)));
        // No path that link the two nodes
        System.out.println("Path between 11 and 47: " + Arrays.toString(g2.BFS(11, 47)));
    }

}