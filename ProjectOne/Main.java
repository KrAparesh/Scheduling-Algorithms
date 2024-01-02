import java.util.*;

// Creating a process handler that stores information about a process
class ProcessHandler {
    int pid, pArrival, pBurst, remTime, startTime, endTime, firstOccur;
    boolean visited;

    ProcessHandler() {
    }

    ProcessHandler(int pid, int pArrival, int pBurst) {
        this.pid = pid;
        this.pArrival = pArrival;
        this.pBurst = pBurst;
        this.remTime = pBurst;
        this.startTime = 0;
        this.endTime = 0;
        this.visited = false; // Additional Data for Round Robin
        this.firstOccur = 0; // Additional Data for Round Robin

    }
}

// Creating a handler to prepare Gantt Chart for Round Robin Scheduler
class RRHandler {
    int sTime;
    int eTime;
    ProcessHandler process;

    RRHandler() {
    }

    RRHandler(ProcessHandler process, int sTime, int eTime) {
        this.process = process;
        this.sTime = sTime;
        this.eTime = eTime;
    }
}

/**
 * The Main class is the entry point of the program and contains the main method.
 * It provides functionality for executing the First Come First Serve (FCFS) and Round Robin scheduling algorithms.
 */
public class Main {

    protected static int timeQuantum = 4;
    protected static ArrayList<ProcessHandler> processList = new ArrayList<>();
    protected static double avgTAT;
    protected static double avgWT;
    protected static double avgRT;

    public static void FCFS(ArrayList<ProcessHandler> processList) {

        Queue<ProcessHandler> gc = new LinkedList<>();

        int timeCounter = 0;

        for (ProcessHandler process : processList) {
            process.startTime = timeCounter;
            timeCounter += process.pBurst;
            process.endTime = timeCounter;
            gc.add(process);
        }

        avgTAT = getAvgTAT();
        avgWT = getAvgWT();
        avgRT = getAvgRT();

        System.out.println("GANTT CHART \n");
        boolean first = true;
        while (!gc.isEmpty()) {
            ProcessHandler p = gc.remove();
            if (first) {
                System.out.print(p.startTime + "  " + "P" + p.pid + "  " + p.endTime);
                first = !first;
            } else {
                System.out.print("  " + "P" + p.pid + "  " + p.endTime);
            }
        }

        System.out.println("\nAvg. Turn Around Time: " + avgTAT);
        System.out.println("Avg. Waiting Time: " + avgWT);
        System.out.println("Avg. Response Time: " + avgRT);

    }

    public static void RoundRobin(ArrayList<ProcessHandler> processList) {
        Queue<RRHandler> gc = new LinkedList<>();
        Queue<ProcessHandler> rq = new LinkedList<>();

        int timeElapsed = 0;

        // Populating the ready queue
        for (ProcessHandler process : processList) {
            if (process.pArrival <= timeQuantum && process.visited == false) {
                rq.add(process);
                process.visited = true;
            }
        }

        while (!rq.isEmpty()) {
            ProcessHandler p = rq.remove();
            if (p.firstOccur == 0 && p.pid != 1)
                p.firstOccur = timeElapsed;

            if (p.remTime > timeQuantum) {
                int tempStart = 0;
                int tempEnd = 0;
                p.startTime = timeElapsed;
                p.remTime = p.remTime - timeQuantum;
                timeElapsed = timeElapsed + timeQuantum;
                p.endTime = timeElapsed + timeQuantum;
                tempStart = p.startTime;
                tempEnd = timeElapsed;
                gc.add(new RRHandler(p, tempStart, tempEnd));
                rq.add(p);
            } else if (p.remTime > 0) {
                int tempStart = 0;
                int tempEnd = 0;
                p.startTime = timeElapsed;
                timeElapsed = timeElapsed + p.remTime;
                p.endTime = timeElapsed;
                p.remTime = 0;
                tempStart = p.startTime;
                tempEnd = p.endTime;
                gc.add(new RRHandler(p, tempStart, tempEnd));
            }

            for (ProcessHandler process : processList) {
                if (process.pArrival <= timeElapsed && process.visited == false) {
                    rq.add(process);
                    process.visited = true;
                }
            }

        }

        System.out.println("GANTT CHART \n");
        boolean first = true;
        while (!gc.isEmpty()) {
            // System.out.println("Stuck gc");
            ProcessHandler p = gc.peek().process;
            int pStart = gc.peek().sTime;
            int pEnd = gc.peek().eTime;
            gc.remove();
            if (first) {
                System.out.print(pStart + "  " + "P" + p.pid + "  " + pEnd);
                first = !first;
            } else {
                System.out.print("  " + "P" + p.pid + "  " + pEnd);
            }
        }

        avgTAT = getAvgTAT();
        avgWT = getAvgWT();
        avgRT = getAvgRtRR();

        System.out.println("\nAvg. Turn Around Time: " + avgTAT);
        System.out.println("Avg. Waiting Time: " + avgWT);
        System.out.println("Avg. Response Time: " + avgRT);
    }

    public static double getAvgTAT() {
        double temp = 0;
        for (ProcessHandler process : processList) {
            temp += (process.endTime - process.pArrival);
        }
        return (temp / processList.size());
    }

    public static double getAvgWT() {
        double temp = 0;
        for (ProcessHandler process : processList) {
            temp += (process.endTime - process.pArrival - process.pBurst);
        }
        return (temp / processList.size());
    }

    public static double getAvgRT() {
        double temp = 0;
        for (ProcessHandler process : processList) {
            temp += process.startTime;
        }
        return (temp / processList.size());
    }

    public static double getAvgRtRR() {
        double temp = 0;
        for (ProcessHandler process : processList) {
            temp += process.firstOccur;
        }
        return temp / processList.size();
    }

    // Round Robin

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int pid = 1;
        // Getting process details
        while (true) {
            System.out.print("Enter arrival time for P" + pid + ": ");
            int at = sc.nextInt();
            System.out.print("Enter burst time for P" + pid + ": ");
            int bt = sc.nextInt();
            ProcessHandler process = new ProcessHandler(pid, at, bt);
            processList.add(process);
            pid++;
            System.out.print("Add more process? (Y/N): ");
            String choice = sc.next();
            if (choice.equalsIgnoreCase("N"))
                break;
        }
        
        while(true) {
            System.out.println("|============================|");
            System.out.println("| Enter your Choice          |");
            System.out.println("|============================|");
            System.out.println("| 1. First Come First Serve  |");
            System.out.println("| 2. Round Robin             |");
            System.out.println("| 3. EXIT                    |");
            System.out.println("|============================|");
            
            int choice = sc.nextInt();
            switch(choice) {
                case 1:
                    FCFS(processList);
                    break;
                case 2:
                    System.out.print("Enter the time quantum: ");
                    timeQuantum = sc.nextInt();
                    RoundRobin(processList);
                    break;
                case 3:
                    System.exit(1);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}