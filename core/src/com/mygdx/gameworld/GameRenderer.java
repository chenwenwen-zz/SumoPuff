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

import java.util.ArrayList;

// class which renders everything
public class GameRenderer {

	private GameWorld myWorld;
    private ActionResolver actionResolver;
	private static OrthographicCamera cam;

	private ShapeRenderer shapeRenderer;
	private ShapeRenderer shapeRenderer1;

	//for calculating the falling curve
	private static int falldistance= 1;
	//for calculating how long to show the start sign.
	private static int showStart=1;

	private SpriteBatch batcher;

	private int midPointX;
	private int gameWidth;

	// Game Objects
	private Puff userPuff;
	private Puff oppPuff;

    //Aspect Ratio and Scaling Components
    private static final int VIRTUAL_WIDTH = 620;
    private static final int VIRTUAL_HEIGHT = 350;
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

    public GameRenderer(GameWorld world, int gameWidth, int gameHeight, int midPointX,ActionResolver actionResolver) {
		myWorld = world;
        this.actionResolver = actionResolver;
		userPuff = myWorld.getUserPuff();
		oppPuff = myWorld.getOppPuff();

		this.gameWidth = gameWidth;
		this.midPointX = midPointX;

		cam = new OrthographicCamera();
		cam.setToOrtho(true, gameWidth, 160);

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
	public void render(float runTime) {
        //Begin Aspect Ratio Conversion
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        height=Gdx.graphics.getHeight();
        width=Gdx.graphics.getWidth();

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
		/* @ Ching Yan, your part to comment if need be. Note: changed puff variables name. */

		// Begin SpriteBatch
		batcher.begin();

		// Disable transparency
		// This is good for performance when drawing images that do not require transparency.

		batcher.disableBlending();
		batcher.draw(AssetLoader.bg, 0, 0, 140, 160);

		// The userPuff needs transparency, so we enable that again.
		batcher.enableBlending();

		// GAMESTATE = READY
		if (myWorld.isReady()) {
			falldistance = 0;
			batcher.draw(AssetLoader.ready, midPointX-25, 50, 50, 25);
			batcher.draw(AssetLoader.puffDefault,  userPuff.getX(), userPuff.getY(), userPuff.getWidth(), userPuff.getHeight());
			batcher.draw(AssetLoader.puffDefaulta,  oppPuff.getX(), oppPuff.getY(), oppPuff.getWidth(), oppPuff.getHeight());
		}

		//GAMESTATE = OVER
		else if(myWorld.isGameOver()){
			showStart =0;

			//if userPuff loses
			if (myWorld.getUserPuff().getX()<15){
				//batcher.draw(AssetLoader.puffDefaulta,  oppPuff.getX(), oppPuff.getY(), oppPuff.getWidth(), oppPuff.getHeight());
				batcher.draw(AssetLoader.puffFall,  (userPuff.getX()+falldistance+3), userPuff.getY()-(fallcurve(falldistance))/10, userPuff.getWidth(), userPuff.getHeight());
				batcher.draw(AssetLoader.puffDefaulta,  oppPuff.getX(), oppPuff.getY(), oppPuff.getWidth(), oppPuff.getHeight());
				if (falldistance<-40){
					batcher.draw(AssetLoader.winner, 0, 0, 138, 160);
				}
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain, midPointX-60, 80, 50, 25);
					batcher.draw(AssetLoader.quit, midPointX+20, 80, 40, 25);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;}

			//if oppPuff loses
			if (myWorld.getOppPuff().getX()>110){
				
				batcher.draw(AssetLoader.puffFalla,  (oppPuff.getX()-falldistance-3), oppPuff.getY()-(fallcurve(falldistance))/10, oppPuff.getWidth(), userPuff.getHeight());
				batcher.draw(AssetLoader.puffDefault,  userPuff.getX(), userPuff.getY(), userPuff.getWidth(), userPuff.getHeight());
				if (falldistance<-40){
					batcher.draw(AssetLoader.winner, 0, 0, 138, 160);
				}
				if (falldistance<-45){
					batcher.draw(AssetLoader.playAgain, midPointX-60, 80, 50, 25);
					batcher.draw(AssetLoader.quit, midPointX+20, 80, 40, 25);}
				if (falldistance<-50){
					myWorld.gameOverReady = true;}
				falldistance--;
			}
		}

		//GAMESTATE = RUNNING
		else{

			if(showStart<20){
				batcher.draw(AssetLoader.start, midPointX-25, 50, 50, 25);
			}
			++showStart;
			batcher.draw(AssetLoader.runningAnimation.getKeyFrame(runTime), userPuff.getX(), userPuff.getY(), userPuff.getWidth(), userPuff.getHeight());

			batcher.draw(AssetLoader.runningAnimation1.getKeyFrame(runTime), oppPuff.getX(), oppPuff.getY(), oppPuff.getWidth(), oppPuff.getHeight());
		}
		// End SpriteBatch

		batcher.end();

		shapeRenderer.begin(ShapeType.Filled);


		// the code takes care of DISPLAYING BOUNDING circle for debugging.
		// TO-DO: make the bounding circle transparent. 
		if (myWorld.isGameOver()){
			// Don't display the bounding circle.
		}
		else{
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.circle(userPuff.getBoundingCircle().x, userPuff.getBoundingCircle().y, userPuff.getBoundingCircle().radius);
			shapeRenderer1.begin(ShapeType.Line);
			shapeRenderer1.setColor(Color.BLUE);
			shapeRenderer1.circle(oppPuff.getBoundingCircle().x, oppPuff.getBoundingCircle().y, oppPuff.getBoundingCircle().radius);
		}

        batcher.begin();
        AssetLoader.font.draw(batcher,actionResolver.requestOppoCount()+"",oppPuff.getX(),oppPuff.getY()-50);

       //Try something here
       ArrayList<String> participants = actionResolver.getParticipants();
       String myId = actionResolver.getMyId();
       int player1 = participants.get(0).hashCode();
       int player2 = participants.get(1).hashCode();
       int me = myId.hashCode();
       if(player1 > player2){
           if(player1 == me){
               Gdx.app.log("me","is p1");
               AssetLoader.font.draw(batcher,"me",oppPuff.getX(),oppPuff.getY());}
           else{
               Gdx.app.log("me","is p2");
               AssetLoader.font.draw(batcher,"me",userPuff.getX(),userPuff.getY());}}

       else{
           if(player1 == me){
               Gdx.app.log("me","is p2");
               AssetLoader.font.draw(batcher,"me",oppPuff.getX(),oppPuff.getY());
           }
           else{
               Gdx.app.log("me","is p1");
               AssetLoader.font.draw(batcher,"me",userPuff.getX(),userPuff.getY());}


       }







        batcher.end();

		shapeRenderer.end();
		shapeRenderer1.end();
	}

    //method to convert real phone coordinates to scaled coordinates, used by input handler
    public static Vector3 unprojectCoords(Vector3 coords){
        cam.unproject(coords, viewport.x, viewport.y, viewport.width, viewport.height);
        return coords;
    }
}

