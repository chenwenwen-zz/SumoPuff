package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gameobjects.PowerUps;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;

import java.util.HashMap;


public class InputHandler implements InputProcessor {
	
	private GameWorld myWorld;
	private Puff leftPuff;
	private Puff rightPuff;
    private ActionResolver actionResolver;
    private int myCount=0;
    private int powerUpCount=0;
    private final int readyState=1;
    private boolean isCordGenerated = false;
    private String whichPowerUp = " ";
   // private boolean ramenSelected=false;
    private HashMap<String,Boolean> powerUpsSelection= new HashMap<String,Boolean>();
    private HashMap<String,PowerUps> powerUps = new HashMap<String,PowerUps>();
    private HashMap<Vector2,Boolean> powerUpCords = new HashMap<Vector2,Boolean>();
    private boolean isPowerUpFreeze = true;
    private boolean isTouched = false;



    public InputHandler(GameWorld myWorld,ActionResolver actionResolver) {
		this.myWorld = myWorld;
        this.actionResolver = actionResolver;
		leftPuff = myWorld.getLeftPuff();
	    rightPuff = myWorld.getRightPuff();
        powerUpsSelection.put("ramen",false);
        powerUpsSelection.put("riceBall",false);
        powerUpsSelection.put("iceCream",false);
	 }


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Conversion to scaled of X and Y coords to scaled values
        Vector3 coords=new Vector3(screenX,screenY,0);
        Vector3 coords2= GameRenderer.unprojectCoords(coords);
        screenX=(int) coords2.x;
        screenY=(int) coords2.y;
        actionResolver.sendMove(1);
        isTouched = true;

        //Opens Manual


        //Initialize state
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

        //Opens Manual
        if (myWorld.isManual()){

            //if exit is pressed;
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

        //Running State
        if(myWorld.isStart() && actionResolver.requestOppGameState()!=3){
            myCount++;
            actionResolver.BroadCastCount(myCount);
            if(isPowerUpFreeze==false) {
                if (3 <= screenX && screenX <= 22 && 5 <= screenY && screenY <= 35) {
                    if (!isCordGenerated) {
                        powerUpCords.putAll(powerUps.get("1").generateCord());
                        whichPowerUp = powerUps.get("1").getPowerUpType();
                        myWorld.powerup();
                    }
                } else if (3 <= screenX && screenX <= 22 && 42 <= screenY && screenY <= 72) {
                    if (!isCordGenerated) {
                        powerUpCords.putAll(powerUps.get("2").generateCord());
                        whichPowerUp = powerUps.get("2").getPowerUpType();
                        myWorld.powerup();
                    }
                }
            }
        }

        //PowerUp State
        if(myWorld.isPowerUp()){
            myCount++;
            actionResolver.BroadCastCount(myCount);
            //Iterat through to check if eggs is touched
            for(Vector2 cord: powerUpCords.keySet()){
                if(powerUpCords.get(cord)==true){
                    continue;
                }
                else{
                if (cord.x <= screenX  && screenX<= (cord.x +18) && cord.y <= screenY && screenY<= cord.y+23){
                    powerUpCords.put(cord, true);
                }
                else{
                    break;
                }
               }
            }

        }

        //PowerupAttackState need to change state after timer is out, will not exit in this case
        if(myWorld.isPowerUpAttack()){
            if(whichPowerUp.equals("riceBall")){
                myCount+=2;
                actionResolver.BroadCastCount(myCount);
            }
        }

        //GameOverState
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

    public void resetPowerupVar(){
        isCordGenerated = false;
        whichPowerUp = " ";
        powerUpCords.clear();
    }
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