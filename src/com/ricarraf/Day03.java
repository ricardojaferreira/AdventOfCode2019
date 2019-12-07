package com.ricarraf;

import com.sun.tools.javac.util.Pair;
import utils.FileReader;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03 {

    public static class GenerateRoutes implements Runnable {

        private List<String> route;
        public Set<Pair<Integer, Integer>> finalRoute;

        public GenerateRoutes(List<String> route) {
            this.route = route;
        }

        public void run() {
           finalRoute = this.generateRoute();
        }

        private Set<Pair<Integer, Integer>> generateRoute() {

            List<Pair<Character, Integer>> movements = this.route.stream()
                    .map( x -> {
                        Character direction = x.charAt(0);
                        Integer movement = Integer.parseInt(x.substring(1));
                        return new Pair<>(direction, movement);
                    }).collect(Collectors.toList());

            List<Pair<Integer, Integer>> newRoute = new ArrayList<>();
            newRoute.add(new Pair<>(1,1));

            movements.forEach(pair -> addPath(newRoute, pair));
            newRoute.remove(0);

            return new HashSet<>(newRoute);
        }
    }

    public static int findBetterIntersection(List<String> wireGrid) throws InterruptedException {

        List<String> wireA = Arrays.asList(wireGrid.get(0).split(","));
        List<String> wireB = Arrays.asList(wireGrid.get(1).split(","));

        List<Pair<Integer, Integer>> routeA = generateRoute(wireA);
        List<Pair<Integer, Integer>> routeB = generateRoute(wireB);

        return routeA.stream()
                .filter(x -> routeB.indexOf(x) != -1)
                .mapToInt(x -> routeA.indexOf(x) + routeB.indexOf(x) + 2)
                .min()
                .orElse(-1);
    }

    public static int findClosestIntersection(List<String> wireGrid) throws InterruptedException {

        List<String> wireA = Arrays.asList(wireGrid.get(0).split(","));
        List<String> wireB = Arrays.asList(wireGrid.get(1).split(","));

        GenerateRoutes generatedRouteA = new GenerateRoutes(wireA);
        GenerateRoutes generatedRouteB = new GenerateRoutes(wireB);
        Thread threadA = new Thread(generatedRouteA);
        Thread threadB = new Thread(generatedRouteB);
        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        Set<Pair<Integer, Integer>> routeA = generatedRouteA.finalRoute;
        Set<Pair<Integer, Integer>> routeB = generatedRouteB.finalRoute;

        List<Pair<Integer, Integer>> route = new ArrayList<>(routeA);
        route.addAll(routeB);

        return route.stream()
                .filter(pair -> Day03.elementIsDuplicated(route, pair))
                .mapToInt(pair -> Math.abs(1-pair.fst)+Math.abs(1-pair.snd))
                .min()
                .orElse(-1);
    }

    private static Boolean elementIsDuplicated(List<Pair<Integer, Integer>> list, Pair<Integer, Integer> pair) {

        long numberOfOccurences = list.stream()
                .filter(x -> x.equals(pair))
                .count();

        return numberOfOccurences > 1;
    }

    private static List<Pair<Integer, Integer>> generateRoute(List<String> wire) {

        List<Pair<Character, Integer>> movements = wire.stream()
                .map( x -> {
                    Character direction = x.charAt(0);
                    Integer movement = Integer.parseInt(x.substring(1));
                    return new Pair<>(direction, movement);
                }).collect(Collectors.toList());

        List<Pair<Integer, Integer>> route = new ArrayList<>();
        route.add(new Pair<>(1,1));

        movements.forEach(pair -> addPath(route, pair));
        route.remove(0);

        return route;
    }

    private static void addPath(List<Pair<Integer, Integer>> route, Pair<Character, Integer> coordinate) {

        Pair<Integer, Integer> actualPosition = route.get(route.size()-1);
        switch (coordinate.fst) {
            case 'R':
                route.addAll(moveRight(actualPosition, coordinate.snd));
                break;
            case 'L':
                route.addAll(moveLeft(actualPosition, coordinate.snd));
                break;
            case 'D':
                route.addAll(moveDown(actualPosition, coordinate.snd));
                break;
            case 'U':
                route.addAll(moveUp(actualPosition, coordinate.snd));
                break;
        }
    }

    private static Collection<? extends Pair<Integer, Integer>> moveUp(Pair<Integer, Integer> actualPosition, Integer movement) {

        return IntStream.rangeClosed(1, movement)
                .mapToObj(x -> new Pair<>(actualPosition.fst, actualPosition.snd+x))
                .collect(Collectors.toList());
    }

    private static Collection<? extends Pair<Integer, Integer>> moveDown(Pair<Integer, Integer> actualPosition, Integer movement) {

        return IntStream.rangeClosed(1, movement)
                .mapToObj(x -> new Pair<>(actualPosition.fst, actualPosition.snd-x))
                .collect(Collectors.toList());
    }

    private static Collection<? extends Pair<Integer, Integer>> moveLeft(Pair<Integer, Integer> actualPosition, Integer movement) {

        return IntStream.rangeClosed(1, movement)
                .mapToObj(x -> new Pair<>(actualPosition.fst-x, actualPosition.snd))
                .collect(Collectors.toList());
    }

    private static Collection<? extends Pair<Integer, Integer>> moveRight(Pair<Integer, Integer> actualPosition, Integer movement) {

        return IntStream.rangeClosed(1, movement)
                .mapToObj(x -> new Pair<>(actualPosition.fst+x, actualPosition.snd))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws InterruptedException {
        FileReader fileReaderA = new FileReader("03A.txt");
        List<String> data = fileReaderA.getListFromFile();
        System.out.println("The closest Match is: " + findClosestIntersection(data));
        System.out.println("The better interaction is: " + findBetterIntersection(data));
    }
}
