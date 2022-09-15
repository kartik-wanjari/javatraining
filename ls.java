
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.*;

/**
 * @author azuga
 * This class mimics the wc command and its options.
 */
public class List {

    /**
     *This method returns the list of files and directories of current directory.
     * @param path - It represents the path of directory.
     */
    public static void ls(String path){
        File f = new File(path);
        File[] file = f.listFiles();     //getting list of files or directories from parent directory
        if(file == null){
            System.out.println("No files or directory present");
        }
        else{
            for (File i : file) {
                if (!i.isHidden()) {
                    System.out.printf(i.getName() + "\t");
                }
            }
        }

    }
    /**
     *This method returns the list of files and directories including hidden files
     * @param path - It represents the path of directory.
     */
    public static void ls_a(String path) {
        File f = new File(path);
        String[] arr = f.list();        //getting list of files or directories
        if(arr != null) {
            for (String i : arr) {
                System.out.printf(i + "\t");
            }
        }
    }

    /**
     *This method returns the list of files and directories in reverse order including files and their information
     * @param path - It represents the path of directory.
     */
    public static void ls_ltr(String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        Map<Long, File> mp = new TreeMap<>(Collections.reverseOrder());         //mapping time and file name and sorting in reverse order
        if(files != null) {
            for (File obj : files) {
                mp.put(obj.lastModified(), obj);
            }
        }
        SimpleDateFormat pdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        mp.forEach((key, value) -> {
            try {
                Path p = Path.of(value.getPath());
                PosixFileAttributes ats = Files.readAttributes(p, PosixFileAttributes.class);       //getting file attributes
                System.out.print(PosixFilePermissions.toString(ats.permissions()) + "\t");
                System.out.print(ats.owner().getName() + "\t");
                System.out.print(ats.group().getName() + "\t");
                System.out.print(ats.size() / 1024 + "kb \t");
                System.out.print(pdf.format(value.lastModified()) + "\t");
                System.out.print(value.getName() + "\t");
                System.out.println();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *This method returns the list of files and directories including hidden files and their information.
     * @param path - It represents the path of directory.
     */
    public static void ls_la(String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        try {
            SimpleDateFormat pdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            if(files != null) {
                for (File f1 : files) {
                    Path p = Path.of(f1.getPath());

                    PosixFileAttributes ats = Files.readAttributes(p, PosixFileAttributes.class);
                    System.out.print(PosixFilePermissions.toString(ats.permissions()) + "\t");
                    System.out.print(f1.isFile() ? 1 + "\t" : files.length + "\t");
                    System.out.print(ats.owner().getName() + "\t");
                    System.out.print(ats.group().getName() + "\t");
                    System.out.print(ats.size() + "\t");
                    System.out.print(pdf.format(f1.lastModified()) + "\t");
                    System.out.print(f1.getName());
                    System.out.println();
                }
            }


        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *This method returns the list of files and directories in sorted order
     * @param path - It represents the path of directory.
     */
    public static void ls_X(String path) {
        File f = new File(path);
        String[] arr = f.list();

        if(arr != null) {
            Arrays.sort(arr);
            for (String i : arr) {
                if (i.charAt(0) != '.') {
                    System.out.println(i);
                }
            }
        }
    }

    /**
     *This method returns the sorted list of files and directories by time.
     * @param path - It represents the path of directory.
     */
    public static void ls_t(String path) {
        File f = new File(path);
        File[] files = f.listFiles();
        Map<Long, String> mp = new TreeMap<>();
        if(files != null) {
            for (File obj : files) {
                mp.put(obj.lastModified(), obj.getName());
            }
        }
        SimpleDateFormat pdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        mp.forEach((key, value) -> System.out.println(pdf.format(key) + " " + value));
    }

    /**
     *This method returns the list of files and directories in reverse order by time.
     * @param path - It represents the path of directory.
     */
    private static void ls_T(String path) {
        File f = new File(path);
        File[] files =f.listFiles();
        Map<Long, String> mp = new TreeMap<>(Collections.reverseOrder());
        if(files != null){
            for (File obj : files) {
                mp.put(obj.lastModified(), obj.getName());
            }
        }

        SimpleDateFormat pdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        mp.forEach((key, value) -> System.out.println(pdf.format(key) + " " + value));
    }

    /**
     * @param args - it is used to take the input from the user as command line argument
     */
    public static void main(String[] args) {


        switch (args[0]) {
            case "ls":
                ls(args[1]);
                break;

            case "ls-a":
                ls_a(args[1]);

                break;

            case "ls-la":
                ls_la(args[1]);
                break;

            case "ls-ltr":
                ls_ltr(args[1]);
                break;
            case "ls-X":
                ls_X(args[1]);

            case "ls-t":
                ls_t(args[1]);

            case "ls-T":
                ls_T(args[1]);
        }

    }
}
