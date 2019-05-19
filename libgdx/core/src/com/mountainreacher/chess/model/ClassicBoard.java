package com.mountainreacher.chess.model;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import com.mountainreacher.chess.model.models.IBoard;
import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class ClassicBoard implements IBoard {

    private static final String TAG = "ChessTag";

    private int rows, columns;
    private ArrayList<Piece> pieces;
    private ArrayList<GenericPiece> kings;

    public ClassicBoard() {
        this.rows = 8;
        this.columns = 8;
        this.pieces = new ArrayList<>();
        this.kings = new ArrayList<>();
        addClassicPieces();
    }

    private void addClassicPieces() {

        GenericPiece blackKing = new GenericPiece(Piece.BLACK_TEAM, Piece.KING, rows - 1, 4, false, rows, columns);
        pieces.add(blackKing);
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.ROOK, rows - 1, 0, false, rows, columns));
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.KNIGHT, rows - 1, 1, false, rows, columns));
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.BISHOP, rows - 1, 2, false, rows, columns));
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.QUEEN, rows - 1, 3, false, rows, columns));
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.BISHOP, rows - 1, 5, false, rows, columns));
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.KNIGHT, rows - 1, 6, false, rows, columns));
        pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.ROOK, rows - 1, 7, false, rows, columns));
        for (int i = 0; i < columns; i++) {
            pieces.add(new GenericPiece(Piece.BLACK_TEAM, Piece.PAWN, rows - 2, i, false, rows, columns));
        }

        GenericPiece whiteKing = new GenericPiece(Piece.WHITE_TEAM, Piece.KING, 0, 4, true, rows, columns);
        pieces.add(whiteKing);
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.ROOK, 0, 0, true, rows, columns));
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.KNIGHT, 0, 1, true, rows, columns));
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.BISHOP, 0, 2, true, rows, columns));
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.QUEEN, 0, 3, true, rows, columns));
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.BISHOP, 0, 5, true, rows, columns));
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.KNIGHT, 0, 6, true, rows, columns));
        pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.ROOK, 0, 7, true, rows, columns));
        for (int i = 0; i < columns; i++) {
            pieces.add(new GenericPiece(Piece.WHITE_TEAM, Piece.PAWN, 1, i, true, rows, columns));
        }

        // Adding kings
        kings.add(blackKing);
        kings.add(whiteKing);

        // Adding cold down
        for (Piece piece : pieces) {
            piece.lockFor(3000);
        }
    }

    @Override
    public List<Piece> getPiecesList() {
        return pieces;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int skipCorners() {
        return 0;
    }

    public GenericPiece getSelected(int team, int row, int column) {
        for (Piece piece : pieces) {
            if (!piece.isMoving() && piece.isAvailable() && piece.getTeam() == team && piece.getRow() == row && piece.getColumn() == column) {
                return (GenericPiece) piece;
            }
        }
        return null;
    }

    public Piece getSelectedNotInTeam(int team, int row, int column) {
        for (Piece piece : pieces) {
            if (!piece.isMoving() && piece.isAvailable() && piece.getTeam() != team && piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
    }

    public Piece getSelected(int row, int column) {
        for (Piece piece : pieces) {
            if (piece.isAvailable() && piece.getRow() == row && piece.getColumn() == column) {
                return piece;
            }
        }
        return null;
    }

    public GenericPiece[] kingIsInDanger() {
        for (GenericPiece king : kings) {
            Piece threat = kingIsInDanger(king.getTeam(), king.getRow(), king.getColumn());
            if (threat != null)
                return new GenericPiece[]{king, (GenericPiece) threat};
        }
        return null;
    }
    
    protected Piece kingIsInDanger(int kingTeam, int row, int column) {
        Gdx.app.log(TAG, "Checking king " + kingTeam + "(" + row + "," + column + ")");
        for (Piece piece : pieces) {
            if (piece.getTeam() != kingTeam && piece.isAvailable()) {

                // Closeness with the other king
                if (piece.getFigure() == Piece.KING) {
		    int rowDistance = Math.abs(piece.getRow() - row);
		    int columnDistance = Math.abs(piece.getColumn() - column);
		    if(Math.max(rowDistance, columnDistance) < 2) {
                        return piece;
		    }
                }

                // The other figure types
                else {
                    List<int[]> positions = ((GenericPiece) piece).getMoves(this, true);
                    Gdx.app.log(TAG, piece.getType() + "(" + piece.getRow() + "," + piece.getColumn()
                            + ") has " + positions.size() + " positons to move");
                    for (int[] pos : positions) {
                        if (pos[0] == row && pos[1] == column) {
                            return piece;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void checkPossibleMovements(GenericPiece piece, List<int[]> possibleMovements) {
        for (Piece king : kings) {
            if (king.getTeam() == piece.getTeam()) {
                for (int i = 0; i < possibleMovements.size(); ) {
                    int[] pos = possibleMovements.get(i);
                    piece.temporalMove(pos[0], pos[1]);
                    Piece killed = getSelectedNotInTeam(piece.getTeam(), pos[0], pos[1]);
                    if (killed != null) {
                        killed.setAvailable(false);
                    }
                    Piece threat = kingIsInDanger(king.getTeam(), king.getRow(), king.getColumn());
                    if (threat != null) {
                        possibleMovements.remove(i);
                    } else {
                        i++;
                    }
                    piece.goBack();
                    if (killed != null) {
                        killed.setAvailable(true);
                    }
                }
            }
        }
    }

    public boolean hasAvailableMoves(int team) {
        for (Piece piece : pieces) {
            if (piece.getTeam() == team) {
                GenericPiece generic = (GenericPiece) piece;
                List<int[]> moves = generic.getMoves(this, false);
                checkPossibleMovements(generic, moves);
                if (moves.size() > 0) {
                    Gdx.app.log(TAG, "Piece " + piece.getType() + "(" + piece.getRow() + "," + piece.getColumn()
                            + ") can move to (" + moves.get(0)[0] + "," + moves.get(0)[1] + ")");
                    return true;
                }
            }
        }
        return false;
    }

    public void reset() {
        for (Piece piece : pieces) {
            ((GenericPiece) piece).reset();
        }
    }
}
