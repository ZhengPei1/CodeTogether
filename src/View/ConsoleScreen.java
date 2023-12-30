package View;

import ExternalCode.TextLineNumber;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// this is the console screen that shows program output
public class ConsoleScreen{
    JFrame frame = new JFrame();
    JTextArea console;
    public ConsoleScreen(){
        // set up JFrame
        frame.setSize(600, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setTitle("Console");
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }

        // create textarea and scroll pane for output
        console = new JTextArea();
        JScrollPane scrollPanel = new JScrollPane(console);
        console.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        console.setForeground(Color.white);
        console.setBackground(Color.black);
        console.setTabSize(2);

        scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanel.setRowHeaderView(new TextLineNumber(console));

        frame.setContentPane(scrollPanel);
        frame.setVisible(false);
    }

    // this method allows other classes to print message to console
    public void printToConsole(String text){
        console.append(text + "\n");
    }

    // this method returns boolean indicates if the user kept the console open or not
    public boolean stillOpen(){
        return frame.isVisible();
    }

    // getters and setters

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JTextArea getConsole() {
        return console;
    }

    public void setConsole(JTextArea console) {
        this.console = console;
    }
}
