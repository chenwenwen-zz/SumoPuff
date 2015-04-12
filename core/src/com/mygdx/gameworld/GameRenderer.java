package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gameobjects.PowerUps;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameobjects.Timer;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.AssetLoader;
import com.mygdx.helpers.InputHandler;

import java.util.ArrayList;
import java.util.HashMap;

// class which renders everything
public class GameRenderer {
	private GameWorld myWorld;
	private static OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private ShapeRenderer shapeRenderer1;

	//for calculating the falling curve
	private static int falldistance= 1;
	//for calculating how long to show the start sign.
	private static int showStart=1;

	private SpriteBatch batcher;

	private int midPointX;


	// Game Objects
	private Puff leftPuff;
	private Puff rightPuff;
    private ActionResolver actionResolver;
    private InputHandler handler;


    // Variables
    private int OppoCount;
    private int delay = 0;
    private float leftPuffX=0;
    private float rightPuffX=0;
    private HashMap<String,Boolean> powerUpsSelection;
    private HashMap<String,PowerUps> powerUps;
    private HashMap<Vector2,Boolean> powerUpCords;
    private Timer attackTimer;
    private ArrayList<TextureRegion> eggs = new ArrayList<TextureRegion>();
    private int eggIndex = 0;
    private int broadcastNoAttack = 0;
    private int broadcastAttack = 0;
    private Timer powerUpTaskTimer = new Timer(2);



    //Aspect Ratio and Scaling Components
    private static final int VIRTUAL_WIDTH = 800;
    private static final int VIRTUAL_HEIGHT = 480;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    private static Rectangle viewport;
    public static Vector2 crop = new Vector2(0f, 0f);
    public static float scale = 1f;
    public static int Case=0;
    public static float width;
    public static float height;
    public static float w;
    public static float h;
    //

    public GameRenderer(GameWorld world, int midPointX,ActionResolver actionResolver,InputHandler handler,Timer attackTimer) {
		myWorld = world;
		leftPuff = myWorld.getLeftPuff();
		rightPuff = myWorld.getRightPuff();
		this.midPointX = midPointX;
        this.actionResolver = actionResolver;
        this.handler = handler;
        this.powerUpsSelection = handler.getPowerUpsSelection();
        this.powerUps = handler.getPowerUps();
        this.powerUpCords = handler.getPowerUpCords();
        this.attackTimer = attackTimer;



        this.eggs.add(AssetLoader.egg0);
        this.eggs.add(AssetLoader.egg1);
        this.eggs.add(AssetLoader.egg2);
        this.eggs.add(AssetLoader.egg3);
        this.eggs.add(AssetLoader.egg4);
        this.eggs.add(AssetLoader.egg5);
        this.eggs.add(AssetLoader.egg6);
        this.eggs.add(AssetLoader.egg7);
        this.eggs.add(AssetLoader.egg8);
        this.eggs.add(AssetLoader.egg9);

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 150, 160);

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer1 = new ShapeRenderer();
		shapeRenderer1.setProjectionMatrix(cam.combined);


	}

	//calculating the trajectory of fall
	public int fallcurve (int x){
		return (int) (-Math.pow((x+Math.sqrt(100)),2)+100);
	}

	// renders everything. 
	public synchronized void render(float runTime) throws InterruptedException {

        //Begin Aspect Ratio Conversion
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        height=Gdx.graphics.getHeight();
        width= Gdx.graphics.getWidth();

        float aspectRatio = (float)width/(float)height;


        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
            Case=1;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (float)(height - VIRTUAL_HEIGHT*scale)/2f;
            Case=2;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        w = (float)VIRTUAL_WIDTH*scale;
        h = (float)VIRTUAL_HEIGHT*scale;


        viewport = new Rectangle(crop.x, crop.y, w, h);
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
        //End aspect ratio conversion


        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();

		// Begin SpriteBatch
		batcher.begin();

	    //Draw BackGround
		batcher.draw(AssetLoader.background, 0, 0, 150, 160);


        //GAMESTATE = INITIALIZE
        if(myWorld.isInitialized()){
           if(powerUpsSelection.get("ramen")==false)
           batcher.draw(AssetLoader.ramen,50,50,15,20);
           if(powerUpsSelection.get("riceBall")==false)
           batcher.draw(AssetLoader.riceball,70,50,15,20);
           if(powerUpsSelection.get("iceCream")==false)
           batcher.draw(AssetLoader.iceCream,90,50,15,20);

        }

		// GAMESTATE = READY
		if (myWorld.isReady()){
            falldistance = 0;
            batcher.draw(AssetLoader.defaultRed,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.defaultBlue, rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
            batcher.draw(AssetLoader.ready,50, 50, 50, 25);
		}




        //GAMESTATE = RUNNING
        if(myWorld.isStart()){
            if(showStart<60){
                batcher.draw(AssetLoader.start,50, 50, 50, 25);
            }
            ++showStart;
           //Timer reset
            attackTimer.stop();


            //sends 3 no attack Packet to ensure the opponent count will be reset
            broadcastAttack=0;
            while(broadcastNoAttack<3){
            actionResolver.sendPowerUpAttack(0);
            broadcastNoAttack++;
            }
            //end of sending

            if(leftPuff.collides(rightPuff)) {
                OppoCount = actionResolver.requestOppoCount();
            }
            leftPuffX=actionResolver.requestLeftPuffX();
            rightPuffX=actionResolver.requestRightPuffX();
            //delay is optional
            if(delay==2){
                if(leftPuff.getId().equals("player1")){
                    actionResolver.updateLeftPuffX(leftPuff.getX());
                    actionResolver.updateRightPuffX(rightPuff.getX());}

                leftPuff.onClick(rightPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
                rightPuff.onClick(leftPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
                delay=0;
            }
            else {
                delay++;
            }

            AssetLoader.font.draw(batcher,actionResolver.requestOppGameState()+"",10,10);
            AssetLoader.font.draw(batcher,actionResolver.requestOppoCount()+" ",80,50);

            batcher.draw(AssetLoader.runningAnimationRed.getKeyFrame(runTime), leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.runningAnimationBlue.getKeyFrame(runTime), rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
        }



        //GameState = PowerUp
        if(myWorld.isPowerUp()){
            eggIndex=0;
            broadcastNoAttack = 0;
            for(Vector2 key:powerUpCords.keySet()){
                if(powerUpCords.get(key)==false)
                   batcher.draw(eggs.get(eggIndex),key.x,key.y,15,15);
                eggIndex++;
            }
            if(powerUpCords.values().contains(false)){
                //powerupfail
            }
            else{
                myWorld.powerupAttack();
            }


            //Normal Game Routine
            if(leftPuff.collides(rightPuff)) {
                OppoCount = actionResolver.requestOppoCount();
            }
            leftPuffX=actionResolver.requestLeftPuffX();
            rightPuffX=actionResolver.requestRightPuffX();
            if(delay==2){
                if(leftPuff.getId().equals("player1")){
                    actionResolver.updateLeftPuffX(leftPuff.getX());
                    actionResolver.updateRightPuffX(rightPuff.getX());}
                leftPuff.onClick(rightPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
                rightPuff.onClick(leftPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
                delay=0;
            }
            else {
                delay++;
            }

            AssetLoader.font.draw(batcher, actionResolver.requestOppGameState() + "", 10, 10);
            AssetLoader.font.draw(batcher,actionResolver.requestOppoCount()+" ",80,50);

            batcher.draw(AssetLoader.runningAnimationRed.getKeyFrame(runTime), leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.runningAnimationBlue.getKeyFrame(runTime), rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());

        }

        //GameState = PowerUpAttack
        if(myWorld.isPowerUpAttack()){
            attackTimer.start();
            if(attackTimer.isTimeUp()){
                myWorld.start();
            }
            //Normal Game Routine
            Gdx.app.log("whichPowerUp",handler.getWhichPowerUp());
            if(leftPuff.collides(rightPuff)) {
                OppoCount = actionResolver.requestOppoCount();
            }
            leftPuffX=actionResolver.requestLeftPuffX();
            rightPuffX=actionResolver.requestRightPuffX();
            if(delay==2){
                if(leftPuff.getId().equals("player1")){
                    actionResolver.updateLeftPuffX(leftPuff.getX());
                    actionResolver.updateRightPuffX(rightPuff.getX());}
                leftPuff.onClick(rightPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
                rightPuff.onClick(leftPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
                delay=0;
            }
            else {
                delay++;
            }

            AssetLoader.font.draw(batcher, actionResolver.requestOppGameState() + "", 10, 10);
            AssetLoader.font.draw(batcher,actionResolver.requestOppoCount()+" ",80,50);

            batcher.draw(AssetLoader.runningAnimationRed.getKeyFrame(runTime), leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.runningAnimationBlue.getKeyFrame(runTime), rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());

            if(handler.getWhichPowerUp().equals("ramen")){
               //reset both counts by sending 3 packets to ensure opponent counts being reset
               while(broadcastAttack<3){
               handler.resetMyCount();
               actionResolver.BroadCastCount(handler.getMyCount());
               actionResolver.sendPowerUpAttack(1);
               broadcastAttack++;
               }
               attackTimer.stop();
               myWorld.start();
            }
            else if(handler.getWhichPowerUp().equals("iceCream")){
                //freeze the powerUp
               actionResolver.sendPowerUpAttack(2);
               attackTimer.stop();
               myWorld.start();
            }


        }





		//GAMESTATE = OVER
		if(myWorld.isGameOver()){
			showStart =0;
			//if userPuff loses
			if (myWorld.getLeftPuff().getX()<15){
				batcher.draw(AssetLoader.redFall,  (leftPuff.getX()+falldistance+3), leftPuff.getY()-(fallcurve(falldistance))/10, leftPuff.getWidth(), leftPuff.getHeight());
				batcher.draw(AssetLoader.defaultBlue,  rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
				if (falldistance<-40){
                    if(player1 > player2){
                        if(player1 == me){
                            batcher.draw(AssetLoader.redLoser, 0, 0, 150, 160);
                        }
                        else{
                            batcher.draw(AssetLoader.blueWinner, 0, 0, 150, 160);
                        }
                    }
                    else{
                        if(player1 == me){
                            batcher.draw(AssetLoader.blueWinner, 0, 0, 150, 160);
                        }
                        else{
                            batcher.draw(AssetLoader.redLoser, 0, 0, 150, 160);
                        }
                    }

                }
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain,5, 80, 40, 30);
					batcher.draw(AssetLoader.quit,112 , 85, 25, 15);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;}

			//if oppPuff loses
			if (myWorld.getRightPuff().getX()>110){
				batcher.draw(AssetLoader.blueFall,  (rightPuff.getX()-falldistance-3), rightPuff.getY()-(fallcurve(falldistance))/10, rightPuff.getWidth(), rightPuff.getHeight());
				batcher.draw(AssetLoader.defaultRed,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
				if (falldistance<-40){
                    if(player1 > player2){
                        if(player1 == me){
                            batcher.draw(AssetLoader.redWinner, 0, 0, 150, 160);
                        }
                        else{
                            batcher.draw(AssetLoader.blueLoser, 0, 0, 150, 160);
                        }
                    }
                    else{
                        if(player1 == me){
                            batcher.draw(AssetLoader.blueLoser, 0, 0, 150, 160);
                        }
                        else{
                            batcher.draw(AssetLoader.redWinner, 0, 0, 150, 160);
                        }
                    }
				}
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain, 5, 80, 40, 30);
					batcher.draw(AssetLoader.quit, 112, 85, 25, 15);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;
			}
		   // Player1 continue broadcasting the updated position to player2 if player2 gamestate!=GameOver
            if(leftPuff.getId().equals("player1")&& actionResolver.requestOppGameState()!=3){
                actionResolver.updateLeftPuffX(leftPuff.getX());
                actionResolver.updateRightPuffX(rightPuff.getX());}
		}

        // Common events for all the State except GameOver State
        if(myWorld.isReady() || myWorld.isStart()||myWorld.isPowerUp()||myWorld.isPowerUpAttack()){
            //Draws Arrow on top of the player
            if(player1 > player2){
                if(player1 == me){
                    batcher.draw(AssetLoader.arrow,leftPuff.getX()+4,leftPuff.getY()-15,10,15);
                }
                else{
                    batcher.draw(AssetLoader.arrow,rightPuff.getX()+4,rightPuff.getY()-15,10,15);
                }
            }
            else{
                if(player1 == me){
                    batcher.draw(AssetLoader.arrow,rightPuff.getX()+4,rightPuff.getY()-15,10,15);
                }
                else{
                    batcher.draw(AssetLoader.arrow,leftPuff.getX()+4,leftPuff.getY()-15,10,15);
                }
            }

            //Draws Powerup Labels on the Screen
            for(int i=1; i<3;i++){
                if(i==1){
                    if(powerUps.get("1").getPowerUpType().equals("ramen")){
                        batcher.draw(AssetLoader.ramen,10,30,10,10);
                    }
                    else if(powerUps.get("1").getPowerUpType().equals("riceBall")){
                        batcher.draw(AssetLoader.riceball,10,30,10,10);
                    }
                    else{
                        batcher.draw(AssetLoader.iceCream,10,30,10,10);
                    }
                }
                if(i==2){
                    if(powerUps.get("2").getPowerUpType().equals("ramen")){
                        batcher.draw(AssetLoader.ramen,10,50,10,10);
                    }
                    else if(powerUps.get("2").getPowerUpType().equals("riceBall")){
                        batcher.draw(AssetLoader.riceball,10,50,10,10);
                    }
                    else{
                        batcher.draw(AssetLoader.iceCream,10,50,10,10);
                    }
                }
            }

            if(actionResolver.checkPowerUpAttack()==1){
                //handler.resetMycount
                handler.resetMyCount();
                actionResolver.BroadCastCount(handler.getMyCount());
            }

            else if(actionResolver.checkPowerUpAttack()==2) {
                // freeze Powerup

            }
        }

		batcher.end();



	}
    //method to convert real phone coordinates to scaled coordinates, used by input handler
    public static Vector3 unprojectCoords(Vector3 coords){
        cam.unproject(coords, viewport.x, viewport.y, viewport.width, viewport.height);
        return coords;
    }
}

