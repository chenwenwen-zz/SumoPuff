package com.mygdx.gamescreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.gameobjects.Timer;
import com.mygdx.gameworld.GameRenderer;
import com.mygdx.gameworld.GameWorld;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.InputHandler;

/**This class implements the Screen interface which is an in built interface in Libgdx framework.
 *It consists 7 abstract methods. Only render() is override. render() is called delta times per second.
 *renderer object and world object update() methods are called under this method to update the game
 * state and game graphics constantly.
 */
public class GameScreen implements Screen {
    // initialize the gameworld, gamerenderer variables.
    private GameWorld world;
    private GameRenderer renderer;
    // variable for updating renderer at runTime.
    private float runTime;


    /** GameWorld,GameRenderer,InputHandler and Timer objects for different purpose are created under
     * the constructor.ActionResolver that is used to broadcast message is passed into both GameWorld
     * and GameRenderer objects.
     *
     * @param actionResolver                An interface that is used to broadcast and receive message
     */
    public GameScreen(ActionResolver actionResolver) {
        //Timer object for power up attack duration
        Timer attackTimer = new Timer(5);
        //Timer object for power up mini game duration
        Timer taskTimer = new Timer(3);
        //Timer object for power up frozen duration
        Timer freezeTimer = new Timer(5);
        Gdx.app.log("GameScreen", "Attached");
        world = new GameWorld(actionResolver,attackTimer,taskTimer,freezeTimer);
        InputHandler handler = new InputHandler(world,actionResolver);
        Gdx.input.setInputProcessor(handler);
        renderer = new GameRenderer(world,actionResolver,handler,attackTimer,freezeTimer,taskTimer) ;

    }

    /**This method calls the update() methods in both GameWorld and GameRender to update the game state
     * and the game graphics in delta time.
     *
     * @param delta                 A float the represents how frequent the method is called(1/60s)
     */
    @Override
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
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

}