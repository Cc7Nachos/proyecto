package nachos.threads;

import nachos.machine.*;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class NewType{

	private long time = 0;
	private KThread currentThread = null;

	public void setTime(long x, KThread y){
		time = x;
		currentThread = y;
	}

	public long getTime(){
		return time;
	}

	public KThread getThread(){
		return currentThread;
	}
	public String toString(){
		return "time = " + time;
	}

}