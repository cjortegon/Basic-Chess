package com.mountainreacher.chess.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.List;

import com.mountainreacher.chess.Resources;
import com.mountainreacher.chess.components.SoundsManager;
import com.mountainreacher.chess.components.generic.ActionListener;
import com.mountainreacher.chess.model.MatchOneToOne;
import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class Board extends Group {

    private static final String TAG = "ChessFaces";

    private SoundsManager sounds;
    private MatchOneToOne model;
    private Image[][] selected;
    private float tileSize, marginSize;
    private ActionListener listener;

    public Board(int size, MatchOneToOne model, SoundsManager sounds, ActionListener actionListener) {
        this.model = model;
        this.sounds = sounds;
        this.listener = actionListener;
        setSize(size, size);

        tileSize = size / model.getBoard().getRows();
        marginSize = tileSize / 20;
        selected = new Image[model.getBoard().getRows()][model.getBoard().getColumns()];

        Texture light = Resources.INSTANCE.getFilledBox((int) (tileSize - marginSize * 2), (int) (tileSize - marginSize * 2), 126 / 255f, 177 / 255f, 204 / 255f, 1);
        Texture dark = Resources.INSTANCE.getFilledBox((int) (tileSize - marginSize * 2), (int) (tileSize - marginSize * 2), 0f, 51 / 255f, 102 / 255f, 1);
        Texture selection = Resources.INSTANCE.getFilledBox((int) (tileSize - marginSize * 2), (int) (tileSize - marginSize * 2), 247 / 255f, 196 / 255f, 43 / 255f, 1);

        CellListener listener = new CellListener();

        // Board
        boolean color = true;
        for (int i = 0; i < model.getBoard().getRows(); i++) {
            if (model.getBoard().getColumns() % 2 == 0) {
                color = !color;
            }
            for (int j = 0; j < model.getBoard().getColumns(); j++) {
                Image image = null;
                if (color) {
                    image = new Image(light);
                } else {
                    image = new Image(dark);
                }
                color = !color;
                image.setPosition(getPostitionfrom(j), getPostitionfrom(i));
                addActor(image);

                // Blue cell
                selected[i][j] = new Image(selection);
                selected[i][j].setPosition(getPostitionfrom(j), getPostitionfrom(i));
                selected[i][j].setName(i + "," + j);
                selected[i][j].addListener(listener);
                selected[i][j].setVisible(false);
                addActor(selected[i][j]);
            }
        }

        // Pieces
        for (Piece piece : model.getBoard().getPiecesList()) {
            PieceActor actor = new PieceActor(this, piece, (int) (tileSize - marginSize * 2));
            actor.setPosition(getPostitionfrom(piece.getColumn()), getPostitionfrom(piece.getRow()));
            actor.addListener(listener);
            addActor(actor);
        }
    }

    @Override
    public void act(float delta) {

        // Turning cells off
        for (int i = 0; i < selected.length; i++) {
            for (int j = 0; j < selected[0].length; j++) {
                selected[i][j].setVisible(false);
            }
        }

        // Turning cells on
        List<int[]> list = model.getPossibleMovements();
        if (list != null) {
            for (int[] cell : list) {
                if (cell[0] >= 0 && cell[0] < selected.length
                        && cell[1] >= 0 && cell[1] < selected[0].length) {
                    selected[cell[0]][cell[1]].setVisible(true);
                }
            }
        }

        super.act(delta);
    }

    public float getPostitionfrom(int rowOrColumn) {
        return rowOrColumn * tileSize + marginSize;
    }

    public void completeAction(Piece piece) {
        model.movedPiece(piece);
        int hackType = model.getCheckType();
        Gdx.app.log(TAG, "CheckType: " + hackType);
        switch (hackType) {
            case MatchOneToOne.CHECK_MATE:
            case MatchOneToOne.CONQUEST:
                listener.onActionPerformed(this, hackType);
            case MatchOneToOne.CHECK:
                sounds.playSound(hackType);
                break;
        }
    }

    private class CellListener extends InputListener {

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            Gdx.app.log(TAG, event.getListenerActor().getName());
            String parts[] = event.getListenerActor().getName().split(",");
            if (model.onCellClick(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]))) {
                sounds.playSound(0);
            }
        }
    }

}
