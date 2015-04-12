package com.mygdx.gameobjects;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by wenwen on 7/4/15.
 */
public class Timer {
    private long start;
    private int duration;
    private boolean isTimerOn = false;
    public Timer(int duration){
        this.duration = duration;
    }
    public void start() throws InterruptedException {
        if(isTimerOn==false){
         start = TimeUtils.millis()/1000;
         isTimerOn = true;
        }
     }
    public boolean isTimeUp(){
       if(isTimerOn) {
           if ((TimeUtils.millis() / 1000 - start) >= duration) {
               return true;
           } else
               return false;
       }

           else
           return false;
       }

    public void stop() {
        isTimerOn = false;
    }

  }

