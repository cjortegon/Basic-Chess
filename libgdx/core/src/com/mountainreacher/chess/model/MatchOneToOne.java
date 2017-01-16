package com.mountainreacher.chess.model;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import com.mountainreacher.chess.model.models.IBoard;
import com.mountainreacher.chess.model.models.IMatch;
import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class MatchOneToOne implements IMatch {

    private static final String TAG = "ChessTag";

    private boolean turn;
    private GenericPiece selected, conquest;
    private ClassicBoard board;
    private List<int[]> possibleMovements;
    private int checkType;

    public MatchOneToOne() {
        this.board = new ClassicBoard();
        this.turn = true;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public boolean onCellClick(int row, int column) {
        if (row == -1 && conquest != null) {
            checkType = 0;
            conquest.changeFigure(column);
            return true;
        } else if (selected != null) {
            if (possibleMovements != null) {
                for (int[] move : possibleMovements) {
                    if (move[0] == row && move[1] == column) {
                        // Move the PieceActor to the desired position
                        if (selected.moveTo(row, column)) {
                            turn = !turn;
                        }
                        break;
                    }
                }
            }
            selected = null;
            possibleMovements = null;
            return true;
        } else {
            selected = board.getSelected(turn ? Piece.WHITE_TEAM : Piece.BLACK_TEAM, row, column);
            if (selected != null) {
                possibleMovements = new ArrayList<>();
                possibleMovements.addAll(((GenericPiece) selected).getMoves(board, false));
                // Checking the movements
                board.checkPossibleMovements(selected, possibleMovements);
                if (possibleMovements.size() == 0) {
                    possibleMovements = null;
                    selected = null;
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<int[]> getPossibleMovements() {
        return possibleMovements;
    }

    @Override
    public void movedPiece(Piece piece) {
        Gdx.app.log(TAG, "movedPiece(" + piece.getType() + ")");

        // Killing the enemy
        Piece killed = board.getSelectedNotInTeam(piece.getTeam(),
                piece.getRow(), piece.getColumn());
        if (killed != null) {
            killed.setAvailable(false);
        }

        // Checking hacks
        GenericPiece[] threat = board.kingIsInDanger();
        if (threat != null) {
            checkType = board.hasAvailableMoves(threat[0].getTeam()) ? CHECK : CHECK_MATE;
        } else {
            checkType = NO_CHECK;
        }

        // Checking castling
        if (piece.getFigure() == Piece.ROOK && ((GenericPiece) piece).getMovesCount() == 1) {
            Piece king = board.getSelected(piece.getTeam(),
                    piece.getRow(), piece.getColumn() + 1);
            if (king != null && king.getFigure() == Piece.KING && ((GenericPiece) king).getMovesCount() == 0) {
                // Left Rook
                if (board.getSelected(piece.getRow(), piece.getColumn() - 1) == null) {
                    king.moveTo(piece.getRow(), piece.getColumn() - 1);
                }
            } else {
                king = board.getSelected(piece.getTeam(),
                        piece.getRow(), piece.getColumn() - 1);
                if (king != null && king.getFigure() == Piece.KING && ((GenericPiece) king).getMovesCount() == 0) {
                    // Right Rook
                    if (board.getSelected(piece.getRow(), piece.getColumn() + 1) == null) {
                        king.moveTo(piece.getRow(), piece.getColumn() + 1);
                    }
                }
            }
        }

        // Conquest
        else if (piece.getFigure() == Piece.PAWN && (piece.getRow() == 0 || piece.getRow() == board.getRows() - 1)) {
            conquest = (GenericPiece) piece;
            checkType = CONQUEST;
        }
    }

    public void reset() {
        turn = true;
        checkType = 0;
        selected = null;
        possibleMovements = null;
        board.reset();
    }

    @Override
    public int getCheckType() {
        return checkType;
    }

    @Override
    public int[] getSelectedPosition() {
        return new int[0];
    }

    @Override
    public void computerThread() {
    }
}