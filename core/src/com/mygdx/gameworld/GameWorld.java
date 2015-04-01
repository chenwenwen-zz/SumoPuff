package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.mygdx.gameobjects.Puff;
import com.mygdx.helpers.ActionResolver;

import java.util.ArrayList;

// this class handles all the functionalities of the gameobjects. 
public class GameWorld {

	private Puff myPuff;
	private Puff oppPuff;

	// current game state.
	private GameState currentState;

	// gameOverReady is the "state" after winner picture is shown.s
	public static boolean gameOverReady = false;
	
	// midPoint of the Game Screen.
	// Only the "X" co-ordinate is used.
	private int midPointX;	
	
	// Enum type for identifying game state.
	public enum GameState {

	    READY, RUNNING, GAMEOVER, COLLISION

	}
	
	
	public GameWorld(int midPointX, ActionResolver actionResolver) {
		
		this.midPointX = midPointX;

		// initial state of the game when GameWorld is initialized. 
		currentState = GameState.READY;
        //Try something here
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();
        if(player1 > player2){
            if(player1 == me){
                Gdx.app.log("me","is puff1");

              }
            else{
                Gdx.app.log("me","is puff2");
                }}

        else{
            if(player1 == me){
                Gdx.app.log("me","is puff2");

            }
            else{
                Gdx.app.log("me","is puff1");
                }


        }
		myPuff = new Puff(midPointX - 30, 120, 13, 24, actionResolver);
		oppPuff = new Puff(midPointX + 5, 120, 13, 24, actionResolver);
	}

	// world is updated delta time by the render method at game screen. 
	public void update(float delta) {
		// update different objects depending on currentState.
		 switch (currentState) {

		 	// initial game state whenever the game starts. Set by default.
	        case READY:
	        	// function called just to differentiate.
	            updateReady(delta);
	            break;
	            
	       	// case Running and default are the same case.
	        case RUNNING:
	        default:
	            updateRunning(delta);
	            break;
	        }
	}

	private void updateRunning(float delta) {
		// updating the myPuff at delta times.
		myPuff.update(delta);
		oppPuff.update(delta);
		
		// if the puff collides, momentarily change their velocity to zero.	
		if(myPuff.collides(oppPuff)){
			myPuff.stop();
			oppPuff.stop();
			// if the condition returns true, these two variables will be set to true by implementation.
			// myPuff.collide = true;
			// oppPuff.collide = true;
		}
		
		//UserPuff loses
		if (myPuff.getX() < 15){
			myPuff.stop();
			oppPuff.stop();
			currentState = GameState.GAMEOVER;
		}
		
		//OpponentPuff loses
		if (oppPuff.getX() > 110){
			myPuff.stop();
			oppPuff.stop();
			currentState = GameState.GAMEOVER;
		}
		
	}
	
	private void updateReady(float delta) {
		// TODO Auto-generated method stub
		// since the renderer renders the ready state items automatically, nothing is called here.
	}
	
	public Puff getUserPuff(){
		return myPuff;
	}

	public Puff getOppPuff(){
		return oppPuff;
	}
	
	public boolean isReady() {
		
        return currentState == GameState.READY;
    }
	
	public void start() {
        currentState = GameState.RUNNING;
    }
	
	public void restart() {
        currentState = GameState.READY;
        gameOverReady = false;
        myPuff.reset(midPointX-45, 120, 13, 24);
        oppPuff.reset(midPointX+10, 120, 13, 24);
        myPuff.collide = false;
        oppPuff.collide = false;
        currentState = GameState.READY;
    }
	
	 public boolean isGameOver() {
	        return currentState == GameState.GAMEOVER;
	 }
	
	 public boolean isGameOverReady(){
		return gameOverReady;
	 }

}