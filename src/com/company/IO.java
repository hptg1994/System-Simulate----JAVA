package com.company;

import java.util.LinkedList;

/**
 * Created by sydridgm on 11/2/16.
 */
public class IO {
    private LinkedList<Process> processes = new LinkedList<>();

    int waitingTime = 60;

    private CPU cpu;

    public void setCPU(CPU cpu) {
        this.cpu = cpu;
    }

    public void addLast(Process process) {
        processes.addLast(process);
    }

    public void notify(int currentTime) {
        if (currentTime != 0 && !processes.isEmpty()) {
            waitingTime--;
            uploadWaitingTime();
            if (waitingTime == 0) {
                Process process = processes.getFirst();
                process.isAddFromIO = true;
                cpu.addLast(process);
                if(cpu.model == CPU.JSF)
                    cpu.jsfSort(cpu.alpha);
                processes.removeFirst();

                waitingTime = 60;
            }
        }
    }

    public boolean isEmpty() {
        if(processes.isEmpty())
            return true;
        else
            return false;
    }

    private void uploadWaitingTime() {
        for(int i = 0; i < processes.size()-1; i++)
            processes.get(i).waitingTimeIOQueue++;
    }
}
