package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {
    private final static String folderPath = "resources/";
    private File file;
    private Scanner sc;

    public FileReader(String fileName) {
        this.file = new File(folderPath+fileName);
    }

    private void getScanner() {
        try {
            this.sc = new Scanner(this.file);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFount, try again");
        }
    }

    public void showFile() {
        this.getScanner();
        while(sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }
    }

    public List<String> getListFromFile() {
        List<String> tempList = new ArrayList<>();
        if (this.file == null) {
            return null;
        }
        this.getScanner();
        while(sc.hasNextLine()) {
            tempList.add(sc.nextLine());
        }

        return tempList;
    }
}
