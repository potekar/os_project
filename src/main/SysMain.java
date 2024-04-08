package main;

import assembler.AsmHandler;

public class SysMain {
    public static void main(String[] args) {
        AsmHandler.instructionReader("src/programs/sum.asm");
    }
}
