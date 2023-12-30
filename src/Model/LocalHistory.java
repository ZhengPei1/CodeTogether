package Model;

import Controller.Controller;
import View.HistoryScreen;

import java.io.*;
import java.util.*;

// this class provides methods for creating and fetching local histories
public class LocalHistory {

    // this method will add an entry in the history file
    public static void saveHistory(String fileEditing, String rootDirectory) {

        // create a localHistory folder in the selected directory
        File historyDirectory = new File(rootDirectory + File.separator + "localHistory");
        if (!historyDirectory.exists())
            historyDirectory.mkdir();

        // extract the file name of the file editing, find the corresponding history file
        File editing = new File(fileEditing);
        String fileName = editing.getName().substring(0, editing.getName().length() - 5) + ".txt";

        // find the history file
        File historyFile = new File(historyDirectory + File.separator + fileName);
        if (!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // add a new entry to the history file, append mode
        try {
            FileWriter writer = new FileWriter(historyFile, true);

            GregorianCalendar date = new GregorianCalendar();
            writer.write(date.get(Calendar.YEAR) + "/" + (date.get(Calendar.MONTH)+1) + "/" + date.get(Calendar.DATE) + "  " +
                    date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND) + "\n");
            writer.write(Controller.getCode());
            writer.write("\n-----\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Controller.printToMessageArea("can not make local history save");
        }
    }

    /*
    public static String loadHistory(String fileEditing, String rootDirectory) {
        ArrayList<String> extractedText = new ArrayList<String>();
        // find the local history directory
        File historyDirectory = new File(rootDirectory + File.separator + "localHistory");
        if (!historyDirectory.exists())
            historyDirectory.mkdir();

        // extract the file name of the file editing, find the corresponding history file
        File editing = new File(fileEditing);
        String fileName = editing.getName().substring(0, editing.getName().length() - 5) + ".txt";

        // find the history file
        File historyFile = new File(historyDirectory + File.separator + fileName);

        // read the file
        try {
            Scanner reader = new Scanner(historyFile);
            StringBuilder fileContent = new StringBuilder();

            // extract every entry separately
            while(reader.hasNextLine()){
                StringBuilder separateEntry = new StringBuilder();
                String nextLine;
                // read the history
                while(!Objects.equals(nextLine = reader.nextLine(), "-----")){
                    separateEntry.append(nextLine).append("\n");
                }
                separateEntry.append(nextLine).append("\n");
                extractedText.add(separateEntry.toString());
            }
            reader.close();

            // store entry in reverse
            for(int i = extractedText.size()-1; i >= 0; --i)
                fileContent.append(extractedText.get(i));

            return fileContent.toString();
        } catch (FileNotFoundException e) {
            Controller.printToMessageArea("can not load local history");
            return "can not load local history";
        }
    }

     */


    public static void loadHistory(String fileEditing, String rootDirectory){
        ArrayList<String> extractedHistory = new ArrayList<String>();
        ArrayList<String> extractedDate = new ArrayList<>();

        // find the local history directory
        File historyDirectory = new File(rootDirectory + "\\localHistory");
        if (!historyDirectory.exists())
            historyDirectory.mkdir();

        // extract the file name of the file editing, find the corresponding history file
        File editing = new File(fileEditing);
        String fileName = editing.getName().substring(0, editing.getName().length() - 5) + ".txt";

        // find the history file
        File historyFile = new File(historyDirectory + File.separator + fileName);
        if(!historyFile.exists()) {
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                Controller.printToMessageArea("can not find correct file path");
                return;
            }
        }

        // extract the history and date
        try {
            Scanner reader = new Scanner(historyFile);
            String nextLine = "";

            while(reader.hasNextLine()){
                StringBuilder temp = new StringBuilder();
                // read the date
                extractedDate.add(reader.nextLine());

                // read the history
                while(!Objects.equals(nextLine = reader.nextLine(), "-----")){
                    temp.append(nextLine).append("\n");
                }
                extractedHistory.add(temp.toString());
            }

            reader.close();
        } catch (FileNotFoundException e) {
            Controller.printToMessageArea("can not load local history");
            return;
        }

        // open the history screen
        new HistoryScreen(fileEditing, rootDirectory, extractedDate, extractedHistory);
    }


}
