package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.helpers.Collision;

// Main puff object. 
// This object is the current users puff object. 

public class Puff {
    //(x,y) coordinates of the Sumo
    private Vector2 position;
    //rate of change of position
    public Vector2 velocity;

    // sets the width and height of the Sumo.
    private int width;
    private int height;

    //running direction
    private String run;
    // indicate me
    private String id;

    // circle object used for detecting collision.
    private Circle boundingCircle;
    public static Boolean collide = false;

    // UserPuff's constructor.
    public Puff(float x, float y, int width, int height,String run,String id) {
        this.width = width;
        this.height = height;
        this.run=run;
        this.id = id;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingCircle = new Circle();

    }

    public void update(float delta) {
        position.add(velocity.cpy());
//        boundingCircle.set(position.x + 6.5f, position.y, 6.4f);
        boundingCircle.set(position.x + 6.5f, position.y, 6.4f);

    }

    // collision detection method
    public boolean collides(Puff pufftarget) {
        return (collide = (Intersector.overlaps(pufftarget.getBoundingCircle(), this.getBoundingCircle())));
    }

    // method called at every click/tap for updating the position.
    public synchronized void onClick(Puff opponentPuff, int myCount,int OppoCount) {

            if (collides(opponentPuff)) {
                if(Math.abs(myCount-OppoCount)<2){
                    velocity.x=0;
                }
                else{
                if ((id == "me" && run == "runtoright")) {
                    Collision.updatedposition(myCount,OppoCount);
                    velocity.x = Collision.getVelocity();
                } else if ((id == "notme" && run == "runtoleft")) {
                    Collision.updatedposition(myCount,OppoCount);
                    velocity.x = Collision.getVelocity();
                } else if (id == "notme" && run == "runtoright") {
                    Collision.updatedposition(myCount,OppoCount);
                    velocity.x = -Collision.getVelocity();
                } else if ((id == "me" && run == "runtoleft")) {
                    Collision.updatedposition(myCount,OppoCount);
                    velocity.x = -Collision.getVelocity();
                }
                }
            }
                // handles default case: userPuff is running
           else {

                    if (run == "runtoright") {
                        velocity.x = 0.5f;
                    } else {
                        velocity.x = -0.5f;
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