package com.mountainreacher.chess.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.mountainreacher.chess.Resources;
import com.mountainreacher.chess.components.generic.ActionListener;
import com.mountainreacher.chess.components.generic.ColorTextButton;
import com.mountainreacher.chess.components.generic.ImageButton;
import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class ConfirmationMessage extends Group implements ActionListener {

    private static final String POSITIVE = "P";
    private static final String NEGATIVE = "N";

//    private Label message;
//    private ColorTextButton positive, negative;
    private ImageButton queen, bishop, knight, rook;
    private ActionListener listener;

    public ConfirmationMessage(float width, float height, ActionListener listener) {
        setSize(width, height);
        this.listener = listener;
        float margin = width / 30;

        // Background
        Image bg = new Image(Resources.INSTANCE.getFilledBox((int) width, (int) height, 0.75f, 0.75f, 0.75f, 1));
        addActor(bg);

        // Logo
        float size = height / 2;

        // Buttons
//        positive = new ColorTextButton("", Resources.INSTANCE.getFont(Resources.FONT_BIG_WHITE, Resources.FONT_SHLOP),
//                (width / 2) - (margin * 3), height / 5, 102 / 255f, 204 / 255f, 102 / 255f, 1f);
//        negative = new ColorTextButton("", Resources.INSTANCE.getFont(Resources.FONT_BIG_WHITE, Resources.FONT_SHLOP),
//                (width / 2) - (margin * 3), height / 5, 1f, 0f, 51 / 255f, 1f);
//        positive.setPosition((width / 2) + (margin * 2), margin);
//        negative.setPosition(margin, margin);
//        positive.setActionListener(this);
//        negative.setActionListener(this);
//        positive.setName(POSITIVE);
//        negative.setName(NEGATIVE);
//        addActor(positive);
//        addActor(negative);

        // Conquest buttons
        size = width / 4;
        queen = new ImageButton(Resources.INSTANCE.getTexture("pieces/black_queen.png"), 100, 100, this);
        queen.setName("" + Piece.QUEEN);
        bishop = new ImageButton(Resources.INSTANCE.getTexture("pieces/black_bishop.png"), 100, 100, this);
        bishop.setName("" + Piece.BISHOP);
        knight = new ImageButton(Resources.INSTANCE.getTexture("pieces/black_knight.png"), 100, 100, this);
        knight.setName("" + Piece.KNIGHT);
        rook = new ImageButton(Resources.INSTANCE.getTexture("pieces/black_rook.png"), 100, 100, this);
        rook.setName("" + Piece.ROOK);

        // Positions
        queen.setPosition(margin, margin);
        bishop.setPosition(margin + size, margin);
        knight.setPosition(margin + size * 2, margin);
        rook.setPosition(margin + size * 3, margin);
        addActor(queen);
        addActor(bishop);
        addActor(knight);
        addActor(rook);
        setVisible(false);
    }

    public void showMessage(String message, String positive, String negative) {
//        this.message.setText(message);
//        this.positive.setText(positive);
//        this.negative.setText(negative);
//        this.positive.setVisible(true);
//        this.negative.setVisible(true);

        queen.setVisible(false);
        bishop.setVisible(false);
        knight.setVisible(false);
        rook.setVisible(false);

        setVisible(true);
    }

    public void showConquest(String message) {
//        this.message.setText(message);
//        this.positive.setVisible(false);
//        this.negative.setVisible(false);

        queen.setVisible(true);
        bishop.setVisible(true);
        knight.setVisible(true);
        rook.setVisible(true);

        setVisible(true);
    }

    @Override
    public void onActionPerformed(Actor source, Object action) {
        switch (source.getName()) {

            case POSITIVE:
                listener.onActionPerformed(this, 1);
                break;

            case NEGATIVE:
                listener.onActionPerformed(this, 0);
                break;

            default:
                listener.onActionPerformed(this, Integer.valueOf(source.getName()));
                break;
        }
        setVisible(false);
    }
}
