package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameworld.GameWorld;

public class InputHandler implements InputProcessor {
	
	private GameWorld myWorld;
	private Puff userPuff;
	private Puff oppPuff;
    private ActionResolver actionResolver;
    private int count=0;

	public InputHandler(GameWorld myWorld,ActionResolver actionResolver) {
		this.myWorld = myWorld;
        this.actionResolver = actionResolver;
		userPuff = myWorld.getUserPuff();
		oppPuff = myWorld.getOppPuff();
	 }


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	
		//System.out.println(screenX + " " + screenY + " " + pointer + " " + button);

		if (myWorld.isReady()) {
			System.out.println("Game Start!");
			SpriteBatch batcher = new SpriteBatch();
			myWorld.start();
			return true;
		}
		
		// increment userPuff press whenever touchDown is pressed. 
		/* Need to change here when integrating with GooglePlayService/ServerSocket */
		userPuff.incrementCounterPress();

		userPuff.onClick(oppPuff);
		oppPuff.onClick(userPuff);


        actionResolver.BroadCastMessage(count);
        count+=1;
			
		// only when gameover screen finish loading
		// ERROR: The screen separation is relative to the screen that one is using. 
		// The value separation doesn't work on the phone as of Friday.

		/* @Ching Yan, the values you put are ABSOLUTE values. Need to change them so that it works on every phone. */
		/* I commented your co-ordinates already */
		if (myWorld.isGameOverReady()) {
			Gdx.app.log("touchDownTesting", myWorld.isGameOverReady() + "");
			Gdx.app.log("ScreenVariablesReading", screenX + " " + screenY + " " + pointer + " " + button);
				// Reset all variables, go to GameState.READ
			
			//play again clicked 
			// if(16<=screenX && 213>=screenX && 148<=screenY && 215>=screenY){
			if(212<=screenX && 920>=screenX && 736<=screenY && 925>=screenY){
//			if (screenX < myWorld.getMidPoint() && screenY > 0){
				// Reset all variables, go to GameState.READY
				myWorld.restart();
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
			return false;
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