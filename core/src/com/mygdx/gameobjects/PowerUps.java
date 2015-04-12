package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;

/**
 * Created by wenwen on 10/4/15.
 */
public class PowerUps {
    private String powerUpType;
    private HashMap<Vector2,Boolean> cordEggs = new HashMap<Vector2,Boolean>();
    public PowerUps(String PowerUpType){ this.powerUpType = PowerUpType; }

    public HashMap<Vector2,Boolean> generateCord(){
        if(powerUpType.equals("ramen")){
           cordEggs.put(new Vector2(50,50),false);
           cordEggs.put(new Vector2(70,50),false);
        }
        else if(powerUpType.equals("riceBall")){
            cordEggs.put(new Vector2(50,50),false);
        }
        else{
            cordEggs.put(new Vector2(50,50),false);
        }
        return cordEggs;
    }

    public String getPowerUpType(){return powerUpType;}
}
