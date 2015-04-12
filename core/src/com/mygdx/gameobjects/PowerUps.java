package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by wenwen on 10/4/15.
 */
public class PowerUps {
    private String powerUpType;
    private static HashMap<Vector2,Boolean> coordEggs = new HashMap<Vector2,Boolean>();
    private static ArrayList<Vector2> vectorList = new ArrayList<Vector2>();




    public PowerUps(String PowerUpType){
        this.powerUpType = PowerUpType;
        vectorList.add(new Vector2(30,10));
        vectorList.add(new Vector2(50,10));
        vectorList.add(new Vector2(70,10));
        vectorList.add(new Vector2(90,10));
        vectorList.add(new Vector2(120,10));
        vectorList.add(new Vector2(30,30));
        vectorList.add(new Vector2(50,30));
        vectorList.add(new Vector2(70,30));
        vectorList.add(new Vector2(90,30));
        vectorList.add(new Vector2(120,30));
        vectorList.add(new Vector2(15,60));
        vectorList.add(new Vector2(35,60));
        vectorList.add(new Vector2(55,60));
        vectorList.add(new Vector2(75,60));
        vectorList.add(new Vector2(95,60));
        vectorList.add(new Vector2(115,60));

    }

    public HashMap<Vector2,Boolean> generateCord(){
        coordEggs.clear();
        if(powerUpType.equals("ramen")){
           coordEggs.putAll(coordinateGenerator(10));
        }
        else if(powerUpType.equals("riceBall")){
              coordEggs.putAll(coordinateGenerator(7));
        }
        else{
            coordEggs.putAll(coordinateGenerator(4));
        }
        return coordEggs;
    }



    public static HashMap<Vector2,Boolean> coordinateGenerator(int numberofPower) {
        HashMap<Vector2,Boolean> tempMap = new HashMap<Vector2,Boolean>();
        tempMap.clear();
        Random random = new Random();
        ArrayList<Vector2> tempList = (ArrayList<Vector2>) vectorList.clone();

        int tempNo =0;
        for (int i = 15; i>0;i--) {
            if (i < numberofPower) {
                if (i != 0) {
                    tempNo = random.nextInt(15) / 1000;
                    tempMap.put(tempList.get(tempNo), false);
                    tempList.remove(tempNo);
                } else {
                    tempMap.put(tempList.get(0), false);
                }
            }

        }

        return tempMap;
    }

    //get PowerUpType
    public String getPowerUpType(){return powerUpType;}
}
