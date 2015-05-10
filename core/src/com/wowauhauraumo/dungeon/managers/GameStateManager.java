package com.wowauhauraumo.dungeon.managers;

import java.util.Stack;

import com.wowauhauraumo.dungeon.main.Game;
import com.wowauhauraumo.dungeon.states.GameState;
import com.wowauhauraumo.dungeon.states.Play;

public class GameStateManager {
	
	private Game game;
	
	private Stack<GameState> gameStates;
	
	public static final int PLAY = 0;
	
	public GameStateManager(Game game) {
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(PLAY);
	}
	
	public void update(float delta) {
		gameStates.peek().update(delta);
	}
	
	public void render() {
		gameStates.peek().render();
	}
	
	public Game getGame() { return game; }
	
	private GameState getState(int id) {
		if(id == PLAY) return new Play(this); // return new play state
		return null;
	}
	
	public void setState(int id) {
		popState();
		pushState(id);
	}
	
	private void pushState(int id) {
		gameStates.push(getState(id));
	}
	
	private void popState() {
		GameState g = gameStates.pop();
		g.dispose();
	}
	
}
