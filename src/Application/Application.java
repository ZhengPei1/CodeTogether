package Application;

import Controller.Controller;

import javax.swing.*;


/*
 - Author: Zheng Pei
 
 - Date: Jan 16, 2023
 
 - Course Code: ICS4U1-01
 
 - Course Instructor: Nicholas Fernandes (Mr.Fernandes)
 
 - Title: codeTogether
 
 - Description: a program that allows multiple users to share and run code at the same time

 - Features to notice
    1. An algorithm is implemented to update the cursor position when more than 1 person are editing the same file
    2. File history
 
 - Major skill:
    Multithreading
    Socket programming(one to one & one to many)
    Recursion
    Tree related algorithm
    File input & output
    Method overloading
    Other basic skills like using data structures, oop programming, inheritance etc.

 - Areas of Concern:
    the algorithm for cursor position could be updated for more efficiency
    when multiple users connected to a server and typing in a really fast speed, the server may crush 
 
 */

public class Application {
    public static void main(String[] args) {

        // use nimbus look and feel for the project
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // open up the controller
        new Controller();
    }

}
