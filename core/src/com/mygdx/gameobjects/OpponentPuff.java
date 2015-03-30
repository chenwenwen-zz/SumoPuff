package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.helpers.Collision;

// Opponents puff object.
// same as the main object except for one method.
public class OpponentPuff {
//hello
	// contains the current postion of the puff object. 
	// updated by delta times as per the update method in the GameWorld to reflect the position. 
	private Vector2 position;

	// It's not the velocity of the puff object. 
	// this vector allows to update the position of the puff delta times. (argument of the update method)
	// technically, this is not required. But it does allow, separation of the two variables during click and no click.
	private Vector2 velocity;

	// testCounter to increment CounterPress of OpponentPuff so as to stop the puff's when the button Presses are equal (not solved yet).
	private int testCounter = 0;

	// width and height; 
	// sets the width and height of the puff.
	private int width;
	private int height;

	// circle object used for detecting collision. 
	private Circle boundingCircle;

	// test count variables for checking movement. 
	public int thisCounterPress = 0;

	// initial collide value. Puffs start from the far end.
	public static Boolean collide = false;

	public OpponentPuff(float x, float y, int width, int height) {
		this.width = width;
		this.height = height;

		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);

		boundingCircle = new Circle();
	}

	public void update(float delta) {
		// code for stopping at the edge. 
		if (position.x < 20) {
			velocity.x =0;
		}

		// updates the position delta times by copying the velocity vector.
		position.add(velocity.cpy().scl(delta));
		// updates the position of the boundingCircle to move along with the puffs.
		boundingCircle.set(position.x + 6.5f, position.y, 6.4f);
	}

	// collision detection method
	public boolean collides(UserPuff pufftarget) {
		return (collide = Intersector.overlaps(pufftarget.getBoundingCircle(), this.getBoundingCircle()));
	}

	// method called at every click/tap for updating the position.
	public void onClick(UserPuff opponentPuff) {
		Float opponentVelocity;
		// thisCounterPress1+=1;
		testCounter += 1;

		// testing/debugging for stopping the puff when press values are the same. 
		if (testCounter == 1){
			thisCounterPress = 1;
		}

		// checks for collision. 
		if (collides(opponentPuff)){
			velocity.x = Collision.getpositions()[0];
			// opponentVelcoity = 
			Gdx.app.log("Velocity of the OpponentPuff", velocity.x + "");
			/*
		  	// ERROR prone part of the code.
         	// if(Counter1>Counter){
         	//      position.x=100;
     	    //  }
			// uncomment
			// Collision collisionobject = new Collision();
			// collisionobject.updatedposition(Counter, Counter1, position.x+this.getWidth(), opponentPuff.getX()+opponentPuff.getWidth());
			// velocity.x=collisionobject.getpositions().get(collisionobject.getpositions().size()-2);
			// collisionobject.updatedposition(thisCounterPress, opponentPuff.getCounterPress(), position.x, opponentPuff.getPuff().getX());
			*/

		// handles default case: userPuff is running
		} else {
			// incremented by value.
			velocity.x -= 5;
		}
	}

	// the velocity is changed whenever the puff is starting to collide.
	public void stop(){
		velocity.x=0;
	}

	// method for resetting the game after GAMEOVER state is reached. 
	public void reset(float x, float y, int width, int height){
		this.width = width;
		this.height = height;
        
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);

		boundingCircle = new Circle();
		thisCounterPress = 0;
	}

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Circle getBoundingCircle() {
		return boundingCircle;
	}

	public int getCounterPress(){
		return thisCounterPress;
	}

	public OpponentPuff getPuff(){
		return this;
	}
}