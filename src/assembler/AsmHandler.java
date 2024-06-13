package assembler;

import memory.Page;
import memory.ProcessPetko;
import memory.Ram;
import system.ShellCommands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AsmHandler {

    public static void instructionReader(ProcessPetko proces)
    {

        List<String> asmFileLines = new ArrayList<>();
        Operations operations = new Operations(proces);

        /*try {
            asmFileLines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading ASM file: " + e.getMessage());
            System.exit(1);
        }*/

        for(int i=0;i<100;i++)
        {
            if(Ram.frames[i] == 1 && Ram.memory.get(i).getProcessName().equalsIgnoreCase(proces.getProcessName()))
            {
                Page p  = Ram.memory.get(i);
                for(int j=0;j<Page.SIZE;j++)
                {
                    if(p.getContent().get(j) != null)
                        asmFileLines.add(p.getContent().get(j));
                }
            }
        }

        for (String line : asmFileLines) {
            instructionRunner(line,proces,operations);
            proces.numExecutedInstructions +=1;
            try {
                proces.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void instructionRunner(String instruction,ProcessPetko proces,Operations operations)
    {
        proces.currentInstruction = instruction;
        String[] arr=instruction.split(" ");


        switch (arr[0]){
            case "ADD":
                operations.add();
                break;

            case "SUB":
                operations.sub();
                break;

            case "MUL":
                operations.mul();
                break;

            case "DIV":
                operations.div();
                break;

            case "PUSH":
                operations.push(arr[1]);
                break;

            case "POP":
                String val=operations.pop();
                System.out.println(proces.getProcessName() + ":" + val);
                System.out.print(">>");
                for(int i=100;i< Ram.NumOfFrames;i++)
                {
                    if(Ram.frames[i] == 2)
                    {
                        Ram.frames[i] = 3;
                        Page p = new Page(Integer.parseInt(val));
                        Ram.memory.put(i,p);
                        break;
                    }
                }
                ShellCommands.threadSet.remove(proces);
                break;

            case "INC":
                operations.inc();
                break;

            case "DEC":
                operations.inc();
                break;
        }
    }
}
