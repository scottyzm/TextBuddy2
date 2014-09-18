import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;


public class TextBuddyTest {

    @Test
    public void testaddToList() {
        LinkedList<String> currentFileTest = new LinkedList<String>();
        int currentSize = currentFileTest.size();
        TextBuddy.addToList(currentFileTest,"testingstring", "testfilename");
        assertEquals(currentSize+1,currentFileTest.size());
    }
    @Test
    public void testdeleteElementFromList() {
        LinkedList<String> currentFileTest = new LinkedList<String>();
        currentFileTest.add("111");
        currentFileTest.add("222");
        currentFileTest.add("333");
        currentFileTest.add("444");
        int currentSize = currentFileTest.size();
        TextBuddy.deleteElementFromList(currentFileTest,
                2, "testfilename");
        assertEquals(currentSize-1,currentFileTest.size());
        
    }
    @Test
    public void testClearAll() {
        LinkedList<String> currentFileTest = new LinkedList<String>();
        currentFileTest.add("111");
        currentFileTest.add("222");
        currentFileTest.add("333");
        currentFileTest.add("444");
        TextBuddy.clearAll(currentFileTest,"testfileName");
        assertEquals(0,currentFileTest.size());
        
    }
    @Test
    public void testsearchList() {
        
    }
}
