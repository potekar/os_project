package memory;

public class Pointer {

    private Block block;
    private Pointer sledbenik;
    private Pointer prethodnik;

    public Pointer(Block b)
    {
        this.block = b;
    }

    public Block getBlock() {
        return block;
    }

    public void setSledbenik(Pointer sledbenik) {
        this.sledbenik = sledbenik;
    }

    public void setPrethodnik(Pointer prethodnik) {
        this.prethodnik = prethodnik;
    }

    public Pointer getSledbenik() {
        return sledbenik;
    }

    public Pointer getPrethodnik() {
        return prethodnik;
    }

    public void setBlock(Block block) {
        this.block = block;
    }


}
