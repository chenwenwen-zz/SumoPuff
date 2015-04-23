package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.helpers.Position;

/**This class contains the properties of the main game character(SumoPuff),including the height, the
 * width,the position, the ID and the running direction of the game character.The constructor takes
 * input to define initial states of the properties. The method collide() checks the collision of
 * the two game characters. update() method updates the position of the game character while onClick()
 * method change the position of the game character according the input count difference of the
 * two players. All the properties can be reset by calling the reset method. There are set methods
 * that returns the corresponding variables.
 *
 */

public class Puff {
    //(x,y) coordinates of the SumoPuff
    private Vector2 position;
    //rate of change of position
    public Vector2 velocity;
    private int width;
    private int height;
    //running direction
    private String run;
    // indicate player
    private String id;
    private final float y;
    // circle object used for detecting collision.
    private Circle boundingCircle;
    public static Boolean collide = false;

    /**The constructor takes in parameters to initialize the properties of the game character.
     *
     * @param x                     A float that represents the x coordinate of the game character
     * @param y                     A float that represents the y coordinate of the game character
     * @param width                 An integer that represents the width of the game character
     * @param height                An integer that represents the height of the game character
     * @param run                   A String that represents the running direction of the game character
     * @param id                    A String that represents the ID of the game character
     */
    public Puff(float x, float y, int width, int height,String run,String id) {
        this.width = width;
        this.height = height;
        this.run=run;
        this.id = id;
        this.y = y;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingCircle = new Circle();

    }

    /**This method updates the position of the game character, it will be called in the game world
     * under RUNNING STATE delta times per second.
     */
    public void update() {
        if(collide==false || id.equals("player1")){ position.add(velocity.cpy());}

        boundingCircle.set(position.x + 10f, position.y, 10f);

    }

    /**This method checks if two game characters collides with each other.
     *
     * @param opponetnPuff              A Puff Object that represents the opponent puff.
     * @return                          A boolean, true = collides; false = otherwise.
     */
    public boolean collides(Puff opponetnPuff) {
        return (collide = (Intersector.overlaps(opponetnPuff.getBoundingCircle(), this.getBoundingCircle())));
    }


    /**This method determines the changes in position of the game character. if the game character's
     * ID = "player1", the position changes based on the count difference, else if the game character's
     * ID = "player2", the position changes based on the input parameters leftPuffx coordinate and
     * rightPuffx coordinate that requested from the player1.
     *
     * @param opponentPuff              A Puff Object that represents the opponent puff.
     * @param myCount                   An integer that represents the local tapping counts
     * @param OppoCount                 An integer that represents the requested opponent tapping counts
     * @param leftPuffX                 A float that represents the x coordinates of the left game character
     * @param rightPuffX                A float that represents the x coordinates of the right game character
     */
    public synchronized void onClick(Puff opponentPuff, int myCount,int OppoCount,float leftPuffX,float rightPuffX) {
            if (collides(opponentPuff)) {
            if(id.equals("player1")) {
                if (Math.abs(myCount - OppoCount)==0) {
                    velocity.x = 0;
                } else {
                    Position.updatedposition(myCount, OppoCount);
                    velocity.x = Position.getVelocity();
                }

            }
            else if(id.equals("player2")){
                 if(run.equals("runtoright"))
                     position.set(leftPuffX,y);
                 else
                     position.set(rightPuffX,y);
            }

            }
           else {

                    if(run.equals("runtoright")) {
                        velocity.x = 0.4f;
                    } else {
                        velocity.x = -0.4f;
                    }

                }
          }

    /**This method set the rate of change of the position of the game character to 0. */
    public void stop(){
        velocity.x = 0;
    }

    /**This method allows the properties of the game character to be reset to its initial status
     *
     * @param x                         A float that represents the x coordinate of the game character
     * @param y                         A float that represents the y coordinate of the game character
     * @param width                     An integer that represents the width of the game character
     * @param height                    An integer that represents the height of the game character
     */
    public void reset(float x, float y, int width, int height){
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        boundingCircle = new Circle();
    }


    public String getId(){
        return id;
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

}