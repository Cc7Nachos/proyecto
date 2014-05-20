package nachos.threads;

import nachos.machine.*;

/**
 * A <i>communicator</i> allows threads to synchronously exchange 32-bit
 * messages. Multiple threads can be waiting to <i>speak</i>,
 * and multiple threads can be waiting to <i>listen</i>. But there should never
 * be a time when both a speaker and a listener are waiting, because the two
 * threads can be paired off at this point.
 */
public class Communicator {
    private Lock lock;
    private Condition2 conditionSpeaker;
    private Condition2 conditionListener;
    
    private int word;
    int contSpeakers;
    int contListeners;
    boolean dataToTransmit;
    /**
     * Allocate a new communicator.
     */
    public Communicator() {
        //BEGIN Nuestras Inicializaciones
        this.communicationLock = new Lock();
        this.speakCondition = new Condition2(this.communicationLock);
        this.listenCondition = new Condition2(this.communicationLock);
        this.numMsg = 0;
        this.existMsg = false;
        //END Nuestras Inicializaciones
    }

    /**
     * Wait for a thread to listen through this communicator, and then transfer
     * <i>word</i> to the listener.
     *
     * <p>
     * Does not return until this thread is paired up with a listening thread.
     * Exactly one listener should receive <i>word</i>.
     *
     * @param	word	the integer to transfer.
     */
    public void speak(int word) {
        communicationLock.acquire();        
        while(numMsg == 0 || existMsg){
            speakCondition.sleep();
        }       
        msg = word;
        existMsg = true;
        listenCondition.wake();         
        communicationLock.release();
    }

    /**
     * Wait for a thread to speak through this communicator, and then return
     * the <i>word</i> that thread passed to <tt>speak()</tt>.
     *
     * @return	the integer transferred.
     */    
    public int listen() {
	   communicationLock.acquire();        
        numMsg++;       
        while(!existMsg){
            speakCondition.wake();
            listenCondition.sleep();
        }
        numMsg--;       
        existMsg = false;
        communicationLock.release();
        return msg;
    }
    private Lock communicationLock;
    private Condition2 speakCondition;
    private Condition2 listenCondition;  
    private int numMsg;    
    private int msg;
    private boolean existMsg;  
}
