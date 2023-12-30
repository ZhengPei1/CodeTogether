package View;

import Model.Utility;

import javax.swing.*;
import java.awt.*;

// this class is the model of pages(tabs) on the tutorial screen, it has overloaded constructor for different request
public class TutorialPanel extends JPanel {
    
    // create a tutorial panel with image and text
    public TutorialPanel(String imageURL, String text){
        
        // set up the panel 
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        setOpaque(false);
        
        // add the image
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.2;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        
        JLabel iconLabel = new JLabel(Utility.imageResize(imageURL, 50, 50));
        add(iconLabel, constraints);
        
        
        // add the textarea and scrollbar
        constraints.gridwidth = 8;
        constraints.gridheight = 3;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weightx = 0.8;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        textArea.setForeground(Color.black);
        textArea.setBackground(Color.gray);
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane, constraints);
        
    }
    
    // create a tutorial panel with text only
    public TutorialPanel(String text){
        // set up the panel
        setLayout(new GridLayout(1, 1));
        setSize(new Dimension(1050, 600));
        setOpaque(false);
        
        // add the textarea and scrollbar
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
        textArea.setForeground(Color.black);
        textArea.setBackground(Color.gray);
        textArea.setEditable(false);
    
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }
    
}
