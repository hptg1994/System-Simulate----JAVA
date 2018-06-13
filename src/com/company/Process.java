package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sydridgm on 11/2/16.
 */
public class Process {
    public int ID;

    // current burst time
    long BurstTime;

    // inter-I/O interval mean
    private int mean;

    // waiting time in ready queue
    public int waitingTimeReadyQueue = 0;

    // waiting time in IO queue
    public int waitingTimeIOQueue = 0;

    // one process has many burst separated by IO interrupt
    public ArrayList<Long> structure = new ArrayList<>();

    // index for current burst in structure
    public int anchor = 0;

    // used in RR model to record anchor-remain time pair
    public Map<Integer, Long> anchor_interval = new HashMap<>();

    // jsf
    public double guess;
    // process come from IO
    public boolean isAddFromIO = false;

    public Process(int ID,
                   int BurstTime,
                   int mean) {

        this.ID = ID;
        this.BurstTime = BurstTime;
        this.mean = mean;
        this.guess = mean;
//        this.Remian = BurstTime;

        init();

//        String s="";
//        for(int i = 0; i < structure.size(); i++) {
//            s += structure.get(i) + " ";
//        }
//
//        System.out.println(ID  + " " + BurstTime + " " + s);
    }

    // initialize process's duration, number of burst and burst length
    private void init() {
        long counter = BurstTime;
        while (true) {
            long interval = utils.getExpDis(mean);
            if(interval == 0) {
                interval = 1;
            }
            counter -= interval;

            if(counter > 0)
                structure.add(interval);
            else{
                structure.add(counter+interval);
                break;
            }
        }
    }

    // determine process go to cpu or IO
    boolean enterCPU() {
        if (structure.get(0)>0)
            return true;
        else
            return false;
    }

    // reset process
    public void reset() {
        anchor = 0;
        waitingTimeReadyQueue = 0;
        guess = mean;
        waitingTimeIOQueue = 0;
        isAddFromIO = false;
        anchor_interval.clear();
    }
}
