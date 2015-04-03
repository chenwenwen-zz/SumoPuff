package com.mygdx.gameworld;

import com.mygdx.gameobjects.Puff;
import com.mygdx.helpers.ActionResolver;

import java.util.ArrayList;

// this class handles all the functionalities of the gameobjects. 
public class GameWorld {

	private Puff leftPuff;
	private Puff rightPuff;

	// current game state.
	private GameState currentState;
	// gameOverReady is the "state" after winner picture is shown.s
	public static boolean gameOverReady = false;
	
	// midPoint of the Game Screen.
	// Only the "X" co-ordinate is used.
	private int midPointX;
	private float gameHeight;
	
	// Enum type for identifying game state.
	public enum GameState {

	    READY, RUNNING, GAMEOVER

	}

	
	public GameWorld(int midPointX, ActionResolver actionResolver, float gameHeight) {
		
		this.midPointX = midPointX;
		this.gameHeight = gameHeight;

		// initial state of the game when GameWorld is initialized. 
		currentState = GameState.READY;
        try{
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();
        if(player1 > player2){
           if(player1 == me){
             leftPuff = new Puff(midPointX - 30, 120, 13, 24, actionResolver,"runtoright","me");
             rightPuff = new Puff(midPointX +30 , 120, 13, 24, actionResolver,"runtoleft","notme");}
           else{
             leftPuff = new Puff(midPointX - 30, 120, 13, 24, actionResolver,"runtoright","notme");
             rightPuff = new Puff(midPointX +30 , 120, 13, 24, actionResolver,"runtoleft","me");
           }

           }
        else{
            if(player1 == me){
                leftPuff = new Puff(midPointX - 30, 120, 13, 24, actionResolver,"runtoright","notme");
                rightPuff = new Puff(midPointX +30 , 120, 13, 24, actionResolver,"runtoleft","me");}
            else{
                leftPuff = new Puff(midPointX - 30, 120, 13, 24, actionResolver,"runtoright","me");
                rightPuff = new Puff(midPointX +30 , 120, 13, 24, actionResolver,"runtoleft","notme");
            }
        }
       }
        finally {

        }

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
		leftPuff.update(delta);
		rightPuff.update(delta);
		
		// if the puff collides, momentarily change their velocity to zero.	
		if(leftPuff.collides(rightPuff)){
			leftPuff.stop();
			rightPuff.stop();
		}
		
		//UserPuff loses
		if (leftPuff.getX() < 15){
			leftPuff.stop();
			rightPuff.stop();
			currentState = GameState.GAMEOVER;
		}
		
		//OpponentPuff loses
		if (rightPuff.getX() > 110){
			leftPuff.stop();
			rightPuff.stop();
			currentState = GameState.GAMEOVER;
		}
		
	}
	
	private void updateReady(float delta) {
		// TODO Auto-generated method stub
		// since the renderer renders the ready state items automatically, nothing is called here.
	}
	
	public Puff getLeftPuff(){
		return leftPuff;
	}

	public Puff getRightPuff(){
		return rightPuff;
	}
	
	public boolean isReady() {
		
        return currentState == GameState.READY;
    }
	
	public void start() {
        currentState = GameState.RUNNING;
    }

    public boolean isStart(){
        return currentState == GameState.RUNNING;
    }
	
	public void restart() {
        currentState = GameState.READY;
        gameOverReady = false;
        leftPuff.reset(midPointX-45, 120, 13, 24);
        rightPuff.reset(midPointX+10, 120, 13, 24);
        leftPuff.collide = false;
        rightPuff.collide = false;
        currentState = GameState.READY;
    }
	
	 public boolean isGameOver() {
	        return currentState == GameState.GAMEOVER;
	 }
	
	 public boolean isGameOverReady(){
		return gameOverReady;
	 }

	public int getMidPoint(){
		return midPointX;
	}

	public float getGameHeight(){
		return gameHeight;
	}

}