import static org.junit.Assert.*;

import java.util.ArrayList;
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
        LinkedList<String> currentFileTest = new LinkedList<String>();
        String searchstring = "222";
        currentFileTest.add("111");
        currentFileTest.add("222");
        currentFileTest.add("333");
        currentFileTest.add("444");
        currentFileTest.add("222");
        currentFileTest.add("111222333");
        ArrayList<Integer> testresultsList = new ArrayList<Integer>();
        testresultsList.add(2);
        testresultsList.add(5);
        testresultsList.add(6);
        assertEquals(testresultsList,TextBuddy.searchList(currentFileTest, searchstring, "testfileName"));
        
    }
    @Test
    public void testsortList() {
        LinkedList<String> currentFileTest = new LinkedList<String>();
        currentFileTest.add("ccc");
        currentFileTest.add("zzz");
        currentFileTest.add("fff");
        currentFileTest.add("ggg");
        currentFileTest.add("123");
        currentFileTest.add("aaa");
        LinkedList<String> expectedSortedTest = new LinkedList<String>();
        currentFileTest.add("aaa");
        currentFileTest.add("ccc");
        currentFileTest.add("fff");
        currentFileTest.add("ggg");
        currentFileTest.add("zzz");
        currentFileTest.add("123");
        assertEquals(expectedSortedTest,TextBuddy.sortList(currentFileTest,"testfileName"));
    }
}
