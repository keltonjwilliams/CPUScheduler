public class Process {
    
    private int name;
    private int arrival;
    private int priority;
    private int cpuTime;
    private int finishTime;

    private static int uniqueNum = 1;


    public Process(int arrival, int priority, int cpuTime) {
        name = uniqueNum;
        uniqueNum++;

        this.arrival = arrival;
        this.priority = priority;
        this.cpuTime = cpuTime;
    }

    public int getName() {
        return name;
    }

    public int getArrival() {
        return arrival;
    }

    public int getPriority() {
        return priority;
    }

    public int getCpuTime() {
        return cpuTime;
    }

    public void decrementCpuTime() {
        cpuTime--;
    }

    public boolean isDone() {
        if (cpuTime == 0) {
            return true;
        }
        return false;
    }


    public void setCpuTime(int cpuTime) {
        this.cpuTime = cpuTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime+1;
    }
}
