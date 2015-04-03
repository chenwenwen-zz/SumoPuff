package com.mygdx.helpers;

import java.util.ArrayList;

/**
 * Created by wenwen on 30/3/15.
 */
public interface ActionResolver {

    public ArrayList<String> getParticipants();
    public int requestOppoCount();
    public int requestUpdates();
    public int requestOppGameStatus();
    public String getMyId();

    //updates = 1(touchdown)
    public void updateScreen(int status);
    public void BroadCastCount(int count);
    public void updateGameStatus(int count);

}
