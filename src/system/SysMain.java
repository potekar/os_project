package system;

import assembler.AsmHandler;
import memory.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SysMain {

    public static void getData(Path p,FileInMemory file)
    {
        try {
            if(Files.isDirectory(p))
            {
                var stream = Files.newDirectoryStream(p);
                Disc.listaFile.add(file);
                for(Path p1:stream)
                {
                    if(Files.isDirectory(p1)) {
                        FileInMemory f =  new FileInMemory(p1.getFileName().toString(),0,file);
                        file.getPodfajlovi().add(f);
                        getData(p1,f);
                    }
                    else {
                        ////
                        List<String> content = Files.readAllLines(p1);
                        FileInMemory file1 = new FileInMemory(p1.getFileName().toString(),content.size(),file);
                        file1.setContent(new ArrayList<>(content));
                        Disc.listaFile.add(file1);
                        file.getPodfajlovi().add(file1);

                        double dummy = content.size()*1.0/Block.size;
                        int br  = (int) Math.ceil(dummy);
                        int index = 0;
                        for(int i=0;i<br;i++)
                        {
                            Block b = Disc.slobodanProstor.getBlock();
                            b.setFileName(file1.getName());
                            for(int y = 0;y<4;y++)
                            {
                                if(index <content.size()) {
                                    b.getContent().add(content.get(index));
                                    index++;
                                }
                                else
                                    break;
                            }

                            b.setOcuppied(true);
                            Disc.slobodanProstor = Disc.slobodanProstor.getSledbenik();
                            Disc.slobodanProstor.setPrethodnik(null);
                            Disc.zauzetProstor.add(b);
                            ////
                        }

                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        //AsmHandler.instructionReader("src/programs/sum.asm");
        for (int i = 100; i < Ram.NumOfFrames; i++) {
            Ram.frames[i] = 2;
        }

        int address =0 ;
        Disc.slobodanProstor = new Pointer(new Block(address));
        address++;
        Pointer pointer = new Pointer(new Block(address));
        Disc.slobodanProstor.setSledbenik(pointer);
        pointer.setPrethodnik(Disc.slobodanProstor);

        for(int i=1;i<1024;i++)
        {
            address++;
            Pointer p1 = new Pointer(new Block(address));
            pointer.setSledbenik(p1);
            p1.setPrethodnik(pointer);
            pointer = p1;
        }



        Path p = Paths.get("Disk");
        FileInMemory file = new FileInMemory(p.getFileName().toString(),0,null);
        getData(p,file);

       /* for(Block b:Disc.zauzetProstor)
        {
            System.out.println(b.getFileName() + "  " + b.getAddress() + "  " + b.getContent());

        }*/


        while(true)
            ShellCommands.getCommand();
    }
}
