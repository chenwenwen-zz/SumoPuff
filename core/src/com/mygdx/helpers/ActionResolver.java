package com.mygdx.helpers;

import java.util.ArrayList;

/**
 * Created by wenwen on 30/3/15.
 */
public interface ActionResolver {

    public ArrayList<String> getParticipants();
    public String getMyId();

    public void BroadCastCount(int count);
    public int requestOppoCount();

    public void BroadCastMyGameState(int state);// Initialize = 0, Ready=1, Running=2, GameOver=3
    public int requestOppGameState();

    public void updateLeftPuffX(float x1);
    public void updateRightPuffX(float x2);
    public float requestLeftPuffX();
    public float requestRightPuffX();


    public void sendPowerUpAttack(int type);// type 0 = no Attack, type 1 = reset count(ramen), type 2 = freeze powerup(iceCream)
    public int  checkPowerUpAttack();

}
