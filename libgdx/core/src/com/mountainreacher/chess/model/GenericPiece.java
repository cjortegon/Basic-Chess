package com.mountainreacher.chess.model;

import java.util.ArrayList;
import java.util.List;

import com.mountainreacher.chess.model.models.Piece;

/**
 *
 */

public class GenericPiece extends Piece {

    private int forward;
    private int startRow, startColumn;
    private int movesCount;
    private int rowsCount, columnsCount;
    private int originalRow, originalColumn, originalFigure;

    public GenericPiece(int team, int figure, int row, int column,
                        boolean lookingForward, int rowsCount, int columnsCount) {
        super(team, figure);
        setPosition(row, column);
        setAvailable(true);
        this.forward = lookingForward ? 1 : -1;
        this.startRow = row;
        this.startColumn = column;
        this.rowsCount = rowsCount;
        this.columnsCount = columnsCount;
        this.originalFigure = figure;
    }

    public void reset() {
        this.changeFigure(originalFigure);
        this.moveTo(startRow, startColumn);
        this.movesCount = 0;
        this.setAvailable(true);
    }

    @Override
    public boolean moveTo(int row, int column) {
        this.movesCount++;
        return super.moveTo(row, column);
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void temporalMove(int row, int column) {
        this.originalRow = getRow();
        this.originalColumn = getColumn();
        setPosition(row, column);
    }

    public void goBack() {
        setPosition(originalRow, originalColumn);
    }

    public List<int[]> getMoves(ClassicBoard board, boolean onlyKilling) {
        ArrayList<int[]> positions = new ArrayList<>();
        switch (getFigure()) {

            case KING:
                conditionalAdd(positions, board, getRow() - 1, getColumn() - 1, true);
                conditionalAdd(positions, board, getRow() - 1, getColumn(), true);
                conditionalAdd(positions, board, getRow() - 1, getColumn() + 1, true);
                conditionalAdd(positions, board, getRow(), getColumn() - 1, true);
                conditionalAdd(positions, board, getRow(), getColumn() + 1, true);
                conditionalAdd(positions, board, getRow() + 1, getColumn() - 1, true);
                conditionalAdd(positions, board, getRow() + 1, getColumn(), true);
                conditionalAdd(positions, board, getRow() + 1, getColumn() + 1, true);
                break;

            case QUEEN:
                addingCycle(positions, board, -1, -1, Integer.MAX_VALUE, Integer.MAX_VALUE, true);// Up - Left
                addingCycle(positions, board, -1, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Up
                addingCycle(positions, board, -1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Up - Right
                addingCycle(positions, board, 0, -1, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Left
                addingCycle(positions, board, 0, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, true);  // Right
                addingCycle(positions, board, 1, -1, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Down - Left
                addingCycle(positions, board, 1, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, true);  // Down
                addingCycle(positions, board, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, true);  // Down - Right
                break;

            case BISHOP:
                addingCycle(positions, board, -1, -1, Integer.MAX_VALUE, Integer.MAX_VALUE, true);// Up - Left
                addingCycle(positions, board, -1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Up - Right
                addingCycle(positions, board, 1, -1, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Down - Left
                addingCycle(positions, board, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, true);  // Down - Right
                break;

            case KNIGHT:
                conditionalAdd(positions, board, getRow() - 2, getColumn() - 1, true);
                conditionalAdd(positions, board, getRow() - 2, getColumn() + 1, true);
                conditionalAdd(positions, board, getRow() - 1, getColumn() - 2, true);
                conditionalAdd(positions, board, getRow() - 1, getColumn() + 2, true);
                conditionalAdd(positions, board, getRow() + 2, getColumn() - 1, true);
                conditionalAdd(positions, board, getRow() + 2, getColumn() + 1, true);
                conditionalAdd(positions, board, getRow() + 1, getColumn() - 2, true);
                conditionalAdd(positions, board, getRow() + 1, getColumn() + 2, true);
                break;

            case ROOK:
                addingCycle(positions, board, -1, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Up
                addingCycle(positions, board, 0, -1, Integer.MAX_VALUE, Integer.MAX_VALUE, true); // Left
                addingCycle(positions, board, 0, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, true);  // Right
                addingCycle(positions, board, 1, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, true);  // Down
                break;

            case PAWN:
                if (onlyKilling) {
                    positions.add(new int[]{getRow() + forward, getColumn() + 1});
                    positions.add(new int[]{getRow() + forward, getColumn() - 1});
                } else {
                    // Advance positions
                    addingCycle(positions, board, forward, 0, getRow() == startRow ? 2 : 1, 0, false);
                    // Diagonal killing
                    try {
                        if (board.getSelected(getRow() + forward, getColumn() + 1).getTeam() != getTeam())
                            positions.add(new int[]{getRow() + forward, getColumn() + 1});
                    } catch (Exception e) {
                    }
                    try {
                        if (board.getSelected(getRow() + forward, getColumn() - 1).getTeam() != getTeam())
                            positions.add(new int[]{getRow() + forward, getColumn() - 1});
                    } catch (Exception e) {
                    }
                }
                break;
        }
        return positions;
    }

    /**
     * @param board
     * @param row
     * @param column
     * @return -1 if cannot move, 0 if can move, 1 if is the last move in this direction
     */
    private int moveType(ClassicBoard board, int row, int column) {
        Piece piece = board.getSelected(row, column);
        if (piece == null) {
            switch (getFigure()) {
                case KING:
                    return board.kingIsInDanger(getTeam(), row, column) == null ? 0 : -1;

                default: // Other figures can move to an empty position
                    return 0;
            }
        } else if (piece.getTeam() == getTeam()) {
            // Cannot move when the cell is occupied by another piece of the same team
            return -1;
        } else { // When the piece is from the enemy
            switch (getFigure()) {
                case KING:
                    return board.kingIsInDanger(getTeam(), row, column) == null ? 0 : -1;

                default: // The last move to kill an enemy
                    return 1;
            }
        }
    }

    private boolean conditionalAdd(List<int[]> positions, ClassicBoard board, int row, int column, boolean killEnemies) {
        if (row >= 0 && column >= 0 && row < rowsCount && column < columnsCount) {
            switch (moveType(board, row, column)) {
                case -1:
                    return false;

                case 0:
                    positions.add(new int[]{row, column});
                    return true;

                case 1:
                    if (killEnemies)
                        positions.add(new int[]{row, column});
                    return false;
            }
        }
        return false;
    }

    private void addingCycle(List<int[]> positions, ClassicBoard board, int rowIncrement, int columnIncrement,
                             int rowsLimit, int columnsLimit, boolean killEnemies) {
        int i = rowIncrement, j = columnIncrement;
        boolean canContinue = true;
        while (canContinue && getRow() + i >= 0 && getRow() + i < rowsCount
                && getColumn() + j >= 0 && getColumn() + j < columnsCount) {
            canContinue = conditionalAdd(positions, board, getRow() + i, getColumn() + j, killEnemies);
            i += rowIncrement;
            j += columnIncrement;
            if (Math.abs(i) > rowsLimit || Math.abs(j) > columnsLimit) {
                canContinue = false;
            }
        }
    }
}