package com.mygdx.helpers;

public class Collision {

	private static int velocity = 0;


	public static void updatedposition(int myCount,int oppCount){
			if(myCount > oppCount){
				velocity = 1;
			}
            else if (myCount<oppCount) {
				velocity = -1;
			}

		}
//	}

	public static int getpositions(){
		return velocity;
	}

}
