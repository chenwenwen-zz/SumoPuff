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

/** The GameRenderer render() method updates the game art according to the GameWorld's currentstate
 *  and the Inputhandler's onclick() method. Apart from the game art the GameRenderer also
 *  handles the animation, fonts and music of the game, using assets from AssetLoader.
 *
 */
public class GameRenderer {

    /** GameRenderer takes in GameWorld which sets the game state.*/
	private GameWorld myWorld;

    /** Objects for game orientation and display.*/
	private static OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private ShapeRenderer shapeRenderer1;

	/** A private counter for calculating tragetory for the SumoPuff's falling animation. */
	private static int falldistance= 1;
	/** A private counter for calculating how long to show the "start" sign.*/
	private static int showStart=1;

	private SpriteBatch batcher;

	/**GameObjects and helpers class objects*/
	private Puff leftPuff;
	private Puff rightPuff;
    private ActionResolver actionResolver;
    private InputHandler handler;
    private Timer attackTimer;
    private Timer freezeTimer;
    private Timer taskTimer;

    /** Variables used to keep track of in game objects such as coordinates of SumoPuffs and
     *  powerup selection.*/
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


    /** Variable used for aspect ratio and scaling components. */
    private static final int VIRTUAL_WIDTH = 800;
    private static final int VIRTUAL_HEIGHT = 480;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;
    private static Rectangle viewport;
    private static Vector2 crop = new Vector2(0f, 0f);
    private static float scale = 1f;
    private static int Case=0;
    private static float width;
    private static float height;
    private static float w;
    private static float h;

    /** Booleans used to decide which music to play.*/
    private Boolean musicgameover=false;
    private Boolean musicsumofalling=false;
    private Boolean musicpowerup=false;

    /** GameRender handles all the in game display art, animation, music, and fonts.
     *
     * @param world                     A GameWorld object used for getting SumoPuffs and game states.
     * @param actionResolver            An ActionResolver object used to broadcast message to players
     * @param handler                   A InputHandler object used to detect users' inputs
     * @param attackTimer               A Timer class object for power up effects.
     * @param freezeTimer               A Timer class object for the power up cool-down period.
     * @param taskTimer                 A Timer class object for the egg-cracking mini-game.
     */
    public GameRenderer(GameWorld world, ActionResolver actionResolver,InputHandler handler,Timer attackTimer,Timer freezeTimer,Timer taskTimer) {
		myWorld = world;
		leftPuff = myWorld.getLeftPuff();
		rightPuff = myWorld.getRightPuff();
        this.actionResolver = actionResolver;
        this.handler = handler;
        this.powerUpsSelection = handler.getPowerUpsSelection();
        this.powerUps = handler.getPowerUps();
        this.powerUpCords = handler.getPowerUpCords();
        this.attackTimer = attackTimer;
        this.freezeTimer= freezeTimer;
        this.taskTimer = taskTimer;

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

    /** A helper method for determine the trajectory of the SumoPuff's fall.
     *
     * @param x             A float that represents the SumoPuff's x coordinates.
     * @return              A float y coordinate, calculated given x.
     */
	public int fallcurve (int x){
		return (int) (-Math.pow((x+Math.sqrt(100)),2)+100);
	}

    /** The render methods sets the game display.
     *
     * @param runTime           A float that represents the accumulated running time
     * @throws InterruptedException
     */
	public synchronized void render(float runTime) throws InterruptedException {

        /** Begin aspect ratio conversion: The game display is scaled to maintain it's aspect
         *  ratio and resolution on different phones.*/
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
        /**End aspect ratio conversion.*/



        /** Gets player ID information */
        ArrayList<String> participants = actionResolver.getParticipants();
        String myId = actionResolver.getMyId();
        int player1 = participants.get(0).hashCode();
        int player2 = participants.get(1).hashCode();
        int me = myId.hashCode();


		/** Begin SpriteBatch*/
		batcher.begin();
	    /**Draw BackGround*/
		batcher.draw(AssetLoader.background, 0, 0, 150, 160);


        /**GAMESTATE = INITIALIZE*/
        if(myWorld.isInitialized()){
            /**reset boolean variable for sound*/
            this.musicgameover=false;
            this.musicsumofalling=false;
            this.musicpowerup=false;
           /** reset showStart*/
            showStart=0;

           /** Draw the power up selection screen */
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

        /**GAMESTATE = MANUAL */
        else if (myWorld.isManual()){

            /** Draws the differnt manual pages */
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


		/**GAMESTATE = READY*/
		else if (myWorld.isReady()){

            /** Reset fall distance */
            falldistance = 0;

            /** Sets the left and right SumoPuff to their default expression.*/
            batcher.draw(AssetLoader.defaultRed,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.defaultBlue, rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
            batcher.draw(AssetLoader.ready,50, 50, 50, 25);
            drawArrow(batcher);
            drawPowerUpDisable(batcher);

		}





        /**GAMESTATE = RUNNING*/
       else if(myWorld.isStart()){

            /** Show "Start" for 60 counts */
            if(showStart<60){
                batcher.draw(AssetLoader.start,50, 50, 50, 25);
            }
            ++showStart;
            handler.resetPowerupVar();
            /**Sends 3 attack Packet to end Reset Count*/
            while(broadcastNoAttack<3){
            actionResolver.sendPowerUpAttack(0);
            broadcastNoAttack++;
            }
            /*End of sending of packets.*/

            /** Draws power up depending on their states, frozen or unfrozen */
            if(handler.isPowerUpFreezed()){
                drawPowerUpDisable(batcher);

            }else{
                drawPowerUpEnable(batcher);
            }


           /** For normal game play rendering (Puff Animation, Draw Arrow, check PowerUpAttack)*/
           generalGameRender(runTime);
        }




        /**GameState = PowerUp*/
       else if(myWorld.isPowerUp()){
            /**Disable PowerUp */
            handler.setPowerUpFreeze(true);
            taskCountDown(batcher);
            /**Render eggs that appear on the screen at powerUp state*/
            eggIndex=0;
            batcher.draw(AssetLoader.powerupScreen,0,0,150,80);

            /** Render the eggs given the vector2 positions in the powerUpCords hashmap*/
            for(Vector2 key:powerUpCords.keySet()){
                if(powerUpCords.get(key)==false)
                   batcher.draw(eggs.get(eggIndex),key.x,key.y,15,20);
                eggIndex++;
            }
            if(powerUpCords.values().contains(false)){

              /** Power up failed */
            }
            else{
                if(musicpowerup==false){
                    AssetLoader.PowerUp.play();
                    musicpowerup=true;
                }
                myWorld.powerupAttack();

            }
            drawPowerUpDisable(batcher);
            /** For normal game play rendering (Puff Animation, Draw Arrow, check PowerUpAttack)*/
            generalGameRender(runTime);

        }

        //GameState = PowerUpAttack
      else if(myWorld.isPowerUpAttack()){
            /** For normal game play rendering (Puff Animation, Draw Arrow, check PowerUpAttack)*/
            generalGameRender(runTime);
            broadcastNoAttack = 0;
            /**Render PowerUp Display*/
            drawPowerUpDisable(batcher);
            if(handler.getWhichPowerUp().equals("ramen")){
               /** Count is reset */
               /** 3 packets are sent to ensure that the opponent's count is being reset*/
               AssetLoader.font.draw(batcher,"Count Reset",45,5);
               while(this.broadcastAttack<5){
               handler.resetMyCount();
               actionResolver.BroadCastCount(handler.getMyCount());
               actionResolver.sendPowerUpAttack(1);
               this.broadcastAttack++;
               }
               this.broadcastAttack=0;
               attackCountDown(batcher);

            }
            else if(handler.getWhichPowerUp().equals("iceCream")){
                /** Opponent's power ups are frozen. */
                /** 3 packets are sent to ensure that the opponent's count is being reset*/

                while(this.broadcastAttack<8){
                    actionResolver.sendPowerUpAttack(2);
                    this.broadcastAttack++;
                }
               this.broadcastAttack=0;
            }
            else{
                /** Player's hits x2. */
                /** 3 packets are sent to ensure that the opponent's count is being reset*/
                AssetLoader.font.draw(batcher,"Hit x2 ",60,5);
                actionResolver.sendPowerUpAttack(3);
                attackCountDown(batcher);
            }
            if(handler.isPowerUpFreezed()){
                drawPowerUpDisable(batcher);
            }else{
                drawPowerUpEnable(batcher);
            }


        }




		/**GAMESTATE = OVER*/
	    else if(myWorld.isGameOver()){
            AssetLoader.BackgroundMusic.stop();
            /**Reset showStart*/
			showStart =0;

			/**If player loses*/
			if (myWorld.getLeftPuff().getX()<15){
                if(musicsumofalling==false){
                    AssetLoader.FallingDown.play();
                    musicsumofalling=true;
                }

                /**Draws the player falling animation, while the opponent's SumoPuff looks on.
                 * The animation depends on which side the player is on, this is decided using the google play ID
                 * The falling animation's trajectory is determined by the fall distance.
                 */
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

                /** The SumoPuff has fallen off screen, and the winner and loser screen has been drawn.
                 *  The GameOver animation has been complete, so GameOverReady is set to true and players
                 *  can select "Play Again"*/
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain,5, 80, 40, 30);
					batcher.draw(AssetLoader.quit,112 , 85, 25, 15);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;}

			/**If opponent loses*/
			if (myWorld.getRightPuff().getX()>110){
                if(musicsumofalling==false){
                    AssetLoader.FallingDown.play();
                    musicsumofalling=true;
                }
                /** Draws the opponent's SumoPuff falling, while the play's Sumopuff looks on. */
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
		   /** Player1 continue broadcasting the updated position to player2 if player2 gamestate!=GameOver*/
            if(leftPuff.getId().equals("player1")&& actionResolver.requestOppGameState()!=3){
                actionResolver.broadCastLeftPuffX(leftPuff.getX());
                actionResolver.broadCastRightPuffX(rightPuff.getX());}
		}

		batcher.end();



	}
    //method to convert real phone coordinates to scaled coordinates, used by input handler

    /** unprojectCoords is a method that converts real phone coordinates to scaled coordinates.
     *  It is used by the InputHandler to ensure that the coordinates of user input is the same
     *  regardless of their phone size.
     *
     * @param coords            A Vector3 that store the (x,y,z) coordinates of the game. Since our game is
     *                          2 Dimensional, z is always set as 0.
     * @return                  A converted Vector3 coordinates that corresponds to the same point regardless
     *                          of  phone size.
     */
    public static Vector3 unprojectCoords(Vector3 coords){
        cam.unproject(coords, viewport.x, viewport.y, viewport.width, viewport.height);
        return coords;
    }


    /** For drawing the arrow that indicates which SumoPuff is the player controlling
     *
     * @param batcher           A SpriteBatcher used for drawing.
     */
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

    /**Draws enabled power ups, according to which power up is selected in the powerUps Hashmap.
     *
     * @param batcher           A SpriteBatcher used for drawing.
     */
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

    /**Draws disabled power ups, according to which power up is selected in the powerUps Hashmap.
     *
     * @param batcher           A SpriteBatcher used for drawing.
     */
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

    /** Draws a notification on the player's screen when they suceed in activating a powerup
     *
     * @param batcher           A SpriteBatcher used for drawing.
     * @throws InterruptedException
     */
    private void checkPowerUpAttacks(SpriteBatch batcher) throws InterruptedException {
        if(actionResolver.checkPowerUpAttack()==1){
            AssetLoader.font.draw(batcher,"Attack: Count Reset",30, 33);
            handler.resetMyCount();
            actionResolver.BroadCastCount(handler.getMyCount());
            requestCountDown(batcher);
        }

        else if(actionResolver.checkPowerUpAttack()==2) {
            AssetLoader.font.draw(batcher,"Attack:Powerup Frozen",30,33);
            handler.setPowerUpFreeze(true);
        }

        else if(actionResolver.checkPowerUpAttack()==3){
            AssetLoader.font.draw(batcher,"Attack:Hit x2 ",45,33);
            requestCountDown(batcher);
        }
    }


    /** For rendering general gameplay*/
    private void generalGameRender(float runTime) throws InterruptedException {
        if (leftPuff.collides(rightPuff)) {
            OppoCount = actionResolver.requestOppoCount();
        }
        leftPuffX = actionResolver.requestLeftPuffX();
        rightPuffX = actionResolver.requestRightPuffX();
        /**delay is optional*/
        if ((actionResolver.checkMove() == 1) || (handler.getIsTouched() == true) || (!leftPuff.collides(rightPuff)) || (actionResolver.requestOppGameState() == 3)) {
                if (leftPuff.getId().equals("player1")) {
                    actionResolver.broadCastLeftPuffX(leftPuff.getX());
                    actionResolver.broadCastRightPuffX(rightPuff.getX());
                }

                leftPuff.onClick(rightPuff, handler.getMyCount(), OppoCount, leftPuffX, rightPuffX);
                rightPuff.onClick(leftPuff, handler.getMyCount(), OppoCount, leftPuffX, rightPuffX);

        }

        if(handler.isPowerUpFreezed()){
            this.freezeTimer.start();
        }
        if(freezeTimer.isTimeUp()){
            handler.setPowerUpFreeze(false);
        }
            /** Shows the player and opponent's count */
            AssetLoader.font.draw(batcher, "You:" + handler.getMyCount() + "", 4, 85);
            AssetLoader.font.draw(batcher, "Opp:" + actionResolver.requestOppoCount() + " ", 4, 95);

            drawArrow(batcher);
            checkPowerUpAttacks(batcher);

            batcher.draw(AssetLoader.runningAnimationRed.getKeyFrame(runTime), leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.runningAnimationBlue.getKeyFrame(runTime), rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
        }

    /* A countdown when power up wears off */
    private void attackCountDown(SpriteBatch batcher){
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

    }

    /* A countdown for the egg mini-game.*/
    private void taskCountDown(SpriteBatch batcher){
        if(taskTimer.checkTimeLeft()<=3 && taskTimer.checkTimeLeft()>2){
            AssetLoader.font.draw(batcher,3+"",70,20);
            actionResolver.broadCastTimeLeft(3);
        }
        if(taskTimer.checkTimeLeft()<=2 && taskTimer.checkTimeLeft()>1){
            AssetLoader.font.draw(batcher,2+"",70,20);
            actionResolver.broadCastTimeLeft(2);
        }
        if(taskTimer.checkTimeLeft()<=1&& taskTimer.checkTimeLeft()>0){
            AssetLoader.font.draw(batcher,1+"",70,20);
            actionResolver.broadCastTimeLeft(1);
        }

    }

    /* A Countdown for when the opponent's power up is wearing off.*/
    public void requestCountDown(SpriteBatch batcher){
        if(actionResolver.getTimeLeft()==1)
            AssetLoader.font.draw(batcher,1+"",70,45);
        if(actionResolver.getTimeLeft()==2)
            AssetLoader.font.draw(batcher,2+"",70,45);
        if(actionResolver.getTimeLeft()==3)
            AssetLoader.font.draw(batcher,3+"",70,45);

    }
   }


