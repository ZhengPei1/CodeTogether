package View.CodeScreen;

import Controller.Controller;
import Model.Utility;
import View.FileChoosers.ExportFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

// this class is the GUI & Logic of creating new file & file export
public class FileGeneration {
    private JFrame frame = new JFrame();
    private JTextField fileName = new JTextField();

    // the constructor, mode = 0 - new file, mode = 1 - export file
    public FileGeneration(String rootDirectory, int mode) {
        // set up JFrame
        frame.setContentPane(new JLabel(new ImageIcon("resources/background.jpg")));
        frame.setResizable(false);
        frame.setSize(500, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setTitle("codeTogether - create new java file ");
        frame.setLocationRelativeTo(null);

        // set up title 
        JLabel title;
        if (mode == 0)
            title = new JLabel("Create java file in src", JLabel.CENTER);
        else
            title = new JLabel("Export file - File name", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 40));
        title.setForeground(Color.white);
        title.setBounds(50, 20, 400, 60);
        frame.add(title);

        // set up textArea for input
        fileName.setFont(new Font("Serif", Font.BOLD, 18));
        fileName.setForeground(Color.black);
        fileName.setBackground(Color.white);
        fileName.setBounds(20, 100, 340, 50);
        frame.add(fileName);

        // set up prompt 
        JLabel prompt = new JLabel(".java", JLabel.CENTER);
        prompt.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        prompt.setForeground(Color.white);
        prompt.setBounds(350, 100, 100, 40);
        frame.add(prompt);

        // set up submit button 
        JButton submitButton = new JButton("Create");
        submitButton.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 30));
        submitButton.setBounds(50, 140, 200, 50);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // error check - valid file name
                if (fileName.getText().contains(" ") || !Utility.isValidFileName(fileName.getText())) {
                    fileName.setBackground(Color.red);
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "Invalid file name!"+
                                    "</html>",
                            "File name error",
                            JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                } else if (fileName.getText() == null || Objects.equals(fileName.getText(), "")) {
                    fileName.setBackground(Color.red);
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "You Must Enter A File Name!"+
                                    "</html>",
                            "File name error",
                            JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                } else if (Character.isDigit(fileName.getText().charAt(0))) {
                    fileName.setBackground(Color.red);
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "Java file can't start with number" +
                                    "</html>",
                            "File name error",
                            JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                } else if (Character.isLowerCase(fileName.getText().charAt(0))) {
                    fileName.setBackground(Color.red);
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "Java file must start with a capital letter" +
                                    "</html>",
                            "File name error",
                            JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

                File fileProcessing;
                if (mode == 0) {
                    // locate file based on the given root directory - new file mode
                    fileProcessing = new File(rootDirectory + File.separator + "src" + File.separator + fileName.getText() + ".java");
                } else {
                    // locate file based on the selected root directory - export mode
                    ExportFile exportFile = new ExportFile();
                    String fileExportPath = exportFile.findExportPath();
                    // locate file based on the export path
                    fileProcessing = new File(fileExportPath + File.separator + fileName.getText() + ".java");
                }

                // error check - exist
                if (fileProcessing.exists()) {
                    fileName.setBackground(Color.red);
                    JOptionPane.showMessageDialog(new JFrame(),
                            "<html>" +
                                    "File with the same name already exists" +
                                    "</html>",
                            "File name error",
                            JOptionPane.ERROR_MESSAGE);
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }


                // create file
                try {
                    fileProcessing.createNewFile();
                    FileWriter writer = new FileWriter(fileProcessing);

                    // write different code based on mode
                    if (mode == 0) {
                        writer.write(
                                "public class " + fileName.getText() + " {\n" +
                                        "\t\n" +
                                        "}"
                        );
                    } else {
                        writer.write(Controller.getCode());

                    }
                    writer.flush();
                    writer.close();
                    Controller.printToMessageArea("File " + fileProcessing.getAbsolutePath() + " generated");
                    Controller.updateTreeView();
                    frame.dispose();
                } catch (IOException exception) {
                    // error message
                    fileName.setBackground(Color.red);
                    fileName.setText("Error, Could be Insufficient Access!");
                    Toolkit.getDefaultToolkit().beep();
                    exception.printStackTrace();
                }
            }
        });
        frame.add(submitButton);


        frame.setVisible(true);
    }
}
