package memory;

import java.util.ArrayList;

public class Disc {

    public static ArrayList<FileInMemory> listaFile = new ArrayList<>();

    public static int NumOfBlocks = 1024 / Block.size;

    public static int SIZE = 1024;

    public static ArrayList<Block> zauzetProstor = new ArrayList<>(NumOfBlocks);
    public static Pointer slobodanProstor;


}
