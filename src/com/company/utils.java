package com.company;

import java.util.*;

/**
 * Created by sydridgm on 11/2/16.
 */
public class utils {

    static Random rand = new Random();

    //genearate exponential distributed by perticular mean
    public static long getExpDis(int mean) {

        double lambda = 1.0 / mean;
        return  Math.round(Math.log(1-rand.nextDouble())/(-lambda));
    }

    // get united distributed number between 2 to 4 minute
    public static int getUniDis() {
        int Min = 2 * 60 * 1000;
        int Max = 4 * 60 * 1000;
//
//        int Min = 100;
//        int Max = 150;
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }

    // use exponential average to guess next time burst
    public static LinkedList<Process> guess(LinkedList<Process> processes, double alpha) {
        LinkedList<Process> guessesList = new LinkedList<>();

        ArrayList<Pair> pairs = new ArrayList<>();

        for(int i = 0; i < processes.size(); i++) {

            Process process = processes.get(i);
            int anchor = process.anchor;
            Pair pair;
            // whether a process come from IO or not.
            // This influence whether using expoinential guess or not
            if(process.isAddFromIO) {
                process.guess = alpha * process.structure.get(anchor - 1)
                        + (1 - alpha) * process.guess;

                process.isAddFromIO = false;
                pair = new Pair(process.guess, process);
            } else {

                if(anchor > 0)
                    pair = new Pair(process.guess, process);
                else
                    pair = new Pair(process.structure.get(0), process);
            }

            pairs.add(pair);
        }

        while(!pairs.isEmpty()) {
            double min = pairs.get(0).index;
            int track = 0;
            for(int i = 1; i < pairs.size(); i++) {
                if(pairs.get(i).index < min) {
                    min = pairs.get(i).index;
                    track = i;
                }
            }

            Process process = pairs.get(track).process;
            guessesList.add(process);
            pairs.remove(track);
        }

        return guessesList;
    }
//
//    public static LinkedList<Process> sort(LinkedList<Process> processes) {
//        LinkedList<Process> sorted = new LinkedList<>();
//        while(!processes.isEmpty()) {
//            long min = processes.get(0).structure.get(0);
//            int track = 0;
//            for(int i = 1; i < processes.size(); i++) {
//                if(min > processes.get(i).structure.get(0)) {
//                    min = processes.get(i).structure.get(0);
//                    track = i;
//                }
//            }
//            Process process = processes.get(track);
//            sorted.add(process);
//            processes.remove(track);
//        }
//
//        return sorted;
//    }
}
