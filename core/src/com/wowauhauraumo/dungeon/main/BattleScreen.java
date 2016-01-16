package com.wowauhauraumo.dungeon.main;

import static com.esotericsoftware.minlog.Log.debug;
import static com.esotericsoftware.minlog.Log.error;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wowauhauraumo.dungeon.entities.PartyMember;
import com.wowauhauraumo.dungeon.entities.Sprite;
import com.wowauhauraumo.dungeon.managers.Atlas;
import com.wowauhauraumo.dungeon.managers.PlayerSingleton;

/**
 * A screen for the FF1-style battles. Runs on top of the play screen. When the battle ends
 * all data is disposed of. 
 * 
 * @author TheGoomy42
 */
public class BattleScreen implements Screen {
	
	private Game game; // will be used for switching back to the play screen
	
	// used in stat checks when attacking
	private PartyMember[] party; // reference to PlayerSingleton.getInstance().getParty()
	private Random battleRandom;
	
	// ui stuff
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table screenTable;
	private Table leftTable;
	private Table HPTable;
	private Table fightTable;
	private Table enemyTable;
	private Table spritesTable;
	private Table enemySpritesTable;
	private Table friendlySpritesTable;
	
	private Label status;
	
	private Array<Option<Choice>> options = new Array<>(5);
	private int currentOption = 0;
	private int currentLeft = 0; // use to return to the same option on the left
	
	public BattleScreen(Game game) {
		this.game = game;
		battleRandom = game.getBattleRandom();
	}
	
	private void create() {
		debug("Creating table");
		stage = new Stage(new ScreenViewport(game.getHUDCamera()));
		
		party = PlayerSingleton.getInstance().getParty();
		
		atlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("ui/battleui.json"));
		skin.addRegions(atlas);
		
		screenTable = new Table();
		leftTable = new Table();
		HPTable = new Table();
		fightTable = new Table();
		enemyTable = new Table();
		spritesTable = new Table();
		enemySpritesTable = new Table();
		friendlySpritesTable = new Table();
		
		// TODO add enemy sprites to their table
		
		setHPTable(party);
		setPartyTable(party);
		
		spritesTable.add(enemySpritesTable).left();
		spritesTable.add(friendlySpritesTable).right();
		
		enemyTable.add(new Label("IMP", skin)).left();
		enemyTable.row();
		enemyTable.add(new Label("G.IMP", skin)).left();
		
		options.add(new Option<Choice>(Choice.FIGHT, skin));
		options.add(new Option<Choice>(Choice.MAGIC, skin));
		options.add(new Option<Choice>(Choice.DRINK, skin));
		options.add(new Option<Choice>(Choice.ITEM, skin));
		options.add(new Option<Choice>(Choice.RUN, skin));
		
		for(int i=0;i<options.size-1;i++) {
			// add cursor column
			fightTable.add(options.get(i).getTable()).left();
			if(i == 0) fightTable.add(options.get(4).getTable()).padLeft(5).left(); // the run option is on the first row too
			fightTable.row();
		}
		
		options.get(0).getCursor().show();
		/*
		 *  __________________
		 * |             |    |
		 * |  SPRITES    | HP |
		 * |             |    |
		 * |-------------|    |
		 * | IMP | FIGHT |    |
		 * |     | MAGIC |    |
		 * |_____|_______|____|
		 */
		leftTable.add(spritesTable).colspan(2).right();
		leftTable.row();
		leftTable.add(enemyTable).top();
		leftTable.add(fightTable).padLeft(20);
		screenTable.add(leftTable).top();
		screenTable.add(HPTable).right();
		
		status = new Label("this is a very long message that should wrap over two lines", skin);
		status.setWrap(true);
		
		screenTable.row();
		screenTable.add(status).left().width(470);
		
		screenTable.top();
		
		screenTable.setFillParent(true);
		stage.addActor(screenTable);
		
		//screenTable.setDebug(true, true);
	}
	
	/**
	 * (Should be) run after all characters have selected the option.
	 * This method performs a selected action, using the stats from the provided
	 * character (in the format party[character]).
	 * 
	 * @param choice the selected choice to perform
	 * @param character the party member who performed the action
	 */
	private void action(Choice choice, int character) {
		// do something i.e. fight, drink etc.
		debug("action: " + choice);
		switch(choice) {
		case FIGHT: // queue a fight table
			status("You attack the air!");
			break;
		case MAGIC: // replace fight table with magic select table
			status("You have no magic!");
			break;
		case DRINK: // replace fight table with drink table
			status("You're not thirsty.");
			break;
		case ITEM: // replace fight table with item weapons table
			status("You have no items.");
			break;
		case RUN: // queue a run action
			run(character);
			break;
		default:
			error("invalid action");
			break;
		}
	}
	
	private void run(int character) {
		// generate random number based on enemy agility
		// use some fancy algorithm to escape based entirely on character agility
		status("Successfully escaped!");
		// close BattleScreen and return to PlayScreen
		game.setScreen(game.getPlayScreen());
	}
	
	
	/**
	 * Change the displayed message.
	 * Needs to take into account the time since the last message, and wait until it has expired.
	 * 
	 * @param msg
	 */
	private void status(String msg) {
		status.setText(msg);
	}
	
	private void setHPTable(PartyMember[] party) {
		for(int i=0;i<party.length;i++) {
			HPTable.add(new Label(party[i].getName(), skin)).pad(5).padLeft(20);
			HPTable.row();
			
			// create table to lay out 'HP' and # nicely
			Table subTable = new Table();
			subTable.add(new Label("HP", skin)).align(Align.left);
			subTable.row();
			subTable.add(new Label("" + party[i].getCurrentHP(), skin)).padLeft(60);
			
			HPTable.add(subTable).align(Align.left).padLeft(20);
			HPTable.row();
		}
	}
	
	private void setPartyTable(PartyMember[] party) {
		// add friendly sprites to their table
		for(PartyMember pm : party) {
			try {
				friendlySpritesTable.add(new BattleActor(pm.getJob().spriteType)).pad(5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			friendlySpritesTable.row();
		}
	}
	
	private void changeOption(int prev, int current) {
		options.get(prev).getCursor().hide();
		options.get(current).getCursor().show();
	}

	@Override
	public void show() {
		create();
		
		Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() + 15, false);
		
		// set up input
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int key) {
				int prevOption = currentOption;
				if(key == Input.Keys.DOWN) {
					if(currentOption == 4) return false;
					if(currentOption < 3) {
						currentOption++;
					} else {
						currentOption = 0;
					}
					changeOption(prevOption, currentOption);
				} else if(key == Input.Keys.UP) {
					if(currentOption == 4) return false;
					if(currentOption > 0) {
						currentOption--;
					} else {
						currentOption = 3;
					}
					changeOption(prevOption, currentOption);
				} else if(key == Input.Keys.RIGHT) {
					currentOption = 4;
					currentLeft = prevOption;
					changeOption(prevOption, currentOption);
				} else if(key == Input.Keys.LEFT) { // move back to the same option you moved right on
					currentOption = currentLeft;
					changeOption(prevOption, currentOption);
				} else if(key == Input.Keys.SPACE) {
					action(options.get(currentOption).getName(), 0);
					currentOption = 0;
					changeOption(prevOption, currentOption);
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {  } // will be used when escape is pressed

	@Override
	public void resume() {  } // will be used when escape is pressed

	@Override
	public void hide() {
		// set window size back to normal
		Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth() + 70, Gdx.graphics.getHeight() - 15, false);
	}

	@Override
	public void dispose() {
		// make this instance good as new
		stage.dispose();
		party = null;
		battleRandom = null;
		status = null;
		options = new Array<Option<Choice>>(5);
		currentOption = 0;
		currentLeft = 0;
	}
	
	private class BattleActor extends Actor {
		TextureRegion sprite;
		
		public BattleActor(Sprite sprite) throws Exception {
			this.sprite = Atlas.battleTextures.get(sprite.texture + '0');
			if(this.sprite == null) {
				throw new Exception("Could not find texture in Atlas: " + sprite.texture + '0');
			}
			setWidth(this.sprite.getRegionWidth()*3f);
			setHeight(this.sprite.getRegionHeight()*3f);
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), 
					getHeight(), getScaleX(), getScaleY(), getRotation());
		}
	}
	

	
	private enum Choice {
		FIGHT, RUN, MAGIC, DRINK, ITEM
	}

}
