package memory;

import assembler.AsmHandler;

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

    public ProcessPetko(String filePath,String id){
        this.processName = filePath;
        this.processName2 = filePath+id;
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

        /*for(int i=0;i<numOfPages;i++)
        {
            System.out.println("Page " + i+ " -> Frame "+PageTable.get(i));
        }*/


        AsmHandler.instructionReader(this);
        /*for(int i=0;i<2;i++)
        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/

       for(Integer i:PageTable.keySet())
       {
           Ram.frames[PageTable.get(i)] = 0;
       }
    }
}
