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

    public int requestOppGameState();
    public void updateGameState(int count);

}
