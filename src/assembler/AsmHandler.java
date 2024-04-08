package assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AsmHandler {

    public static void instructionReader(String filePath)
    {

        List<String> asmFileLines = null;

        try {
            asmFileLines = Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading ASM file: " + e.getMessage());
            System.exit(1);
        }

        for (String line : asmFileLines) {
            instructionRunner(line);
        }
    }

    private static void instructionRunner(String instruction)
    {
        String[] arr=instruction.split(" ");

        switch (arr[0]){
            case "ADD":
                Operations.add();
                break;

            case "SUB":
                Operations.sub();
                break;

            case "PUSH":
                Operations.push(arr[1]);
                break;

            case "POP":
                String val=Operations.pop();
                System.out.println(val);
                break;

            case "INC":
                Operations.inc();
                break;

            case "DEC":
                Operations.inc();
                break;
        }
    }
}
