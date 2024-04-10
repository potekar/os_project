package memory;

import java.util.ArrayList;

public class Page {


    public static final int SIZE = 2;
    private ArrayList<String> content;

    private String processName;
    //private boolean occupied;
    private int PageNumber;


    public Page(int PageNumber,String processName)
    {
        //this.occupied = false;
        this.PageNumber = PageNumber;
        this.content = new ArrayList<>(SIZE);
        this.processName = processName;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public String getProcessName() {
        return processName;
    }

    @Override
    public String toString() {
        return "Process:" + this.processName +" -> "+"Page " + this.PageNumber + " : content = " + this.getContent();
    }
}
