package com.mygdx.helpers;

public class Collision {

	private static Float velocity = Float.valueOf(0);


	public static void updatedposition(int myCount,int oppCount, float thisPuffXVal){
			if(myCount > oppCount){
				velocity = 20f;
			}
            else if (myCount<oppCount) {
				velocity = -20f;
			}
		}
//	}

	public static Float getpositions(){
		return velocity;
	}

}
