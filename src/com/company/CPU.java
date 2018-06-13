package com.company;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by sydridgm on 11/2/16.
 */
public class CPU {

    // three simulation model
    public final static int FCFS = 1;
    public final static int JSF = 2;
    public final static int RR = 3;

    int model;

    // process id
    private int pid;

    // interval time of process's partition
    public long interval;

    // record which partition of process is executing
    private int anchor;

    // process list
    private LinkedList<Process> processes = new LinkedList<>();

    // current executing process
    private Process executingProcess;

    // io reference
    private IO io;

    // is cpu is idle
    public boolean isIdle = false;

    // sum of cpu active time
    public int useTime = 0;

    public void setIO (IO io) {
        this.io = io;
    }

    public void addLast(Process process) {
        processes.addLast(process);
    }

    // record waiting time in Ready queue for different processes
    private Map<Integer, Integer> recordWaitingTimeReadyQueue = new HashMap<>();

    // record waiting time in IO queue for different processes
    private Map<Process, Integer> recordWaitingTimeIOQueue = new HashMap<>();

    // when process add to IO, timer should skip to notify IO at current time if IO queue is empty
    boolean isSkip = false;
    // jsf
    double alpha;

    // rr
    int timeSlice;
    int timeSliceCounter;
    int contextSwitchTime;
    int contextSwitchTimeCounter=0;

    // writer
    FileWriter writer;

    public void notify(int currentTime) {

        if (currentTime == 0) {
            if(model == JSF)
                jsfSort(alpha);
            init(currentTime);
        }
        else {
            useTime++;
            interval--;
            timeSliceCounter--;
            uploadWaitingTime();

            if(model == RR) {
                if(timeSliceCounter == 0) {
                    if(interval <= 0) {
                        handleIntervalZero(currentTime);
                    }else {
                        if(!processes.isEmpty()) {
                            // do context switch
                            recordGanttInterval(true, false);
                            executingProcess.anchor_interval.put(anchor, interval);
                            this.addLast(executingProcess);
                            // to see if ready queue is empty
                            if (!processes.isEmpty())
                                init(currentTime);
                            else
                                isIdle = true;

                            contextSwitchTimeCounter = contextSwitchTime;
                        }
                    }
                    timeSliceCounter = timeSlice;
                }
                else
                    handleIntervalZero(currentTime);
            }else{
                handleIntervalZero(currentTime);
            }

        }

    }

    // init CPU
    public void init(int currentTime) {
        executingProcess = processes.removeFirst();
        pid = executingProcess.ID;
        anchor = executingProcess.anchor;

        try {
            writer.write(executingProcess.ID + " " + currentTime + " ");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(model != RR) {
            interval = executingProcess.structure.get(anchor);
        }else {
            if(executingProcess.anchor_interval.containsKey(anchor))
                interval = executingProcess.anchor_interval.get(anchor);
            else {
                interval = executingProcess.structure.get(anchor);
            }
            executingProcess.anchor_interval.put(anchor, interval);
            timeSliceCounter = timeSlice;
        }
    }

    public boolean isQueueEmpty() {
        if(processes.isEmpty())
            return true;
        else
            return false;
    }

    public void jsfSort(double alpha) {
        processes = utils.guess(processes, alpha);
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public void setWriter(FileWriter writer) {
        this.writer = writer;
    }

    public void setContextSwitchTime(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
    }

    public void uploadWaitingTime() {
        for(int i = 0; i < processes.size(); i++) {
            processes.get(i).waitingTimeReadyQueue++;
        }
    }

    public double getAverageWaitingTime() {
        int sum = 0;
        for(Map.Entry<Integer, Integer> record : recordWaitingTimeReadyQueue.entrySet()) {
            sum += record.getValue();
        }

        return sum * 1.0 / recordWaitingTimeReadyQueue.size();
    }

    public void printTurnaroundTime() {
        for(Map.Entry<Process, Integer> record : recordWaitingTimeIOQueue.entrySet()) {
            Process process = record.getKey();
            int waitingTimeIOQueue = record.getValue();

            long turnaroundTime = process.waitingTimeReadyQueue
                    + process.BurstTime
                    + process.waitingTimeIOQueue
                    + process.structure.size() * 60;

            System.out.println("id: " + process.ID + " turnaround time: " + turnaroundTime);
        }
    }

    // handle situation when process should add to IO queue and when process finished
    private void handleIntervalZero(int currentTime) {
        if(interval <= 0 && anchor != executingProcess.structure.size()-1) {
            if(model == RR){
                recordGanttInterval(false, true);
            }else
                recordGanttInterval(false, false);
            // process remember position of partition of self
            executingProcess.anchor++;

            if(io.isEmpty())
                isSkip = true;
            // put process into IO waiting queue
            io.addLast(executingProcess);

            if (!processes.isEmpty()) {
                // init next process
                init(currentTime);
            }
            else {
                isIdle = true;
            }
        }
        else if(interval <= 0 && anchor == executingProcess.structure.size() -1) {

            if(model == RR){
                recordGanttInterval(false, true);
            }else
                recordGanttInterval(false, false);

            recordWaitingTimeReadyQueue.
                    put(executingProcess.ID, executingProcess.waitingTimeReadyQueue);

            recordWaitingTimeIOQueue.
                    put(executingProcess, executingProcess.waitingTimeIOQueue);

            if (!processes.isEmpty()) {
                // init next process
                init(currentTime);
            } else {
                isIdle = true;
            }
        }

    }

    // record Gantt data in log file
    public void recordGanttInterval(boolean isTimeSliceEnd, boolean isRRProcessIOorEnd){
        try {
            if(isTimeSliceEnd){
                writer.write(timeSlice+"\n");
            }else{
                if(isRRProcessIOorEnd){
                    writer.write(executingProcess.anchor_interval.get(anchor)+"\n");
                }else
                    writer.write(executingProcess.structure.get(anchor)+"\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
