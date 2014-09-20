import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.InputMismatchException;
/**
 * Textbuddy v1.3 
 * This program allows a user to edit an existing text document.
 * The program expects an argument in the form of the filename it expects to edit.
 * It assumes that the file to be edited had already been created and there is no need to create a new file.
 * The following are operations that the textbuddy can perform:
 * 1.add (text)-->adds a line to the file.
 * 2.delete (integer) -->removes a specific line in the file.
 * 3.clear -->remove all content in the file.
 * 4.display -->show the content in the file.
 * 5.exit -->ends the program
 * 6.help -->shows available commands
 * @author YAP ZHENG MOU, SCOTT |  A0111082Y
 *
 */

public class TextBuddy {
    /**
     * The following shows error messages that can be encountered during runtime.
     */
    private static final String ERROR_READING = "Error while reading file line by line:";
    private static final String ERROR_NO_FILE_NAME = "There is no filename specified!";
    private static final String ERROR_INVALID_COMMAND = "No such command! Type help for list of available commands.";
    private static final String ERROR_EMPTY_FILE = "%1$s is empty";
    private static final String ERROR_NO_SUCH_ELEMENT = "Element not found in file";
    private static final String ERROR_INPUT_NOT_INTEGER = "Please enter in this format : delete integer";
    private static final String ERROR_NULL_INPUT = "Input cannot be null!";
    private static final String ERROR_STRING_NOT_FOUND = "%1$s not found!";
    
    /**
     * The following shows messages that will be display after specificed operations are completed.
     */
    private static final String MESSAGE_COMMAND_LIST = "List of commands: 1.display 2.add (text) "
                                     + "3.delete (number) 4.clear 5. search (text) 6.sort 7.exit";
    private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
    private static final String MESSAGE_ENTER_COMMAND = "Please enter a command:";
    private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
    private static final String MESSAGE_CONTENT_DELETED = "all content deleted from %1$s";
    private static final String MESSAGE_STRING_FOUND = "%1$s is found in the following lines :";
    private static final String MESSAGE_EXIT = "Goodbye!";

    enum COMMAND_TYPE {
        ADD, DISPLAY, DELETE, CLEAR, EXIT, HELP, INVALID, SEARCH, SORT
    };

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        LinkedList<String> currentFile = new LinkedList<String>();

        String fileName = "";
        
        
        fileName = checkParameter(args, fileName);
        
       
        readFromFile(currentFile, fileName);
        
        
        showToUser(MESSAGE_ENTER_COMMAND);
        String command = sc.next();
        COMMAND_TYPE instruction = readCommand(command, sc, currentFile,
                fileName);
        executeCommand(sc, instruction, currentFile, fileName);
        
        //keep waiting for commands until there is an exit command
        while (command != "exit") {
            showToUser(MESSAGE_ENTER_COMMAND);
            String nextCommand = sc.next();
            COMMAND_TYPE instructionNext = readCommand(nextCommand, sc,
                    currentFile, fileName);
            executeCommand(sc, instructionNext, currentFile, fileName);
        }

    }

    private static void showToUser(String text) {
        System.out.println(text);
    }

    private static void executeCommand(Scanner sc, COMMAND_TYPE instruction,
            LinkedList<String> currentFile, String fileName) {
        String inputText = "";
        String searchString = "";
        switch (instruction) {
            case DISPLAY:
                displayFile(currentFile, fileName);
                break;

            case ADD:
                inputText = sc.nextLine().trim();
                addToList(currentFile, inputText, fileName);
                break;

            case DELETE:
                try {
                    int delElementIndex = sc.nextInt();
                    deleteElementFromList(currentFile, delElementIndex, fileName);
                } catch (InputMismatchException exception) {
                    showToUser(ERROR_INPUT_NOT_INTEGER);
                }
                break;

            case CLEAR:
                clearAll(currentFile, fileName);
                break;

            case HELP:
                showToUser(MESSAGE_COMMAND_LIST);
                break;

            case INVALID:
                showToUser(ERROR_INVALID_COMMAND);
                break;

            case EXIT:
                showToUser(MESSAGE_EXIT);
                System.exit(0);
                break;
            
            case SEARCH:
                searchString = sc.nextLine().trim();
                searchList(currentFile, searchString);
                break;
            case SORT:
                sortList(currentFile);
                writeToFile(currentFile, fileName);
                displayFile(currentFile, fileName);
                break;

            default:
                showToUser(ERROR_INVALID_COMMAND);

        }

    }
    //Checks inputs and returns the correct command to execute
    private static COMMAND_TYPE readCommand(String command, Scanner sc,
            LinkedList<String> currentFile, String fileName) {

        if (command == null) {
            throw new Error(ERROR_NULL_INPUT);
        }
        if (command.equalsIgnoreCase("display")) {
            return COMMAND_TYPE.DISPLAY;

        } else if (command.equalsIgnoreCase("add")) {
            return COMMAND_TYPE.ADD;

        } else if (command.equalsIgnoreCase("delete")) {
            return COMMAND_TYPE.DELETE;

        } else if (command.equalsIgnoreCase("clear")) {
            return COMMAND_TYPE.CLEAR;

        } else if (command.equalsIgnoreCase("help")) {
            return COMMAND_TYPE.HELP;
        } else if (command.equalsIgnoreCase("exit")) {
            return COMMAND_TYPE.EXIT;
        } else if (command.equalsIgnoreCase("search")) {
            return COMMAND_TYPE.SEARCH;
        } else if (command.equalsIgnoreCase("sort")) {
            return COMMAND_TYPE.SORT;    
        } else {
            return COMMAND_TYPE.INVALID;

        }

    }
    
    // Performs a startup check to ensure there is an argument during the program runtime
    private static String checkParameter(String[] args, String fileName) {
        if (args.length == 0) {
            showToUser(ERROR_NO_FILE_NAME);
            System.exit(0);
        }

        else {
            fileName = args[0];
            showToUser(String.format(MESSAGE_WELCOME, fileName));

        }
        return fileName;
    }

    //Displays all the text currently stored in the list
    private static void displayFile(LinkedList<String> currentFile,
            String fileName) {
        int count = 0;
        
        if (currentFile.size() == 0) {
            showToUser(String.format(ERROR_EMPTY_FILE, fileName));
            
        }

        while (count < currentFile.size()) {
            showToUser((count + 1) + ". " + currentFile.get(count));
            
            
            count++;
        }
    }
    
    //Opens the textfile and read it line by line, storing each line in a linkedlist
    private static void readFromFile(LinkedList<String> currentFile,
            String fileName) {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bufferReader = new BufferedReader(fr);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                currentFile.add(line);
            }

            bufferReader.close();
        } catch (Exception e) {

            showToUser(ERROR_READING
                    + e.getMessage());
            System.exit(0);

        }
    }
    
    //Copies all content in the linkedlist and write to the textfile
    private static void writeToFile(LinkedList<String> currentFile,
            String fileName) {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (int count = 0; count < currentFile.size(); count++) {
                String content = currentFile.get(count);
                bw.write(content);
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    //Remove all contents from the file
    public static void clearAll(LinkedList<String> currentFile, String fileName) {
        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String content = "";
            bw.write(content);
            bw.close();
            currentFile.clear();
            showToUser(String.format(MESSAGE_CONTENT_DELETED, fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    public static void addToList(LinkedList<String> currentFile,
            String inputText, String fileName) {
        currentFile.add(inputText);
        showToUser(String.format(MESSAGE_ADDED, fileName, inputText));
        writeToFile(currentFile, fileName);
    }

    public static void deleteElementFromList(LinkedList<String> currentFile,
            int element, String fileName) {

        if ((element < currentFile.size()) && element > 0) {
            showToUser("deleted from " + fileName + ": \""
                    + currentFile.get(element - 1) + "\"");
            currentFile.remove(element - 1);
            writeToFile(currentFile, fileName);

        } else
            showToUser(ERROR_NO_SUCH_ELEMENT);
    }
    
    //Search for input string in the entire linkedlist
    public static ArrayList<Integer> searchList(LinkedList<String> currentFile, String searchString) {
        Iterator<String> li = currentFile.iterator();
        int counter = 1;
        ArrayList<Integer> resultsList = new ArrayList<Integer>();
        while (li.hasNext()) {
            if (li.next().contains(searchString)) {
                resultsList.add(counter);
            } 
            counter++;
        }
        if(resultsList.size()==0) {
        showToUser(String.format(ERROR_STRING_NOT_FOUND, searchString));
        }
        else {
            showToUser(String.format(MESSAGE_STRING_FOUND, searchString));
            System.out.println(resultsList);
        }
        return resultsList;
    }
    
    /*Sort the linkedlist in alphabetical order. Items starting with digits will be
      before items starting with alphabets.
    */
    public static LinkedList<String> sortList(LinkedList<String> currentFile) {
        Collections.sort(currentFile);
        return currentFile;
    }
    }
