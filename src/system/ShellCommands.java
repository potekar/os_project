package system;

import GUI.Controller;
import memory.*;
import memory.Process;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ShellCommands {


    private static Scanner sc=new Scanner(System.in);
    private static String currentDir=System.getProperty("user.dir");
    //private static String currentDir="src\\programs";
    public static String trenutniDirNaziv;

    public StringBuilder sb=new StringBuilder();
    public static String getCurrentDir() {
        return currentDir;
    }

    //public static HashSet<ProcessPetko> threadSet = new HashSet<>();
    public static ArrayList<Process> threadSet = new ArrayList<>();
    public static void setCurrentDir(String currentDir) {
        ShellCommands.currentDir = currentDir;
    }

    private static ProcessScheduler scheduler;


    public String getCommand(String input)
    {
            String[] command = input.split(" ");
            sb.delete(0,sb.length());

            switch (command[0].toLowerCase()) {
                case "cd":
                    try {
                        if (command[1].equals("..")) {
                            File parrent = new File(new File(currentDir).getParent());
                            currentDir = parrent.getAbsolutePath().toString();
                            System.setProperty("user.dir", currentDir);

                            trenutniDirNaziv = currentDir.substring(currentDir.lastIndexOf("\\")+1);
                            return currentDir;

                        } else {
                            if (changeDirectory(command[1]))
                            {
                                return (currentDir);

                            }
                            else
                            {
                                return ("The system cannot find the path specified.");

                            }

                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        return (getCurrentDir());
                    }

                case "dir", "ls":
                    return listDirectory(currentDir);


                case "tree":
                    return listDirectoryTree(new File(currentDir), "");


                case "ps":
                    sb.append("Processes:\n");
                    for (Process t : threadSet) {
                        sb.append("Process name:" + t.getProcessName() + "; CurrentInstruciton: " + t.currentInstruction +
                                "; Number of executed instructions:" + t.numExecutedInstructions + "; Usage of RAM:" + t.getNumOfPages() + " frames"
                        +";  STANJE: " + t.stanje + "\n");
                    }
                    return sb.toString();

                case "mkdir", "md":
                    if(!makeDirectory(command[1]))
                    {
                        return "directory already exists.";
                    }


                case "run","run&save":
                    int id = 0;

                    if (!threadSet.isEmpty()) {
                        for (Process p : threadSet) {
                            if(command[1].contains("/"))
                            {
                                int index = command[1].lastIndexOf("/");

                                if (command[1].substring(index+1).equalsIgnoreCase(p.getProcessName().substring(0,p.getProcessName().length()-3))) {
                                     int x= p.getIdProces();
                                     if (x >= id)
                                        id = x + 1;
                                }

                            }else{
                                if (p.getProcessName().substring(0, command[1].length()).equalsIgnoreCase(command[1])) {
                                    int x = p.getIdProces();
                                    //p.getProcessName().substring(command[1].length() + 1, p.getProcessName().length() - 1);
                                    if (x >= id)
                                        id = x + 1;
                                }
                            }

                        }
                    }

                    String path;
                    if (command[1].contains("\\") || command[1].contains("/")) {
                        path = command[1];
                    } else {
                        path = currentDir + "\\" + command[1];
                    }

                    File dir=new File(path);
                    if(dir.exists()) {
                        Process t = new Process(path, id);
                        if (command.length != 3 &&  command[0].equalsIgnoreCase("run&save")) {
                            t.setSave(true);
                            t.setSaveFileName(command[2]);
                        }


                        threadSet.add(t);
                        ProcessScheduler.red.add(t);

                        if (scheduler == null) {
                            scheduler = new ProcessScheduler();
                            scheduler.start();
                        }

                    /*if(threadSet.size() == 1)
                    {
                        ProcessScheduler ps = new ProcessScheduler();
                        ps.start();
                    }*/
                        return "executed";
                    }
                    else{
                        return "path does not exist";
                    }



                case "shutdown":
                    exitProcedure();
                    break;





                case "rmdir":

                    if (removeDirectory(command[1])) {
                        return ("Directory has been removed successfully.");
                    } else {
                        return ("Directory hasn't been removed.");
                    }



                case "mem":
                    sb.append("Memory usage:\n");
                    for (int i = 0; i < Ram.NumOfFrames; i++) {
                        if (Ram.frames[i] == 1) {
                            sb.append("Frame " + i + " -> " + Ram.memory.get(i)+"\n");
                        }
                        if (Ram.frames[i] == 3) {
                            sb.append("Frame " + i + "; value:" + Ram.memory.get(i).getValue()+"\n");
                        }
                    }
                    return sb.toString();

                case "block":
                    for (int i = 0; i < threadSet.size(); i++)
                    {
                        if(threadSet.get(i).getProcessName().equalsIgnoreCase(command[1]))
                        {
                            threadSet.get(i).blockProcess();
                            return ("Process "+command[1]+" blocked");
                        }
                    }
                    return "Process not found";

                case "unblock":
                    for (int i = 0; i < threadSet.size(); i++)
                    {
                        if(threadSet.get(i).getProcessName().equalsIgnoreCase(command[1]))
                        {
                            threadSet.get(i).UnblockProcess();
                            return ("Process "+command[1]+" unblocked");
                        }
                    }
                    return "Process not found";


                case "help":
                    try {
                        return helpProcedure(command[1]);
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                       return helpProcedure();

                    }

                case "fs":
                    //slobodan prostor
                    sb.append("Freespace:\n ");
                    Pointer x = Disc.slobodanProstor;
                    int brojac = 0;
                    while(x.getSledbenik()!= null)
                    {
                        sb.append(x.getBlock().getAddress()).append("\n");
                        x = x.getSledbenik();
                        brojac++;
                        if(brojac == 25)
                            break;
                    }
                   return sb.toString();

                case "us":
                    sb.append("Used space:\n");
                    for(Block b:Disc.zauzetProstor) {
                        sb.append(b.getFileName() + "; " + b.getAddress() + "; " + b.getContent()).append("\n");
                    }
                    return sb.toString();

                case "fl":
                    //lista fajlova
                    return (Disc.listaFile.toString());

                case "clear","cls":
                    Controller.clear();
                    return "";


                default:
                    return ("'" + command[0] + "' is not recognized as an internal or external command");

            }
            return "";
    }

    private String listDirectoryTree(File dir,String indent) {
        //System.out.println(currentDir);
        File[] files = dir.listFiles();
        if (files == null) {
           sb.append(indent + dir.getName()+"\n");
            return sb.toString();
        }

        for (int i=0;i<files.length;i++) {
            String arrow="├────";
            if(i==files.length-1)
                arrow="└─────";
            if (files[i].isDirectory()) {
                sb.append(indent + arrow + files[i].getName()+"\n");
                listDirectoryTree(files[i], indent + "│    ");
            } else {
                sb.append(indent + arrow + files[i].getName()+"\n");
            }
        }

        return sb.toString();
    }

    private String listDirectory(String dirPath) {
        File dir = new File(dirPath);

        File[] firstLevelFiles = dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                if (aFile.isDirectory()) {
                    sb.append("[" + aFile.getName() + "]"+"\n");
                } else {
                    sb.append(aFile.getName()+"\n");
                }
            }
        }

        return sb.toString();
    }

    private static boolean changeDirectory(String dirName) {
        trenutniDirNaziv = dirName;

        File dir=new File(currentDir);

        File[] firstLevelFiles=dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                if (aFile.isDirectory() && aFile.getName().equals(dirName))
                {
                    currentDir=aFile.getAbsolutePath().toString();
                    System.setProperty("user.dir",currentDir);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean makeDirectory(String dirName) {
        File dir=new File(currentDir+'\\'+dirName);
        if(!dir.exists())
        {
            for(FileInMemory f:Disc.listaFile)
                if(f.getName().equals(trenutniDirNaziv))
                {
                    FileInMemory fim = new FileInMemory(dirName,0,f);
                    Disc.listaFile.add(fim);
                    f.getPodfajlovi().add(fim);
                    break;
                }

            return dir.mkdir();
        }
        else
        {
            System.out.println("directory already exists.");
        }
        return false;
    }



    private static boolean removeDirectory(String dirName)
    {
                File dir=new File(currentDir+'\\'+dirName);
                Iterator<FileInMemory> it = Disc.listaFile.iterator();

                while(it.hasNext())
                {

                    FileInMemory f = it.next();
                    if(f.getName().equals(dirName))
                    {
                        if(f.getContent().size()>0)

                        {     Iterator<Block> it1 = Disc.zauzetProstor.iterator();
                            while(it1.hasNext())
                            {   Block b = it1.next();
                                if(b.getFileName().equals(dirName)) {



                                    b.setContent(new ArrayList<>());
                                    b.setOcuppied(false);

                                    Pointer x = Disc.slobodanProstor;
                                    while (x.getSledbenik() != null) {

                                        if (x.getBlock().getAddress() < b.getAddress()) {
                                            if (x.getSledbenik() != null)
                                                x = x.getSledbenik();
                                            else {
                                                Pointer novi = new Pointer(b);
                                                novi.setPrethodnik(x);
                                                x.setSledbenik(novi);
                                                break;
                                            }
                                        }
                                        else {
                                            if (x.getPrethodnik() == null) {
                                                Pointer novi = new Pointer(b);
                                                novi.setSledbenik(x);
                                                x.setPrethodnik(novi);
                                                Disc.slobodanProstor = novi;

                                            } else {
                                                Pointer novi = new Pointer(b);
                                                novi.setSledbenik(x);
                                                novi.setPrethodnik(x.getPrethodnik());
                                                x.getPrethodnik().setSledbenik(novi);
                                                x.setPrethodnik(novi);

                                            }
                                            break;
                                        }
                                    }

                                    it1.remove();
                                }
                            }
                        }
                    it.remove();
                        break;
                    }
                }
                return dir.delete();
    }



    private static void exitProcedure() {
        System.exit(0);
    }

    private static String helpProcedure(String command) {
        switch (command){
            case "cd":
                return ("Displays the name of or changes the current directory \n" +
                        "CD <directory>\n" +
                        "CD <..>\n" +
                        "  ..   Specifies that you want to change to the parent directory.\n" +
                        "\n" +
                        "Type CD without parameters to display the current drive and directory.");

            case "dir","ls":
                return ("Displays a list of files and subdirectories in a directory.");

            case "tree":
                return ("Graphically displays the folder structure of a drive or path.");

            case "ps":
                return ("Lists current processes and their state.\n" +
                        "[Process name] [CurrentInstruciton] [Number of executed instructions] [Usage of RAM]");

            case "mkdir","md":
                return ("Creates a directory.\n" +
                        "\n" +
                        "MKDIR <directory name>\n" +
                        "MD <directory name>");


            case "rmdir","rd":
                return ("Removes (deletes) a directory.\n\n" +
                        "RMDIR <directory name>\n" +
                        "RD <directory name>");


            case "mem":
                return ("Shows current memory usage.\n" +
                        "[Frame] [Process name] [Page] [Page content] -> process memory usage\n" +
                        "[Frame] [Value] -> shared memory");


            case "shutdown":
                return ("Allows proper local shutdown of machine.");

            case "run":
                return("Starts a process.\n" +
                        "RUN <process name in current direcory>\n" +
                        "RUN <relative/absolute process path>");

            case "run&save":
                return("Starts a process and saves it in a txt file.\n" +
                        "RUN <process name in current direcory>\n" +
                        "RUN <relative/absolute process path>"+
                        "RUN&SAVE <process name> <saveFileName>");

            case "help":
                return ("Provides help information for commands.\n" +
                        "\n" +
                        "HELP [command]\n" +
                        "\n" +
                        "    command - displays help information on that command.");


            default:
                return ("'" + command + "' is not recognized as an internal or external command");
        }

    } //TODO dodati ostale komande

    private static String helpProcedure()
    {
        return  ("For more information on a specific command, type HELP <command-name>\n" +
                "CD             Displays the name of or changes the current directory.\n" +
                "DIR            Displays a list of files and subdirectories in a directory.\n" +
                "TREE           Graphically displays the directory structure of a drive or path.\n" +
                "PS             Lists current processes and their state.\n" +
                "MKDIR          Creates a directory.\n" +
                "RMDIR          Removes a directory.\n" +
                "MEM            Shows current memory usage.\n" +
                "SHUTDOWN       Allows proper local shutdown of machine.\n" +
                "RUN            Starts a process.\n" +
                "HELP           Provides Help information for commands.\n" +
                "BLOCK          Blocks a process.\n" +
                "UNBLOCK        Unblocks a process\n" +
                "LS             Displays a list of files and subdirectories in a directory."+
                "FS             Returns free space\n" +
                "FL             Returns file list\n" +
                "US             Returns used space\n"+
                "RUN&SAVE       Start a process and saves the result in a txt file");
    }

    public static String getOutput(String output)
    {
        return output;
    }

}
