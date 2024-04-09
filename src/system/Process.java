package system;

public class Process {
    private String processName;
    private int pid;
    private ProcessState processState;
    private int pc;

    public Process(String processName, int pid, ProcessState processState, int pc) {
        this.processName = processName;
        this.pid = pid;
        this.processState = processState;
        this.pc = pc;
    }

}
