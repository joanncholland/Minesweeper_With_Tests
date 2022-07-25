package com.joannholland.minesweeper;

import com.joannholland.minesweeper.Cell;
import com.joannholland.minesweeper.Location;
import com.joannholland.minesweeper.Minesweeper;

import java.util.ArrayList;

/**
 * Represents the minesweeper board.
 * @author Joann Holland
 */
public class Board extends Minesweeper {
    private final int numCols;
    private final int numRows;
    private ArrayList<Location> mineLocations;
    private final int numberOfMines;
    private Cell[][] board;
    private final ArrayList<Location> flagLocations = new ArrayList<>();
    private int numberOfAccurateRevealedCells;

    /**
     * Creates a new board.
     * @param numCols The number of columns on the board.
     * @param numRows The number of rows on the board.
     * @param numberOfMines The number of mines on the board.
     */
    public Board(int numCols, int numRows, int numberOfMines) {
        this.numCols= numCols;
        this.numRows = numRows;
        this.numberOfMines = numberOfMines;
        setNumberOfAccurateRevealedCells((numCols * numRows) - numberOfMines);
    }

    /**
     * Gets the number of rows on the board.
     * @return An integer specifying the number of rows on the board.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Gets the number of columns on the board.
     * @return An integer specifying the number of columns on the board.
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Get the number of mines on the board.
     * @return An integer specifying the number of mines on the board.
     */
    public int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Get the flag locations.
     * @return An ArrayList containing the locations of the flagged cells.
     */
    public ArrayList<Location> getFlagLocations() {
        return flagLocations;
    }

    /**
     * Get the number of revealed cells on the board.
     * @return An integer specifying the number of revealed cells.
     */
    public int getNumberOfAccurateRevealedCells() {
        return numberOfAccurateRevealedCells;
    }

    /**
     * Counts the number of revealed cells on the board.
     * @param board A com.joannholland.minesweeper.Cell[][] 2d array that has the cells on the board.
     * @return An integer specifying how many revealed cells are on the board.
     */
    public int countNumberOfRevealedCells(Cell[][] board) {
        int count = 0;
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                if (board[i][j].isRevealed()) count++;
            }
        }
        return count;
    }

    /**
     * Counts the number of flagged cells on the board.
     * @param board A com.joannholland.minesweeper.Cell[][] 2d array that has the cells on the board.
     * @return An integer specifying the number of flagged cells are on the board.
     */
    public int countNumberOfFlags(Cell[][] board) {
        int count = 0;
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                if (board[i][j].isFlagged()) count++;
            }
        }
        return count;
    }

    /**
     * Set the number for the total number of cells that can be revealed that aren't mines.
     * @param numberOfAccurateRevealedCells An integer representing the total number of cells that can be revealed that aren't mines.
     */
    public void setNumberOfAccurateRevealedCells(int numberOfAccurateRevealedCells) {
        this.numberOfAccurateRevealedCells = numberOfAccurateRevealedCells;
    }

    /**
     * Gets the board.
     * @return A com.joannholland.minesweeper.Cell[][] 2d array that represents the board.
     */
    public Cell[][] getBoard() {
        return this.board;
    }

    /**
     * Sets the board.
     */
    public void setBoard() {
        board = new Cell[this.numCols][this.numRows];

        // fill array with 0s
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                Location loc = new Location();
                loc.setRow(i);
                loc.setColumn(j);

                Cell cell = new Cell(loc, this.numCols, this.numRows, this.numberOfMines, false, false, false);

                board[i][j] = cell;
            }
        }

        // place mines
        for (Location location : mineLocations) {
            Cell cell = new Cell(location, this.numCols, this.numRows, this.numberOfMines, true, false, false);
            board[location.getRow()][location.getColumn()] = cell;
        }

        // count surrounding mines
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                board[i][j].setNumberSurroundingMines(countSurroundingMines(i,j, this.board));
            }
        }
    }

    /**
     * Generate the mines based on random locations - makes sure there are no duplicates and
     * that on the player's first move no mines are generated surrounding the player's first cell.
     * Takes note of players first move and generates mines away from first move location.
     */
    public void generateMines(Location firstLocation) {
        mineLocations = new ArrayList<>();
        // while mineLocations doesn't have the specified number of mines, loop
        while(mineLocations.size() < this.numberOfMines) {
            Location loc = new Location();
            // generate random row, column within board limits
            int row = getRandomNumber(this.numCols);
            int column = getRandomNumber(this.numRows);
            loc.setRow(row);
            loc.setColumn(column);

            // get firstLocation surrounding locations
            ArrayList<Location> firstLocationSurrounding = getSurroundingCellLocations(firstLocation);

            // if mine locations doesn't already contain location
            // and if loc not in surrounding cell locations, add location
            if (!mineLocations.contains(loc) && !firstLocationSurrounding.contains(loc)) {
                mineLocations.add(loc);
            }
        }
    }

    // get surrounding cell locations
    public ArrayList<Location> getSurroundingCellLocations(Location location) {
        ArrayList<Location> surroundingLocations = new ArrayList<>();
        try {
            for (int i = location.getRow()-1; i <= location.getRow() + 1; i++) {
                for (int j = location.getColumn()-1; j <= location.getColumn()+1; j++) {
                    Location surroundingLocation = new Location();
                    surroundingLocation.setRow(i);
                    surroundingLocation.setColumn(j);
                    surroundingLocations.add(surroundingLocation);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return surroundingLocations;
    }

    /**
     * Get a random number - used for generating random mine locations.
     * @param max The integer which is the max limit for a random number to be generated.
     * @return An integer containing the random number.
     */
    public int getRandomNumber(int max) {
        return (int) ((Math.random() * (max)) + 0);
    }

    /**
     * Counts the mines surrounding the cell.
     * @param row An integer containing the row location of the cell.
     * @param column An integer containing the column location of the cell.
     * @param board A com.joannholland.minesweeper.Cell[][] 2d array containing the board.
     * @return An integer that contains the number of surrounding mines of the cell.
     */
    public int countSurroundingMines(int row, int column, Cell[][] board) {
        int mines = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i + row >= 0 && i + row < numCols && j + column >= 0 && j + column < numRows) { // check if valid inderow
                    if (board[i+row][j+column].isMine()) {
                        mines++;
                    }
                }
            }
        }
        return mines;
    }

    /**
     * Floodfill used if the selected cell has 0 surrounding mines - uses a recursive algorithm.
     * @param location A com.joannholland.minesweeper.Location object containing the location of the cell.
     */
    public void floodfill(Location location, Cell[][] board) {
        if (!board[location.getRow()][location.getColumn()].isRevealed()) {
            int numberSurroundingMines = countSurroundingMines(location.getRow(), location.getColumn(), board);

            try {
                if (numberSurroundingMines > 0) {
                    board[location.getRow()][location.getColumn()].setRevealed(true);
                } else {
                    board[location.getRow()][location.getColumn()].setRevealed(true);
                    if (location.getRow() > 0) { // top
                        Location loc1 = new Location();
                        loc1.setRow(location.getRow()-1);
                        loc1.setColumn(location.getColumn());
                        floodfill(loc1, board);
                    }
                    if (location.getRow() < numRows-1) { // bottom
                        Location loc2 = new Location();
                        loc2.setRow(location.getRow()+1);
                        loc2.setColumn(location.getColumn());
                        floodfill(loc2, board);
                    }
                    if (location.getColumn() > 0) { // left
                        Location loc3 = new Location();
                        loc3.setRow(location.getRow());
                        loc3.setColumn(location.getColumn()-1);
                        floodfill(loc3, board);
                    }
                    if (location.getColumn() < numCols-1) { // right
                        Location loc4 = new Location();
                        loc4.setRow(location.getRow());
                        loc4.setColumn(location.getColumn()+1);
                        floodfill(loc4, board);
                    }
                    if (location.getColumn() > 0 && location.getRow() > 0) { // top left
                        Location loc5 = new Location();
                        loc5.setRow(location.getRow()-1);
                        loc5.setColumn(location.getColumn()-1);
                        floodfill(loc5, board);
                    }
                    if (location.getColumn() < numCols-1 && location.getRow() > 0) { // top right
                        Location loc6 = new Location();
                        loc6.setRow(location.getRow()-1);
                        loc6.setColumn(location.getColumn()+1);
                        floodfill(loc6, board);
                    }
                    if (location.getColumn() > 0 && location.getRow() < numRows-1) { // bottom left
                        Location loc7 = new Location();
                        loc7.setRow(location.getRow()+1);
                        loc7.setColumn(location.getColumn()-1);
                        floodfill(loc7, board);
                    }
                    if (location.getColumn() < numCols-1 && location.getRow() < numRows-1) { // bottom right
                        Location loc8 = new Location();
                        loc8.setRow(location.getRow()+1);
                        loc8.setColumn(location.getColumn()+1);
                        floodfill(loc8, board);
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * Reveals all the cells on the board - used in win/lose cases.
     * @param board The com.joannholland.minesweeper.Cell[][] 2d array containing the board.
     */
    public void revealAll(Cell[][] board) {
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                if (board[i][j].isMine()) {
                    board[i][j].setFlagged(false);
                }
                board[i][j].setRevealed(true);
            }
        }
    }

    /**
     * Prints the board in a readable format.
     * @param board The com.joannholland.minesweeper.Cell[][] 2d array containing the board.
     */
    public void printBoard(Cell[][] board) {
        System.out.print("\t");
        for (int i = 0; i < numCols; i++) {
            System.out.print(i + "\t");
        }
        System.out.print("    ");

        System.out.print("\n");
        for (int i = 0; i < numCols; i++) {
            if (i >= 10) {
                System.out.print(i + "");
            } else {
                System.out.print(i + " ");
            }
            for (int j = 0; j < numRows; j++) {
                System.out.print("\t" + board[i][j]);
            }
            System.out.print(" \n");
        }

        System.out.print("   ");

        System.out.println();
    }
}
