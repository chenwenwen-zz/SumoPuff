package com.mygdx.helpers;

public class Collision {

	private static float velocity = 0;
	public static void updatedposition(int myCount,int oppCount){
			if(myCount > oppCount){
				velocity = 0.13f;
			}
            else if (myCount<oppCount) {
				velocity = -0.13f;
			}

		}

	public static float getVelocity(){
		return velocity;
	}

}
