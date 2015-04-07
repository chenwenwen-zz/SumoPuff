package com.mygdx.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gameobjects.Puff;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.AssetLoader;
import com.mygdx.helpers.InputHandler;

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
    private int timer = 0;
    private float leftPuffX=0;
    private float rightPuffX=0;

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

    public GameRenderer(GameWorld world, int midPointX,ActionResolver actionResolver,InputHandler handler) {
		myWorld = world;
		leftPuff = myWorld.getLeftPuff();
		rightPuff = myWorld.getRightPuff();
		this.midPointX = midPointX;
        this.actionResolver = actionResolver;
        this.handler = handler;

		cam = new OrthographicCamera();
		cam.setToOrtho(true, 136, 160);

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
	public synchronized void render(float runTime) {

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

		// Begin SpriteBatch
		batcher.begin();

		// Disable transparency
		// This is good for performance when drawing images that do not require transparency.
		batcher.disableBlending();
		batcher.draw(AssetLoader.bg, 0, 0, 140, 160);
        AssetLoader.font.draw(batcher,actionResolver.requestOppGameState()+"",rightPuff.getX(),rightPuff.getY()-50);
		// The userPuff needs transparency, so we enable that again.
		batcher.enableBlending();

        //GAMESTATE = INITIALIZE
        if(myWorld.isInitialized()){
            batcher.draw(AssetLoader.puffDefault,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.puffDefaulta, rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
        }

		// GAMESTATE = READY
		if (myWorld.isReady()){
            falldistance = 0;
            batcher.draw(AssetLoader.puffDefault,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
            batcher.draw(AssetLoader.puffDefaulta, rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
            batcher.draw(AssetLoader.ready,50, 50, 50, 25);
		}


		//GAMESTATE = OVER
		else if(myWorld.isGameOver()){
			showStart =0;
			//if userPuff loses
			if (myWorld.getLeftPuff().getX()<15){
				batcher.draw(AssetLoader.puffFall,  (leftPuff.getX()+falldistance+3), leftPuff.getY()-(fallcurve(falldistance))/10, leftPuff.getWidth(), leftPuff.getHeight());
				batcher.draw(AssetLoader.puffDefaulta,  rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
				if (falldistance<-40){
					batcher.draw(AssetLoader.winner, 0, 0, 138, 160);
				}
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain,8, 80, 50, 25);
					batcher.draw(AssetLoader.quit,85, 80, 40, 25);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;}

			//if oppPuff loses
			if (myWorld.getRightPuff().getX()>110){

				batcher.draw(AssetLoader.puffFalla,  (rightPuff.getX()-falldistance-3), rightPuff.getY()-(fallcurve(falldistance))/10, rightPuff.getWidth(), rightPuff.getHeight());
				batcher.draw(AssetLoader.puffDefault,  leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
				if (falldistance<-40){
					batcher.draw(AssetLoader.winner, 0, 0, 138, 160);
				}
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain, 8, 80, 50, 25);
					batcher.draw(AssetLoader.quit, 85, 80, 40, 25);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;
			}
            actionResolver.BroadCastMyGameState(3);
            if(leftPuff.getId().equals("player1")&& actionResolver.requestOppGameState()!=3){
                actionResolver.updateLeftPuffX(leftPuff.getX());
                actionResolver.updateRightPuffX(rightPuff.getX());}
		}

		//GAMESTATE = RUNNING
		else if(myWorld.isStart()){
			if(showStart<60){
				batcher.draw(AssetLoader.start,50, 50, 50, 25);
			}
			++showStart;
            if(leftPuff.collides(rightPuff)){
            OppoCount = actionResolver.requestOppoCount();
            }
            leftPuffX=actionResolver.requestLeftPuffX();
            rightPuffX=actionResolver.requestRightPuffX();
            if(timer==2){
                if(leftPuff.getId().equals("player1")){
                    actionResolver.updateLeftPuffX(leftPuff.getX());
                    actionResolver.updateRightPuffX(rightPuff.getX());}

            leftPuff.onClick(rightPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
            rightPuff.onClick(leftPuff, handler.getMyCount(),OppoCount,leftPuffX,rightPuffX);
            timer=0;
            }
            else {
                timer++;
            }

           // handler.update();
           AssetLoader.font.draw(batcher,actionResolver.requestOppGameState()+"",rightPuff.getX(),rightPuff.getY()-50);
         /*   ArrayList<String> participants = actionResolver.getParticipants();
            String myId = actionResolver.getMyId();
            int player1 = participants.get(0).hashCode();
            int player2 = participants.get(1).hashCode();
            int me = myId.hashCode();
            if(player1 > player2){
                if(player1 == me){
                    AssetLoader.font.draw(batcher,"me",leftPuff.getX(),leftPuff.getY()-50);}
                else{
                    AssetLoader.font.draw(batcher,"me",rightPuff.getX(),rightPuff.getY()-50);}}

            else{
                if(player1 == me){
                    AssetLoader.font.draw(batcher,"me",rightPuff.getX(),rightPuff.getY()-50);
                }
                else{
                    AssetLoader.font.draw(batcher,"me",leftPuff.getX(),leftPuff.getY()-50);}

            }*/
            AssetLoader.font.draw(batcher,leftPuff.getX()+" ", leftPuff.getX(), leftPuff.getY() - 80);
            AssetLoader.font.draw(batcher,rightPuff.getX()+" ",rightPuff.getX(),rightPuff.getY()-100);

			batcher.draw(AssetLoader.runningAnimation.getKeyFrame(runTime), leftPuff.getX(), leftPuff.getY(), leftPuff.getWidth(), leftPuff.getHeight());
			batcher.draw(AssetLoader.runningAnimation1.getKeyFrame(runTime), rightPuff.getX(), rightPuff.getY(), rightPuff.getWidth(), rightPuff.getHeight());
		}
		// End SpriteBatch

		batcher.end();
		shapeRenderer.begin(ShapeType.Filled);

        
		if (myWorld.isGameOver()){
		}
		else{
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.circle(leftPuff.getBoundingCircle().x, leftPuff.getBoundingCircle().y, leftPuff.getBoundingCircle().radius);
			shapeRenderer1.begin(ShapeType.Line);
			shapeRenderer1.setColor(Color.BLUE);
			shapeRenderer1.circle(rightPuff.getBoundingCircle().x, rightPuff.getBoundingCircle().y, rightPuff.getBoundingCircle().radius);
		}


        shapeRenderer.end();
        shapeRenderer1.end();
	}
    //method to convert real phone coordinates to scaled coordinates, used by input handler
    public static Vector3 unprojectCoords(Vector3 coords){
        cam.unproject(coords, viewport.x, viewport.y, viewport.width, viewport.height);
        return coords;
    }
}

