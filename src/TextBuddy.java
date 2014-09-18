import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String ERROR_INVALID_COMMAND = "No such command! Type help for list of availiable commands.";
    private static final String ERROR_EMPTY_FILE = "%1$s is empty";
    private static final String ERROR_NO_SUCH_ELEMENT = "Element not found in file";
    private static final String ERROR_INPUT_NOT_INTEGER = "Please enter in this format : delete integer";
    private static final String ERROR_NULL_INPUT = "Input cannot be null!";
    
    /**
     * The following shows messages that will be display after specificed operations are completed.
     */
    private static final String COMMAND_LIST = "List of commands: 1.display 2.add (text) 3.delete (number) 4.clear 5.exit";
    private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %1$s is ready for use";
    private static final String ENTER_COMMAND = "Please enter a command:";
    private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
    private static final String CONTENT_DELETED = "all content deleted from %1$s";
    private static final String EXIT_MESSAGE = "Goodbye!";

    enum COMMAND_TYPE {
        ADD_TOFILE, DISPLAY_FILE, DELETE_FILECONTENT, CLEAR_ENTIRE_FILE, EXIT, HELP, INVALID, SEARCH
    };

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        // Linkedlist to store contents from the file
        LinkedList<String> currentFile = new LinkedList<String>();

        String fileName = "";
        // Check if filename is specified.Prints Welcome message
        fileName = checkParameter(args, fileName);
        // Read in file and store each line in a LinkList
        readFromFile(currentFile, fileName);
        // Read in command
        showToUser(ENTER_COMMAND);
        String command = sc.next();
        COMMAND_TYPE instruction = readCommand(command, sc, currentFile,
                fileName);
        executeCommand(sc, instruction, currentFile, fileName);
        //keep waiting for commands until there is an exit command
        while (command != "exit") {
            showToUser(ENTER_COMMAND);
            String commandNext = sc.next();
            COMMAND_TYPE instructionNext = readCommand(commandNext, sc,
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
            case DISPLAY_FILE:
                displayFile(currentFile, fileName);
                break;

            case ADD_TOFILE:
                inputText = sc.nextLine().trim();
                addToList(currentFile, inputText, fileName);
                break;

            case DELETE_FILECONTENT:
                try {
                    int delElement = sc.nextInt();
                    deleteElementFromList(currentFile, delElement, fileName);
                } catch (InputMismatchException exception) {
                    showToUser(ERROR_INPUT_NOT_INTEGER);
                }
                break;

            case CLEAR_ENTIRE_FILE:
                clearAll(currentFile, fileName);
                break;

            case HELP:
                showToUser(COMMAND_LIST);
                break;

            case INVALID:
                showToUser(ERROR_INVALID_COMMAND);
                break;

            case EXIT:
                showToUser(EXIT_MESSAGE);
                System.exit(0);
                break;
            
            case SEARCH:
                searchString = sc.nextLine().trim();
                searchList(currentFile, searchString, fileName);
                break;

            default:
                showToUser(ERROR_INVALID_COMMAND);

        }

    }

    private static COMMAND_TYPE readCommand(String command, Scanner sc,
            LinkedList<String> currentFile, String fileName) {

        if (command == null) {
            throw new Error(ERROR_NULL_INPUT);
        }
        if (command.equalsIgnoreCase("display")) {
            return COMMAND_TYPE.DISPLAY_FILE;

        } else if (command.equalsIgnoreCase("add")) {
            return COMMAND_TYPE.ADD_TOFILE;

        } else if (command.equalsIgnoreCase("delete")) {
            return COMMAND_TYPE.DELETE_FILECONTENT;

        } else if (command.equalsIgnoreCase("clear")) {
            return COMMAND_TYPE.CLEAR_ENTIRE_FILE;

        } else if (command.equalsIgnoreCase("help")) {
            return COMMAND_TYPE.HELP;
        } else if (command.equalsIgnoreCase("exit")) {
            return COMMAND_TYPE.EXIT;
        } else if (command.equalsIgnoreCase("search")) {
            return COMMAND_TYPE.SEARCH;    
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
            showToUser(String.format(WELCOME_MESSAGE, fileName));

        }
        return fileName;
    }

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
    // opens the textfile and read it line by line, storing each line in a linkedlist
    private static void readFromFile(LinkedList<String> currentFile,
            String fileName) {
        try {
            FileReader inputFile = new FileReader(fileName);
            BufferedReader bufferReader = new BufferedReader(inputFile);
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
            showToUser(String.format(CONTENT_DELETED, fileName));

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
    public static ArrayList<Integer> searchList(LinkedList<String> currentFile, String searchString,String filename) {
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
        showToUser("String not found!");
        }
        else {
            showToUser("String found in the following lines :");
            System.out.println(resultsList);
        }
        return resultsList;
    }
}