package View.CodeScreen;

import Controller.Controller;
import ExternalCode.TextLineNumber;
import Model.Utility;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

// this is the main coding screen
public class CodeScreen {

    // the frame
    JFrame frame = new JFrame();

    //
    FileTree fileExplorer;

    //
    JPanel optionMenu = new JPanel();
    JButton saveButton = new JButton(Utility.imageResize("resources/saveButton.png", 50, 50));
    JButton newFileButton = new JButton(Utility.imageResize("resources/newFileButton.png", 50, 50));
    JButton inviteButton = new JButton(Utility.imageResize("resources/inviteButton.png", 50, 50));
    JButton runCodeButton = new JButton(Utility.imageResize("resources/runCodeButton.png", 50, 50));
    JButton helpButton = new JButton(Utility.imageResize("resources/informationButton.png", 50, 50));
    JButton disconnectButton = new JButton(Utility.imageResize("resources/disconnectButton.png", 50, 50));
    JButton exportButton = new JButton(Utility.imageResize("resources/exportButton.png", 50, 50));
    JButton deleteButton = new JButton(Utility.imageResize("resources/deleteButton.png", 50, 50));
    JButton settingButton = new JButton(Utility.imageResize("resources/settingButton.png", 50, 50));
    JButton historyButton = new JButton(Utility.imageResize("resources/fileHistoryButton.png", 50, 50));
    JButton[] buttonList;


    // coding area
    JPanel codingPane = new JPanel();
    JTextArea codeShareArea = new JTextArea(Utility.readCode(Controller.fileEditing));


    // the console panel that's placed at the bottom
    JPanel messagePanel = new JPanel();
    JTextArea messageTextArea = new JTextArea();

    public CodeScreen(String rootDirectory, int mode) {
        // set up JFrame
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setContentPane(new JLabel(new ImageIcon("resources/background.jpg")));
        frame.setLayout(new GridBagLayout());
        try {
            frame.setIconImage(ImageIO.read(new File("resources/codeTogetherIcon.png")));
        } catch (IOException e) {
            System.out.println("can not load icon");
        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        // based on the mode given, load different code screen
        // mode: 0 - offline, 1 - client, 2 - server owner
        loadMode(mode);

        // set up the file explorer area - only for offline and server owner
        if (mode == 0 || mode == 2) {
            // constrains
            constraints.weightx = 0.1;
            constraints.weighty = 1;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridheight = 8;
            constraints.gridwidth = 1;


            // set up
            fileExplorer = new FileTree(new File(rootDirectory));
            JScrollPane scrollPane = new JScrollPane(fileExplorer);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, constraints);
        }

        // set up option menu
        optionMenu.setLayout(new GridLayout(buttonList.length, 0, 0, 20));
        optionMenu.setOpaque(false);

        for (JButton button : buttonList) {
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setBorderPainted(true);
            button.setMinimumSize(new Dimension(50, 50));
            optionMenu.add(button);
        }

        saveButton.setToolTipText("save the current file");
        newFileButton.setToolTipText("create new Java file");
        runCodeButton.setToolTipText("run the current file");
        if (mode == 0)
            inviteButton.setToolTipText("create/join server");
        else {
            inviteButton.setToolTipText("view server info");
            inviteButton.setIcon(Utility.imageResize("resources/inviteButtonOnlineMode.png", 50, 50));
        }
        deleteButton.setToolTipText("delete a file");
        settingButton.setToolTipText("change program setting");
        historyButton.setToolTipText("view file history");
        helpButton.setToolTipText("more information");
        disconnectButton.setToolTipText("disconnect from the server");
        exportButton.setToolTipText("export this file to...");

        constraints.weightx = 0.1;
        constraints.weighty = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 8;
        constraints.gridwidth = 1;
        frame.add(optionMenu, constraints);

        // set up coding area
        codingPane.setLayout(new BorderLayout(0, 20));
        codingPane.setOpaque(false);

        codeShareArea.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 25));
        codeShareArea.setForeground(Color.white);
        codeShareArea.setBackground(Color.black);
        codeShareArea.setTabSize(2);

        JScrollPane scrollPanel = new JScrollPane(codeShareArea);
        scrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPanel.setRowHeaderView(new TextLineNumber(codeShareArea));


        // set up prompt in the coding area
        JLabel label1 = new JLabel("Shared Code", JLabel.CENTER);
        label1.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 40));
        label1.setForeground(Color.white);

        codingPane.add(label1, BorderLayout.PAGE_START);
        codingPane.add(scrollPanel, BorderLayout.CENTER);

        constraints.weightx = 0.8;
        constraints.weighty = 0.875;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 7;
        constraints.gridwidth = 8;
        frame.add(codingPane, constraints);

        // set up console panel
        messagePanel.setLayout(new BorderLayout(20, 0));
        messagePanel.setOpaque(false);

        JLabel messageAreaPrompt = new JLabel("Message", JLabel.CENTER);
        messageAreaPrompt.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 40));
        messageAreaPrompt.setForeground(Color.white);
        messagePanel.add(messageAreaPrompt, BorderLayout.PAGE_START);

        messageTextArea.setFont(new Font("Serif", Font.BOLD, 25));
        messageTextArea.setForeground(Color.white);
        messageTextArea.setBackground(Color.black);
        messageTextArea.setEditable(false);

        JScrollPane scrollPane2 = new JScrollPane(messageTextArea);
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane2.setRowHeaderView(new TextLineNumber(messageTextArea));
        messagePanel.add(scrollPane2, BorderLayout.CENTER);

        constraints.weightx = 0.8;
        constraints.weighty = 0.125;
        constraints.gridx = 2;
        constraints.gridy = 7;
        constraints.gridheight = 1;
        constraints.gridwidth = 8;
        frame.add(messagePanel, constraints);

        // set up the frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    // based on the mode given, load different code screen
    // mode: 0 - offline, 1 - client, 2 - server owner
    private void loadMode(int mode) {

        // load different buttons and frame titles
        if (mode == 0) {
            // offline
            buttonList = new JButton[]{
                    saveButton,
                    newFileButton,
                    deleteButton,
                    runCodeButton,
                    historyButton,
                    inviteButton,
                    settingButton,
                    helpButton
            };
            frame.setTitle("codeTogether - offline version");
        } else if (mode == 1) {
            // client
            buttonList = new JButton[]{
                    exportButton,
                    runCodeButton,
                    disconnectButton,
                    inviteButton,
                    helpButton
            };
            frame.setTitle("codeTogether - client version");
        } else {
            // server owner
            buttonList = new JButton[]{
                    saveButton,
                    newFileButton,
                    deleteButton,
                    disconnectButton,
                    runCodeButton,
                    historyButton,
                    inviteButton,
                    settingButton,
                    helpButton
            };
            frame.setTitle("codeTogether - server owner version");
        }

    }

    // getters and setters
    public FileTree getFileExplorer() {
        return fileExplorer;
    }

    public void setFileExplorer(FileTree fileExplorer) {
        this.fileExplorer = fileExplorer;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JButton[] getButtonList() {
        return buttonList;
    }

    public void setButtonList(JButton[] buttonList) {
        this.buttonList = buttonList;
    }

    public JTextArea getCodeShareArea() {
        return codeShareArea;
    }

    public void setCodeShareArea(JTextArea codeShareArea) {
        this.codeShareArea = codeShareArea;
    }

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public void setMessageTextArea(JTextArea messageTextArea) {
        this.messageTextArea = messageTextArea;
    }

    public JPanel getOptionMenu() {
        return optionMenu;
    }

    public void setOptionMenu(JPanel optionMenu) {
        this.optionMenu = optionMenu;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    public JButton getNewFileButton() {
        return newFileButton;
    }

    public void setNewFileButton(JButton newFileButton) {
        this.newFileButton = newFileButton;
    }

    public JButton getInviteButton() {
        return inviteButton;
    }

    public void setInviteButton(JButton inviteButton) {
        this.inviteButton = inviteButton;
    }

    public JButton getRunCodeButton() {
        return runCodeButton;
    }

    public void setRunCodeButton(JButton runCodeButton) {
        this.runCodeButton = runCodeButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public void setHelpButton(JButton helpButton) {
        this.helpButton = helpButton;
    }

    public JPanel getCodingPane() {
        return codingPane;
    }

    public void setCodingPane(JPanel codingPane) {
        this.codingPane = codingPane;
    }

    public JPanel getMessagePanel() {
        return messagePanel;
    }

    public void setMessagePanel(JPanel messagePanel) {
        this.messagePanel = messagePanel;
    }

    public JButton getDisconnectButton() {
        return disconnectButton;
    }

    public void setDisconnectButton(JButton disconnectButton) {
        this.disconnectButton = disconnectButton;
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public void setExportButton(JButton exportButton) {
        this.exportButton = exportButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public JButton getHistoryButton() {
        return historyButton;
    }

    public void setHistoryButton(JButton historyButton) {
        this.historyButton = historyButton;
    }

    public JButton getSettingButton() {
        return settingButton;
    }

    public void setSettingButton(JButton settingButton) {
        this.settingButton = settingButton;
    }
}

