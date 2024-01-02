import java.util.*;
/**
 * The Main class represents the entry point of the program and contains the main logic for the Banker's algorithm.
 * It manages the process list, calculates remaining and available instances, and determines if the system is in a safe state.
 */
public class Main {

    protected static int totalA, availA;
    protected static int totalB, availB;
    protected static int totalC, availC;
    protected static int totalD, availD;

    protected static ArrayList<ProcessHandler> processList = new ArrayList<>();

    class ProcessHandler {
        int pid;
        int allotedA, maxNeedA, remNeedA;
        int allotedB, maxNeedB, remNeedB;
        int allotedC, maxNeedC, remNeedC;
        int allotedD, maxNeedD, remNeedD;
        boolean visited;

        ProcessHandler(){}

        ProcessHandler(int pid, int allotedA, int allotedB, int allotedC, int allotedD, int maxNeedA, int maxNeedB, int maxNeedC, int maxNeedD){
            this.pid = pid;
            this.allotedA = allotedA;
            this.allotedB = allotedB;
            this.allotedC = allotedC;
            this.allotedD = allotedD;
            this.maxNeedA = maxNeedA;
            this.maxNeedB = maxNeedB;
            this.maxNeedC = maxNeedC;
            this.maxNeedD = maxNeedD;
            this.visited = false;
        }
    }

    public void getRemainingInstance() {
        for(ProcessHandler process : processList) {
            process.remNeedA = process.maxNeedA - process.allotedA;
            process.remNeedB = process.maxNeedB - process.allotedB;
            process.remNeedC = process.maxNeedC - process.allotedC;
            process.remNeedD = process.maxNeedD - process.allotedD;
        }
    }

    public void getAvailableInstance() {
        int tempA = 0, tempB = 0, tempC = 0, tempD = 0;
        for(ProcessHandler process : processList) {
            tempA += process.allotedA;
            tempB += process.allotedB;
            tempC += process.allotedC;
            tempD += process.allotedD;
        }
        availA = totalA - tempA;
        availB = totalB - tempB;
        availC = totalC - tempC;
        availD = totalD - tempD;
    }

    public void printNeedMatrix() {
        getRemainingInstance();
        System.out.println("     PROCESS  REMAINING TIME");
        for(ProcessHandler process: processList) {
            System.out.println("\t" + process.pid + " \t " + process.remNeedA + " " + process.remNeedB + " " + process.remNeedC + " " + process.remNeedD);
        }
    }

    public void Banker() {
        Queue<ProcessHandler> freeQueue = new LinkedList<>();
        Queue<ProcessHandler> sequence = new LinkedList<>();

        getRemainingInstance();

        getAvailableInstance();

        while(true) {

            // Calculating remaining Instance for each process
            for(ProcessHandler process: processList) {
                if(!process.visited) {
                    if(availA >= process.remNeedA && availB >= process.remNeedB && availC >= process.remNeedC && availD >= process.remNeedD) {
                        process.visited = true;
                        freeQueue.add(process);
                    }
                }
            }

            if(freeQueue.isEmpty() && sequence.size() != processList.size()){
                // Un-safe exit

                System.out.println("A deadlock has been found!");
                for(ProcessHandler p : processList) {
                    if(!p.visited) {
                        System.out.println("Not enough Instance to allocate to process P" + p.pid);
                        System.out.println("Instance Required: " + p.remNeedA + " " + p.remNeedB + " " + p.remNeedC + " " + p.remNeedD);
                        System.out.println("Instance Available: " + availA + " " + availB + " " + availC + " " + availD);
                    }
                }
                return;
            }

            if(freeQueue.isEmpty() && sequence.size() == processList.size()) {
                // Safe exit

                System.out.println("SAFE EXIT!");
                System.out.println("Process Sequence: " );
                while(!sequence.isEmpty()) {
                    ProcessHandler p = sequence.remove();
                    System.out.print("P" + p.pid + " -> ");
                }
                System.out.println("END");

                return;
            }

            while(!freeQueue.isEmpty()) {
                ProcessHandler p = freeQueue.remove();

                p.allotedA = 0;
                p.allotedB = 0;
                p.allotedC = 0;
                p.allotedD = 0;
                sequence.add(p);

                getAvailableInstance();
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Main ob = new Main();
        ob.populate();
        ob.printNeedMatrix();
        ob.Banker();
    }
    
    
    public void populate() {

        // Example 1 (3 Resources )

        // ProcessHandler p1 = new ProcessHandler(1, 0, 1, 0, 7, 5, 3);
        // ProcessHandler p2 = new ProcessHandler(2, 2, 0, 0, 3, 2, 2);
        // ProcessHandler p3 = new ProcessHandler(3, 3, 0, 2, 9, 0, 2);
        // ProcessHandler p4 = new ProcessHandler(4, 2, 1, 1, 4, 2, 2);
        // ProcessHandler p5 = new ProcessHandler(5, 0, 0, 2, 5, 3, 3);
        // processList.add(p1);
        // processList.add(p2);
        // processList.add(p3);
        // processList.add(p4);
        // processList.add(p5);

        // totalA = 10;
        // totalB = 5;
        // totalC = 7;
        // availA = 0;
        // availB = 0;
        // availC = 0;

        // ====================================================================

        // Example 2 ( 3 Resources )

        // ProcessHandler p1 = new ProcessHandler(1, 1, 0, 1, 4, 3, 1);
        // ProcessHandler p2 = new ProcessHandler(2, 1, 1, 2, 2, 1, 4);
        // ProcessHandler p3 = new ProcessHandler(3, 1, 0, 3, 1, 3, 3);
        // ProcessHandler p4 = new ProcessHandler(4, 2, 0, 0, 5, 4, 1);
        
        // processList.add(p1);
        // processList.add(p2);
        // processList.add(p3);
        // processList.add(p4);
    
        // totalA = 8;
        // totalB = 4;
        // totalC = 6;
        // availA = 0;
        // availB = 0;
        // availC = 0;

        // ====================================================================

        // Example 3 ( 4 Resources )

        ProcessHandler p1 = new ProcessHandler(1, 0, 0, 1, 2, 0, 0, 1, 2);
        ProcessHandler p2 = new ProcessHandler(2, 2, 0, 0, 0, 2, 7, 5, 0);
        ProcessHandler p3 = new ProcessHandler(3, 0, 0, 3, 4, 6, 6, 5, 6);
        ProcessHandler p4 = new ProcessHandler(4, 2, 3, 5, 4, 4, 3, 5, 6);
        ProcessHandler p5 = new ProcessHandler(5, 0, 3, 3, 2, 0, 6, 5, 2);

        processList.add(p1);
        processList.add(p2);
        processList.add(p3);
        processList.add(p4);
        processList.add(p5);

        totalA = 6;
        totalB = 7;
        totalC = 12;
        totalD = 12;

        availA = 0;
        availB = 0;
        availC = 0;
        availD = 0;
    }

}