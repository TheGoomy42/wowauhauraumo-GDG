//package com.wowauhauraumo.dungeon.managers;
//
//import java.util.Stack;
//
//import com.wowauhauraumo.dungeon.main.Game;
//import com.wowauhauraumo.dungeon.states.GameState;
//import com.wowauhauraumo.dungeon.states.PlayScreen;
//
///**
// * Controls what 'GameState' is running, and calls its update and render methods.
// * In the future will contain a main menu state, and possibly others.
// * (This system is probably not essential, and will probably be replaced.)
// * 
// * 
// * 
// * @author TheGoomy42
// */
//public class GameStateManager {
//	
//	// a reference to the Game class to pass through to the states
//	private Game game;
//	
//	// GSM uses a stack to control which state is currently running
//	private Stack<GameState> gameStates;
//	
//	/** Enum for the id's of the GameStates */
//	public enum StateId {
//		PLAY
//	}
//	
//	/**
//	 * Constructor initialises the stack and the first state.
//	 * @param game A reference to the game class
//	 */
//	public GameStateManager(Game game) {
//		this.game = game;
//		gameStates = new Stack<GameState>();
//		pushState(StateId.PLAY);
//	}
//	
//	/**
//	 * Update method. Game logic such as player movement is handled here.
//	 * @param delta
//	 */
//	public void update(float delta) {
//		gameStates.peek().update(delta);
//	}
//	
//	/**
//	 * Render method. Moving cameras and rendering graphics is handled here.
//	 */
//	public void render() {
//		gameStates.peek().render();
//	}
//	
//	public Game getGame() { return game; }
//	
//	/**
//	 * Public method to change the state.
//	 * @param id StateId of the state to switch to.
//	 */
//	public void setState(StateId id) {
//		popState();
//		pushState(id);
//	}
//	
//	private GameState getState(StateId id) {
//		if(id == StateId.PLAY) return new PlayScreen(this); // return new play state
//		return null;
//	}
//	
//	private void pushState(StateId id) {
//		gameStates.push(getState(id));
//	}
//	
//	private void popState() {
//		GameState g = gameStates.pop();
//		g.dispose();
//	}
//	
//}
