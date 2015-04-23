package com.mygdx.helpers;

import java.util.ArrayList;

/**This interface contains the methods for broadcasting different messages to the other player.
 * It is implemented under android-->java-->AndroidLauncher.
 */
public interface ActionResolver {

    /** Method that requests for the ID of the participants in the Google Play game room*/
    public ArrayList<String> getParticipants();
    /** Method that requests for the player's ID*/
    public String getMyId();


    /** Method that broadcasts the player's tapping counts to the opponent player*/
    public void BroadCastCount(int count);
    /** Method that requests for the opponent player's tapping counts*/
    public int requestOppoCount();


    /** Method that broadcasts the player's current game state to the opponent player*/
    public void BroadCastMyGameState(int state);// Initialize = 0, Ready=1, Running=2, GameOver=3
    /** Method that requests for the opponent player's current game state*/
    public int requestOppGameState();


    /**Method that broadcasts the left SumoPuff's X coordinates(only applicable to player with ID "player1")*/
    public void broadCastLeftPuffX(float x1);
    /**Method that broadcasts right SumoPuff's X coordinates(only applicable to player with ID "player1")*/
    public void broadCastRightPuffX(float x2);


    /**Method that requests for left SumoPuff's X coordinates(only applicable to player with ID "player2")*/
    public float requestLeftPuffX();
    /**Method that requests for right SumoPuff's X coordinates(only applicable to player with ID "player2")*/
    public float requestRightPuffX();


    /**Method that broadcast the powerUp attack to the opponent player*/
    public void sendPowerUpAttack(int type);// type 0 = no Attack, type 1 = reset count(ramen), type 2 = freeze powerup(iceCream) type 3= double count powerUp(riceBall)
    /**Method that checks the powerUp attack from the opponent player*/
    public int  checkPowerUpAttack();


    /**Method that informs the opponent player to update the game character's positions*/
    public void sendMove(int move); // Move = 1, Stop = 0;
    /**Method that checks if update the game character's positions is needed*/
    public int  checkMove(); // Move = 1, Stop = 0;


    /**Method that broadcast the attack time left to the opponent player*/
    public void broadCastTimeLeft(int sec);
    /**Method that request for the attack time left from the opponent player*/
    public int getTimeLeft();

}
