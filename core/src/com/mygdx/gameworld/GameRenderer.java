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
    private Timer attackTimer;
    private Timer taskTimer;
<<<<<<< HEAD
//    private Timer freezeTimer;
=======
    private Timer freezeTimer;
>>>>>>> origin/master


    // Variables
    private int OppoCount;
    private float leftPuffX=0;
    private float rightPuffX=0;
    private HashMap<String,Boolean> powerUpsSelection;
    private HashMap<String,PowerUps> powerUps;
    private HashMap<Vector2,Boolean> powerUpCords;
    private ArrayList<TextureRegion> eggs = new ArrayList<TextureRegion>();
    private static int eggIndex = 0;
    private int broadcastNoAttack = 0;
    private int broadcastAttack = 0;
<<<<<<< HEAD

    private Timer freezeTimer = new Timer(10);




=======
    private Boolean musicgameover=false;
    private Boolean musicsumofalling=false;
    private Boolean musicpowerup=false;
>>>>>>> origin/master
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
    public static Boolean musicgameover=false;
    public static Boolean musicsumofalling=false;
    public static Boolean musicpowerup=false;
    public static Boolean musicbkgd=false;




    public GameRenderer(GameWorld world, int midPointX,ActionResolver actionResolver,InputHandler handler,Timer attackTimer,Timer taskTimer,Timer freezeTimer) {
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
        this.taskTimer = taskTimer;
        this.freezeTimer= freezeTimer;


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
        if(musicbkgd==false) {
            AssetLoader.BackgroundMusic.setVolume(0.3f);
            AssetLoader.BackgroundMusic.play();
            AssetLoader.BackgroundMusic.setLooping(true);

            musicbkgd=true;
        }
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



        //Get Players ID information
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();

        //Gdx.app.log("搞什么东西啊",actionResolver.checkPowerUpAttack()+"");

		// Begin SpriteBatch
		batcher.begin();
	    //Draw BackGround
		batcher.draw(AssetLoader.background, 0, 0, 150, 160);


        //GAMESTATE = INITIALIZE
        if(myWorld.isInitialized()){

           batcher.draw(AssetLoader.powerupBackground, 0,0,150,160);
           batcher.draw(AssetLoader.chooseapower,10,10,130,25);
           batcher.draw(AssetLoader.help, 138,138, 10,20);
           if(powerUpsSelection.get("ramen")==false)
                batcher.draw(AssetLoader.ramenpower,27,45,30,100);
           else
                batcher.draw(AssetLoader.bwramenpower,27,45,30,100);
           if(powerUpsSelection.get("riceBall")==false)
                batcher.draw(AssetLoader.riceballpower,60,45,30,100);
            else
                batcher.draw(AssetLoader.bwriceballpower,60,45,30,100);
           if(powerUpsSelection.get("iceCream")==false)
                batcher.draw(AssetLoader.iceCreampower,93, 45,30,100);
            else
                batcher.draw(AssetLoader.bwiceCreampower,93, 45,30,100);
        }


        else if (myWorld.isManual()){

            if (myWorld.getPage()==1){
                batcher.draw(AssetLoader.manual1,0,0,150,160);
            }

            if (myWorld.getPage()==2){
                batcher.draw(AssetLoader.manual2,0,0,150,160);
            }

            if (myWorld.getPage()==3){
                batcher.draw(AssetLoader.manual3,0,0,150,160);
            }

            AssetLoader.font.draw(batcher, "Exit", 65, 147);
        }


		// GAMESTATE = READY
		else if (myWorld.isReady()){
            falldistance = 0;
            batcher.draw(AssetLoader.defaultRed,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.defaultBlue, rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
            batcher.draw(AssetLoader.ready,50, 50, 50, 25);
            drawArrow(batcher);
            drawPowerUpDisable(batcher);

		}





        //GAMESTATE = RUNNING
       else if(myWorld.isStart()){
            if(showStart<60){
                batcher.draw(AssetLoader.start,50, 50, 50, 25);
            }
            ++showStart;
            handler.resetPowerupVar();
            //sends 3 no attack Packet to end Reset Count
            while(broadcastNoAttack<3){
            actionResolver.sendPowerUpAttack(0);
            broadcastNoAttack++;
            }
            //end of sending
            if(handler.isPowerUpFreezed()){
                drawPowerUpDisable(batcher);
                AssetLoader.font.draw(batcher,"PowerUp Frozen",35,35);
            }else{
                drawPowerUpEnable(batcher);
            }


           //normal game render routine(Puff Animation, Draw Arrow, check PowerUpAttack)
           generalGameRender(runTime);
        }



        //GameState = PowerUp
       else if(myWorld.isPowerUp()){
            //Disable PowerUp
            handler.setPowerUpFreeze(true);

            //Render eggs that appear on the screen at powerUp state
            eggIndex=0;
            batcher.draw(AssetLoader.powerupScreen,0,0,150,80);
            for(Vector2 key:powerUpCords.keySet()){
                if(powerUpCords.get(key)==false)
                   batcher.draw(eggs.get(eggIndex),key.x,key.y,15,20);
                eggIndex++;
            }
            if(powerUpCords.values().contains(false)){

                //powerupfail
            }
            else{
                if(musicpowerup==false){
                    AssetLoader.PowerUp.play();
                    musicpowerup=true;
                }
                myWorld.powerupAttack();

            }
            drawPowerUpDisable(batcher);
            //normal game render routine(Puff Animation, Draw Arrow, check PowerUpAttack)
            generalGameRender(runTime);

        }

        //GameState = PowerUpAttack
      else if(myWorld.isPowerUpAttack()){
            //normal game render routine(Puff Animation, Draw Arrow, check PowerUpAttack)
            generalGameRender(runTime);
            broadcastNoAttack = 0;
            //Render PowerUp Display
            drawPowerUpDisable(batcher);
            if(handler.getWhichPowerUp().equals("ramen")){
               showStart=0;
               //reset both counts by sending 3 packets to ensure opponent counts being reset
               AssetLoader.font.draw(batcher,"Count Reset",40,5);
               while(this.broadcastAttack<3){
               handler.resetMyCount();
               AssetLoader.font.draw(batcher,"Count Reset",40,5);
               actionResolver.BroadCastCount(handler.getMyCount());
               actionResolver.sendPowerUpAttack(1);
               this.broadcastAttack++;
               }
               this.broadcastAttack=0;
               countDown(batcher);

            }
            else if(handler.getWhichPowerUp().equals("iceCream")){
                //freeze the powerUp
                //reset both counts by sending 3 packets to ensure opponent's powerup is freezed
                while(this.broadcastAttack<3){
                    AssetLoader.font.draw(batcher, "Powerup Frozen ", 35, 5);
                    //Gdx.app.log("你到底有没有send","也是醉了");
                    actionResolver.sendPowerUpAttack(2);
                    this.broadcastAttack++;
                }
               this.broadcastAttack=0;
               //attackTimer.stop();
              // myWorld.start();
            }
            else{
                AssetLoader.font.draw(batcher,"Hit x2 ",60,5);
                actionResolver.sendPowerUpAttack(3);
                countDown(batcher);
                showStart=0;
            }
            if(handler.isPowerUpFreezed()){
                drawPowerUpDisable(batcher);
                AssetLoader.font.draw(batcher,"PowerUp Frozen",35,35);
            }else{
                drawPowerUpEnable(batcher);
            }


        }




		//GAMESTATE = OVER
	    else if(myWorld.isGameOver()){
            AssetLoader.BackgroundMusic.stop();

            //reset showStart
			showStart =0;


            //reset myCount
            //handler.resetMyCount();
            //actionResolver.BroadCastCount(handler.getMyCount());
			//if userPuff loses
			if (myWorld.getLeftPuff().getX()<15){
                if(musicsumofalling==false){
                    AssetLoader.FallingDown.play();
                    musicsumofalling=true;
                }
				batcher.draw(AssetLoader.redFall,  (leftPuff.getX()+falldistance+3), leftPuff.getY()-(fallcurve(falldistance))/10, leftPuff.getWidth(), leftPuff.getHeight());
				batcher.draw(AssetLoader.defaultBlue,  rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
				if (falldistance<-40){
                    if(player1 > player2){
                        if(player1 == me){

                            if(musicgameover==false){
                                AssetLoader.GameOver.pause();
                            AssetLoader.GameOver.play();
                                musicgameover=true;}

                            batcher.draw(AssetLoader.redLoser, 0, 0, 150, 160);

                        }
                        else{
                            if(musicgameover==false){
                            AssetLoader.Fanfare.play();
                                musicgameover=true;}
                            batcher.draw(AssetLoader.blueWinner, 0, 0, 150, 160);
                        }
                    }
                    else{
                        if(player1 == me){
                            if(musicgameover==false){
                                AssetLoader.Fanfare.play();
                                musicgameover=true;
                            }

                            batcher.draw(AssetLoader.blueWinner, 0, 0, 150, 160);
                        }
                        else{
                            if(musicgameover==false){
                                AssetLoader.GameOver.play();
                                musicgameover=true;
                            }
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
                if(musicsumofalling==false){
                    AssetLoader.FallingDown.play();
                    musicsumofalling=true;
                }
				batcher.draw(AssetLoader.blueFall,  (rightPuff.getX()-falldistance-3), rightPuff.getY()-(fallcurve(falldistance))/10, rightPuff.getWidth(), rightPuff.getHeight());
				batcher.draw(AssetLoader.defaultRed,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
				if (falldistance<-40){
                    if(player1 > player2){
                        if(player1 == me){
                            if(musicgameover==false){
                                AssetLoader.Fanfare.play();
                                musicgameover=true;
                            }
                            batcher.draw(AssetLoader.redWinner, 0, 0, 150, 160);
                        }
                        else{
                            if(musicgameover==false){
                            AssetLoader.GameOver.play();
                                musicgameover=true;}
                            batcher.draw(AssetLoader.blueLoser, 0, 0, 150, 160);
                        }
                    }
                    else{
                        if(player1 == me){
                            if(musicgameover==false){
                                AssetLoader.GameOver.play();
                                musicgameover=true;
                            }

                            batcher.draw(AssetLoader.blueLoser, 0, 0, 150, 160);
                        }
                        else{
                            if(musicgameover==false){
                                AssetLoader.Fanfare.play();
                                musicgameover=true;
                            }

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
                actionResolver.broadCastLeftPuffX(leftPuff.getX());
                actionResolver.broadCastRightPuffX(rightPuff.getX());}
		}

		batcher.end();



	}
    //method to convert real phone coordinates to scaled coordinates, used by input handler
    public static Vector3 unprojectCoords(Vector3 coords){
        cam.unproject(coords, viewport.x, viewport.y, viewport.width, viewport.height);
        return coords;
    }


    //Draws Arrow on top of the player
    private void drawArrow(SpriteBatch batcher) {
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();
        if (player1 > player2) {
            if (player1 == me) {
                batcher.draw(AssetLoader.arrow, leftPuff.getX() + 4, leftPuff.getY() - 15, 10, 15);
            } else {
                batcher.draw(AssetLoader.arrow, rightPuff.getX() + 4, rightPuff.getY() - 15, 10, 15);
            }
        } else {
            if (player1 == me) {
                batcher.draw(AssetLoader.arrow, rightPuff.getX() + 4, rightPuff.getY() - 15, 10, 15);
            } else {
                batcher.draw(AssetLoader.arrow, leftPuff.getX() + 4, leftPuff.getY() - 15, 10, 15);
            }
        }
    }

     //Draw PowerUp(enable Mode)
     private void drawPowerUpEnable(SpriteBatch batcher){
         for (int i = 1; i < 3; i++) {
             if (i == 1) {
                 if (powerUps.get("1").getPowerUpType().equals("ramen")) {
                     batcher.draw(AssetLoader.ramen, 4, 5, 18, 30);
                 } else if (powerUps.get("1").getPowerUpType().equals("riceBall")) {
                     batcher.draw(AssetLoader.riceball, 4, 5, 18, 30);
                 } else {
                     batcher.draw(AssetLoader.iceCream, 4, 5, 18, 30);
                 }
             }
             if (i == 2) {
                 if (powerUps.get("2").getPowerUpType().equals("ramen")) {
                     batcher.draw(AssetLoader.ramen, 4, 42, 18, 30);
                 } else if (powerUps.get("2").getPowerUpType().equals("riceBall")) {
                     batcher.draw(AssetLoader.riceball, 4, 42, 18, 30);
                 } else {
                     batcher.draw(AssetLoader.iceCream, 4, 38, 18, 30);
                 }
             }
         }
      }





    //Draw PowerUp(disable Mode)
    private void drawPowerUpDisable(SpriteBatch batcher){
        for(int i=1; i<3;i++){
            if(i==1){
                if(powerUps.get("1").getPowerUpType().equals("ramen")){
                    batcher.draw(AssetLoader.bwramen,4,5,18,30);
                }
                else if(powerUps.get("1").getPowerUpType().equals("riceBall")){
                    batcher.draw(AssetLoader.bwriceball,4,5,18,30);
                }
                else{
                    batcher.draw(AssetLoader.bwiceCream,4,5,18,30);
                }
            }
            if(i==2){
                if(powerUps.get("2").getPowerUpType().equals("ramen")){
                    batcher.draw(AssetLoader.bwramen,4,42,18,30);
                }
                else if(powerUps.get("2").getPowerUpType().equals("riceBall")){
                    batcher.draw(AssetLoader.bwriceball,4,42,18,30);
                }
                else{
                    batcher.draw(AssetLoader.bwiceCream,4,42,18,30);
                }
            }
        }
    }

    private void checkPowerUpAttacks(SpriteBatch batcher) throws InterruptedException {
        if(actionResolver.checkPowerUpAttack()==1){
            AssetLoader.font.draw(batcher,"Attack: Count Reset",40,5);
            attackTimer.start();
            handler.resetMyCount();
            actionResolver.BroadCastCount(handler.getMyCount());
            countDown(batcher);
        }

        else if(actionResolver.checkPowerUpAttack()==2) {
            AssetLoader.font.draw(batcher,"Attack: Froze Powerup ",50,5);
            Gdx.app.log("freeze", String.valueOf(actionResolver.checkPowerUpAttack()));
            handler.setPowerUpFreeze(true);
        }

        else if(actionResolver.checkPowerUpAttack()==3){
            AssetLoader.font.draw(batcher,"Attack: Opponent Hit x2 ",45,5);
            requestCountDown(batcher);

        }
    }


    //update puff animation and other frames
    private void generalGameRender(float runTime) throws InterruptedException {
        if (leftPuff.collides(rightPuff)) {
            OppoCount = actionResolver.requestOppoCount();
        }
        leftPuffX = actionResolver.requestLeftPuffX();
        rightPuffX = actionResolver.requestRightPuffX();
        //delay is optional
        if ((actionResolver.checkMove() == 1) || (handler.getIsTouched() == true) || (!leftPuff.collides(rightPuff)) || (actionResolver.requestOppGameState() == 3)) {
           // if (delay == 2) {
                if (leftPuff.getId().equals("player1")) {
                    actionResolver.broadCastLeftPuffX(leftPuff.getX());
                    actionResolver.broadCastRightPuffX(rightPuff.getX());
                }

                leftPuff.onClick(rightPuff, handler.getMyCount(), OppoCount, leftPuffX, rightPuffX);
                rightPuff.onClick(leftPuff, handler.getMyCount(), OppoCount, leftPuffX, rightPuffX);

           // } //else {
            //    delay++;
           // }
        }

        if(handler.isPowerUpFreezed()){
            this.freezeTimer.start();
           // AssetLoader.font.draw(batcher,"PowerUp Frozen",35,35);
        }
        if(freezeTimer.isTimeUp()){
            handler.setPowerUpFreeze(false);
        }

            AssetLoader.font.draw(batcher, "You:" + handler.getMyCount() + "", 4, 85);
            AssetLoader.font.draw(batcher, "Opp:" + actionResolver.requestOppoCount() + " ", 4, 95);

            drawArrow(batcher);
            checkPowerUpAttacks(batcher);

            batcher.draw(AssetLoader.runningAnimationRed.getKeyFrame(runTime), leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.runningAnimationBlue.getKeyFrame(runTime), rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
        }


    private void countDown(SpriteBatch batcher){
        if(attackTimer.checkTimeLeft()<=3 && attackTimer.checkTimeLeft()>2){
            AssetLoader.font.draw(batcher,3+"",70,20);
            actionResolver.broadCastTimeLeft(3);
        }
        if(attackTimer.checkTimeLeft()<=2 && attackTimer.checkTimeLeft()>1){
            AssetLoader.font.draw(batcher,2+"",70,20);
            actionResolver.broadCastTimeLeft(2);
        }
        if(attackTimer.checkTimeLeft()<=1&& attackTimer.checkTimeLeft()>0){
            AssetLoader.font.draw(batcher,1+"",70,20);
            actionResolver.broadCastTimeLeft(1);
        }
        if(actionResolver.getTimeLeft()==0){
            actionResolver.broadCastTimeLeft(0);
        }
    }



    public void requestCountDown(SpriteBatch batcher){
        if(actionResolver.getTimeLeft()==1)
            AssetLoader.font.draw(batcher,1+"",70,20);
        if(actionResolver.getTimeLeft()==2)
            AssetLoader.font.draw(batcher,2+"",70,20);
        if(actionResolver.getTimeLeft()==3)
            AssetLoader.font.draw(batcher,3+"",70,20);
        /*if(actionResolver.getTimeLeft()==0){
            showStart=0;
        }*/
    }



    }


