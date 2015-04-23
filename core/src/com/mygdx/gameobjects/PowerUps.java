package com.mygdx.gameobjects;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**This class consists constructor that allows the input of a string to define power up type upon
 * the object created. Method generateCoord() returns a HashMap of a coordinates for allocating the
 * egg images during the power up mini game. A static method called coordinatesSelector(number) is
 * called within generateCoord() to returns a HashMap of the coordinates that are randomly selected
 * from a vector list. The getPowerUpType() simply returns the type of the power up.
 *
 */
public class PowerUps {
    private String powerUpType;
    private static HashMap<Vector2,Boolean> coordEggs = new HashMap<Vector2,Boolean>();
    private static ArrayList<Vector2> vectorList = new ArrayList<Vector2>();

    /**This is the class constructor that accept a string input to define the type of the powerup
     * A set of vectors is added into the ArrayList for coordinates selections.
     *
     * @param PowerUpType           A String tha represents the power up type
     */
    public PowerUps(String PowerUpType){
        this.powerUpType = PowerUpType;
        vectorList.add(new Vector2(30,5));
        vectorList.add(new Vector2(50,5));
        vectorList.add(new Vector2(70,5));
        vectorList.add(new Vector2(90,5));
        vectorList.add(new Vector2(120,5));
        vectorList.add(new Vector2(30,30));
        vectorList.add(new Vector2(50,30));
        vectorList.add(new Vector2(70,30));
        vectorList.add(new Vector2(90,30));
        vectorList.add(new Vector2(120,30));
        vectorList.add(new Vector2(25,55));
        vectorList.add(new Vector2(45,55));
        vectorList.add(new Vector2(65,55));
        vectorList.add(new Vector2(85,55));
        vectorList.add(new Vector2(105,55));
        vectorList.add(new Vector2(125,55));

    }

    /**This method is to get a HashMap<Vector2,Boolean> of coordinators for egg images of the power
     * up mini game. The size of the HashMap is defined differently according the different types
     * of power up.
     *
     * @return             A HashMap<Vector2,Boolean> of coordinators
     */
    public HashMap<Vector2,Boolean> generateCoord(){
        coordEggs.clear();
        if(powerUpType.equals("ramen")){
           coordEggs.putAll(coordinateSelector(6));
        }
        else if(powerUpType.equals("riceBall")){
              coordEggs.putAll(coordinateSelector(6));
        }
        else{
            coordEggs.putAll(coordinateSelector(4));
        }
        return coordEggs;
    }


    /**This method is to select the coordinates randomly from an ArrayList of vector2.
     * A HashMap<Vector2,Boolean> is returned with the size that define by the input parameter
     * number
     *
     * @param number            An integer that defines the number of coordinators needed to be selected
     * @return                  An HashMap<Vector2,Boolean> of coordinators
     */
    public static HashMap<Vector2,Boolean> coordinateSelector(int number) {
        HashMap<Vector2,Boolean> tempMap = new HashMap<Vector2,Boolean>();
        tempMap.clear();
        Random random = new Random();
        ArrayList<Vector2> tempList = (ArrayList<Vector2>) vectorList.clone();

        int tempNo =0;
        for (int i = 15; i>0;i--) {
            if (i <= number) {
                if (i != 0) {
                    tempNo = random.nextInt(15);
                    tempMap.put(tempList.get(tempNo), false);
                    tempList.remove(tempNo);
                } else {
                    tempMap.put(tempList.get(0), false);
                }
            }

        }

        return tempMap;
    }

    /**This is the method to return the powerUpType of the Power up Object.
     *
     * @return          A String represents type of the power up
     */
   public String getPowerUpType(){return powerUpType;}
}
