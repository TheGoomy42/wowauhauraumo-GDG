package com.wowauhauraumo.dungeon.main;

import static com.esotericsoftware.minlog.Log.debug;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wowauhauraumo.dungeon.entities.PartyMember;
import com.wowauhauraumo.dungeon.entities.Sprite;
import com.wowauhauraumo.dungeon.managers.Atlas;

/**
 * A screen for the FF1-style battles. Runs on top of the play screen. When the battle ends
 * all data is disposed of. 
 * 
 * @author TheGoomy42
 */
public class BattleScreen implements Screen {
	
	@SuppressWarnings("unused")
	private Game game; // will be used for switching back to the play screen
	
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
	
	public BattleScreen(Game game) {
		this.game = game;
	}
	
	private void create() {
		debug("Creating table");
		stage = new Stage(new FitViewport(Game.width, Game.height));
		Gdx.input.setInputProcessor(stage);
		
		atlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		skin.addRegions(atlas);
		
		screenTable = new Table();
		leftTable = new Table();
		HPTable = new Table();
		fightTable = new Table();
		enemyTable = new Table();
		spritesTable = new Table();
		enemySpritesTable = new Table();
		friendlySpritesTable = new Table();
		
		// add enemy sprites to their table
		enemySpritesTable.add();
		
		spritesTable.add(enemySpritesTable);
		spritesTable.add(friendlySpritesTable).padLeft(50);
		
		enemyTable.add(new Label("IMP", skin));
		fightTable.add(new Label("FIGHT", skin));
		
		screenTable.setFillParent(true);
		stage.addActor(screenTable);
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
		leftTable.add(spritesTable).colspan(2).top(); // sprites
		leftTable.row();
		leftTable.add(enemyTable); // IMP
		leftTable.add(fightTable); // FIGHT
		screenTable.add(leftTable).top();
		screenTable.add(HPTable).right(); // HP
		
		screenTable.setDebug(true, true);
	}
	
	private void setHPTable(PartyMember[] party) {
		for(int i=0;i<party.length;i++) {
			String string = party[i].getName() + "\nHP \n  " + party[i].getCurrentHP();
			HPTable.add(new Label(string, skin));
			HPTable.row();
		}
	}
	
	private void setPartyTable(PartyMember[] party) {
		// add friendly sprites to their table
		for(PartyMember pm : party) {
			try {
				friendlySpritesTable.add(new BattleActor(pm.getJob().spriteType));
			} catch (Exception e) {
				e.printStackTrace();
			}
			friendlySpritesTable.row();
		}
	}
	
	public void setParameters(PartyMember[] party) {
		setHPTable(party);
		setPartyTable(party);
	}

	@Override
	public void show() {
		create();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
		game = null;
	}
	
	private class BattleActor extends Actor {
		TextureRegion sprite;
		
		public BattleActor(Sprite sprite) throws Exception {
			this.sprite = Atlas.battleTextures.get(sprite.texture + '0');
			if(this.sprite == null) {
				throw new Exception("Could not find texture in Atlas: " + sprite.texture + '0');
			}
			setWidth(this.sprite.getRegionWidth()*1.5f);
			setHeight(this.sprite.getRegionHeight()*1.5f);
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), 
					getHeight(), getScaleX(), getScaleY(), getRotation());
		}
	}

}
