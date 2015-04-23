package com.mygdx.gameworld;

import com.mygdx.gameobjects.Puff;
import com.mygdx.gameobjects.Timer;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.AssetLoader;

import java.util.ArrayList;

// this class handles all the functionalities of the gameobjects.

/** GameWorld create the Puff objects leftPuff and rightPuff and decides which player is which
 *  Sumopuff based on their google play ID. The GameWorld contains the 7 game states : Initialize,
 *  Ready, Running, GameOver, PowerUp and PowerUpAttack and updates the game using GameRenderer,
 *  depending on which state it is currently in. Certain states, such as PowerUp
 *  also uses the Timer class to control the duration that the player can remain in the class.
 *  The GameOver state uses the ActionResolver broadcast() method to notify players that
 *  a player has lost.
 *
 */
public class GameWorld {

	private Puff leftPuff;
	private Puff rightPuff;
    private ActionResolver actionResolver;
    private Timer attackTimer;
    private Timer taskTimer;
    private Timer freezeTimer;
    private final int gameOverState = 3;
    private static int pageNumber=1;

	/** The current game state */
	private GameState currentState;
	/** gameOverReady is the "state" after winner picture is shown.
     *  This boolean is to ensure that players can only restart after the GameOver animation has played finished.*/
	public static boolean gameOverReady = false;


	/** Enum type for identifying game states */
	public enum GameState {
	   INITIALIZE, MANUAL, READY, RUNNING, GAMEOVER, POWERUP,POWERUPATTACK
	}

    /** Initializes the GameWorld.
     *
     * @param actionResolver            An ActionResolver class object used for passing messages between phones.
     * @param attackTimer               A Timer class object to measure the duration of power up effects.
     * @param taskTimer                 A Timer class object to measure the time for the egg-cracking mini-game.
     * @param freezeTimer               A Timer class object to measure the time when power up cannot be used.
     */
	public GameWorld(ActionResolver actionResolver,Timer attackTimer,Timer taskTimer, Timer freezeTimer) {
		/** The GameWorld always starts in the Initialize state */
		currentState = GameState.INITIALIZE;
        this.actionResolver=actionResolver;
        this.attackTimer = attackTimer;
        this.taskTimer = taskTimer;
        this.freezeTimer = freezeTimer;

        /** To decide the player's orientation in game.
         *  The player with the bigger playerID is always set on the left.
         *
         */
        try{
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();
        if(player1 > player2){
           if(player1 == me){
             leftPuff = new Puff(20, 100, 22, 45,"runtoright","player1");
             rightPuff = new Puff(108, 100, 22, 45,"runtoleft","player1");}
           else{
             leftPuff = new Puff(20, 100, 22, 45,"runtoright","player2");
             rightPuff = new Puff(108, 100, 22, 45, "runtoleft","player2");
           }

           }
        else{
            if(player1 == me){
                leftPuff = new Puff(20, 100, 22, 45,"runtoright","player2");
                rightPuff = new Puff(108, 100, 22, 45,"runtoleft","player2");}
            else{
                leftPuff = new Puff(20, 100, 22, 45,"runtoright","player1");
                rightPuff = new Puff(108, 100, 22, 45,"runtoleft","player1");
            }
        }
       }
        finally {


        }

	}

    /** The GameWorld is updated in delta time by the render method at game screen.
     *
     * @param delta             A float that represents how often the screen is updated (1/60sec)
     * @throws InterruptedException
     */
	public void update(float delta) throws InterruptedException {
		/** Updates different objects depending on currentState. */
		 switch (currentState) {
		 	/** Whenever a new GameWorld is created. State is set to Initialize by default.
             *  Manual and Powerups can be selected here.
             */
            case INITIALIZE:
                updateInitialize(delta);
                break;
            /** Opens the manual page */
            case MANUAL:
                updateManual(delta);
                break;
            /** When a player has selected their powerups. */
	        case READY:
	            updateReady(delta);
	            break;
            /** When a player clicks on a powerup during the game. Triggers the mini-game. */
	        case POWERUP:
                updatePowerup(delta);
                updateRunning(delta);
                break;
            /** When a player completes the minigame and activates the powerup. */
            case POWERUPATTACK:
                updatePowerupAttack(delta);
                updateRunning(delta);
                break;
	       	/** For usual gameplay. */
	        case RUNNING:
                updateRunning(delta);
                break;
            /** When one player crosses the threshold and loses */
            case GAMEOVER:
                updateGameOver();
                break;
	        default:
	            break;
	        }
	}

    /** Updates GameWorld when game state is Initialize.
     *
     * @param delta             A float that represents 1/60sec.
     */
    private void updateInitialize(float delta){}

    private void updateManual (float delta){}
    private void updateReady(float delta) {
        /**Checks if the opponent is ready. */
        if(actionResolver.requestOppGameState()==1){
            currentState = GameState.RUNNING;
        }
    }

    /** Updates GameWorld when game state is PowerUp.
     *
     * @param delta             A float that represents 1/60sec.
     * @throws InterruptedException
     */
    private void updatePowerup(float delta) throws InterruptedException {
        /**Sets timer for minigame */
        taskTimer.start();
        if(taskTimer.isTimeUp()){
            currentState = GameState.RUNNING;
        }
    }

    /** Updates GameWorld when game state is PowerUpAttack.
     *
     * @param delta             A float that represents 1/60sec.
     * @throws InterruptedException
     */
    private void updatePowerupAttack(float delta) throws InterruptedException {
           /** Sets timer for powerup effect duration. */
            attackTimer.start();
            if(attackTimer.isTimeUp()){
               currentState = GameState.RUNNING;
            }
    }

    /** Updates GameWorld when game state is Running.
     *
     * @param delta             A float that represents 1/60sec.
     */
	private void updateRunning(float delta) {

		/** Updates the left and right SumoPuffs */
           leftPuff.update();
           rightPuff.update();

		/** If the SumoPuffs collide, momentarily change their velocity to zero. */
		if(leftPuff.collides(rightPuff)){
			leftPuff.stop();
			rightPuff.stop();
		}
		/** If the left SumoPuff's x coordinate is less than 15, it has fallen off the ledge.
         *  Game state is set to GameOver.*/
		if (leftPuff.getX() < 15){
			leftPuff.stop();
			rightPuff.stop();
			currentState = GameState.GAMEOVER;
		}

        /** If the right SumoPuff's x coordinates is more than 115px, it has fallen off the ledge.
         *  Game state is set to GameOver.*/
		if (rightPuff.getX() > 115){
			leftPuff.stop();
			rightPuff.stop();
			currentState = GameState.GAMEOVER;
		}
		
	}

    /** Updates GameWorld when game state is GameOver.
     *  Used to notify opponents that a player has lost.*/
    public void updateGameOver(){
        actionResolver.BroadCastMyGameState(gameOverState);

    }

    /** Get methods for the left and right Puff object.*/
	public Puff getLeftPuff(){return leftPuff;}
	public Puff getRightPuff(){return rightPuff;}

    /** Set methods for the game states*/
    public void ready() {currentState =GameState.READY;}
    public void closeManual() {currentState = GameState.INITIALIZE;}
    public void openManual() {currentState = GameState.MANUAL;}
	public void start() {currentState = GameState.RUNNING; }
    public void gameOver(){currentState = GameState.GAMEOVER;}
    public void powerup(){currentState = GameState.POWERUP;}
    public void powerupAttack(){currentState = GameState.POWERUPATTACK;}

    /** Boolean methods that are used to check which state the GameWorld is currently in.*/
    public boolean isInitialized(){ return currentState== GameState.INITIALIZE;}
    public boolean isManual() {return currentState == GameState.MANUAL;}
    public boolean isReady() {return currentState == GameState.READY;}
    public boolean isStart(){return currentState == GameState.RUNNING;}
    public boolean isGameOver(){return currentState == GameState.GAMEOVER;}
    public boolean isGameOverReady(){return gameOverReady;}
    public boolean isPowerUp(){return currentState==GameState.POWERUP;}
    public boolean isPowerUpAttack(){return currentState==GameState.POWERUPATTACK;}

    /** Turns the manual page to the left.*/
    public void pageTurnLeft(int pageNumber){
        if (pageNumber == 1){this.pageNumber =3;}
        else{this.pageNumber--;}
    }
    /** Turns the manual page to the right.*/
    public void pageTurnRight(int pageNumber){
        if (pageNumber == 3){this.pageNumber =1;}
        else{this.pageNumber++;}
    }
    /** @return             An integer that represents current manual page*/
    public int getPage(){return pageNumber;}

    /** When the player enters GameOver state and decides to press "Play Again."
     *  Resets the current game state, SumoPuff, powerups, counts and timers.
     *  ActionResolver is used to broadcast this reset to the other player, to ensure that the
     *  reset is synchronized on both phones.
     */
	public void restart() {
        currentState = GameState.INITIALIZE;

        gameOverReady = false;
        leftPuff.reset(20, 100, 22, 45);
        rightPuff.reset(108, 100, 22, 45);
        leftPuff.collide = false;
        rightPuff.collide = false;
        actionResolver.BroadCastMyGameState(0);
        actionResolver.sendPowerUpAttack(0);
        freezeTimer.stop();
        attackTimer.stop();
        taskTimer.stop();
        AssetLoader.BackgroundMusic.play();
        AssetLoader.BackgroundMusic.setLooping(true);
    }
	


}