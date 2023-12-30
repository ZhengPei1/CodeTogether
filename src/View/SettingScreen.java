package View;

import Controller.Controller;
import Model.Setting;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// this is the setting screen that allows the user to modify settings
public class SettingScreen {
    JFrame frame = new JFrame();

    // GUI components
    JSpinner localHistoryTimer;
    JCheckBox saveBeforeRun; 
    JCheckBox saveBeforeSwitch;
    JButton saveButton = new JButton("Save");

    public SettingScreen(){
        // set up JFrame
        frame.setSize(600, 470);
        frame.setContentPane(new JLabel(new ImageIcon("resources/background.jpg")));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setTitle("Setting");
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }

        // set up checkboxes and prompts for auto save 
        JLabel prompt1 = new JLabel("<html>save code before run: <html>");
        prompt1.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        prompt1.setForeground(Color.white);
        prompt1.setBounds(50, 50, 220, 80);
        frame.add(prompt1);
        
        saveBeforeRun = new JCheckBox();
        saveBeforeRun.setSelected(Setting.SAVE_BEFORE_RUN);
        saveBeforeRun.setBounds(400, 50, 50, 50);
        frame.add(saveBeforeRun);
        
        JLabel prompt2 = new JLabel("<html>save code when switching to another file: </html>");
        prompt2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        prompt2.setForeground(Color.white);
        prompt2.setBounds(50, 150, 320, 80);
        frame.add(prompt2);
        
        saveBeforeSwitch = new JCheckBox();
        saveBeforeSwitch.setSelected(Setting.SAVE_BEFORE_SWITCH);
        saveBeforeSwitch.setBounds(400, 150, 50, 50);
        frame.add(saveBeforeSwitch);
        
        // set up spinner for auto file history timer
        JLabel prompt3 = new JLabel("record file history every ");
        prompt3.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        prompt3.setForeground(Color.white);
        prompt3.setBounds(50, 250, 270, 50);
        frame.add(prompt3);

        localHistoryTimer = new JSpinner(new SpinnerNumberModel(2, 1, 60, 1));
        localHistoryTimer.setBounds(330, 250, 70, 50);
        localHistoryTimer.setFont(new Font("Serif", Font.PLAIN, 16));
        localHistoryTimer.setValue(Setting.HISTORY_SAVE_INTERVAL);
        frame.add(localHistoryTimer);

        JLabel prompt4 = new JLabel("minute(s)");
        prompt4.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        prompt4.setForeground(Color.white);
        prompt4.setBounds(410, 250, 150, 50);
        frame.add(prompt4);

        // set up the save button
        saveButton.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        saveButton.setBounds(200, 350, 200, 50);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // change setting
                Setting.SAVE_BEFORE_RUN = saveBeforeRun.isSelected();
                Setting.SAVE_BEFORE_SWITCH = saveBeforeSwitch.isSelected();
                Setting.HISTORY_SAVE_INTERVAL = (int)localHistoryTimer.getValue();

                // write to the setting file
                try {
                    FileWriter writer = new FileWriter("src/Data/setting.txt");
                    writer.write(Setting.SAVE_BEFORE_RUN ? "1" : "0");
                    writer.write("\n");
                    writer.write(Setting.SAVE_BEFORE_SWITCH ? "1" : "0");
                    writer.write("\n");
                    writer.write(Integer.toString(Setting.HISTORY_SAVE_INTERVAL));
                    writer.flush();
                    writer.close();

                    // print message
                    Controller.printToMessageArea("settings saved");
                } catch (IOException ex) {
                    Controller.printToMessageArea("can not save setting");
                }

            }
        });
        frame.add(saveButton);

        // the spinner will only accept valid input
        JFormattedTextField formattedTextField = ((JSpinner.DefaultEditor) localHistoryTimer.getEditor()).getTextField();
        ((NumberFormatter)formattedTextField.getFormatter()).setAllowsInvalid(false);

        frame.setVisible(false);
    }



    // getters and setters
    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JSpinner getLocalHistoryTimer() {
        return localHistoryTimer;
    }

    public void setLocalHistoryTimer(JSpinner localHistoryTimer) {
        this.localHistoryTimer = localHistoryTimer;
    }

    public JCheckBox getSaveBeforeRun() {
        return saveBeforeRun;
    }

    public void setSaveBeforeRun(JCheckBox saveBeforeRun) {
        this.saveBeforeRun = saveBeforeRun;
    }

    public JCheckBox getSaveBeforeSwitch() {
        return saveBeforeSwitch;
    }

    public void setSaveBeforeSwitch(JCheckBox saveBeforeSwitch) {
        this.saveBeforeSwitch = saveBeforeSwitch;
    }
}
