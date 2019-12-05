package com.ricarraf;

import utils.FileReader;

import java.util.*;
import java.util.stream.Collectors;

public class Day02 {

    public static final int NOUNVERB = 19690720;

    public enum Operations {
        ADD(1),
        MULTIPLY(2),
        HALT(99);

        int code;
        static Map<Integer, Operations> enumMap;

        Operations(int code) {
            this.code  = code;
        }

        public int geCode() {
            return this.code;
        }

        static {
            enumMap = new HashMap<>();
            for(Operations op : Operations.values()) {
                enumMap.put(op.geCode(), op);
            }
        }
    }

    public static int runIntCode(List<String> integers) {
        List<Integer> intCodes = integers.stream()
                .map(line->line.split(","))
                .flatMap(Arrays::stream)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        return processIntCode(intCodes, 0);
    }

    public static int processIntCode(List<Integer> intCodes, int startIndex) {

        if (startIndex+3 > intCodes.size() || startIndex+2 > intCodes.size() || startIndex+1>intCodes.size()) {
            return intCodes.get(0);
        }

        Operations operation = Operations.enumMap.get(intCodes.get(startIndex));
        int indexFirst = intCodes.get(startIndex+1);
        int indexSecond = intCodes.get(startIndex+2);
        int storeValueIndex = intCodes.get(startIndex+3);
        int valueResult=0;
        switch (operation) {
            case ADD:
                valueResult = intCodes.get(indexFirst) + intCodes.get(indexSecond);
                break;
            case MULTIPLY:
                valueResult = intCodes.get(indexFirst) * intCodes.get(indexSecond);
                break;
            case HALT:
                return intCodes.get(0);
        }

        intCodes.set(storeValueIndex, valueResult);
        return processIntCode(intCodes, startIndex+4);
    }

    public static int findNounAndVerb(List<String> integers) {
        List<Integer> intCodesOriginal = integers.stream()
                .map(line->line.split(","))
                .flatMap(Arrays::stream)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int noun = 0;
        int verb = 0;
        List<Integer> intCodes = new ArrayList<>(intCodesOriginal);
        intCodes.set(1, noun);

        while (noun <= 99) {
            if (processIntCode(intCodes, 0) > NOUNVERB) {
                --noun;
                break;
            }
            intCodes = new ArrayList<>(intCodesOriginal);
            ++noun;
            intCodes.set(1, noun);
        }

        intCodes = new ArrayList<>(intCodesOriginal);
        intCodes.set(1, noun);
        intCodes.set(2, verb);

        while (verb <= 99) {
            if (processIntCode(intCodes, 0) > NOUNVERB) {
                --verb;
                break;
            }
            intCodes = new ArrayList<>(intCodesOriginal);
            intCodes.set(1, noun);
            ++verb;
            intCodes.set(2, verb);
        }

        return 100*noun+verb;
    }

    public static void main(String[] args) {
        FileReader fileReaderA = new FileReader("02A_start.txt");
        List<String> data = fileReaderA.getListFromFile();
        System.out.println("The result code is: " + runIntCode(data));

        FileReader fileReaderB = new FileReader("02B.txt");
        List<String> dataB = fileReaderB.getListFromFile();
        System.out.println("The combination of noun plus verb is: " + findNounAndVerb(dataB));

    }

    //31 e 46
}
