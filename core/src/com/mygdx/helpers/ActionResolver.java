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

    public void broadCastLeftPuffX(float x1);
    public void broadCastRightPuffX(float x2);
    public float requestLeftPuffX();
    public float requestRightPuffX();


    public void sendPowerUpAttack(int type);// type 0 = no Attack, type 1 = reset count(ramen), type 2 = freeze powerup(iceCream) type 3= double count powerUp(riceBall)
    public int  checkPowerUpAttack();


    public void sendMove(int move); // Move = 1, Stop = 0;
    public int  checkMove(); // Move = 1, Stop = 0;


    public void broadCastTimeLeft(int sec);
    public int getTimeLeft();

}
