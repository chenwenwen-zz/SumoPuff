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

    public void updateScreen(int move);
    public int  requestUpdateScreen();


    public void updateLeftPuffX(float x1);
    public void updateRightPuffX(float x2);
    public float requestLeftPuffX();
    public float requestRightPuffX();

}
