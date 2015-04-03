package com.mygdx.helpers;

public class Collision {

	private static Float position = Float.valueOf(0);


	public static void updatedposition(int myCount,int oppCount, float thisPuffXVal){
		int tapCounterDiff = 0;
		tapCounterDiff = Math.abs(myCount - oppCount);
			if(myCount > oppCount){
				position = (thisPuffXVal + tapCounterDiff)/2f;


			}
            else if(tapCounterDiff<2){
                position = 0f;
            }

            else if (myCount<oppCount) {

				position = -(thisPuffXVal + tapCounterDiff)/ 2f;

			}
		}
//	}

	public static Float getpositions(){
		return position;
	}

}
