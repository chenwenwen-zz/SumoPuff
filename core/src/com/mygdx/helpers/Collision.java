package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;


// still debugging. Need to change quite a lot depending on concurrency issues.
// currently everything is static, so not a good idea for concurrency. Don't know if it is required to do so.
public class Collision {
	// int Puffpress;
	// int Puff1press;
	// int PuffX;
	// int Puff1X;

	// private static ArrayList<Float> position = new ArrayList<Float>();
	// Changed to Float[] type so that data is only changed. Less use of resources. 
	private static Float[] position = new Float[1];


//    Collision(int Puffpress,int Puff1press,int PuffX,int Puff1X){
//        this.Puffpress=Puffpress;
//        this.Puff1press=Puff1press;
//        this.PuffX=PuffX;
//        this.Puff1X=Puff1X;
//    }

	public static void updatedposition(int thisPressCounter,int opponentPressCounter, float thisPuffXVal, float opponentPufFXVal){
		int tapCounterDiff = 0;

		// calculate the difference between the Press values.
		// if (thisPressCounter > opponentPressCounter){
		tapCounterDiff = Math.abs(thisPressCounter - opponentPressCounter);
		// } else { tapCounterDiff = opponentPressCounter - thisPressCounter; }

		Gdx.app.log("TapDiffVal", tapCounterDiff + "");

        Gdx.app.log("Collision", position.toString());
       	 
		// if(tapCounterDiff < 5){
		// 	Gdx.app.log("Collision", position.toString());
		// 	position.add(thisPuffXVal);
		// 	position.add(opponentPufFXVal);
		// } else {

			if(thisPressCounter > opponentPressCounter){
				thisPuffXVal = (thisPuffXVal + tapCounterDiff) / 1.2f;
				// opponentPufFXVal = opponentPufFXVal + tapCounterDiff;

				position[0] = thisPuffXVal;
				// position.add(thisPuffXVal);
				// position.add(opponentPufFXVal);

			}
            else if (tapCounterDiff<5) {
                position[0] = 0f;
            }
            else if (thisPressCounter<opponentPressCounter) {

				// thisPuffXVal = thisPuffXVal + tapCounterDiff;
				opponentPufFXVal = (opponentPufFXVal + tapCounterDiff) / 1.2f;

				position[0] = -opponentPufFXVal;
				// position.add(thisPuffXVal);
				// position.add(opponentPufFXVal);
			}
		}
//	}

	public static Float[] getpositions(){
		return position;
	}

}
