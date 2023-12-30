package View;

import Model.Utility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// this class is the tutorial screen
public class Tutorial {
    
    private JFrame frame = new JFrame();
    private JTabbedPane tabbedPane = new JTabbedPane();
    
    // resources needed to load tutorial panels
    String[] imageLinks = new String[]{
            "resources/saveButton.png",
            "resources/newFileButton.png",
            "resources/deleteButton.png",
            "resources/exportButton.png",
            "resources/runCodeButton.png",
            "resources/fileHistoryButton.png",
            "resources/inviteButton.png",
            "resources/settingButton.png",
            "resources/disconnectButton.png",
            "resources/inviteButtonOnlineMode.png",
    };
    
    ArrayList<String> tutorialTexts = new ArrayList<>();
    ArrayList<String> panelTitles = new ArrayList<>();
    
    // initialize tutorial screen
    public Tutorial(){
        // set up JFrame
        frame.setSize(1350, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setTitle("codeTogether - tutorial");
        frame.setContentPane(new JLabel(new ImageIcon("resources/background.jpg")));
        frame.setLayout(new GridLayout(1, 1));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // set up icon
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }

        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }
        
        // loading texts
        loadTutorialText();
        
        // create panels then add them to tabbed pane
        for(int i = 0; i < tutorialTexts.size(); ++i){
            if(i <= imageLinks.length - 1)
                tabbedPane.addTab(panelTitles.get(i), Utility.imageResize(imageLinks[i], 30, 30), new TutorialPanel(imageLinks[i], tutorialTexts.get(i)));
            else
                tabbedPane.addTab(panelTitles.get(i), new TutorialPanel(tutorialTexts.get(i)));
        }
        tabbedPane.setOpaque(false);
        frame.add(tabbedPane);
        
        // open the frame
        frame.setVisible(true);
    }
    
    // load the text from tutorial.txt
    private void loadTutorialText() {
        try {
            // read info from tutorial.txt
            Scanner scanner = new Scanner(new File("src/Data/tutorial.txt"));
            
            while(scanner.hasNextLine()){
                StringBuilder builder = new StringBuilder();
                
                // extract titles
                String title = scanner.nextLine();
                panelTitles.add(title);
                builder.append(title).append("\n");
                
                // read tutorial texts
                String nextLine;
                while(scanner.hasNextLine() && !(nextLine = scanner.nextLine()).contains("---")){
                    builder.append(nextLine).append("\n");
                }
                
                tutorialTexts.add(builder.toString());
            }
            scanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("can not load tutorial");
        }
    }

}
