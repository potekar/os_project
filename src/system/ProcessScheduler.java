package system;

import memory.ProcessPetko;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProcessScheduler extends Thread{

    //public static Queue<ProcessPetko> red = new LinkedList<>();

   public static BlockingQueue<ProcessPetko> red = new LinkedBlockingQueue<>();


    public static long quantum = 4000;

    @Override
    public void run() {

            while(true)
            {

                try{

                    //ProcessPetko p = red.poll();
                    ProcessPetko p = red.take();
                    if(p.stanje == ProcessState.BLOCKED)
                    {
                        red.put(p);
                        continue;
                    }
                    else if(p.stanje == ProcessState.TERMINATED)
                        continue;



                    if(p.indikator == 0) {
                        p.indikator = 1;
                        p.stanje = ProcessState.RUNNING;
                        p.start();
                    }
                    else if(p.indikator == 1) {
                        p.resumeProcess();
                    }



                    while(true)
                    {
                        if(p.quantumCheck<=0)
                        {
                            break;
                        }
                    }
                    //this.sleep(200);





                    if (p.stanje != ProcessState.DONE && p.stanje != ProcessState.BLOCKED) {
                        p.pauseProcess();
                        red.add(p);
                    }else if(p.stanje == ProcessState.BLOCKED)
                    {
                        red.put(p);
                    }


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
    }

}



