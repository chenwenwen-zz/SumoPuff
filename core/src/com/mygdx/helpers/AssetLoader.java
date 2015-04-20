package com.mygdx.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
    public static Texture texture;
    public static TextureRegion background,powerupBackground;
    public static TextureRegion powerupScreen;
    public static TextureRegion manual1, manual2, manual3;

    public static Animation runningAnimationRed,runningAnimationBlue;

     public static TextureRegion ramen,bwramen,riceball,bwriceball,iceCream,bwiceCream,ramenpower, bwramenpower, riceballpower, bwriceballpower, iceCreampower,bwiceCreampower, chooseapower;

    public static TextureRegion defaultRed, defaultBlue, redRun1, redRun2, redFall, blueRun1 ,blueRun2, blueFall;
    public static TextureRegion egg0,egg1,egg2,egg3,egg4,egg5,egg6,egg7,egg8,egg9;
    
    public static TextureRegion arrow,ready,start,redWinner, blueWinner, redLoser,blueLoser,playAgain,quit,help;
    public static BitmapFont font,shadow,arial;
    public static Music BackgroundMusic;
    public static Sound EggPressed;
    public static Sound Fanfare;
    public static Sound GameOver;
    public static Sound FallingDown;
    public static Sound PowerUp;
    // loads every texture from the disk. 
    // Called when SPGame is intialized. 
    public static void load(){
        BackgroundMusic=Gdx.audio.newMusic(Gdx.files.internal("BackgroundMusic.mp3"));
//        BackgroundMusic=Gdx.audio.newMusic(Gdx.files.internal("BackgroundMusic.mp3"));
        Fanfare=Gdx.audio.newSound(Gdx.files.internal("Fanfare.mp3"));
        GameOver=Gdx.audio.newSound(Gdx.files.internal("GameOver.mp3"));
        EggPressed=Gdx.audio.newSound(Gdx.files.internal("EggPressed.mp3"));
        FallingDown=Gdx.audio.newSound(Gdx.files.internal("FallingDown.mp3"));
        PowerUp=Gdx.audio.newSound(Gdx.files.internal("PowerUp.mp3"));

        background = new TextureRegion(new Texture(Gdx.files.internal("background.png")));
        background.flip(false, true);
        powerupBackground = new TextureRegion(new Texture(Gdx.files.internal("powerupbackground.png")));
        powerupBackground.flip(false,true);
        powerupScreen = new TextureRegion(new Texture(Gdx.files.internal("powerupscreen.png")));
        powerupScreen.flip(false, true);

        manual1 = new TextureRegion(new Texture(Gdx.files.internal("manual1.png")));
        manual1.flip(false, true);
        manual2 = new TextureRegion(new Texture(Gdx.files.internal("manual2.png")));
        manual2.flip(false, true);
        manual3 = new TextureRegion(new Texture(Gdx.files.internal("manual3.png")));
        manual3.flip(false, true);
     	
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
        help = new TextureRegion(new Texture(Gdx.files.internal("help.png")));
        help.flip(false, true);

        ramen = new TextureRegion(new Texture(Gdx.files.internal("ramen.png")));
        ramen.flip(false, true);
        bwramen = new TextureRegion(new Texture(Gdx.files.internal("bwramen.png")));
        bwramen.flip(false, true);
        ramenpower = new TextureRegion(new Texture(Gdx.files.internal("ramenpower.png")));
        ramenpower.flip(false, true);
        bwramenpower = new TextureRegion(new Texture(Gdx.files.internal("bwramenpower.png")));
        bwramenpower.flip(false, true);
        
        riceball = new TextureRegion(new Texture(Gdx.files.internal("riceball.png")));
        riceball.flip(false, true);
        bwriceball = new TextureRegion(new Texture(Gdx.files.internal("bwriceball.png")));
        bwriceball.flip(false, true);
        riceballpower = new TextureRegion(new Texture(Gdx.files.internal("riceballpower.png")));
        riceballpower.flip(false, true);
        bwriceballpower = new TextureRegion(new Texture(Gdx.files.internal("bwriceballpower.png")));
        bwriceballpower.flip(false, true);
        
        iceCream = new TextureRegion(new Texture(Gdx.files.internal("icecream.png")));
        iceCream.flip(false, true);
        bwiceCream = new TextureRegion(new Texture(Gdx.files.internal("bwicecream.png")));
        bwiceCream.flip(false, true);
        iceCreampower = new TextureRegion(new Texture(Gdx.files.internal("icecreampower.png")));
        iceCreampower.flip(false, true);
        bwiceCreampower = new TextureRegion(new Texture(Gdx.files.internal("bwicecreampower.png")));
        bwiceCreampower.flip(false, true);
        
        chooseapower = new TextureRegion(new Texture(Gdx.files.internal("chooseapower.png")));
        chooseapower.flip(false, true);

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
        font.setScale(.13f, -.13f);
        shadow = new BitmapFont(Gdx.files.internal("fonts/shadow.fnt"));
        shadow.setScale(.25f, -.25f);
        arial = new BitmapFont(Gdx.files.internal("fonts/arial.fnt"));
    }

    public static void dispose() {
        // We must dispose of the texture when we are finished.
       texture.dispose();

    }

}

