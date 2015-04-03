package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;


// still debugging. Need to change quite a lot depending on concurrency issues.
// currently everything is static, so not a good idea for concurrency. Don't know if it is required to do so.
public class Collision {

    // private static ArrayList<Float> position = new ArrayList<Float>();
    // Changed to Float[] type so that data is only changed. Less use of resources.
    private static Float[] position = new Float[2];


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
            if(thisPuffXVal>0) {
                thisPuffXVal = (thisPuffXVal) / 1.2f;
                opponentPufFXVal = (thisPuffXVal -14) / 1.2f;
                // opponentPufFXVal = opponentPufFXVal + tapCounterDiff;

                position[0] = thisPuffXVal;
                position[1] = opponentPufFXVal ;
                // position.add(thisPuffXVal);
                // position.add(opponentPufFXVal);
            }
            else{
                thisPuffXVal = (thisPuffXVal) / 1.2f;
                opponentPufFXVal = (thisPuffXVal +14) / 1.2f;


                position[0] = thisPuffXVal;
                position[1] = opponentPufFXVal ;
            }

        }
//            else if (tapCounterDiff<5) {
//                position[0] = 0f;
//            }
        else if (opponentPressCounter>thisPressCounter) {
            if(opponentPufFXVal>0) {
                opponentPufFXVal = (opponentPufFXVal) / 1.2f;
                thisPuffXVal = (opponentPufFXVal) - 14 / 1.2f;
                position[0] = thisPuffXVal;
                position[1] = opponentPufFXVal ;
            }
            else{
                opponentPufFXVal = (opponentPufFXVal) / 1.2f;
                thisPuffXVal = (opponentPufFXVal) + 14 / 1.2f;
                position[0] = thisPuffXVal;
                position[1] = opponentPufFXVal ;
            }
        }
    }

//	}

    public static Float[] getpositions(){
        return position;
    }

}
