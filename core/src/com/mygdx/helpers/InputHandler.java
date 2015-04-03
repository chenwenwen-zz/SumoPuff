package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameworld.GameWorld;

public class InputHandler implements InputProcessor {
	
	private GameWorld myWorld;
	private Puff LeftPuff;
	private Puff RightPuff;
    private ActionResolver actionResolver;
    private int count=0;

	public InputHandler(GameWorld myWorld,ActionResolver actionResolver) {
		this.myWorld = myWorld;
        this.actionResolver = actionResolver;
		LeftPuff = myWorld.getLeftPuff();
	    RightPuff = myWorld.getRightPuff();
	 }


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {


		 if (myWorld.isReady()) {
			//SpriteBatch batcher = new SpriteBatch();
			myWorld.start();
            return true;
		}

        actionResolver.BroadCastMessage(count);
        actionResolver.updateScreen(1);
        LeftPuff.onClick(RightPuff,count);
        RightPuff.onClick(LeftPuff,count);
        count+=1;
			
		// only when gameover screen finish loading
		// ERROR: The screen separation is relative to the screen that one is using. 
		// The value separation doesn't work on the phone as of Friday.

		/* @Ching Yan, the values you put are ABSOLUTE values. Need to change them so that it works on every phone. */
		/* I commented your co-ordinates already */
		if (myWorld.isGameOverReady()) {

			// Reset all variables, go to GameState.READ
			
			//play again clicked 
			// if(16<=screenX && 213>=screenX && 148<=screenY && 215>=screenY){
			if(212<=screenX && 920>=screenX && 736<=screenY && 925>=screenY){
//			if (screenX < myWorld.getMidPoint() && screenY > 0){
				// Reset all variables, go to GameState.READY
				myWorld.restart();
                count=0;
			}
			
			//quit screen is clicked
			if(302<=screenX && 465>=screenX && 114<=screenY && 219>=screenY){
//			if (screenX >= myWorld.getMidPoint() && screenY > 0){
			// System.out.println("quit");
				Gdx.app.log("QuitTesting", "Exitted");
				Gdx.app.exit();
			}
		}

			return true;
	}
    public int getCount(){return count;}
	@Override
	public boolean keyDown(int keycode) {
			return false;
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
            actionResolver.updateScreen(0);
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