package View.FileChoosers;

import Controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

// this is a file chooser that deletes the chosen file
public class DeleteFile {

    private JFrame frame = new JFrame();
    private JFileChooser fileChooser = new JFileChooser();

    // constructor that initializes the file chooser
    public DeleteFile(String rootDirectory, String fileEditing) {

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // set up icon
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }

        // set up file chooser
        fileChooser.setDialogTitle("Choose the file you want to delete - codeTogether");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File(rootDirectory + File.separator + "src"));
        fileChooser.setMultiSelectionEnabled(true);

        // open the dialog
        int selection = fileChooser.showDialog(frame, "Delete");

        // attempt to delete the file
        if (selection == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();

            for(File file : files){
                if(file.getAbsolutePath().equals(fileEditing)){
                    Controller.printToMessageArea("you are attempting to delete a file that's currently opened in the coding area: action denied");
                    continue;
                }

                if (file.delete()) {
                    Controller.printToMessageArea("Deleted the file: " + file.getAbsolutePath());
                } else {
                    Controller.printToMessageArea("Failed to delete the file " + file.getAbsolutePath() + " Insufficient access," +
                            " try switching to another file");
                }
            }


            Controller.updateTreeView();

        } else {
            frame.dispose();
        }
    }

}
