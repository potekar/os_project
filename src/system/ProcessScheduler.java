package system;

import java.util.Scanner;

public class ProcessScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        int[] arrivalTimes = new int[n];
        int[] burstTimes = new int[n];
        int[] waitingTimes = new int[n];
        int[] turnaroundTimes = new int[n];

        System.out.println("Enter the arrival times:");
        for (int i = 0; i < n; i++) {
            arrivalTimes[i] = scanner.nextInt();
        }

        System.out.println("Enter the burst times:");
        for (int i = 0; i < n; i++) {
            burstTimes[i] = scanner.nextInt();
        }

        int timeQuantum = 3;
        int completedProcesses = 0;
        int currentTime = 0;
        int remainingBurstTime[] = new int[n];
        for (int i = 0; i < n; i++) {
            remainingBurstTime[i] = burstTimes[i];
        }

        while (completedProcesses < n) {
            int currentProcess = -1;
            for (int i = 0; i < n; i++) {
                if (arrivalTimes[i] <= currentTime && remainingBurstTime[i] > 0) {
                    currentProcess = i;
                    break;
                }
            }

            if (currentProcess == -1) {
                currentTime++;
                continue;
            }

            int timeSlice = Math.min(timeQuantum, remainingBurstTime[currentProcess]);
            currentTime += timeSlice;
            remainingBurstTime[currentProcess] -= timeSlice;

            if (remainingBurstTime[currentProcess] == 0) {
                completedProcesses++;
                waitingTimes[currentProcess] = currentTime - arrivalTimes[currentProcess] - burstTimes[currentProcess];
                turnaroundTimes[currentProcess] = currentTime - arrivalTimes[currentProcess];
            }
        }

        System.out.println("Process\tArrival Time\tBurst Time\tWaiting Time\tTurnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.printf("%d\t%d\t\t%d\t\t%d\t\t%d\t\n", i + 1, arrivalTimes[i], burstTimes[i], waitingTimes[i], turnaroundTimes[i]);
        }

        double avgWaitingTime = 0.0;
        double avgTurnaroundTime = 0.0;
        for (int i = 0; i < n; i++) {
            avgWaitingTime += waitingTimes[i];
            avgTurnaroundTime += turnaroundTimes[i];
        }
        avgWaitingTime /= n;
        avgTurnaroundTime /= n;

        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }
}
