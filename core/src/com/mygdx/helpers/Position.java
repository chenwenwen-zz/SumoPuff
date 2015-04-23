package com.mygdx.helpers;


/**This class defines the rate of change of the position based on the tapping count differences of the
 * two players after the two game character collides with each other. This class will only be usde under
 * Puff class to update two game character's positions.
 */
public class Position {

	private static float velocity = 0;

    /**This method is to update the rate of change of the game character's positions based on the
     * tapping count difference of the two players.
     *
     * @param myCount               An integer that represents the player's tapping counts
     * @param oppCount              An integer that represents the opponent player's tapping counts
     */
	public static void updatedposition(int myCount,int oppCount){
			if(myCount > oppCount){
				velocity = 0.13f;
			}
            else if (myCount<oppCount) {
				velocity = -0.13f;
			}

		}


    /**This method return the rate of change of the game character's positions.
     *
     * @return                      A float that represents the the rate of change of the game character's positions
     */
	public static float getVelocity(){
		return velocity;
	}

}
