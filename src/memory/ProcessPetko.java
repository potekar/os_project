package memory;

import assembler.AsmHandler;
import system.ProcessScheduler;
import system.ProcessState;
import system.ShellCommands;

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

    private ArrayList<String> instructions = new ArrayList<>();

    public String currentInstruction;
    public int numExecutedInstructions = 0;

    public ProcessState stanje = ProcessState.READY;

    public int indikator = 0;

    public long quantumCheck = 4000;
    private long remainingSleepTime = 0;
    private final Object lock = new Object();

    public ProcessPetko(String filePath,String id){
        this.processName = filePath;
        try{
            this.processName2 = filePath.substring(ShellCommands.getCurrentDir().length()+1)+id;
        }
        catch (StringIndexOutOfBoundsException e)
        {
            this.processName2=filePath+id;
        }
    }

    public String getProcessName() {
        return processName2;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    @Override
    public void run() {
        try {
            this.instructions = (ArrayList<String>) Files.readAllLines(Paths.get(this.processName));
        } catch (IOException e) {
            System.err.println("Error reading ASM file: " + e.getMessage());
        }

        this.numOfPages = this.instructions.size() / Page.SIZE;
        int br = 0;

        for(int i=0;i<numOfPages;i++)
        {
            Page p = new Page(i,processName2);
            int x = 0;

            while(x<Page.SIZE)
            {
                if(br<this.instructions.size())
                    p.getContent().add(this.instructions.get(br));
                x++;
                br++;
            }

            for(int j = 0;j< Ram.NumOfFrames;j++)
            {
                if(Ram.frames[j] == 0)
                {
                    Ram.frames[j] = 1;
                    Ram.memory.put(j,p);
                    this.PageTable.put(i,j);
                    break;
                }
            }
        }

        AsmHandler.instructionReader(this);

        for(Integer i:PageTable.keySet())
        {
            Ram.frames[PageTable.get(i)] = 0;
        }

        if(this.stanje == ProcessState.DONE) {
            ShellCommands.threadSet.remove(this);
            ProcessScheduler.red.remove(this);
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
            lock.notify();
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
