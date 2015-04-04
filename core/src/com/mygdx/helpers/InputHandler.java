package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;


public class InputHandler implements InputProcessor {
	
	private GameWorld myWorld;
	private Puff leftPuff;
	private Puff rightPuff;
    private ActionResolver actionResolver;
    private int myCount=0;
    private int myPreviousCount=1;



	public InputHandler(GameWorld myWorld,ActionResolver actionResolver) {
		this.myWorld = myWorld;
        this.actionResolver = actionResolver;
		leftPuff = myWorld.getLeftPuff();
	    rightPuff = myWorld.getRightPuff();
	 }


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Conversion to scaled of X and Y coords to scaled values
        Vector3 coords=new Vector3(screenX,screenY,0);
        Vector3 coords2= GameRenderer.unprojectCoords(coords);
        screenX=(int) coords2.x;
        screenY=(int) coords2.y;
		 if (myWorld.isReady()) {
			 myWorld.start();
             return true;
		}
        if(myWorld.isStart()){
               myCount++;
               if(myCount==myPreviousCount){
               actionResolver.BroadCastCount(myCount);
               myPreviousCount += myPreviousCount;
               }
               leftPuff.onClick(rightPuff, myCount);
               rightPuff.onClick(leftPuff, myCount);

        }

		if (myWorld.isGameOverReady()) {
            if(6<=screenX && 60>=screenX && 80<=screenY && 100>=screenY){
//			if (screenX < myWorld.getMidPoint() && screenY > 0){
                // Reset all variables, go to GameState.READY
                Gdx.app.log("QuitTesting","Restarted");
                myCount=0;
                actionResolver.BroadCastCount(myCount);
                myWorld.restart();
            }

            //quit screen is clicked
            if(66<screenX && 100>=screenX && 80<=screenY && 100>=screenY){
//			if (screenX >= myWorld.getMidPoint() && screenY > 0){
                // System.out.println("quit");
                Gdx.app.log("QuitTesting", "Exitted");
                myCount=0;
                Gdx.app.exit();
            }
		}

			return true;
	}

    public int getMyCount(){return myCount;}
   // public int getOppoCount(){return OppoCount;}

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