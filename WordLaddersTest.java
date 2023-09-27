import org.junit.Test;
import java.util.Hashtable;

/**
 * Testing class for WordLadders
 * @author Vo Linh Chi Dao
 */
public class WordLaddersTest{

    String file0 = "text";
    String file1 = "src/text4.txt";
    String file2 = "src/text5.txt";
    String file3 = "src/text6.txt";

    /**
     * Test readWordGraph method
     */
    @Test
    public void testReadWordGraph(){

        Graph<Integer,String> g1 = WordLadders.readWordGraph(file1);
        Graph<Integer,String> g2 = WordLadders.readWordGraph(file2);
        Graph<Integer,String> g3 = WordLadders.readWordGraph(file3);

        // Read appropriate text file and create graph
        System.out.println("The nodes in Graph 1:");
        g1.printGraph();
        System.out.println("The nodes in Graph 2:");
        g2.printGraph();
        System.out.println("The nodes in Graph 3:");
        g3.printGraph();

        // Add non-existing file to the graph
        System.out.println("Nodes in Graph 4: ");
        try{
            WordLadders.readWordGraph(file0);
        }
        catch(RuntimeException e){
            System.out.println("This is an error file");
        }
    }

    /**
     * Test hashGraph method
     */
    @Test
    public void testHashGraph(){

        Hashtable<String,Integer> t1 = WordLadders.hashGraph(file1);
        Hashtable<String,Integer> t2 = WordLadders.hashGraph(file2);
        Hashtable<String,Integer> t3 = WordLadders.hashGraph(file3);

        // Read appropriate text file and create HashTable
        System.out.println("HashTable 1:");
        System.out.println(t1);
        System.out.println("HashTable 2:");
        System.out.println(t2);
        System.out.println("HashTable 3:");
        System.out.println(t3);

        // Add non-existing file to the graph
        System.out.println("HashTable 4: ");
        try{
            WordLadders.hashGraph(file0);
        }
        catch(RuntimeException e){
            System.out.println("This is an error file");
        }
    }

}