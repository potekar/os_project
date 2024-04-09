package system;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ShellCommands {
    static Scanner sc=new Scanner(System.in);
    static String currentDir=System.getProperty("user.dir");

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
                //TODO makes new directory
                break;

            case "run":
                //TODO runs a process
                break;

            case "exit":
                //TODO system shutdown
                break;

            case "rm":
                //TODO removes a directory or a file
                break;

            case "mem":
                //TODO memory state
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

    private static boolean changeDirectory(String dirName)
    {

        File dir=new File(currentDir);
        File[] firstLevelFiles=dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                if (aFile.isDirectory() && aFile.getName().equals(dirName))
                {
                    currentDir=aFile.getAbsolutePath().toString();
                    return true;
                }
            }
        }
        return false;
    }
}
