package system;

import memory.ProcessPetko;
import memory.Ram;

import java.io.File;
import java.util.Scanner;

public class ShellCommands {
    private static Scanner sc=new Scanner(System.in);
    private static String currentDir=System.getProperty("user.dir");

    public static String getCurrentDir() {
        return currentDir;
    }

    public static void setCurrentDir(String currentDir) {
        ShellCommands.currentDir = currentDir;
    }

    public static void getCommand()
    {
        System.out.print(">>");
        String[] command=sc.nextLine().split(" ");

        switch (command[0].toLowerCase())
        {
            case "cd":

                if(command[1].equals(".."))
                {
                    File parrent= new File(new File(currentDir).getParent());
                    currentDir=parrent.getAbsolutePath().toString();
                    System.setProperty("user.dir",currentDir);
                    System.out.println(currentDir);
                }
                else {
                    if(changeDirectory(command[1]))
                        System.out.println(currentDir);
                    else
                        System.out.println("The system cannot find the path specified.");
                }
                break;

            case "dir", "ls":
                listDirectory(currentDir);
                break;

            case "dirtree":
                listDirectoryTree(new File(currentDir),"");
                break;

            case "ps":
                //TODO lista procese i osnovne informacije o njima: trenutna mašinska instrukcija, potrošnja RAM-a, broj izvršenih instrukcija itd.
                break;

            case "mkdir":
                makeDirectory(command[1]);
                break;

            case "run":
                //TODO runs a process
                ProcessPetko p = new ProcessPetko(command[1]);
                break;

            case "exit":
                exitProcedure();
                break;

            case "rm":
                if(removeDirectory(command[1]))
                {
                    System.out.println("Directory has been removed successfully.");
                }
                else
                {
                    System.out.println("Directory hasn't been removed.");
                }
                break;

            case "mem":
                //TODO memory state
                for(int i = 0; i< Ram.NumOfFrames; i++)
                {
                    if(Ram.frames[i] != 0)
                    {
                        System.out.println("Frame " + i+ " -> "+ Ram.memory.get(i));
                    }
                }
                break;

            default:
                System.out.println("'"+command[0]+"' is not recognized as an internal or external command.dir");;

        }

    }

    private static void listDirectoryTree(File dir,String indent) {
        //System.out.println(currentDir);
        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println(indent + dir.getName());
            return;
        }

        for (int i=0;i<files.length;i++) {
            String arrow="├────";
            if(i==files.length-1)
                arrow="└─────";
            if (files[i].isDirectory()) {
                System.out.println(indent + arrow + files[i].getName());
                listDirectoryTree(files[i], indent + "│    ");
            } else {
                System.out.println(indent + arrow + files[i].getName());
            }
        }
    }

    private static void listDirectory(String dirPath) {
        File dir = new File(dirPath);
        File[] firstLevelFiles = dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                if (aFile.isDirectory()) {
                    System.out.println("[" + aFile.getName() + "]");
                } else {
                    System.out.println(aFile.getName());
                }
            }
        }
    }

    private static boolean changeDirectory(String dirName) {

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
        if(dir.exists())
        {
            Scanner s=new Scanner(System.in);
            System.out.println("are you sure you want to remove "+dirName+ "[Y/N]");
            if(s.next().equalsIgnoreCase("y"))
            {
                return dir.delete();
            }
        }
        else
        {
            System.out.println("directory does not exist.");
        }
        return false;
    }

    private static void exitProcedure() {
        System.exit(0);
    }
}
