package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;


public class InputHandler implements InputProcessor,GestureDetector.GestureListener {
	
	private GameWorld myWorld;
	private Puff LeftPuff;
	private Puff RightPuff;
    private ActionResolver actionResolver;
    private int myCount=0;
    private ClickListener clickListener = new ClickListener();



	public InputHandler(GameWorld myWorld,ActionResolver actionResolver) {
		this.myWorld = myWorld;
        this.actionResolver = actionResolver;
		LeftPuff = myWorld.getLeftPuff();
	    RightPuff = myWorld.getRightPuff();
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
           LeftPuff.onClick(RightPuff,myCount);
           RightPuff.onClick(LeftPuff,myCount);
           actionResolver.BroadCastCount(myCount);


        }
        Gdx.app.log("x",screenX+"");
        Gdx.app.log("y",screenY+"");
        Gdx.app.log("pointer",pointer+"");
        Gdx.app.log("button",button+"");

		if (myWorld.isGameOverReady()) {

			// Reset all variables, go to GameState.READ
			
			//play again clicked 
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
    public void update()
    {

        if(clickListener.isOver()){
            actionResolver.updateScreen(1);
            LeftPuff.onClick(RightPuff,myCount);
            RightPuff.onClick(LeftPuff,myCount);
            myCount++;
        }
    }
    public int getCount(){return myCount;}

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


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}