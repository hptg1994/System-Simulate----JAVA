package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by sydridgm on 11/2/16.
 */
public class Timer {
    CPU cpu;

    IO io;

    // the virtual timer
    int timer = 0;

    public Timer(CPU cpu,
                 IO io) {

        this.cpu = cpu;
        this.io = io;
    }

    FileWriter writer;

    // simulate FCFS model
    public void startFCFS() {

        // set up environment for FCFS
        cpu.setIO(io);
        cpu.setModel(CPU.FCFS);
        io.setCPU(cpu);

        File fcfsGantt = new File("fcfsGantt");
        if(fcfsGantt.exists())
            fcfsGantt.delete();

        try {
            writer = new FileWriter("fcfsGantt", true);
            cpu.setWriter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start runing simulate system
        while(true) {

            // notify cpu
            if(!cpu.isQueueEmpty() || !cpu.isIdle) {
                cpu.notify(timer);
            }

            // notify io
            if(!io.isEmpty() && !cpu.isSkip) {
                io.notify(timer);

                if(!cpu.isQueueEmpty() && cpu.isIdle) {
                    cpu.init(timer);
                    cpu.isIdle = false;
                }
            }

            if(cpu.isSkip)
                cpu.isSkip = false;

            // simulation finish
            if(cpu.isQueueEmpty() && io.isEmpty() && cpu.isIdle)
                break;

            timer++;
        }

        println("fcfs: ");
        println("cpu utilization: ");
//        println(getUtilization()+ " " + timer + " " +cpu.useTime);
        println(getUtilization()+"");
        println("average waiting time: ");
        println("" + cpu.getAverageWaitingTime());
        println("turnaround time: ");
        cpu.printTurnaroundTime();
        println("throughput time: ");
        printThroughput();

        println("");
    }

    // simulate JSF model
    public void startJSF(double alpha) {

        // set up environment for JSF
        cpu.setIO(io);
        cpu.setModel(CPU.JSF);
        cpu.setAlpha(alpha);

        io.setCPU(cpu);

        File fcfsGantt = new File("jsfGantt");
        if(fcfsGantt.exists())
            fcfsGantt.delete();

        try {
            writer = new FileWriter("jsfGantt", true);
            cpu.setWriter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start runing simulate system
        while(true) {

            if(!cpu.isQueueEmpty() || !cpu.isIdle) {
                cpu.notify(timer);
            }

            if(!io.isEmpty()  && !cpu.isSkip) {
                io.notify(timer);

                if(!cpu.isQueueEmpty() && cpu.isIdle) {
                    cpu.init(timer);
                    cpu.isIdle = false;
                }
            }

            if(cpu.isSkip)
                cpu.isSkip = false;

            if(cpu.isQueueEmpty() && io.isEmpty() && cpu.isIdle)
                break;

            timer++;
        }

        println("jfs: alpha = " + alpha);
        println("cpu utilization: ");
//        println(getUtilization()+" " + timer + " " + cpu.useTime);
        println(getUtilization()+"");
        println("average waiting time: ");
        println( "" + cpu.getAverageWaitingTime());
        println("turnaround time: ");
        cpu.printTurnaroundTime();
        println("throughput time: ");
        printThroughput();

        println("");
    }

    // simulate RR model
    public void startRR(int timeSlice, int contextSwitchTime) {

        // set up environment for RR
        cpu.setIO(io);
        cpu.setModel(CPU.RR);
        cpu.setTimeSlice(timeSlice);
        cpu.setContextSwitchTime(contextSwitchTime);
        io.setCPU(cpu);

        File fcfsGantt = new File("rrGantt");
        if(fcfsGantt.exists())
            fcfsGantt.delete();

        try {
            writer = new FileWriter("rrGantt", true);
            cpu.setWriter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start runing simulate system
        while(true) {

//            println(contextSwitchTime + "");
            if(cpu.contextSwitchTimeCounter == 0) {
                if (!cpu.isQueueEmpty() || !cpu.isIdle) {
                    cpu.notify(timer);
                }
            }else {
                cpu.contextSwitchTimeCounter--;
                cpu.uploadWaitingTime();
            }

            if(!io.isEmpty() && !cpu.isSkip) {
                io.notify(timer);

                if(!cpu.isQueueEmpty() && cpu.isIdle) {
                    cpu.init(timer);
                    cpu.isIdle = false;
                }
            }

            if(cpu.isSkip)
                cpu.isSkip = false;

            if(cpu.isQueueEmpty() && io.isEmpty() && cpu.isIdle)
                break;

            timer++;
        }

        println("rr: " + timeSlice);
        println("cpu uqtilization: ");
//        println(getUtilization()+ " " + timer + " " +cpu.useTime);
        println(getUtilization()+"");
        println("average waiting time: ");
        println("" + cpu.getAverageWaitingTime());
        println("turnaround time: ");
        cpu.printTurnaroundTime();
        println("throughput time: ");
        printThroughput();

//        println("");
    }

    public double getUtilization() {
        return cpu.useTime * 1.0 / timer;
    }

    private void printThroughput() {
       println(10.0 / timer * 1000 * 60 + "");
    }

    private void println(String s) {
        System.out.println(s);
    }
}
