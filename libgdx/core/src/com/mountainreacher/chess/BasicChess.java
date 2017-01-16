package com.mountainreacher.chess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mountainreacher.chess.screens.GameScreen;

import java.util.LinkedList;

public class BasicChess extends Game {

	private static final String TAG = "Main";
	private static final int NUMBER_OF_SCREENS = 10;

	// Dimension
	public static final int V_WITH = 720;
	public static final int V_HEIGHT = 1280;

	public static final int GAME_CREEN = 0;

	private Screen screens[];
	private LinkedList<Integer> stack;

	public static BasicChess INSTANCE;

	public BasicChess() {
		this.screens = new Screen[NUMBER_OF_SCREENS];
		this.stack = new LinkedList<>();
		INSTANCE = this;
	}

	@Override
	public void create() {
//        batch = new SpriteBatch();
		changeScreen(GAME_CREEN, null);
	}

	public void changeScreen(int index, Object bundle) {
		stack.push(index);
		Screen screen = screens[index];
		if (screen == null) {
			switch (index) {

				case GAME_CREEN:
					screen = new GameScreen();
					break;
			}
		}
		screens[index] = screen;
		setScreen(screen);
	}

	public void lastScreen() {
		stack.poll();
		changeScreen(stack.poll(), null);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		Gdx.app.exit();
	}
}
