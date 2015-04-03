package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
    public static Texture texture;
    public static TextureRegion bg;

    public static Animation runningAnimation,runningAnimation1;
    public static TextureRegion puffDefault, puffDefaulta, puff1, puff2, puff3, puff4, puff5, puffFall,puff1a,puff2a,puff3a, puffFalla;
    
    public static TextureRegion ready,start,winner,playAgain,quit;
    public static BitmapFont font,shadow;

    // loads every texture from the disk. 
    // Called when SPGame is intialized. 
    public static void load(){

        /* @ Ching Yan, your part to comment if need be. */
        bg = new TextureRegion(new Texture(Gdx.files.internal("bg.png")));
        bg.flip(false, true);

        
     	puffDefault = new TextureRegion(new Texture(Gdx.files.internal("puffDefault.png")));
        puffDefault.flip(false, true);

        puff1 = new TextureRegion(new Texture(Gdx.files.internal("puff1.png")));
        puff1.flip(false, true);

        puff2 = new TextureRegion(new Texture(Gdx.files.internal("puff2.png")));
        puff2.flip(false, true);

        puff3 = new TextureRegion(new Texture(Gdx.files.internal("puff3.png")));
        puff3.flip(false, true);

    	puffFall = new TextureRegion(new Texture(Gdx.files.internal("puffFall.png")));
        puffFall.flip(false, true);
        
        
        
        puffDefaulta = new TextureRegion(new Texture(Gdx.files.internal("puffDefault.png")));
        puffDefaulta.flip(true, true);

        puff1a = new TextureRegion(new Texture(Gdx.files.internal("puff1.png")));
        puff1a.flip(true, true);

        puff2a = new TextureRegion(new Texture(Gdx.files.internal("puff2.png")));
        puff2a.flip(true, true);

        puff3a = new TextureRegion(new Texture(Gdx.files.internal("puff3.png")));
        puff3a.flip(true, true);


        puffFall = new TextureRegion(new Texture(Gdx.files.internal("puffFall.png")));
        puffFall.flip(false, true);
        
        puffFalla = new TextureRegion(new Texture(Gdx.files.internal("puffFall.png")));
        puffFalla.flip(true, true);

        TextureRegion[] puffRun = { puff1, puff2, puff3};
        runningAnimation = new Animation(0.12f, puffRun);
        runningAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        TextureRegion[] puffRun1 = { puff1a, puff2a, puff3a};
        runningAnimation1 = new Animation(0.12f, puffRun1);
        runningAnimation1.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        ready = new TextureRegion(new Texture(Gdx.files.internal("ready.png")));
        ready.flip(false,true);
        start = new TextureRegion(new Texture(Gdx.files.internal("start.png")));
        start.flip(false, true);
        winner = new TextureRegion(new Texture(Gdx.files.internal("winner.png")));
        winner.flip(false, true);
        quit = new TextureRegion(new Texture(Gdx.files.internal("Quit.png")));
        quit.flip(false, true);
        playAgain = new TextureRegion(new Texture(Gdx.files.internal("PlayAgain.png")));
        playAgain.flip(false, true);

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

