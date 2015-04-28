package com.game.sumopuff.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;
import com.mygdx.helpers.ActionResolver;
import com.mygdx.helpers.AssetLoader;
import com.mygdx.sumogame.SPGame;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** This class implements Google Play Services(GPS) functions by implementing the corresponding interfaces.
 *  The functions implemented including connecting to GPS,Creating and joining the virtual game room, sending
 *  and receiving messages. ActionResolver interface is implemented here for the usage in the Core folder
 *  for message passing and receiving in the game. The messages are sent and received in bytes by calling
 *  the respective APIs.
 *  The room status and listeners are handled by overriding the methods in the relative interfaces.
 */


public class AndroidLauncher extends AndroidApplication implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, RealTimeMessageReceivedListener,
        RoomStatusUpdateListener, RoomUpdateListener,ActionResolver {



    final static String TAG = "SumoPuff";

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Client used to interact with Google APIs.
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;
;

    // Create game view
    private ApplicationListener game;
    private View gameView;
    private LinearLayout linearLayout;


    //Variables for message passing
    private int oppoCount = 0;
    private ArrayList<String> participants = new ArrayList<String>();
    private  byte[] bytes = new byte[5];
    private int gameState = 0;
    private float leftPuffx=0;
    private float rightPuffx=0;
    private int powerUpType=0;
    private int move=0;
    private int timeLeft=0;


    /** This method is called when the activity is first created. Google Api client, gameView and main
     *  game object are initialized and created under this method.
     *
     *  @param savedInstanceState            The saved state of the application in a bundle, null if no data was supplied
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Create the Google Api Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        // set up a click game for everything we care about
        for (int id : CLICKABLES) {
            findViewById(id).setOnClickListener(this);
        }
        game = new SPGame(this);
        gameView = initializeForView(game,new AndroidApplicationConfiguration());
        linearLayout = (LinearLayout) findViewById(R.id.screen_game);
        linearLayout.addView(gameView,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
        }

    /** This method handles all the Views in the layout. Based on the input(button clicked) to display the
     * corresponding view in the layout
     *
     * @param v             It is and View object that is visible in the current state
     */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.button_sign_in:
                    // check if there is any setup problem
                    if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
                        Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                    }
                    mSignInClicked = true;
                    mGoogleApiClient.connect();
                    break;

                case R.id.button_sign_out:
                    Log.d(TAG, "Sign-out button clicked");
                    mSignInClicked = false;
                    Games.signOut(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    switchToScreen(R.id.screen_sign_in);
                    break;

                case R.id.button_quick_game:
                startQuickGame();
                break;
        }
    }

    /** This method quick-start a game with 1 randomly selected opponent. The virtual game room is
     *  initiated when the first player joins the room. The maximum and minimum number of playser are
     *  defined when calling the Api autoMatchCriteria.
     */
    void startQuickGame() {
        final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
                MAX_OPPONENTS, 0);
        RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
        rtmConfigBuilder.setMessageReceivedListener(this);
        rtmConfigBuilder.setRoomStatusUpdateListener(this);
        rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
        switchToScreen(R.id.screen_wait);
        keepScreenOn();
        resetGame();
        Games.RealTimeMultiplayer.create(mGoogleApiClient, rtmConfigBuilder.build());
    }


    /**This method handles the result of each requests
     *
     * @param requestCode               An integer that represents the different different requests
     * @param responseCode              An integer that represents the response status on the request
     * @param intent                    An Intent that carries the result data
     */
    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        switch (requestCode) {
            case RC_WAITING_ROOM:
                if (responseCode == Activity.RESULT_OK) {
                    // ready to start playing
                    Log.d(TAG, "Starting game (waiting room returned OK).");
                    startGame();
                } else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    // player indicated that they want to leave the room
                    leaveRoom();
                } else if (responseCode == Activity.RESULT_CANCELED) {

                    leaveRoom();
                }
                break;
            case RC_SIGN_IN:
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + responseCode + ", intent=" + intent);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (responseCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this,requestCode,responseCode, R.string.signin_other_error);
                }
                break;
        }
        super.onActivityResult(requestCode, responseCode, intent);
    }


    /** The is method is called when the activity is stopped.*/
    @Override
    public void onStop() {
        Log.d(TAG, "**** got onStop");
        leaveRoom();
        stopKeepingScreenOn();

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){
            switchToScreen(R.id.screen_sign_in);
        }
        else {
            switchToScreen(R.id.screen_wait);
        }
        super.onStop();
    }


    /** This method is called when the activity is started.*/
     @Override
    public void onStart() {
        switchToScreen(R.id.screen_wait);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.w(TAG,
                    "GameHelper: client was already connected on onStart()");
        } else {
            Log.d(TAG,"Connecting client.");
            mGoogleApiClient.connect();
        }
        super.onStart();
    }


    // Handle back key to make sure we cleanly leave a game if we are in the middle of one

    /** This method handles the back key, to make sure the game will be stop and leave the game room properly
     *
     * @param keyCode                 An integer that represents the code key pressed
     * @param e                       An KeyEvent of the key pressed
     * @return                        A boolean that indicates whether the back key is pressed
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCurScreen == R.id.screen_game) {
            leaveRoom();
            return true;
        }
        else{
            System.exit(0);
        }
        return super.onKeyDown(keyCode, e);
    }

    /** This method defines the way to leave the virtual game room. The game variables are reset. And
     *  the screen is switch back from the game screen to main screen.
     */
    void leaveRoom() {
        Log.d(TAG, "Leaving room.");
        resetGame();
        stopKeepingScreenOn();
        if (mRoomId != null) {
            Games.RealTimeMultiplayer.leave(mGoogleApiClient, this, mRoomId);
            mRoomId = null;
            switchToScreen(R.id.screen_wait);
        } else {
            switchToMainScreen();
        }
    }

    /** This method handles the UI for the waiting room.
     *
     * @param room              The virtual game room that is initiated by the player who first join the room
     */
    void showWaitingRoom(Room room) {

        final int MIN_PLAYERS = Integer.MAX_VALUE;
        Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, MIN_PLAYERS);

        // show waiting room UI
        startActivityForResult(i, RC_WAITING_ROOM);
    }


    /** The following methods are the override methods of the interfaces to handle the activities
     * under different connection status.
     */

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");
        Log.d(TAG, "Sign-in succeeded.");
        switchToMainScreen();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
                    connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
        switchToScreen(R.id.screen_sign_in);
    }


    @Override
    public void onConnectedToRoom(Room room) {
        Log.d(TAG, "onConnectedToRoom.");

        // get room ID, participants and my ID:
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(mGoogleApiClient));

        // print out the list of participants (for debug purposes)
        Log.d(TAG, "Room ID: " + mRoomId);
        Log.d(TAG, "My ID " + mMyId);
        Log.d(TAG, "<< CONNECTED TO ROOM>>");
    }

    //Called when the participant leaves the room
    @Override
    public void onLeftRoom(int statusCode, String roomId) {
        Log.d(TAG, "onLeftRoom, code " + statusCode);
        switchToMainScreen();
        AssetLoader.BackgroundMusic.stop();
        resetGame();
    }


    // Called when getting disconnected from the room
    @Override
    public void onDisconnectedFromRoom(Room room) {
        mRoomId = null;
        showGameError();
    }

    // Show error message about game being cancelled and return to main screen.
    void showGameError() {
        BaseGameUtils.makeSimpleDialog(this, getString(R.string.game_problem));
        switchToMainScreen();
    }

    // Called when room has been created
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            showGameError();
            return;
        }
        // show the waiting room UI
        showWaitingRoom(room);
    }


    // Called when room is fully connected.
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }
        updateRoom(room);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesStatusCodes.STATUS_OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            showGameError();
            return;
        }

        // show the waiting room UI
        showWaitingRoom(room);
    }


    @Override
    public void onPeerDeclined(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> arg1) {
        updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(String participant) {
    }

    @Override
    public void onP2PConnected(String participant) {
    }

    @Override
    public void onPeerJoined(Room room, List<String> arg1) {
        updateRoom(room);
    }


    //Called when the other particaipants leaves the room
    @Override
    public void onPeerLeft(Room room, List<String> peersWhoLeft) {
        AssetLoader.BackgroundMusic.stop();
        resetGame();
        updateRoom(room);

    }

    @Override
    public void onRoomAutoMatching(Room room) {
        updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, List<String> peers) {
        updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> peers) {
        updateRoom(room);
        resetGame();
    }

    /** Update the game with the participants in the game room */
    void updateRoom(Room room) {
        if (room != null) {
            mParticipants = room.getParticipants();
        }

    }


    // Start the game play phase of the game.
    void startGame() {
        switchToScreen(R.id.screen_game);
    }



    /** This method is called when receiving a real-time message from the network. The message is differentiate
     * by the first byte of the message to indicate the type of message that received(eg. buf[0]=='c' indicates that
     * the message received is the tapping count).
     *
     * @param rtm
     */
    @Override
    public void onRealTimeMessageReceived(RealTimeMessage rtm) {
        byte[] buf = rtm.getMessageData();
        //opponent's tapping count
        if (buf[0] == 'c'){
            oppoCount = (buf[4] & 0xFF)| ((buf[3] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[1] & 0xFF) << 24);
        }
        //game state
        if (buf[0] == 's'){
            gameState = (buf[4] & 0xFF)| ((buf[3] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[1] & 0xFF) << 24);
        }
        //left puff coordinates
        if (buf[0] == 'l'){
            byte[] bytes_cp = Arrays.copyOfRange(buf,1,5);
            leftPuffx = ByteBuffer.wrap(bytes_cp).getFloat();
        }
        //right puff coordinates
        if (buf[0] == 'r'){
            byte[] bytes_cp = Arrays.copyOfRange(buf,1,5);
            rightPuffx = ByteBuffer.wrap(bytes_cp).getFloat();
        }
        //power up attack
        if (buf[0] == 'p'){
            powerUpType = (buf[4] & 0xFF)| ((buf[3] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[1] & 0xFF) << 24);
        }
        //update characters' movement
        if (buf[0] == 'm'){
            move = (buf[4] & 0xFF)| ((buf[3] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[1] & 0xFF) << 24);
        }
        //time left of the power up attack
        if (buf[0] == 't'){
            timeLeft = (buf[4] & 0xFF)| ((buf[3] & 0xFF) << 8)| ((buf[2] & 0xFF) << 16)| ((buf[1] & 0xFF) << 24);
        }

    }


    /** This method broadcast the tapping count to opponent.
     *
     * @param count             An integer that represents the tapping count of the player
     */
    @Override
    public void BroadCastCount(int count) {
        bytes[0] = 'c';
        bytes[1] = (byte) ((count >> 24) & 0xFF);
        bytes[2] = (byte) ((count >> 16) & 0xFF);
        bytes[3] = (byte) ((count >> 8) & 0xFF);
        bytes[4] = (byte) (count & 0xFF);
        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;

                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId, p.getParticipantId());
            }
        }

    }

    /** This method broadcast the current game state to opponent.
     *
     * @param state             An integer that represents the game state
     */
    @Override
    public void BroadCastMyGameState(int state) {
        bytes[0] = 's';
        bytes[1] = (byte) ((state >> 24) & 0xFF);
        bytes[2] = (byte) ((state >> 16) & 0xFF);
        bytes[3] = (byte) ((state >> 8) & 0xFF);
        bytes[4] = (byte) (state & 0xFF);
        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                //Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient,bytes,mRoomId);
                Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null, bytes, mRoomId, p.getParticipantId());
            }
        }
    }

    /** This method broadcast the X coordinate of the left puff to opponent.(Only applicable for player1)
     *
     * @param x1                A float that represents the X coordinate of the left puff
     */
    @Override
    public void broadCastLeftPuffX(float x1) {
        int leftx = Float.floatToIntBits(x1);
        bytes[0] = 'l';
        bytes[1] = (byte) ((leftx >> 24) & 0xFF);
        bytes[2] = (byte) ((leftx >> 16) & 0xFF);
        bytes[3] = (byte) ((leftx >> 8) & 0xFF);
        bytes[4] = (byte) (leftx & 0xFF);

        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                // Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null,bytes, mRoomId, p.getParticipantId());
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId, p.getParticipantId());
            }
        }

    }

    /** This method broadcast the X coordinate of the right puff to opponent.(Only applicable for player1)
     *
     * @param x2                    A float that represents the X coordinate of the right puff
     */
    @Override
    public void broadCastRightPuffX(float x2) {
        int rightx = Float.floatToIntBits(x2);
        bytes[0] = 'r';
        bytes[1] = (byte) ((rightx >> 24) & 0xFF);
        bytes[2] = (byte) ((rightx >> 16) & 0xFF);
        bytes[3] = (byte) ((rightx >> 8) & 0xFF);
        bytes[4] = (byte) (rightx & 0xFF);

        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                // Games.RealTimeMultiplayer.sendReliableMessage(mGoogleApiClient, null,bytes, mRoomId, p.getParticipantId());

                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId, p.getParticipantId());
            }
        }
    }


    /** This method broadcast the activated power up attack to opponent.
     *
     * @param type                  An integer that represents the type of the power up attack the being activated
     */
    @Override
    public void sendPowerUpAttack(int type) {
        bytes[0] = 'p';
        bytes[1] = (byte) ((type >> 24) & 0xFF);
        bytes[2] = (byte) ((type >> 16) & 0xFF);
        bytes[3] = (byte) ((type >> 8) & 0xFF);
        bytes[4] = (byte) (type & 0xFF);

        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                //Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient,bytes,mRoomId);
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId, p.getParticipantId());
            }
        }
    }


    /** This method broadcast the current moving state to opponent. 0 = no updates, 1 = Otherwise
     *
     * @param move                     An integer to indicate whether the opponent should update the positions of the game characters.
     */
    @Override
    public void sendMove(int move) {
        bytes[0] = 'm';
        bytes[1] = (byte) ((move >> 24) & 0xFF);
        bytes[2] = (byte) ((move >> 16) & 0xFF);
        bytes[3] = (byte) ((move >> 8) & 0xFF);
        bytes[4] = (byte) (move & 0xFF);

        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                //Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient,bytes,mRoomId);
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId, p.getParticipantId());
            }
        }
    }


    /** This method broadcast the time left of the power up attack to opponent.
     *
     * @param sec                   An integer that represents the number of seconds left for power up attack
     */
    @Override
    public void broadCastTimeLeft(int sec) {
        bytes[0] = 't';
        bytes[1] = (byte) ((sec >> 24) & 0xFF);
        bytes[2] = (byte) ((sec >> 16) & 0xFF);
        bytes[3] = (byte) ((sec >> 8) & 0xFF);
        bytes[4] = (byte) (sec & 0xFF);

        if(mRoomId!=null) {
            for (Participant p : mParticipants) {
                if (p.getParticipantId().equals(mMyId))
                    continue;
                if (p.getStatus() != Participant.STATUS_JOINED)
                    continue;
                //Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient,bytes,mRoomId);
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, bytes, mRoomId, p.getParticipantId());
            }
        }
    }

    /** This method gets the participants in the game room and passes the ArrayList into the core game for assigning players.
     *
     * @return                      An ArrayList of the participants in the room
     */
    @Override
    public ArrayList<String> getParticipants() {
        for(Participant p:mParticipants){
            participants.add(p.getParticipantId());
        }
        return participants;

    }

    /** This method returns the player's ID to the core game.
     *
     * @return                  A String that represents the player's ID
     */
    @Override
    public String getMyId() {
        return mMyId;
    }


    /** The following override methods are called when requesting the information from the opponent player.*/
    @Override
    public int requestOppoCount(){
        return oppoCount;
    }
    @Override
    public int requestOppGameState() {
        return gameState;
    }
    @Override
    public float requestLeftPuffX() {
        return leftPuffx;
    }
    @Override
    public float requestRightPuffX() {
        return rightPuffx;
    }
    @Override
    public int checkPowerUpAttack() {
        return powerUpType;
    }
    @Override
    public int checkMove() {return move;}
    @Override
    public int getTimeLeft() { return timeLeft ;}






    // This array lists everything that's clickable(buttons available in the game)
    final static int[] CLICKABLES = {
            R.id.button_quick_game, R.id.button_sign_in, R.id.button_sign_out,
    };

    // This array lists all the individual screens that the game has.
    final static int[] SCREENS = {
            R.id.screen_game, R.id.screen_main, R.id.screen_sign_in,
            R.id.screen_wait
    };
    int mCurScreen = -1;


    /** This method makes the requested screen visible; hide all others.*/
    void switchToScreen(int screenId) {
        for (int id : SCREENS) {
            findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
            if(screenId == R.id.screen_game){
            gameView.setVisibility(screenId == R.id.screen_game?View.VISIBLE : View.GONE);
            }
        }

        mCurScreen = screenId;

    }


    /**This method switches the screen back to main screen*/
    void switchToMainScreen() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            switchToScreen(R.id.screen_main);
        }
        else {
            switchToScreen(R.id.screen_sign_in);
        }
    }




    // Sets the flag to keep this screen on. It's recommended to do that during
    void keepScreenOn() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    // Clears the flag that keeps the screen on.
    void stopKeepingScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    /** This method resets all the game variables. */
    public void resetGame(){
        this.oppoCount = 0;
        this.participants = new ArrayList<>();
        this.bytes = new byte[5];
        this.leftPuffx=0;
        this.rightPuffx=0;
        this.gameState = 0;
        this.powerUpType=0;
        this.move=0;
        linearLayout.removeViewAt(0);
        game =new SPGame(this);
        gameView = initializeForView(game,new AndroidApplicationConfiguration());
        linearLayout.addView(gameView,0,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

    }


}