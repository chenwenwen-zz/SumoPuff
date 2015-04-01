package com.mygdx.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.Collision;

// Main puff object. 
// This object is the current users puff object. 

public class Puff {

    // contains the current postion of the puff object.
    // updated by delta times as per the update method in the GameWorld to reflect the position.
    private Vector2 position;

    // It's not the velocity of the puff object.
    // this vector allows to update the position of the puff delta times. (argument of the update method)
    // technically, this is not required. But it does allow, separation of the two variables during click and no click.
    private Vector2 velocity;

    // width and height;
    // sets the width and height of the puff.
    private int width;
    private int height;

    // circle object used for detecting collision.
    private Circle boundingCircle;
    private ActionResolver actionResolver;
    // initial collide value. Puffs start from the far end.
    public static Boolean collide = false;
    private String directions = "left";
    // test count variables for checking movement.
    private int thisCounterPress = 1; // = 2;

    // UserPuff's constructor.
    public Puff(float x, float y, int width, int height, ActionResolver actionResolver) {
        this.width = width;
        this.height = height;
        this.actionResolver=actionResolver;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingCircle = new Circle();
    }

    public void update(float delta) {
        // code for stopping at the edge.
        // if (position.x > 120) {
        //     velocity.x = 0;
        // }

        // updates the position delta times by copying the velocity vector.
        position.add(velocity.cpy().scl(delta));
        // updates the position of the boundingCircle to move along with the puffs.
        boundingCircle.set(position.x + 6.5f, position.y, 6.4f);
    }

    // collision detection method
    public boolean collides(Puff pufftarget) {
        return (collide = (Intersector.overlaps(pufftarget.getBoundingCircle(), this.getBoundingCircle())));
    }
	


    // method to increase the CounterPress value with each touchdown();
    // momentarily fixed the counterPressValues for debugging issues.
    public int incrementCounterPress(){
        thisCounterPress += 1;
        // Gdx.app.log("counter", thisCounterPress+" ");
        return thisCounterPress;
    }

    // method called at every click/tap for updating the position.
    public void onClick(Puff opponentPuff) {
        Float opponentVelocity;
        

        // checks for collision.
        if (collides(opponentPuff)){

            Gdx.app.log("Something", opponentPuff.getX() + "");

            // Static class method call.

            if(getDirections().equals("left")){
                Collision.updatedposition(thisCounterPress, opponentPuff.getCounterPress(), position.x, opponentPuff.getPuff().getX());
                velocity.x = Collision.getpositions()[0];

                // if (opponentVelocity == velocity.x){ velocity.x = 0; }
                Gdx.app.log("Velocity of the UserPuff", velocity.x + "");
            }
            else if (getDirections().equals("right")){
                // Collision.updatedposition(opponentPuff.getCounterPress(),thisCounterPress, position.x, opponentPuff.getPuff().getX());
                velocity.x = Collision.getpositions()[0];
            }

            // handles default case: userPuff is running
        } else {
            // incremented by value.
            if(getDirections().equals("left")){
                velocity.x += 5;
            }
            else{
                velocity.x-=5;
            }
        }
    }

    // the velocity is changed whenever the puff is starting to collide.
    public void stop(){
        velocity.x = 0;
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
    public void setDirections(String directions){
        this.directions = directions;
    }
   
    public String getDirections(){
        return this.directions;
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
        return actionResolver.requestOppoCount();
    }

    public Puff getPuff(){
        return this;
    }

}