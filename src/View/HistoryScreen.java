package View;

import Controller.Controller;
import ExternalCode.TextLineNumber;
import Model.LocalHistory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// this is the historyScreen that displays the file history
public class HistoryScreen {
    JFrame frame = new JFrame();
    JTextArea textArea = new JTextArea();
    GridBagConstraints constraints = new GridBagConstraints();

    public HistoryScreen(String fileEditing, String rootDirectory, ArrayList<String> buttonName, ArrayList<String> historyEntry) {

        // set up JFrame
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setTitle("File History: "  + fileEditing);
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }

        // create buttons and scroll pane that allows user to choose file history
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(200, 100 * buttonName.size()));
        buttonPanel.setLayout(new GridLayout(Math.max(buttonName.size(), 5), 1));

        JScrollPane scrollPanel1 = new JScrollPane(buttonPanel);
        scrollPanel1.getVerticalScrollBar().setUnitIncrement(16);
        scrollPanel1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPanel1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // initialize buttons in reverse order (put the most up-to-date entry first)
        for(int i = buttonName.size()-1; i >= 0; --i){
            JButton newButton = new JButton(buttonName.get(i));
            newButton.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
            newButton.setToolTipText(buttonName.get(i));

            int finalI = i;
            newButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    textArea.setText(historyEntry.get(finalI));
                }
            });
            buttonPanel.add(newButton);
        }

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.2;
        constraints.weighty = 0.75;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridheight = 6;
        constraints.gridwidth = 1;
        frame.add(scrollPanel1, constraints);

        // set the text to the last entry
        textArea.setText(historyEntry.get(historyEntry.size()-1));

        // create textarea and scroll pane for history file content
        JScrollPane scrollPanel2 = new JScrollPane(textArea);
        textArea.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        textArea.setEditable(false);
        textArea.setForeground(Color.black);
        textArea.setBackground(Color.gray);
        textArea.setTabSize(2);

        scrollPanel2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanel2.setRowHeaderView(new TextLineNumber(textArea));

        constraints.weightx = 0.8;
        constraints.weighty = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 8;
        constraints.gridwidth = 4;
        frame.add(scrollPanel2, constraints);

        // revert button
        JButton revert = new JButton("Revert");
        revert.setToolTipText("Create a history entry for the code in the coding area, then revert the code");
        revert.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        revert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create a history entry first
                LocalHistory.saveHistory(fileEditing, rootDirectory);
                Controller.modifyCodeArea(textArea.getText());
            }
        });

        constraints.weightx = 0.2;
        constraints.weighty = 0.125;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        frame.add(revert, constraints);
        
        // clear button
        JButton clearHistory = new JButton("Clear History");
        clearHistory.setToolTipText("Clear all the previous histories, then create one history entry");
        clearHistory.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        clearHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // find the local history directory and history file
                File historyDirectory = new File(rootDirectory + "\\localHistory");
                File editing = new File(fileEditing);
                String fileName = editing.getName().substring(0, editing.getName().length() - 5) + ".txt";
                File historyFile = new File(historyDirectory + File.separator + fileName);


                // use file writer to clear the text
                try {
                    FileWriter writer = new FileWriter(historyFile);
                    writer.write("");
                    writer.flush();
                    writer.close();
                    LocalHistory.saveHistory(fileEditing, rootDirectory);
                } catch (IOException ex) {
                    Controller.printToMessageArea("can not clear history");
                    return;
                }

                // reopen the screen
                frame.dispose();
                LocalHistory.loadHistory(fileEditing, rootDirectory);
            }
        });

        constraints.weightx = 0.2;
        constraints.weighty = 0.125;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        frame.add(clearHistory, constraints);


        frame.setVisible(true);

    }

}
