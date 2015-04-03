package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
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
    public Vector2 velocity;

    // width and height;
    // sets the width and height of the puff.
    private int width;
    private int height;

    private String run;
    private String id;

    // circle object used for detecting collision.
    private Circle boundingCircle;
    private ActionResolver actionResolver;
    // initial collide value. Puffs start from the far end.
    public static Boolean collide = false;


    // UserPuff's constructor.
    public Puff(float x, float y, int width, int height, ActionResolver actionResolver,String run,String id) {
        this.width = width;
        this.height = height;
        this.actionResolver=actionResolver;
        this.run=run;
        this.id = id;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingCircle = new Circle();


    }

    public void update(float delta) {
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


    // method called at every click/tap for updating the position.
    public void onClick(Puff opponentPuff, int myCount) {
        if (collides(opponentPuff)){
            if((id=="me" && run=="runtoright")){
                Collision.updatedposition(myCount, actionResolver.requestOppoCount(), position.x);
                velocity.x = Collision.getpositions();}
            else if((id=="notme" && run=="runtoleft")){
                 Collision.updatedposition(myCount,actionResolver.requestOppoCount(), opponentPuff.getPuff().getX());
                 velocity.x = Collision.getpositions();
                }
            else if(id=="notme" && run=="runtoright"){
                Collision.updatedposition(myCount,actionResolver.requestOppoCount(), opponentPuff.getPuff().getX());
                velocity.x = -Collision.getpositions();
            }

            else if((id=="me" && run=="runtoleft")){
                Collision.updatedposition(myCount, actionResolver.requestOppoCount(), position.x);
                velocity.x = -Collision.getpositions();
            }


            }



            // handles default case: userPuff is running
        else {
            if(run=="runtoright"){
                velocity.x+=5;
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

    public Puff getPuff(){
        return this;
    }

}