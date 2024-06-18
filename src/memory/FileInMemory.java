package memory;

import java.util.ArrayList;

public class FileInMemory {

        private String name;
        private int size;



        private FileInMemory parentFile;
        private ArrayList<FileInMemory> podfajlovi;

        private ArrayList<String> content;

        public FileInMemory(String fileName,int size,FileInMemory parentFile)
        {
            this.name = fileName;
            this.size = size;
            this.podfajlovi = new ArrayList<>();
            this.content = new ArrayList<>();
            this.parentFile = parentFile;
        }


    @Override
    public String toString() {
        return this.name;
    }

    public void addToPodfajl(FileInMemory file)
    {
        this.podfajlovi.add(file);
    }
    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<FileInMemory> getPodfajlovi() {
        return podfajlovi;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileInMemory getParentFile() {
        return parentFile;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPodfajlovi(ArrayList<FileInMemory> podfajlovi) {
        this.podfajlovi = podfajlovi;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public ArrayList<String> getContent() {
        return content;
    }
}
