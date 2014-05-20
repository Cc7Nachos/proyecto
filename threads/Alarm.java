package nachos.threads;

import nachos.machine.*;
import java.util.*;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
     private static LinkedList<NewType> wakeList = new LinkedList<NewType>();
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
	//KThread.currentThread().yield();
        boolean intStatus = Machine.interrupt().disable();
        if(wakeList.size()>0){ 
            int cont = 0;
            while(cont < wakeList.size()){
                NewType aux = wakeList.get(cont);
                long time = aux.getTime();
                //System.out.println(time + " <<<< " + Machine.timer().getTime());
                if(time < Machine.timer().getTime()){
                    //System.out.println(wakeList);
                    KThread auxiliar = aux.getThread();
                    auxiliar.ready();
                    wakeList.remove(cont);

                }
                cont++;
            }
            Machine.interrupt().restore(intStatus);
        }
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    public void waitUntil(long x) {
     //COdigo original   
	       // for now, cheat just to get something working (busy waiting is bad)
	       /*long wakeTime = Machine.timer().getTime() + x;
	       while (wakeTime > Machine.timer().getTime())
	       KThread.yield();*/

    //mi codigo
        //obtengo cuanto tiempo debe esperar
        long wakeTime = Machine.timer().getTime() + x;
        //desabilito interrupciones
        boolean intStatus = Machine.interrupt().disable();
        //creo una variable de un nuevo tipo
        NewType tX = new NewType();
        tX.setTime(wakeTime,KThread.currentThread());
        wakeList.add(tX);
        //duerme el thread
        KThread.currentThread().sleep();
        //habilita interrupciones
        Machine.interrupt().restore(intStatus);
    //termina mi codigo
    }
}
