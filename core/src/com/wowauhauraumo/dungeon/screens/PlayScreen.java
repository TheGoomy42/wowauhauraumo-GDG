package com.wowauhauraumo.dungeon.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.wowauhauraumo.dungeon.Game;
import com.wowauhauraumo.dungeon.collisions.NpcEntity;
import com.wowauhauraumo.dungeon.collisions.PlayerEntity;
import com.wowauhauraumo.dungeon.content.PlayerSingleton;
import com.wowauhauraumo.dungeon.map.Area;
import com.wowauhauraumo.dungeon.map.Map;
import com.wowauhauraumo.dungeon.map.Map.PortalData;
import com.wowauhauraumo.dungeon.util.Direction;

import static com.esotericsoftware.minlog.Log.debug;

/**
 * Screen showing overworld exploration with the player, NPCs etc.
 * <p>
 * Is covered by battle, setting and option screens and can be re-shown when those screens are closed.
 * The user can see the map, the player, any NPCs and can also travel through the world using portals.
 *
 * @author Louis Van Steene
 */
public class PlayScreen implements Screen {
    private Game game;
    private boolean shouldDispose = false;

    private PlayerInputAdapter input;

    private SpriteBatch sb;
    private OrthographicCamera cam;

    private PlayerEntity player;

    private Array<? extends NpcEntity> npcList;

    // map stuff
    private Map map;
    private boolean shouldTravel; // whether the player should travel to a new map next tick
    private Vector2 newSpawn;     // coords of where the player should be sent
    //    private Portal fromPortal;    // the portal object that the player is travelling through
    private Area newMap;         // The identifier of the new area

    // encounter battles
    private int battleStep;            // the number of steps the player has taken in a hostile world
    private int maxBattleStep = 50;    // the next time the player will encounter an enemy, should be random

    /**
     * Creates screen.
     *
     * @param game reference to the game object
     */
    public PlayScreen(Game game) {
        this.game = game;
        sb = game.getSpriteBatch();
        cam = game.getCamera();

        map = new Map();
        map.setMap(Area.OVERWORLD);

        player = new PlayerEntity(PlayerSingleton.getInstance().getParty()[0].getSprite().getTexture(),
                                  this, map, 0, 0);

        input = new PlayerInputAdapter();

        npcList = new Array<>(false, 5);
    }

    /**
     * Called when the player opens up a new profile so this instance of PlayScreen will be reset.
     */
    public void shouldDispose() {
        shouldDispose = true;
    }

//    public void enterPortal(Portal p) {
//        debug("Player has entered a portal.");
//        if(p != null) {
//            if(p.isActive()) {
//                fromPortal = p;
//                shouldTravel = true;
//            } else {
//                debug("Portal is inactive.");
//            }
//        } else {
//            error("Portal is null! Cancelling player teleport...");
//        }
//    }
//
//    public void reactivatePortal(Portal p) {
//        info("Reactivating portal...");
//        p.setActive(true);
//    }

    public void enterPortal(Vector2 position) {
        PortalData data = map.getDataFromPortal(position);
        map.setMap(data.destination);
        player.setColumn((int) data.spawnPosition.x);
        player.setRow((int) data.spawnPosition.y);
    }

//    old update method, may need to copy some logic
//    public void update(float delta) {
//
//        // if the player wants to go through a portal
//        if(shouldTravel) {
//            // this is the id of the map the player wants to go to
//            newMap = fromPortal.getEntranceToMap();
//            int id = fromPortal.getEntranceToId();
//            info("Player teleporting to map " + newMap.toString() + " spawn " + id);
//            // creates the new map, discarding the old one
//            map.createMap(newMap, world);
//            // gets the location of the spawn
//            newSpawn = map.getPortal(id).getBody().getPosition();
//            // stop the player moving
//            player.getBody().setLinearVelocity(0, 0);
//            // move the player to the correct spawn in the new map
//            player.getBody().setTransform(newSpawn.x, newSpawn.y, 0);
//            info("Player successfully teleported to map " + newMap.toString());
//            // make the player slower in the overworld
//            if(newMap == Areas.OVERWORLD)
//                player.setMoveSpeed(moveSpeedOverworld);
//            else
//                player.setMoveSpeed(moveSpeedNormal);
//            // cancel all movement and input
////			GameKeys.resetKeys();
//            player.getBody().setLinearVelocity(0, 0);
//            player.setMoving(false);
//            // don't run this again
//            shouldTravel = false;
//            return;
//        }
//
//        // get user input
////		handleInput();
//
//        if(!map.getCurrentMap().friendly) {
//            if(player.isMoving()) {
//                battleStep++;
//                if(battleStep >= maxBattleStep) {
//                    // start encounter
//                    BattleScreen bs = game.getBattleScreen();
//                    game.setScreen(bs);
//                    battleStep = 0;
//                    maxBattleStep = 50; // in the future this will be somehow randomised.
//                    return;
//                }
//            }
//        }
//    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        input.update();

        // set camera to follow player
        int w = map.getColumns() * map.getTileSize();
        int h = map.getRows() * map.getTileSize();
        Vector2 playerPos = player.getPosition();
        Vector2 pos = new Vector2(Game.width / 2, Game.height / 2);

        if(playerPos.x - Game.width / 2 > 0) {
            if(playerPos.x + Game.width / 2 < w) {
                pos.x = playerPos.x;
            }
            else {
                pos.x = w - Game.width / 2;
            }
        }
        if(playerPos.y - Game.height / 2 > 0) {
            if(playerPos.y + Game.height / 2 < h) {
                pos.y = playerPos.y;
            }
            else {
                pos.y = h - Game.height / 2;
            }
        }

        // set the camera's position to this Vector2
        cam.position.set((int) pos.x, (int) pos.y, 0);
        cam.update();

        // draw tile map
        map.render(cam);

        // draw player last (on top)
        sb.setProjectionMatrix(cam.combined);
        player.render(sb, delta);
    }

    public Array<? extends NpcEntity> getEntities() {
        return npcList;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void hide() {
        debug("PlayScreen hidden");
    }

    @Override
    public void dispose() {
        if(shouldDispose) {
            shouldDispose = false;
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    private class PlayerInputAdapter extends InputAdapter {
        private int moveKey;

        @Override
        public boolean keyDown(int key) {
            if(key == Input.Keys.UP || key == Input.Keys.DOWN || key == Input.Keys.RIGHT || key == Input.Keys.LEFT) {
                moveKey = key;
            }

            return true;
        }

        @Override
        public boolean keyUp(int key) {
            if(key == moveKey) {
                moveKey = 0;
            }

            return true;
        }

        public void update() {
            if(moveKey == Input.Keys.UP) {
                player.move(Direction.NORTH);
            }
            else if(moveKey == Input.Keys.DOWN) {
                player.move(Direction.SOUTH);
            }
            else if(moveKey == Input.Keys.RIGHT) {
                player.move(Direction.EAST);
            }
            else if(moveKey == Input.Keys.LEFT) {
                player.move(Direction.WEST);
            }
        }
    }
}
