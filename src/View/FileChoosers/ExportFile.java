package View.FileChoosers;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

// this is a file chooser that allows the user to choose where to export the file
public class ExportFile {
    
    private JFrame frame = new JFrame();
    
    // default directory is D disk
    private JFileChooser fileChooser = new JFileChooser("D:\\");
    
    // constructor that initializes the file chooser
    public ExportFile() {
        
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // set up file chooser
        fileChooser.setDialogTitle("Choose the directory to export your file to - codeTogether");
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
    public String findExportPath() {
        // open the dialog
        int selection = fileChooser.showSaveDialog(frame);
        
        // returns the chosen path
        if (selection == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else{
            frame.dispose();
        }
        return null;
    }
    
    
}
