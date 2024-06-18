package memory;

import system.ShellCommands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DMAConntroler {

    public static void fromDiskToRam(ProcessPetko procces)
    {
        try {
            procces.setInstructions((ArrayList<String>) Files.readAllLines(Paths.get(procces.getFilePath())));
        } catch (IOException e) {
            System.err.println("Error reading ASM file: " + e.getMessage());
        }

        procces.setNumOfPages(procces.getInstructions().size() / Page.SIZE);
        int br = 0;

        for(int i=0;i<procces.getNumOfPages();i++)
        {
            Page p = new Page(i,procces.getProcessName());
            int x = 0;

            while(x<Page.SIZE)
            {
                if(br<procces.getInstructions().size())
                    p.getContent().add(procces.getInstructions().get(br));
                x++;
                br++;
            }

            for(int j = 0;j< Ram.NumOfFrames;j++)
            {
                if(Ram.frames[j] == 0)
                {
                    Ram.frames[j] = 1;
                    Ram.memory.put(j,p);
                    procces.getPageTable().put(i,j);
                    break;
                }
            }
        }
    }


    public static void fromRamToDisk(ProcessPetko p)
    {   try {
        File newFile = new File(ShellCommands.getCurrentDir() + "\\" + p.getSaveFileName() + ".txt");
        if (!newFile.exists()) {
            for (FileInMemory f : Disc.listaFile)
                if (f.getName().equals(ShellCommands.trenutniDirNaziv)) {
                    FileInMemory fim = new FileInMemory(p.getSaveFileName(), 1, f);
                    Disc.listaFile.add(fim);
                    f.getPodfajlovi().add(fim);


                    newFile.createNewFile();

                    FileWriter fw = new FileWriter(newFile);
                    String poruka = "Rezultat izvrsavanja: " + p.getRezultat();
                    fw.write(poruka);
                    fw.close();
                    ArrayList<String> lista = new ArrayList<>();
                    lista.add(poruka);

                    ////
                    fim.setContent(lista);


                    Block b = Disc.slobodanProstor.getBlock();
                    b.setFileName(p.getSaveFileName());
                    b.getContent().add(poruka);

                    b.setOcuppied(true);
                        Disc.slobodanProstor = Disc.slobodanProstor.getSledbenik();
                        Disc.slobodanProstor.setPrethodnik(null);
                        Disc.zauzetProstor.add(b);
                        ////

                        break;

                } else {
                    System.out.println("directory already exists.");
                    return;
                }


        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    }
}
