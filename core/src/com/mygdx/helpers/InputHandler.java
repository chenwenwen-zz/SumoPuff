package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gameobjects.PowerUps;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;

import java.util.HashMap;

/**This class handles the touches and clicks from the user by implementing the InputProcessor interface.
 * The interface is an in built interface in Libgdx framework for receiving input events.
 * The input events are handled differently under different game states.  Power up objects are created
 * under this class based on the user inputs on the selection of the power ups.
 */
public class InputHandler implements InputProcessor {
	
	private GameWorld myWorld;
    private ActionResolver actionResolver;
    private int myCount=0;
    private int powerUpCount=0;
    private final int readyState=1;
    private boolean isCordGenerated = false;
    private String whichPowerUp = " ";
    private HashMap<String,Boolean> powerUpsSelection= new HashMap<String,Boolean>();
    private HashMap<String,PowerUps> powerUps = new HashMap<String,PowerUps>();
    private HashMap<Vector2,Boolean> powerUpCords = new HashMap<Vector2,Boolean>();
    private boolean isPowerUpFreeze = true;
    private boolean isTouched = false;


    /** The constructor takes in GameWorld and ActionResolver objects. GameWorld object is used to check
     * the current game state and change the game state based on the input events. While ActionResolver
     * Object is used to broadcast message to and request messages from opponent player based on the
     * input events.
     *
     * @param myWorld                    A GameWorld object used for getting SumoPuffs and game states.
     * @param actionResolver             An ActionResolver object used to broadcast message to players
     */
    public InputHandler(GameWorld myWorld,ActionResolver actionResolver) {
		this.myWorld = myWorld;
        this.actionResolver = actionResolver;
        powerUpsSelection.put("ramen",false);
        powerUpsSelection.put("riceBall",false);
        powerUpsSelection.put("iceCream",false);
	 }


    /**This method is and override method that keep track of the touch events. The touch events under
     * different game state trigger different activity events.
     *
     * @param screenX               An integer that represents the X coordinate of the touch point
     * @param screenY               An integer that represents the Y coordinate of the touch point
     * @param pointer               Unused
     * @param button                Unused
     * @return                      A boolean that determines if the touch event happened
     */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Conversion to scaled of X and Y coords to scaled values
        Vector3 coords=new Vector3(screenX,screenY,0);
        Vector3 coords2= GameRenderer.unprojectCoords(coords);
        screenX=(int) coords2.x;
        screenY=(int) coords2.y;
        actionResolver.sendMove(1);
        isTouched = true;


        /** Initialized State
         * Two different Power Up objects are created based on the touch events on the selection
         * of power ups.
         */
        if(myWorld.isInitialized()){

            if (135<=screenX && screenX <=150 && 135<= screenY && screenY<=160){
                myWorld.openManual();
            }

            if(27<=screenX && screenX<= 57 && 45<=screenY && screenY<=92){
                if(powerUpsSelection.get("ramen").equals(false)){
                  powerUpCount++;
                  powerUpsSelection.put("ramen",true);
                  powerUps.put(""+powerUpCount,new PowerUps("ramen"));
                }
            }
            else if(60<=screenX && screenX<=90 && 45<=screenY && screenY<=92){
                if(powerUpsSelection.get("riceBall").equals(false)){
                powerUpCount++;
                powerUpsSelection.put("riceBall",true);
                powerUps.put(""+powerUpCount,new PowerUps("riceBall"));
                }
            }
            else if(92<=screenX && screenX<=122 && 45<=screenY && screenY<=92){
               if(powerUpsSelection.get("iceCream").equals(false)){
                   powerUpCount++;
                   powerUpsSelection.put("iceCream",true);
                   powerUps.put(""+powerUpCount,new PowerUps("iceCream"));
                }
            }
            if(powerUpCount==2){
               actionResolver.BroadCastMyGameState(readyState);
               myWorld.ready();
            }

        }

        /** Manual State
         *  Displaying manual pages based on the touch events
         */
        if (myWorld.isManual()){
            if (30<=screenX && screenX<=120 && 140<=screenY && screenY <=160){
                myWorld.closeManual();
            }
            if (120<=screenX && screenX<=150 && 0<=screenY && screenY<=130){
                Gdx.app.log("Screen Touched","pressed right");
                myWorld.pageTurnRight(myWorld.getPage());
            }
            if (0<=screenX && screenX<=30 && 0<=screenY && screenY<=130){
                Gdx.app.log("Screen touched", "press left");
                myWorld.pageTurnLeft(myWorld.getPage());
            }
        }

        /** RUNNING State
         *  The player's tapping counts increase by 1 for every touch event. The corresponding power
         *  up is activated based on the touch events over the power up location on the phone screen.
         */
        if(myWorld.isStart() && actionResolver.requestOppGameState()!=3){
            myCount++;
            actionResolver.BroadCastCount(myCount);
            if(isPowerUpFreeze==false) {
                if (3 <= screenX && screenX <= 22 && 5 <= screenY && screenY <= 35) {
                    if (!isCordGenerated) {
                        powerUpCords.putAll(powerUps.get("1").generateCoord());
                        whichPowerUp = powerUps.get("1").getPowerUpType();
                        myWorld.powerup();
                    }
                } else if (3 <= screenX && screenX <= 22 && 42 <= screenY && screenY <= 72) {
                    if (!isCordGenerated) {
                        powerUpCords.putAll(powerUps.get("2").generateCoord());
                        whichPowerUp = powerUps.get("2").getPowerUpType();
                        myWorld.powerup();
                    }
                }
            }
        }

        /** PowerUp State
         *  The player's tapping counts increase by 1 for every touch event.Iterate through to check
         *  if eggs in the mini game has been touched. Set the corresponding boolean value in the
         *  HashMap<Vector2,Boolean> of the eggs coordinates.
         *
         */
        if(myWorld.isPowerUp()){
            myCount++;
            actionResolver.BroadCastCount(myCount);
            for(Vector2 cord: powerUpCords.keySet()){
                if(powerUpCords.get(cord)==true){
                    continue;
                }
                else{
                if (cord.x <= screenX  && screenX<= (cord.x +18) && cord.y <= screenY && screenY<= cord.y+23){
                    powerUpCords.put(cord, true);
                        AssetLoader.EggPressed.play(1f);
                }
                else{
                    break;
                }
               }
            }

        }

        /** PowerUpAttack State
         *  The player's tapping counts increase by 2 for every touch event if the power up type is
         *  rice ball, or increase by 1 otherwise.
         *
         */
        if(myWorld.isPowerUpAttack()){
            if(whichPowerUp.equals("riceBall")){
                myCount+=2;
                actionResolver.BroadCastCount(myCount);
            }
            else if(whichPowerUp.equals("iceCream")){
                myCount+=1;
                actionResolver.BroadCastCount(myCount);
            }
        }

        /** GameOver State
         *  If the "Play Again" button is selected, the game is reset and players go back to the Initialization screen.
         *  If "Quit" is selected, the player exits the game.
         */
		if (myWorld.isGameOverReady()) {
            if (4<=screenX && 45>=screenX && 45<=screenY && 110>=screenY) {
                // Reset all variables, go to GameState.READY
                Gdx.app.log("QuitTesting", "Restarted");
                resetGameVar();

                powerUpsSelection.put("ramen",false);
                powerUpsSelection.put("riceBall",false);
                powerUpsSelection.put("iceCream",false);
                actionResolver.BroadCastCount(myCount);
                myWorld.restart();
            }

           //quit screen is clicked
            if (113<=screenX && 140>=screenX && 83<=screenY && 101>=screenY) {
                Gdx.app.log("QuitTesting", "Exitted");
                resetGameVar();
                powerUpsSelection.put("ramen",false);
                powerUpsSelection.put("riceBall",false);
                powerUpsSelection.put("iceCream",false);
                Gdx.app.exit();
            }
        }
			return true;
	}

    public int getMyCount(){return myCount;}
    public void resetMyCount(){this.myCount=0;}
    public boolean getIsTouched(){return isTouched;}

    public boolean isPowerUpFreezed(){return isPowerUpFreeze;}
    public void setPowerUpFreeze(boolean isPowerUpFreeze){this.isPowerUpFreeze = isPowerUpFreeze;}

    public HashMap<String,PowerUps> getPowerUps(){return powerUps;}
    public HashMap<String,Boolean> getPowerUpsSelection(){return powerUpsSelection;}
    public HashMap<Vector2,Boolean> getPowerUpCords(){return powerUpCords;}
    public String getWhichPowerUp(){return whichPowerUp;}


    /*
     * Reset all the Power Up related variables
     */
    public void resetPowerupVar(){
        isCordGenerated = false;
        whichPowerUp = " ";
        powerUpCords.clear();
    }

    /*
     * Reset all the game related variables
     */
    public void resetGameVar(){
        myCount = 0;
        powerUpCount=0;
        isCordGenerated = false;
        isPowerUpFreeze = true;

    }

	@Override
	public boolean keyDown(int keycode) {

        return true;

	}

	@Override
	public boolean keyUp(int keycode) {
        return false;
	}

	@Override
	public boolean keyTyped(char character) {
			return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        actionResolver.sendMove(0);
        isTouched=false;
        return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
			return false;
	}

	@Override
	public boolean scrolled(int amount) {
			return false;
	}



}