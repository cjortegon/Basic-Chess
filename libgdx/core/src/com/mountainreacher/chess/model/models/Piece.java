package com.mountainreacher.chess.model.models;

/**
 * This is an abstract representation of any kind of Piece in the Chess game.
 */

public abstract class Piece {

    public static final int KING = 1;
    public static final int QUEEN = 2;
    public static final int BISHOP = 3;
    public static final int KNIGHT = 4;
    public static final int ROOK = 5;
    public static final int PAWN = 6;

    public static final int WHITE_TEAM = 10;
    public static final int BLACK_TEAM = 20;
    public static final int RED_TEAM = 30;
    public static final int BLUE_TEAM = 40;

    private int type, team, figure, row, column, moveToRow, moveToColumn;
    private long lockedUntil, lockedTime;
    private boolean available, moving, mutation;

    /**
     * Constructor with team and figure type.
     *
     * @param team   From the Piece.
     * @param figure From the Piece.
     */
    public Piece(int team, int figure) {
        this.type = team + figure;
        this.team = team;
        this.figure = figure;
    }

    /**
     * This will identify the image to display for this Piece.
     *
     * @return Team + Figure
     */
    public int getType() {
        return type;
    }

    /**
     * @return The figure that is representing this Piece
     */
    public int getFigure() {
        return figure;
    }

    /**
     * @return The team which figure belongs
     */
    public int getTeam() {
        return team;
    }

    /**
     * @return The actual known row from the Piece
     */
    public int getRow() {
        return row;
    }

    /**
     * @return The actual known column from the Piece
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return The row where the Piece is travelling to.
     */
    public int getMoveToRow() {
        return moveToRow;
    }

    /**
     * @return The column where the Piece is travelling to.
     */
    public int getMoveToColumn() {
        return moveToColumn;
    }

    /**
     * @return If the figure is moving.
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     * @return If the figure has been changed to another figure.
     */
    public boolean hasMutations() {
        boolean mutation = this.mutation;
        this.mutation = false;
        return mutation;
    }

    /**
     * @param moving If the figure is moving.
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
        this.moveToRow = row;
        this.moveToColumn = column;
    }

    /**
     * This method is called to make the Piece go to a new position.
     *
     * @param row    The desired row.
     * @param column The desired column.
     * @return
     */
    public boolean moveTo(int row, int column) {
        boolean value = row != this.row || column != this.column;
        this.moveToRow = row;
        this.moveToColumn = column;
        return value;
    }

    /**
     * @return If the Figure needs to execute a movement.
     */
    public boolean needsToMove() {
        return row != moveToRow || column != moveToColumn;
    }

    /**
     * Called after a movement has finished. This will update the last known position to the travelling position.
     */
    public void completeMoving() {
        this.row = moveToRow;
        this.column = moveToColumn;
    }

    /**
     * @return If the Piece is alive.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * To set the alieve state of the Piece.
     *
     * @param available If the Piece is alive.
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * This will save the record that the Piece cannot move for certain amount of time.
     *
     * @param milliseconds The milliseconds to lock the Piece.
     */
    public void lockFor(long milliseconds) {
        this.lockedUntil = System.currentTimeMillis() + milliseconds;
        this.lockedTime = milliseconds;
    }

    /**
     * @return If the figure is locked to be moved.
     */
    public boolean isLocked() {
        return lockedUntil > System.currentTimeMillis();
    }

    public float getLockedPercent() {
        long time = lockedUntil - System.currentTimeMillis();
        if (time > 0) {
            return time / (float) lockedTime;
        } else {
            return 0;
        }
    }

    public void changeFigure(int newFigure) {
        if (this.figure != newFigure) {
            this.type = team + newFigure;
            this.figure = newFigure;
            this.mutation = true;
        }
    }

}
