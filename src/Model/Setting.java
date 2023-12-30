package Model;

import java.io.File;
import java.util.Scanner;

// This class contains static fields that represents different settings
public class Setting {
    public static boolean SAVE_BEFORE_RUN = true;
    public static boolean SAVE_BEFORE_SWITCH = true;
    // the time interval here is in minute
    public static int HISTORY_SAVE_INTERVAL = 2;


    // initialize setting from setting.txt
    public static void initializeSetting(){
        try{
            Scanner reader = new Scanner(new File("src/Data/setting.txt"));
            if(reader.hasNext())
                SAVE_BEFORE_RUN = reader.nextInt() == 1;
            if(reader.hasNext())
                SAVE_BEFORE_SWITCH = reader.nextInt() == 1;
            if(reader.hasNext())
                HISTORY_SAVE_INTERVAL = reader.nextInt();
            reader.close();
        }catch (Exception e){
            // do nothing
            e.printStackTrace();
        }
    }

}
