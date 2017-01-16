package com.mountainreacher.chess.model.models;

import java.util.List;

/**
 * The class that implements this interface will contain the list of pieces and the settings of the board.
 */

public interface IBoard {

    /**
     * @return List of Pieces from all the teams
     */
    public List<Piece> getPiecesList();

    /**
     * @return Number of rows in the board
     */
    public int getRows();

    /**
     * @return Number of columns in the board
     */
    public int getColumns();

    /**
     * @return Number of cells to remove from the corners. This is valid for more than 2 players mode.
     */
    public int skipCorners();

}
