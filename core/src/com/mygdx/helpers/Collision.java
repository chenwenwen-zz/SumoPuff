package com.mygdx.helpers;

public class Collision {

	private static Float velocity = Float.valueOf(0);


	public static void updatedposition(int myCount,int oppCount, float thisPuffXVal){
		int tapCounterDiff = 0;
		tapCounterDiff = Math.abs(myCount - oppCount);
			if(myCount > oppCount){
				velocity = (thisPuffXVal + tapCounterDiff)/2f;
			}
            else if (myCount<oppCount) {
				velocity = -(thisPuffXVal + tapCounterDiff)/ 2f;
			}
		}
//	}

	public static Float getpositions(){
		return velocity;
	}

}
