import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Scheduler {

    public static void main(String[] args) {
        new Scheduler();
    }

    public Scheduler(){
        schedule(input());
    }

    private String[] input() {
        Scanner scan = new Scanner(System.in);

        // first line takes number of processes
        int num = Integer.parseInt(scan.nextLine());
        String[] input = new String[num];

        // scan each process [arrival priority cpu_time]
        for (int i = 0; i < input.length; i++) {
            input[i] = scan.nextLine();
        }

        scan.close();
        return input;
    }

    private void schedule(String[] in) {
        Process[] processes = new Process[in.length];
        for (int i = 0; i < in.length; i++) {
            String[] split = in[i].split(" ");
            int arrival = Integer.parseInt(split[0]);
            int priority = Integer.parseInt(split[1]);
            int cpuTime = Integer.parseInt(split[2]);
            processes[i] = new Process(arrival, priority, cpuTime);
        }

        LinkedList<Process> readyQ = new LinkedList<Process>();
        
        boolean done = false;
        String gantt = "";
        int time = 0;
        int procIndex = 0;        

        while (!done) {
            // Adds arriving processes to readyQ
            if (procIndex < processes.length && 
                processes[procIndex].getArrival() == time) {
                readyQ.add(processes[procIndex]);
                quickSort(readyQ);
                procIndex++;   
            }            
            
            // Checks if there is a tie, if tie do Round robin with a quantum of 2 if tie
            boolean tie = false;
            for (int i = 0; i < readyQ.size()-1; i++) {
                if (readyQ.get(i).getPriority() == readyQ.get(i+1).getPriority()) {
                    tie = true;
                    String last1 = "none";
                    String last2 = "none";
                    if (gantt.length() >= 1) {
                        last1 = gantt.substring(gantt.length()-1, gantt.length());
                    }
                    if (gantt.length() >= 2) {
                        last2 = gantt.substring(gantt.length()-2, gantt.length());
                    }
                    
                    // if last 2 processes are first in readyQ, do second process
                    if (last2.equals(Integer.toString(readyQ.get(i).getName()).repeat(2))) {
                        System.out.println("Does Process #" + readyQ.get(i+1).getName());
                        gantt += readyQ.get(i+1).getName();
                        readyQ.get(i+1).decrementCpuTime();
                        if (readyQ.get(i+1).isDone()) {
                            readyQ.get(i+1).setFinishTime(time);
                            readyQ.remove(i+1);
                        }
                        break;
                    // if last 2 processes are second in readyQ, do first process
                    } else if (last2.equals(Integer.toString(readyQ.get(i+1).getName()).repeat(2))) {
                        System.out.println("Does Process #" + readyQ.get(i).getName());
                        gantt += readyQ.get(i).getName();
                        readyQ.get(i).decrementCpuTime();
                        if (readyQ.get(i).isDone()) {
                            readyQ.get(i).setFinishTime(time);
                            readyQ.remove(i);
                        }
                        break;
                    } else if (last1.equals(Integer.toString(readyQ.get(i).getName()))) {
                        System.out.println("Does Process #" + readyQ.get(i).getName());
                        gantt += readyQ.get(i).getName();
                        readyQ.get(i).decrementCpuTime();
                        if (readyQ.get(i).isDone()) {
                            readyQ.get(i).setFinishTime(time);
                            readyQ.remove(i);
                        }
                        break;
                    } else if (last1.equals(Integer.toString(readyQ.get(i+1).getName()))) {
                        System.out.println("Does Process #" + readyQ.get(i+1).getName());
                        gantt += readyQ.get(i+1).getName();
                        readyQ.get(i+1).decrementCpuTime();
                        if (readyQ.get(i+1).isDone()) {
                            readyQ.get(i+1).setFinishTime(time);
                            readyQ.remove(i+1);
                        }
                        break;
                    }
                }
            }

            // Pre-emptive priority if no tie.
            if (readyQ.size() > 0 && !tie) {
                System.out.println("Does Process #" + readyQ.getFirst().getName());
                gantt += readyQ.getFirst().getName();
                readyQ.getFirst().decrementCpuTime();
                if (readyQ.getFirst().isDone()) {
                    readyQ.getFirst().setFinishTime(time);
                    readyQ.removeFirst();
                }
            }
            
            time++;
            if (time == 100000) {
                done = true;

                System.out.println("Gantt Chart: " + gantt);
                
                int sum = 0;
                int num = 0;
                for (Process p : processes) {
                    double turnaroundTime = p.getFinishTime() - p.getArrival();
                    System.out.println("Turnaround Time for Process #" + p.getName() + " = " + turnaroundTime);
                    // System.out.println(p.getFinishTime() + " - " + p.getArrival());
                    sum += turnaroundTime;
                    num++;
                }
                double avg = sum/num;
                System.out.println("Average Turnaround Time = " + avg);
            }
        }
    }

    private void quickSort(LinkedList<Process> list) {
        quickSort(list, 0, list.size()-1);
    }
    
    private void quickSort(LinkedList<Process> list, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(list, left, right);
            quickSort(list, left, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, right);
        }
    }

    private int partition(LinkedList<Process> list, int left, int right) {
        Process pivot = list.get(right);
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (list.get(j).getPriority() < pivot.getPriority()) {
                i++;
                Process temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        Process temp = list.get(i+1);
        list.set(i+1, list.get(right));
        list.set(right, temp);
        return i + 1;
    }
}