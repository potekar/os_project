package system;

import assembler.AsmHandler;
import memory.Ram;

public class SysMain {
    public static void main(String[] args) {
        //AsmHandler.instructionReader("src/programs/sum.asm");
        for(int i=100;i< Ram.NumOfFrames;i++)
        {
            Ram.frames[i] = 2;
        }

        while(true)
            ShellCommands.getCommand();
    }
}
