package memory;

import assembler.AsmHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessPetko {

    private Map<Integer,Integer> PageTable = new HashMap<>();

    private int numOfPages;

    private String processName;

    private ArrayList<String> instructions = new ArrayList<>();

    public ProcessPetko(String filePath){
        this.processName = filePath;

        try {
            this.instructions = (ArrayList<String>) Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading ASM file: " + e.getMessage());
        }

        this.numOfPages = this.instructions.size() / Page.SIZE;
        int br = 0;

        for(int i=0;i<numOfPages;i++)
        {
            Page p = new Page(i,processName);
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

        for(int i=0;i<numOfPages;i++)
        {
            System.out.println("Page " + i+ " -> Frame "+PageTable.get(i));
        }
        AsmHandler.instructionReader(filePath);
    }


}
