package com.mountainreacher.chess.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import com.mountainreacher.chess.Resources;
import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class PieceActor extends Group {

    private static final Object[][] IMAGES = {
            {Piece.WHITE_TEAM + Piece.KING, "pieces/white_king.png"},
            {Piece.WHITE_TEAM + Piece.QUEEN, "pieces/white_queen.png"},
            {Piece.WHITE_TEAM + Piece.BISHOP, "pieces/white_bishop.png"},
            {Piece.WHITE_TEAM + Piece.KNIGHT, "pieces/white_knight.png"},
            {Piece.WHITE_TEAM + Piece.ROOK, "pieces/white_rook.png"},
            {Piece.WHITE_TEAM + Piece.PAWN, "pieces/white_pawn.png"},
            {Piece.BLACK_TEAM + Piece.KING, "pieces/black_king.png"},
            {Piece.BLACK_TEAM + Piece.QUEEN, "pieces/black_queen.png"},
            {Piece.BLACK_TEAM + Piece.BISHOP, "pieces/black_bishop.png"},
            {Piece.BLACK_TEAM + Piece.KNIGHT, "pieces/black_knight.png"},
            {Piece.BLACK_TEAM + Piece.ROOK, "pieces/black_rook.png"},
            {Piece.BLACK_TEAM + Piece.PAWN, "pieces/black_pawn.png"},
    };

    private Piece piece;
    private Board board;
    private Image image;

    public PieceActor(Board board, Piece piece, int size) {
        this.board = board;
        this.piece = piece;
        this.setSize(size, size);

        for (Object[] pair : IMAGES) {
            if ((int) pair[0] == piece.getType()) {
                image = new Image(Resources.INSTANCE.getTexture((String) pair[1]));
                image.setSize(size, size);
                this.addActor(image);
                break;
            }
        }
    }

    @Override
    public void act(float delta) {
        if (!piece.isAvailable()) {
            setVisible(false);
        } else {
            setVisible(true);
            setName(piece.getRow() + "," + piece.getColumn());
        }
        // Has to move
        if (!piece.isMoving() && (piece.getMoveToRow() != piece.getRow() || piece.getMoveToColumn() != piece.getColumn())) {
            piece.setMoving(true);
            if (piece.hasMutations()) {
                for (Object[] pair : IMAGES) {
                    if ((int) pair[0] == piece.getType()) {
                        image.setDrawable(new SpriteDrawable(new Sprite((Resources.INSTANCE.getTexture((String) pair[1])))));
                        break;
                    }
                }
            }
            addAction(Actions.sequence(Actions.moveTo(board.getPostitionfrom(piece.getMoveToColumn()),
                    board.getPostitionfrom(piece.getMoveToRow()), 0.5f), new CompletitionAction()));
        } else if (piece.hasMutations()) {
            piece.setMoving(true);
            addAction(Actions.sequence(Actions.rotateBy(360, 1), new MutationAction()));
        }
        super.act(delta);
    }

    private class CompletitionAction extends Action {

        @Override
        public boolean act(float delta) {
            piece.completeMoving();
            piece.setMoving(false);
            board.completeAction(piece);
            return true;
        }
    }

    private class MutationAction extends Action {

        @Override
        public boolean act(float delta) {
            piece.setMoving(false);
            for (Object[] pair : IMAGES) {
                if ((int) pair[0] == piece.getType()) {
                    image.setDrawable(new SpriteDrawable(new Sprite((Resources.INSTANCE.getTexture((String) pair[1])))));
                    break;
                }
            }
            board.completeAction(piece);
            return true;
        }
    }
}
