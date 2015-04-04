package com.mygdx.gamescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.gameobjects.Puff;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.InputHandler;


public class GameScreen implements Screen {
    // initialize the gameworld, gamerenderer variables.
    private GameWorld world;
    private GameRenderer renderer;
    private Puff leftPuff;
    private Puff rightPuff;
    // variable for updating renderer at runTime.
    private float runTime;

    public GameScreen(ActionResolver actionResolver) {
        Gdx.app.log("GameScreen", "Attached");
        // get the screenWidth and screenHeight using framework graphics.
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // determined gameWidth.
        float gameWidth = 136;
        float gameHeight = screenHeight / (screenWidth / gameWidth); // not used? 

        // midPoint of game screen.
        int midPointX = (int) (gameWidth / 2);
        world = new GameWorld(midPointX, actionResolver, gameHeight);
        InputHandler handler = new InputHandler(world,actionResolver);
        Gdx.input.setInputProcessor(handler);
        renderer = new GameRenderer(world, (int) gameWidth,(int)gameHeight, midPointX,actionResolver,handler) ;
//        try{
//            ArrayList<String> participants = actionResolver.getParticipants();
//            String myId = actionResolver.getMyId();
//            int player1 = participants.get(0).hashCode();
//            int player2 = participants.get(1).hashCode();
//            int me = myId.hashCode();
//            if(player1 > player2){
//                if(player1 == me){
//                    leftPuff = new Puff(20, 120, 13, 24, actionResolver,"runtoright","me");
//                    rightPuff = new Puff(105, 120, 13, 24, actionResolver,"runtoleft","notme");}
//                else{
//                    leftPuff = new Puff(20, 120, 13, 24, actionResolver,"runtoright","notme");
//                    rightPuff = new Puff(105, 120, 13, 24, actionResolver,"runtoleft","me");
//                }
//
//            }
//            else{
//                if(player1 == me){
//                    leftPuff = new Puff(20, 120, 13, 24, actionResolver,"runtoright","notme");
//                    rightPuff = new Puff(105, 120, 13, 24, actionResolver,"runtoleft","me");}
//                else{
//                    leftPuff = new Puff(20, 120, 13, 24, actionResolver,"runtoright","me");
//                    rightPuff = new Puff(105, 120, 13, 24, actionResolver,"runtoleft","notme");
//                }
//            }
//        }
//        finally {
//
//        }
    }

    @Override
    // render called by the framework at delta fps.
    public void render(float delta) {
        runTime += delta;
        // update the gameWorld.
        world.update(delta);
        // render the gameGraphics.
        renderer.render(runTime);
//        leftPuff.update(delta);
//        rightPuff.update(delta);


    }


    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize called");
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");     
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");        
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");       
    }

    @Override
    public void dispose() {
        // Leave blank
    }

}