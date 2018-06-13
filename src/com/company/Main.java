package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    CPU cpu;

    IO io;

    LinkedList<Process> processes = new LinkedList<>();

    public static void main(String[] args) {

        Main main = new Main();

        // 10 inter-I/O interval means for 10 processes
        int[] means = {30, 35, 40, 45, 50, 55, 60, 65, 70, 75};

        /*
         * shuffle the means to simulate process coming to cpu
         * is not in a increase inter-I/O interval mean
         */
        shuffleArray(means);

        // init processes
        for(int i = 0; i < means.length; i++) {
            Process process = new Process(i + 1, utils.getUniDis(), means[i]);
            main.processes.add(process);
        }

        // fcfs
        main.fcfs(means);

        // jfs
        ArrayList<Double> alphas = new ArrayList<>();

        double alpha = 0;
        for(int i = 0; i < 100; i++) {
            alpha += 0.0001;
            alphas.add(alpha);
        }
//
//        alphas.add(1.0);
//        alphas.add(1.0/2);
//        alphas.add(1.0/3);

        for(int i = 0; i < alphas.size(); i++) {
            main.jsf(means, alphas.get(i));
        }

        // rr
        ArrayList<Integer> timeSlices = new ArrayList<>();

        int timeSlice = 0;
        for(int i = 0; i < 800; i++) {
            timeSlice+=1;
            timeSlices.add(timeSlice);
        }

//        timeSlices.add(1);


        for(int i = 0; i < timeSlices.size(); i++) {
            main.rr(means, timeSlices.get(i), 1);
        }
    }

    // initialize cpu, io, and reset 10 processes
    private void init(int[] means) {

        cpu = new CPU();

        io = new IO();

        for(int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            process.reset();

            if(process.enterCPU()) {
                cpu.addLast(process);
            }
            else
                io.addLast(process);
        }
    }

    // start fcfs model
    private void fcfs(int[] means) {
        init(means);
        Timer timer = new Timer(cpu, io);
        timer.startFCFS();
    }

    // start jsf model
    private void jsf(int[] means,
                     double alpha) {

        init(means);
        Timer timer = new Timer(cpu,io);
        timer.startJSF(alpha);
    }

    // start rr model
    private void rr(int[] means,
                    int timeSlice,
                    int contextSwitchTime) {

        init(means);
        Timer timer = new Timer(cpu, io);
        timer.startRR(timeSlice, contextSwitchTime);
    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
