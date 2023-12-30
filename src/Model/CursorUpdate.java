package Model;

/*
when different people are editing the same file together, their cursor position will change when other
people add/delete texts. This class calculates the location the cursor should move to after insert/deleting texts
 */

public class CursorUpdate {
    private int oldTextLength;
    private int oldCursorPosition;
    private String textBeforeCursor;
    private String textAfterCursor;
    
    // constructor
    public CursorUpdate(int oldTextLength, int oldCursorPosition, String text) {
        setOldTextLength(oldTextLength);
        setOldCursorPosition(oldCursorPosition, text);
    }
    
    //
    public int findNewCursorPos(String newText){
        int newTextLength = newText.length();
        
        // default case: cursor position doesn't change
        int newCursorPosition = oldCursorPosition;
        
        // these two variables indicates where the change happened - before or after the cursor
        boolean changeBeforeCursor = !newText.contains(textBeforeCursor);
        boolean changeAfterCursor = !newText.contains(textAfterCursor);
        
        /*
        Decide cursor change
        1. if both text before and after the cursor changed, that means the cursor line is deleted, set cursor to 0
        2. if the change take place before the cursor, then the position of cursor needs to shift forward/backward
        3. if the change take place after the cursor, then we do not need to apply any change
         */

        if(changeAfterCursor && changeBeforeCursor)
            newCursorPosition = 0;

        if(changeBeforeCursor)
            newCursorPosition = oldCursorPosition + newTextLength - oldTextLength;

        // avoid potential error
        if(newCursorPosition < 0 || newCursorPosition > newTextLength){
            newCursorPosition = 0;
        }
        
        // update variables
        setOldCursorPosition(newCursorPosition, newText);
        setOldTextLength(newTextLength);
        return newCursorPosition;
    }
    
    // getters and setters
    public int getOldTextLength() {
        return oldTextLength;
    }
    
    public void setOldTextLength(int oldTextLength) {
        this.oldTextLength = oldTextLength;
    }
    
    public int getOldCursorPosition() {
        return oldCursorPosition;
    }
    
    
    public void setOldCursorPosition(int oldCursorPosition, String text) {
        this.oldCursorPosition = oldCursorPosition;
        textBeforeCursor = text.substring(0, oldCursorPosition);
        textAfterCursor = text.substring(oldCursorPosition);

    }
    
    // toString
    @Override
    public String toString() {
        return "CursorUpdate{" +
                "overallTextLength=" + oldTextLength +
                ", oldCursorPosition=" + oldCursorPosition +
                '}';
    }
}
