package View.CodeScreen;

import Controller.Controller;
import Model.Utility;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;

// this class creates a tree view of classes in the folder(file explorer)
public class FileTree extends JPanel {
    
    private JTree tree;
    public FileTree(File rootDirectory) {
        // set up the layout and tree
        setLayout(new GridLayout(1, 1));
        tree = new JTree(findNode(new DefaultMutableTreeNode(rootDirectory.getAbsolutePath()), rootDirectory));
        tree.setRowHeight(30);

        // expand the tree if it contains less than 5 folders
        if(tree.getRowCount()<=5){
            // expand all the nodes
            for (int i = 0; i < tree.getRowCount(); i++)
                tree.expandRow(i);
        }
        
        add(tree);
    }
    
    // using recursion to find root node and all the sub nodes
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parentNode, File directory){
        
        File[] allFiles = directory.listFiles();
        for(File file: allFiles){
            
            // if the file is a non-empty directory, perform recursion based on this directory
            if(file.isDirectory() && file.listFiles() != null){
                // only displays the src and localHistory directory
                if(file.getName().equals("src") || file.getName().equals("localHistory"))
                    parentNode.add(findNode(new DefaultMutableTreeNode(file.getName()), file));
            }
            else{
                // only displays java file
                if(file.getName().contains(".java"))
                    parentNode.add(new DefaultMutableTreeNode(file.getName()));
            }

        }
        
        return parentNode;
        
    }
    
    // update method - called when new files are created
    public void update(File rootDirectory){
        // update the tree by reestablishing the root node
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        model.setRoot(findNode(new DefaultMutableTreeNode(rootDirectory.getAbsolutePath()), rootDirectory));

        // expand all the nodes
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);
    }
    
    // getters and setters
    public JTree getTree() {
        return tree;
    }
    
    public void setTree(JTree tree) {
        this.tree = tree;
    }
}




           
         
    
    
    
  