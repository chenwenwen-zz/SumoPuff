package com.mygdx.gameobjects;

import com.badlogic.gdx.utils.TimeUtils;

/**This class consist of methods to define the basic functions of a timer. It will be used to create
 * different timer objects for the game section. The constructor takes in input to set the duration
 * of the timer. Method start() turns on the timer by setting the isTimeOn boolean variable to be
 * true. And isTimerUp() Method returns a boolean to determine whether the time is up. Method
 * checkTimeLeft() returns an integer that represents the number of seconds left. Method stop()
 * stops the timer by setting the isTimeOn to false.
 *
 */
public class Timer {
    private long start;
    private int duration;
    private boolean isTimerOn = false;

    /**This class constructor takes in an integer input to define the duration of the timer.
     *
     * @param duration          An integer that defines the duration of the timer.
     */
    public Timer(int duration){
        this.duration = duration;
    }


    /**This method set the boolean variable isTimeOn to true to allow the timer to start recording
     * the duration which is the time elapsed.
     *
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        if(isTimerOn==false){
         start = TimeUtils.millis()/1000;
         isTimerOn = true;
        }
     }


    /**This method checks if time elapsed equals the timer duration that is defined. It returns true
     * when they are equal, or returns false otherwise.
     *
     * @return              An boolean, true = time is up; false = otherwise.
     */
    public boolean isTimeUp(){
       if(isTimerOn) {
           if ((TimeUtils.millis() / 1000 - start) >= duration) {
               isTimerOn=false;
               return true;
           } else
               return false;
       }

           else
           return false;
       }

    /**This method is to check how much time left. It is calculated by using the current time minus
     * the starting time.
     *
     * @return          An integer that represents the number of seconds that left.
     */
    public int checkTimeLeft(){
        return (int) (duration-((TimeUtils.millis() / 1000 - start)));
    }

    /**This method stops the timer by setting the the boolean variable isTimerOn to be false.
     *
     */
    public void stop() {
        isTimerOn = false;
    }

  }

