package View.FileChoosers;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;

// this is a file chooser that allows the user to choose the folder being used as the project folder
public class OpenProject {
    
    private JFrame frame = new JFrame();
    
    // default directory is C disk
    private JFileChooser fileChooser = new JFileChooser("C:\\");
    
    // constructor that initializes the file chooser
    public OpenProject(){
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // set up icon
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }
    
        // set up file chooser
        fileChooser.setDialogTitle("Choose your project folder - codeTogether");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }
    
            @Override
            public String getDescription() {
                return "Directory";
            }
        });

    }
    
    // this method returns the path to the folder the user has selected
    public String chosenDirectory(){
        // open the dialog
        int selection = fileChooser.showOpenDialog(frame);
        
        // returns the chosen path
        if(selection == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getAbsolutePath();
        else
                System.exit(0);
        
        return null;
    }

    
}
