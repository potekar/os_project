package memory;

import assembler.AsmHandler;
import system.ProcessScheduler;
import system.ProcessState;
import system.ShellCommands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ProcessPetko extends Thread{

    private Map<Integer,Integer> PageTable = new HashMap<>();

    public  Stack <String> stack= new Stack<>();

    private int numOfPages;
    private String processName;

    private String processName2;

    private boolean save = false;

    private int rezultat;

    private String saveFileName;

    private ArrayList<String> instructions = new ArrayList<>();

    public String currentInstruction;
    public int numExecutedInstructions = 0;

    public ProcessState stanje = ProcessState.READY;

    public int indikator = 0;

    public long quantumCheck = 4000;
    private long remainingSleepTime = 0;

    private int idProces;
    private final Object lock = new Object();

    public ProcessPetko(String filePath,int id){
        this.processName = filePath;
        int x = -1;
        this.idProces = id;
        if(filePath.contains("/"))
         x = filePath.lastIndexOf("/");
        else if(filePath.contains("\\"))
                x = filePath.lastIndexOf("\\");
        try{
            //this.processName2 = filePath.substring(ShellCommands.getCurrentDir().length()+1)+id;
            this.processName2 = this.processName.substring(x+1) + "(" +id+")";
        }
        catch (StringIndexOutOfBoundsException e)
        {
            this.processName2=filePath+"("+id+")";
        }
    }

    public int getIdProces() {
        return idProces;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public int getRezultat() {
        return rezultat;
    }

    public void setRezultat(int rezultat) {
        this.rezultat = rezultat;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public Map<Integer, Integer> getPageTable() {
        return PageTable;
    }



    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setPageTable(Map<Integer, Integer> pageTable) {
        PageTable = pageTable;
    }

    public void setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
    }




    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public String getProcessName() {
        return processName2;
    }

    public String getFilePath(){
        return this.processName;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    @Override
    public void run() {
        AsmHandler asmHandler=new AsmHandler();
        DMAConntroler.fromDiskToRam(this);

        asmHandler.instructionReader(this);

        for(Integer i:PageTable.keySet())
        {
            Ram.frames[PageTable.get(i)] = 0;
        }

        if(this.stanje == ProcessState.DONE) {
            ShellCommands.threadSet.remove(this);
            ProcessScheduler.red.remove(this);
        }

        if(this.save)
        {
            DMAConntroler.fromRamToDisk(this);
        }
    }

    public void pauseProcess() {
        synchronized (lock) {
            this.stanje = ProcessState.READY;
            quantumCheck = 4000;
        }
    }

    public void blockProcess() {
        synchronized (lock) {
            this.stanje = ProcessState.BLOCKED;
            quantumCheck = 4000;
        }
    }

    public void UnblockProcess() {
        synchronized (lock) {
            this.stanje = ProcessState.READY;
            //lock.notify();
        }
    }

    public void waitForUnblock() {
        synchronized (lock) {
            while (this.stanje == ProcessState.BLOCKED) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }



    public void resumeProcess() {
        synchronized (lock) {
           this.stanje = ProcessState.RUNNING;
           lock.notify();
        }
    }

    public void waitForResume() {
        synchronized (lock) {
            while (this.stanje != ProcessState.RUNNING) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void setRemainingSleepTime(long remainingSleepTime) {
        this.remainingSleepTime = remainingSleepTime;
    }

    public long getRemainingSleepTime() {
        return remainingSleepTime;
    }

    @Override
    public String toString() {
        return processName2;
    }
}
