package com.mygdx.gamescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.gameobjects.Timer;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.InputHandler;


public class GameScreen implements Screen {
    // initialize the gameworld, gamerenderer variables.
    private GameWorld world;
    private GameRenderer renderer;
    // variable for updating renderer at runTime.
    private float runTime;
    private final int midPointX = Gdx.graphics.getWidth()/2;

    public GameScreen(ActionResolver actionResolver) {
        Timer attackTimer = new Timer(5);
        Timer taskTimer = new Timer(3);
        Timer freezeTimer = new Timer(5);
        Gdx.app.log("GameScreen", "Attached");
        world = new GameWorld(actionResolver,attackTimer,taskTimer,freezeTimer);
        InputHandler handler = new InputHandler(world,actionResolver);
        Gdx.input.setInputProcessor(handler);
        renderer = new GameRenderer(world,midPointX,actionResolver,handler,attackTimer,freezeTimer,taskTimer) ;


    }

    @Override
    // render called by the framework at delta fps.
    public void render(float delta) {
        runTime += delta;
        // update the gameWorld.
        try {
            world.update(delta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // render the gameGraphics.
        try {
            renderer.render(runTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize called");
    }

    @Override
    public void show() {


    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {


    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {


    }

}