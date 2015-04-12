package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
    public static Texture texture;
    public static TextureRegion background,initialBackGround;

    public static Animation runningAnimationRed,runningAnimationBlue;
    public static TextureRegion defaultRed, defaultBlue, redRun1, redRun2, redFall, blueRun1 ,blueRun2, blueFall;
    public static TextureRegion egg0,egg1,egg2,egg3,egg4,egg5,egg6,egg7,egg8,egg9;

    public static TextureRegion ramen,riceball,iceCream;
    
    public static TextureRegion arrow,ready,start,redWinner, blueWinner, redLoser,blueLoser,playAgain,quit;
    public static BitmapFont font,shadow;

    // loads every texture from the disk. 
    // Called when SPGame is intialized. 
    public static void load(){

        /* @ Ching Yan, your part to comment if need be. */
        background = new TextureRegion(new Texture(Gdx.files.internal("background.png")));
        background.flip(false, true);
        initialBackGround = new TextureRegion(new Texture(Gdx.files.internal("powerupscreen.png")));
        initialBackGround.flip(false,true);

        
     	defaultRed = new TextureRegion(new Texture(Gdx.files.internal("defaultred.png")));
        defaultRed.flip(false, true);

        redRun1 = new TextureRegion(new Texture(Gdx.files.internal("redrun1.png")));
        redRun1.flip(false, true);

        redRun2 = new TextureRegion(new Texture(Gdx.files.internal("redrun2.png")));
        redRun2.flip(false, true);

    	redFall = new TextureRegion(new Texture(Gdx.files.internal("redfall.png")));
        redFall.flip(false, true);
        
        
        
        defaultBlue = new TextureRegion(new Texture(Gdx.files.internal("defaultblue.png")));
        defaultBlue.flip(false, true);

        blueRun1  = new TextureRegion(new Texture(Gdx.files.internal("bluerun1.png")));
        blueRun1 .flip(false, true);

        blueRun2  = new TextureRegion(new Texture(Gdx.files.internal("bluerun2.png")));
        blueRun2 .flip(false, true);
        
        blueFall = new TextureRegion(new Texture(Gdx.files.internal("bluefall.png")));
        blueFall.flip(false, true);

        TextureRegion[] redRun = { redRun1, redRun2};
        runningAnimationRed = new Animation(0.12f, redRun);
        runningAnimationRed.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        TextureRegion[] blueRun = { blueRun1 , blueRun2 };
        runningAnimationBlue = new Animation(0.12f, blueRun);
        runningAnimationBlue.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        arrow = new TextureRegion(new Texture(Gdx.files.internal("arrow.png")));
        arrow.flip(false,true);
        ready = new TextureRegion(new Texture(Gdx.files.internal("ready.png")));
        ready.flip(false,true);
        start = new TextureRegion(new Texture(Gdx.files.internal("start.png")));
        start.flip(false, true);
        
        redWinner = new TextureRegion(new Texture(Gdx.files.internal("redwinner.png")));
        redWinner.flip(false, true);
        blueWinner = new TextureRegion(new Texture(Gdx.files.internal("bluewinner.png")));
        blueWinner.flip(false, true);
        redLoser = new TextureRegion(new Texture(Gdx.files.internal("redloser.png")));
        redLoser.flip(false, true);
        blueLoser = new TextureRegion(new Texture(Gdx.files.internal("blueloser.png")));
        blueLoser.flip(false, true);

        quit = new TextureRegion(new Texture(Gdx.files.internal("Quit.png")));
        quit.flip(false, true);
        playAgain = new TextureRegion(new Texture(Gdx.files.internal("PlayAgain.png")));
        playAgain.flip(false, true);

        ramen = new TextureRegion(new Texture(Gdx.files.internal("ramen.png")));
        ramen.flip(false,true);
        riceball = new TextureRegion(new Texture(Gdx.files.internal("riceball.png")));
        riceball.flip(false,true);
        iceCream = new TextureRegion(new Texture(Gdx.files.internal("icecream.png")));
        iceCream.flip(false,true);

        egg0 = new TextureRegion(new Texture(Gdx.files.internal("egg0.png")));
        egg0.flip(false, true);
        egg1 = new TextureRegion(new Texture(Gdx.files.internal("egg1.png")));
        egg1.flip(false, true);
        egg2 = new TextureRegion(new Texture(Gdx.files.internal("egg2.png")));
        egg2.flip(false, true);
        egg3 = new TextureRegion(new Texture(Gdx.files.internal("egg3.png")));
        egg3.flip(false, true);
        egg4 = new TextureRegion(new Texture(Gdx.files.internal("egg4.png")));
        egg4.flip(false, true);
        egg5 = new TextureRegion(new Texture(Gdx.files.internal("egg5.png")));
        egg5.flip(false, true);
        egg6 = new TextureRegion(new Texture(Gdx.files.internal("egg6.png")));
        egg6.flip(false, true);
        egg7 = new TextureRegion(new Texture(Gdx.files.internal("egg7.png")));
        egg7.flip(false, true);
        egg8 = new TextureRegion(new Texture(Gdx.files.internal("egg8.png")));
        egg8.flip(false, true);
        egg9 = new TextureRegion(new Texture(Gdx.files.internal("egg9.png")));
        egg9.flip(false, true);
        //for displaying on screen
        font = new BitmapFont(Gdx.files.internal("fonts/text.fnt"));
        font.setScale(.25f, -.25f);
        shadow = new BitmapFont(Gdx.files.internal("fonts/shadow.fnt"));
        shadow.setScale(.25f, -.25f);
        
    }

    public static void dispose() {
        // We must dispose of the texture when we are finished.
      //  texture.dispose();
    }

}

