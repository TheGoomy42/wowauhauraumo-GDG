package com.wowauhauraumo.dungeon.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.wowauhauraumo.dungeon.managers.Atlas;

/**
 * Creates a table with a label (e.g. 'FIGHT') and a cursor.
 * You must manually add the table to a stage.
 * 
 * @param T an enum type with a list of the possible options
 * @author TheGoomy42
 */
public class Option<T> {
	T name;
	Table table;
	CursorActor cursor;
	
	public Option(T name, Skin skin) {
		this.name = name;
		cursor = new CursorActor();
		create(skin);
	}

	private void create(Skin skin) {
		// create table with two columns for cursor and label
		table = new Table();
		table.add(cursor).width(32);
		table.add(new Label(name.toString(), skin));
	}
	
	public Table getTable() { return table; }
	public T getName() { return name; }
	public CursorActor getCursor() { return cursor; }
	
	
	class CursorActor extends Actor {
		TextureRegion sprite;
		boolean shown;
		
		public CursorActor() {
			sprite = Atlas.guiTextures.get("cursor");
			setSize(32, 32);
			shown = false;
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			if(shown) {
				Color color = getColor();
				batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
				batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(), getWidth(), 
						getHeight(), getScaleX(), getScaleY(), getRotation());
		
			}
		}
		
		public void show() {
			// this now represents the chosen option
			shown = true;
		}
		
		public void hide() {
			// this option is no longer selected
			shown = false;
		}
	}
}
