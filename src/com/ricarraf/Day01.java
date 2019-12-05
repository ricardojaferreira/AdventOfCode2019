package com.ricarraf;

import utils.FileReader;
import java.util.List;

public class Day01 {

    public static int extraFuelPerMass(int n) {

        n = n/3 -2;
        if (n/3 <= 2) {
            return n;
        }
        return n + extraFuelPerMass(n);
    }

    public static int massCalculator(List<String> data) {

        return data.stream()
                .map(Integer::parseInt)
                .map(n -> n = n/3 - 2)
                .reduce(0, Integer::sum);
    }

    public static int extraFuelCalculator(List<String> data) {

        return data.stream()
                .map(Integer::parseInt)
                .map(Day01::extraFuelPerMass)
                .reduce(0, Integer::sum);
    }

    public static void main(String[] args) {

        FileReader fileReaderA = new FileReader("01.txt");
        List<String> data = fileReaderA.getListFromFile();
        int result = massCalculator(data);
        System.out.println("Total mass is: " + result);

        FileReader fileReaderB = new FileReader("01B.txt");
        List<String> dataB = fileReaderB.getListFromFile();

        System.out.println("Total extra fuel is: " + extraFuelCalculator(dataB));
    }
}
