package com.mountainreacher.chess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.mountainreacher.chess.Resources;
import com.mountainreacher.chess.components.generic.ImageButton;
import com.mountainreacher.chess.game.Board;
import com.mountainreacher.chess.components.SoundsManager;
import com.mountainreacher.chess.components.generic.ActionListener;
import com.mountainreacher.chess.components.generic.ColorTextButton;
import com.mountainreacher.chess.components.ConfirmationMessage;
import com.mountainreacher.chess.model.MatchOneToOne;
import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class GameScreen implements Screen, ActionListener {

    private static final String TAG = "ChessFaces";
    private static final String BOARD = "BOARD";
    private static final String RESTART = "RESTART";
    private static final String CONFIRM = "CONFIRM";
    private static final float BG_COLOR = 0.875f;

    // Graphics
    private Stage stage;

    // Model
    private MatchOneToOne model;

    // Interface
    private SoundsManager sounds;
    private Board board;
    private ConfirmationMessage confirmation;

    public GameScreen() {
        sounds = new SoundsManager(SoundsManager.GAME_SOUNDS);
        model = new MatchOneToOne();
    }

    private void visualElementsSetup(int width, int height) {

        // Sizes
        int min = Math.min(width, height);
        int startX = (width - min) / 2;
        int startY = (height - min) / 2;
        int margin = width / 30;
        int heightSpace = startY - margin * 2;

        // Board
        board = new Board(min, model, sounds, this);
        board.setPosition(startX, startY);
        board.setName(BOARD);
        stage.addActor(board);

        // Restart button
        ImageButton restart = new ImageButton(1f, 0.5f, 0.5f, 1f, Resources.INSTANCE.getTexture("pieces/black_king.png"),
                width - margin * 2, Math.min(heightSpace, width / 3));
        restart.setPosition(margin, margin);
        restart.setName(RESTART);
        restart.setActionListener(this);
        stage.addActor(restart);

        // Confirmation message
        confirmation = new ConfirmationMessage(width - margin * 2, height / 2, this);
        confirmation.setPosition(margin, height / 3);
        confirmation.setName(CONFIRM);
        stage.addActor(confirmation);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(BG_COLOR, BG_COLOR, BG_COLOR, 1);
        if (stage != null) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Recycle
        if (stage != null) {
            stage.dispose();
        }

        // Graphics
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        visualElementsSetup(width, height);
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

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
    }

    @Override
    public void onActionPerformed(Actor source, Object action) {
        switch (source.getName()) {

            case RESTART:
                confirmation.showMessage("Restart the game", "Yes", "No");
                break;

            case BOARD:
                switch ((Integer) action) {

                    case MatchOneToOne.CHECK_MATE:
                        confirmation.showMessage("Checkmate", "Restart", "Dismiss");
                        break;

                    case MatchOneToOne.CONQUEST:
                        confirmation.showConquest("Select one to replace the pawn");
                        break;
                }
                break;

            case CONFIRM:
                switch ((int) action) {
                    case 1:
                        model.reset();
                        break;

                    case Piece.QUEEN:
                    case Piece.BISHOP:
                    case Piece.KNIGHT:
                    case Piece.ROOK:
                        model.onCellClick(-1, (int) action);
                        break;
                }
                break;
        }
    }
}
