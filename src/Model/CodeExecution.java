package Model;


/*
this class is being used to compile and run the java code written by the user

use the class instance.runCode() to run the code that needs to be executed, the output and/or the error messages will
be printed on the console
 */

import Controller.Controller;
import View.ConsoleScreen;

import javax.swing.*;
import javax.tools.*;
import java.awt.*;
import java.io.*;
import java.util.Objects;

public class CodeExecution {
    // the console screen that prints the message
    ConsoleScreen console = new ConsoleScreen();

    // the constructor, it will initialize the project folder
    public CodeExecution(String rootDirectory) {
        // create a src folder and onlineTemp in the selected directory
        File src = new File(rootDirectory + File.separator + "src");
        if (!src.exists())
            src.mkdir();

        File onlineTemp = new File(rootDirectory + File.separator + "onlineTemp");
        if (!onlineTemp.exists())
            onlineTemp.mkdir();

        // create a main file with basic code in the src directory, if src doesn't have any file
        File mainFile = new File(src.getAbsolutePath() + File.separator + "Main.java");
        if (!mainFile.exists() && src.isDirectory() && Objects.requireNonNull(src.list()).length == 0) {

            try {
                mainFile.createNewFile();
                FileWriter writer = new FileWriter(mainFile);
                writer.write(
                        """
                                import javax.swing.*;
                                import java.awt.*;
                                public class Main {
                                \tprivate static JFrame frame = new JFrame("A welcome message");
                                \t\tpublic static void main(String[] args) {
                                \t\tframe.setSize((Toolkit.getDefaultToolkit().getScreenSize()));
                                \t\tframe.setContentPane(new JLabel(new ImageIcon("resources/helloWorld.jpg")));
                                \t\tframe.setVisible(true);
                                \t\tframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                                \t}
                                }
                                """
                );
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // this function will compile all the files in the src folder and run the main file
    public void runCode(String pathToSrc, String mainFile) {
        File src = new File(pathToSrc);

        // make the console visible and clear all the previous texts
        console.getFrame().setVisible(true);
        console.getConsole().setText("");

        // compile all the files
        if (src.exists() && src.isDirectory()) {
            try {
                // use javac command
                Process process = Runtime.getRuntime().exec(new String[]{"javac", pathToSrc + "\\*.java"});
                boolean error = false;

                // read error message, if there's any
                String errorMessage;
                BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((errorMessage = errorStream.readLine()) != null) {
                    // ignore the error command caused by system setting
                    if (errorMessage.equals("Picked up JAVA_TOOL_OPTIONS: -Duser.language=en")) {
                        continue;
                    }
                    console.printToConsole(errorMessage);
                    error = true;
                }
                if (error)
                    return;


            } catch (IOException e) {
                console.printToConsole(e.getMessage());
                return;
            }
        } else {
            console.printToConsole("can not find src");
            return;
        }

        // run the main file
        File applicationFile = new File(mainFile);
        if (applicationFile.exists()) {
            try {
                // extract class name (without .java), then execute file
                String className = applicationFile.getName().substring(0, applicationFile.getName().length() - 5);
                Process process = Runtime.getRuntime().exec(new String[]{"java", "-cp", pathToSrc, className});

                // get the outputStream from command line
                String output = null;
                BufferedReader consoleOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // show the output
                console.printToConsole("----------------------- the program output:");
                while ((output = consoleOutput.readLine()) != null && console.stillOpen()) {
                    console.printToConsole(output);
                }

                // get the error message from command line
                String errorMessage = null;
                BufferedReader errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                // show error messages
                console.printToConsole("----------------------- the system/error message:");
                while ((errorMessage = errorStream.readLine()) != null) {
                    console.printToConsole(errorMessage);
                }

                // wait for the program to finish
                process.waitFor();

                // print the program execution code
                console.printToConsole("exit value(" + process.exitValue() + ")");
                console.printToConsole("-------------------");


            } catch (IOException e) {
                // print error message when run process failed
                console.printToConsole(e.getMessage());
            } catch (InterruptedException e) {
                console.printToConsole(e.getMessage());
            }
        } else {
            console.printToConsole("current/main file doesn't exist");
        }


    }
}

