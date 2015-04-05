package com.mygdx.gameworld;

import com.mygdx.gameobjects.Puff;
import com.mygdx.helpers.ActionResolver;

import java.util.ArrayList;

// this class handles all the functionalities of the gameobjects. 
public class GameWorld {

	private Puff leftPuff;
	private Puff rightPuff;
    private ActionResolver actionResolver;

	// current game state.
	private GameState currentState;
	// gameOverReady is the "state" after winner picture is shown.s
	public static boolean gameOverReady = false;

	
	// Enum type for identifying game state.
	public enum GameState {
	   INITIALIZE, READY, RUNNING, GAMEOVER
	}

	
	public GameWorld(ActionResolver actionResolver) {
		// initial state of the game when GameWorld is initialized. 
		currentState = GameState.INITIALIZE;
        this.actionResolver=actionResolver;

        try{
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();
        if(player1 > player2){
           if(player1 == me){
             leftPuff = new Puff(20, 120, 13, 24,"runtoright","me");
             rightPuff = new Puff(105, 120, 13, 24,"runtoleft","notme");}
           else{
             leftPuff = new Puff(20, 120, 13, 24,"runtoright","notme");
             rightPuff = new Puff(105, 120, 13, 24, "runtoleft","me");
           }

           }
        else{
            if(player1 == me){
                leftPuff = new Puff(20, 120, 13, 24,"runtoright","notme");
                rightPuff = new Puff(105, 120, 13, 24,"runtoleft","me");}
            else{
                leftPuff = new Puff(20, 120, 13, 24,"runtoright","me");
                rightPuff = new Puff(105, 120, 13, 24,"runtoleft","notme");
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
            case INITIALIZE:
                updateInitialize(delta);
                break;
            //When Both collides
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

    private void updateInitialize(float delta){

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
        if(actionResolver.requestOppGameState()==1){
            currentState = GameState.RUNNING;
        }
	}
	
	public Puff getLeftPuff(){
		return leftPuff;
	}

	public Puff getRightPuff(){
		return rightPuff;
	}

    public void ready() {currentState =GameState.READY;}
	public void start() {
     currentState = GameState.RUNNING;
    }
    public void gameOver(){currentState = GameState.GAMEOVER;}

    public boolean isInitialized(){ return currentState== GameState.INITIALIZE;}
    public boolean isReady() {return currentState == GameState.READY;}
    public boolean isStart(){
        return currentState == GameState.RUNNING;
    }
    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }
    public boolean isGameOverReady(){
        return gameOverReady;
    }

	public void restart() {
        currentState = GameState.INITIALIZE;
        gameOverReady = false;
        leftPuff.reset(20, 120, 13, 24);
        rightPuff.reset(105, 120, 13, 24);
        leftPuff.collide = false;
        rightPuff.collide = false;
        actionResolver.BroadCastMyGameState(0);

    }
	


}