package memory;

import java.util.ArrayList;

public class Page {


    public static final int SIZE = 2;
    private ArrayList<String> content;

    private String processName;
    //private boolean occupied;
    private int PageNumber;

    private int value;


    public Page(int PageNumber,String processName)
    {
        //this.occupied = false;
        this.PageNumber = PageNumber;
        this.content = new ArrayList<>(SIZE);
        this.processName = processName;
    }

    public Page(int value)
    {
        this.value = value;
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

    public int getValue()
    {
        return value;
    }

    @Override
    public String toString() {
        return "Process:" + this.processName +" -> "+"Page " + this.PageNumber + " : content = " + this.getContent();
    }
}
