package com.mygdx.helpers;

public class Collision {

	private static Float velocity = Float.valueOf(0);


	public static void updatedposition(int myCount,int oppCount, float thisPuffXVal){
            int tapCounterDiff=0;
			if(myCount > oppCount){
				velocity = 10f;
			}
            else if (myCount<oppCount) {
				velocity = -10f;
			}
		}
//	}

	public static Float getpositions(){
		return velocity;
	}

}
