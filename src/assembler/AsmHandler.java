package assembler;

import GUI.Controller;
import memory.Page;
import memory.Process;
import memory.Ram;
import system.ProcessState;
import system.ShellCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsmHandler {

    //private TextArea ta=c.getTextArea();
    public void instructionReader(Process proces)
    {
        List<String> asmFileLines = new ArrayList<>();
        Operations operations = new Operations(proces);


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

        Random r = new Random();
        /*instructionRunner(asmFileLines.get(0),proces,operations);
        proces.numExecutedInstructions +=1;*/
        long quantum = 4000;

        for (int i=0;i<asmFileLines.size();i++) {

            proces.waitForResume();

            long x = r.nextInt(4001) + 2000 + proces.getRemainingSleepTime();
            proces.setRemainingSleepTime(0);

            if (x < quantum) {
                quantum -= x;
                proces.quantumCheck -= x;
                try {
                    proces.sleep(x);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                instructionRunner(asmFileLines.get(i), proces, operations);
                proces.numExecutedInstructions += 1;

            } else if (x >quantum) {
                proces.setRemainingSleepTime(x - quantum);
                quantum = 4000;
                try {
                    proces.sleep(x-proces.getRemainingSleepTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                proces.quantumCheck = 0;
                instructionRunner(asmFileLines.get(i), proces, operations);
                proces.numExecutedInstructions += 1;
                try {
                    proces.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }

        /*if(proces.getRemainingSleepTime()>0)
        {
            try {
                proces.sleep(proces.getRemainingSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/

        proces.stanje = ProcessState.DONE;
    }

    private void instructionRunner(String instruction, Process proces, Operations operations)
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
                proces.setRezultat(Integer.parseInt(val));
                Controller.updateTa(proces.getProcessName() + ":" + val);

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
                proces.stanje = ProcessState.DONE;
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
