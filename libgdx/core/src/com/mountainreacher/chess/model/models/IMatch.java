package com.mountainreacher.chess.model.models;

import java.util.List;

/**
 * The class that implements this interface will contain the internal logic for any kind of Chess play style.
 */

public interface IMatch {

    public static final int NO_CHECK = 0;
    public static final int CHECK = 1;
    public static final int CHECK_MATE = 2;
    public static final int CONQUEST = 3;

    /**
     * @return The IBoard class which has the board settings.
     */
    public IBoard getBoard();

    /**
     * This method is called every time the user touches a cell in the board.
     *
     * @param row    Touched row.
     * @param column Touched column.
     * @return True if the position matches a valid clickable cell in order to play a sound.
     */
    public boolean onCellClick(int row, int column);

    /**
     * This method is used to paint in a different color the cells where the user can press to complete a move.
     *
     * @return The list of clickable cells.
     */
    public List<int[]> getPossibleMovements();

    /**
     * Called every time a Piece movement finishes.
     *
     * @param piece The moved Piece.
     */
    public void movedPiece(Piece piece);

    /**
     * Called to play an special sound for checks
     *
     * @return 0 (no check), 1 (check), 2 (checkmate), 3 conquest
     */
    public int getCheckType();

    /**
     * Used to paint the cell from the selected Piece in a different color.
     *
     * @return Null if no Piece is selected or an array of (row, column) from the selected position.
     */
    public int[] getSelectedPosition();

    /**
     * This is called every time the Game Engine cycle to handle computer moves.
     * Due to different phone capacities and graphics load at each time, the frequency is not always the same.
     */
    public void computerThread();

}
