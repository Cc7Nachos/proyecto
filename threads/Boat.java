package nachos.threads;
import nachos.ag.BoatGrader;
import java.util.*;

public class Boat
{
    static BoatGrader bg;

    private static Lock conditionLock = new Lock();
    public  static Condition2 conditionAdult = new Condition2(conditionLock);
    public  static Condition2 conditionChild = new Condition2(conditionLock);

    private static int childOahu = 0;
    private static int childMolokai = 0;
    private static int adultOahu = 0;
    private static int adultMolokai = 0;
    private static boolean island = false;

    
    public static void selfTest()
    {
	BoatGrader b = new BoatGrader();
	
	System.out.println("\n ***Testing Boats with only 2 children***");
    System.out.println("");
	begin(3, 4, b);

//	System.out.println("\n ***Testing Boats with 2 children, 1 adult***");
//  	begin(1, 2, b);

//  	System.out.println("\n ***Testing Boats with 3 children, 3 adults***");
//  	begin(3, 3, b);
    }

    public static void begin( int adults, int children, BoatGrader b )
    {
    	childOahu = children;
    	adultOahu = adults;
		if(((adults>0)||(children > 0))){
			// Store the externally generated autograder in a class
			// variable to be accessible by children.
			bg = b;

			// Instantiate global variables here
			
			// Create threads here. See section 3.4 of the Nachos for Java
			// Walkthrough linked from the projects page.
			Runnable rA = new Runnable() {
			    public void run() {
		            AdultItinerary();
		        }
			};
			Runnable rC = new Runnable() {
			    public void run() {
		            ChildItinerary();
		        }
			};
            for(int i = 0; i < adults;i++){
                KThread tA = new KThread(rA);
                tA.fork();
            }
            for(int i = 0; i < children;i++){
                KThread tC = new KThread(rC);
                tC.fork();
            }
		}
    }

    static void AdultItinerary()
    {
        conditionLock.acquire();
        while( (childOahu > 0) || (adultOahu > 0) ){
            if(childOahu > 1){
                conditionAdult.sleep();
            }
        	if(!island){
                //System.out.println(adultOahu + "ni;os --- " + childOahu + " isla " + island);
                if((childOahu == 1)&&(adultOahu >= 1)){
                    //System.out.println(adultOahu + "ni;os --- " + childOahu + " isla " + island);
                    bg.AdultRowToMolokai();
                    adultOahu -= 1;
                    adultMolokai += 1;
                    island = true;
                }
                if((childOahu == 0)&&(adultOahu >= 1)){
                    bg.AdultRowToMolokai();
                    adultOahu -= 1;
                    adultMolokai += 1;
                    island = true;
                }
        	}
            if(island){
                if(childOahu == 1){
                    conditionChild.wake();
                    conditionAdult.sleep();
                }
            }
        }
        conditionLock.release();
    }

    static void ChildItinerary()
    {
        conditionLock.acquire();
    	while( (childOahu > 0) || (adultOahu > 0) ){
            //System.out.println("ni = " + childOahu);
            //System.out.println("ad = " + adultOahu);
    		if(!island){
                
                if(((childOahu == 1)||(childOahu == 0))  && (adultOahu >= 1)){
                    //System.out.println(adultOahu + "ni;os --- " + childOahu + " isla " + island);
                    conditionAdult.wake();
                    conditionChild.sleep();
                }
                if(childOahu > 1){
                    bg.ChildRowToMolokai();
                    bg.ChildRideToMolokai();
                    childOahu -= 2;
                    childMolokai += 2;
                    island = true;
                }
    		}
    		if(island){
                if((childOahu >= 1)||(adultOahu >= 1)){
                    bg.ChildRowToOahu();
                    childMolokai -= 1;
                    childOahu += 1;
                    island = false;
                }
    		}
    	}
        conditionLock.release();
    }

    static void SampleItinerary()
    {
	// Please note that this isn't a valid solution (you can't fit
	// all of them on the boat). Please also note that you may not
	// have a single thread calculate a solution and then just play
	// it back at the autograder -- you will be caught.
	System.out.println("\n ***Everyone piles on the boat and goes to Molokai***");
	bg.AdultRowToMolokai();
	bg.ChildRideToMolokai();
	bg.AdultRideToMolokai();
	bg.ChildRideToMolokai();
    }
    
}
