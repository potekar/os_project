package memory;

import java.util.ArrayList;

public class Block {

    public static int size  = 4;
    private ArrayList<String> content = new ArrayList<>();

    private int address;

    private boolean ocuppied;

    private String fileName;

    public Block(int address)
    {
        this.address = address;
        this.ocuppied = false;
    }


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public int getAddress() {
        return address;
    }

    public boolean isOcuppied() {
        return ocuppied;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setOcuppied(boolean ocuppied) {
        this.ocuppied = ocuppied;
    }
}
