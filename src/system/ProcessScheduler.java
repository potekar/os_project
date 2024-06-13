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

                    if(p.indikator ==0) {
                        p.start();
                        p.indikator = 1;
                    }
                    else if(p.indikator == 1) {
                        p.resumeProcess();
                    }


                    while(true)
                    {
                        if(p.quantumCheck<=0)
                            break;
                    }
                    this.sleep(200);


                    p.pauseProcess();


                    if (!p.isFinished()) {
                        red.add(p);
                    }
                    //System.out.println(red);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
    }

}



