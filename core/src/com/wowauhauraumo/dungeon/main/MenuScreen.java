package com.wowauhauraumo.dungeon.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Screen for the main menu. Uses scene2d.ui for the GUI.
 * 
 * @author TheGoomy42
 */
public class MenuScreen implements Screen {
	
	private Game game; // reference to main game class
	
	private Stage stage; // the stage to render the UI in
	private Table table; // a table for layout (it's OK, it's not HTML)
	private TextureAtlas atlas;
	private Skin skin;

	public MenuScreen(Game game) {
		this.game = game;
		create();
	}
	
	/**
	 * Make everything here so that it can work with show(), resume() etc.
	 */
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage); // lets the state detect user input
		
		atlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		skin.addRegions(atlas);
		
		table = new Table();
		// fills the parent widget as the stage doesn't do this automatically
		table.setFillParent(true);
		stage.addActor(table); // add the table as an actor in the stage
		
		Label title = new Label("GENERIC DUNGEON GAME", skin);
		title.setAlignment(Align.center);
		TextButton newGame = new TextButton("NEW GAME", skin);
		TextButton play = new TextButton("CONTINUE", skin);
		TextButton quit = new TextButton("QUIT", skin);
		
		newGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getPlayScreen());
			}
		});
		
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getPlayScreen());
			}
		});
		
		quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		table.add(title).fill().height(100);
		table.add().width(325);
		table.row();
		table.add(newGame).fill().height(100);
		table.row();
		table.add(play).fill().height(100);
		table.row();
		table.add(quit).fill().height(100);
		table.row();
		
		
		table.setDebug(true);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// update and draw stage
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}


	@Override
	public void show() {
		
	}
	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
	}

}
