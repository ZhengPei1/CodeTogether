package Model;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Character.isDigit;

// utility class contains public static methods that can be used anywhere in the package without initializing this class
public class Utility {
    
    // resize image
    public static ImageIcon imageResize(String url, int width, int height){
        return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    
    // save to code to the given file path
    public static boolean saveCode(String filePath, String code){
        // create a new file if the file doesn't exist
        File file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            // write to the file
            FileWriter writer = new FileWriter(filePath, false);
            writer.write(code);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // extract the name of the java class based on the given code, only needed for client version of run code
    public static String extractClassName(String code) {
        String className = null;
        // find the index of the first curly bracket {
        int firstBracket = 0;
        for (int i = 0; i < code.length(); ++i) {
            // if we find the bracket, break the loop
            if (code.charAt(i) == '{') {
                firstBracket = i;
                break;
            }
        }

        // find the index for the last and the first character of the word before the curly bracket(this word is the name
        // of the class)
        int lastChar = 0;
        int firstChar = 0;
        for (int i = firstBracket - 1; i > 0; --i) {
            // first we find the last character of the word, skip all the white spaces and empty line
            if (lastChar == 0) {
                if (code.charAt(i) != ' ' || code.charAt(i) != '\n') {
                    lastChar = i + 1;
                }
            } else {
                // after we find the last character of the word, we loop backward to find the first character of the word
                // terminate when there's a white space
                if (code.charAt(i) == ' ') {
                    firstChar = i + 1;
                    break;
                }
            }
        }
        // generate the classNmae based on the information we have
        if (firstBracket != 0 && lastChar - firstChar >= 1) {
            className = code.substring(firstChar, lastChar);
            // remove bank space in the classNmae
            className = className.trim();
        }

        // check if we were able to extract the class name
        if (className == null) {
            return null;
        } else {
            // generate the file name
            Controller.printToMessageArea("extracted class name: " + className + "\n");
            className += ".java";
            return className;
        }

    }
    
    
    // read the code from the given file path
    public static String readCode(String filePath){
        try {
            Scanner input = new Scanner(new File(filePath));
            StringBuilder code = new StringBuilder();
            while(input.hasNextLine()){
                code.append(input.nextLine()).append("\n");
            }
            input.close();
            return code.toString();
        } catch (FileNotFoundException e) {
            return "can not find " + Controller.fileEditing + "\nPlease open another file to edit";
        }
        
    }
    
    
    // check if a server name is valid or not
    public static boolean isValidServerName(String serverName){
        // check if server name is empty
        if (Objects.equals(serverName, "") || serverName == null)
            return false;
    
        // check if server name is valid
        for(int i = 0; i < serverName.length(); ++i)
            if (!Character.isDigit(serverName.charAt(i)))
                return false;
        return true;
    }
    
    
    // check if server password is valid
    public static boolean isValidServerPassword(String password){
        // check if server password is empty
        if (Objects.equals(password, "") || password == null)
            return false;
    
        // check if server password is valid
        for(int i = 0; i < password.length(); ++i)
            if (!Utility.isDigitOrAlpha(password.charAt(i)))
                return false;
        return true;
    }

    // check if a java file name is valid or not
    public static boolean isValidFileName(String fileName){
        // check if file name is empty
        if (Objects.equals(fileName, "") || fileName == null)
            return false;

        // check if file name is valid, digit and alphabet only 
        for(int i = 0; i < fileName.length(); ++i)
            if (!Utility.isDigitOrAlpha(fileName.charAt(i)))
                return false;
        return true;
    }
    
    // check if a character is digit or alphabet
    private static boolean isDigitOrAlpha(char c){
        if(Character.isAlphabetic(c))
            return true;
        
        if(Character.isDigit(c))
            return true;
        
        return false;
    }
    
    
}
