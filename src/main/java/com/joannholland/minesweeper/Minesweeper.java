package com.joannholland.minesweeper;

import java.util.Scanner;

/**
 * Represents the minesweeper game.
 * @author Joann Holland
 */
public class Minesweeper {

    private static boolean playing = true;
    private static Scanner userInput = new Scanner(System.in);
    public static Location firstMove;

    public static void main(String[] args) {

        System.out.println("Welcome to com.joannholland.minesweeper.Minesweeper!\n");

        playGame();

    }

    /**
     * Sets the user's first move.
     * @param row An integer for the selected row coordinate.
     * @param col An integer for the selected column coordinate.
     */
    public static void setFirstMove(int row, int col) {
        Location location = new Location();
        location.setRow(row);
        location.setColumn(col);
        firstMove = location;
    }

    /**
     * Initiates a new game of minesweeper.
     */
    public static void playGame() {
        Board board = chooseDifficulty();

        boolean validFirstMove = false;
        System.out.println("\nPlease enter the row-column position of your first move from 0 to " + board.getNumRows() + " (i.e. 3-3)");
        while (!validFirstMove) {

            // validations for first move location
            String firstLocationStr = userInput.nextLine();
            try {
                if (!firstLocationStr.matches("\\d{1,2}-\\d{1,2}") || firstLocationStr.isEmpty()) {
                    System.out.println("Invalid input - please enter a valid row-column coordinate (e.g. 3-3)");
                } else {

                    String[] firstLocationCoordinates = firstLocationStr.split("-");

                    if (Integer.parseInt(firstLocationCoordinates[0]) < 0 || Integer.parseInt(firstLocationCoordinates[0]) >= board.getNumRows() ||
                            Integer.parseInt(firstLocationCoordinates[1]) < 0 || Integer.parseInt(firstLocationCoordinates[1]) >= board.getNumCols()) {
                        throw new ArrayIndexOutOfBoundsException();
                    } else {
                        validFirstMove = true;

                        setFirstMove(Integer.parseInt(firstLocationCoordinates[0]), Integer.parseInt(firstLocationCoordinates[1]));
                        board.generateMines(firstMove);
                        board.setBoard();
                        board.floodfill(firstMove, board.getBoard());
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid row-column coordinates - please make sure they're in range, from 0 to " + board.getNumRows());
            }
        }

        // print out command instructions so the user knows what to enter
        System.out.println("\nCommand instructions:");
        System.out.println("reveal 3-3\treveals the cell at row-column location 3-3");
        System.out.println("flag 3-3\tflags the cell at row-column location 3-3");
        System.out.println("unflag 3-3\tunflags the cell at row-column location 3-3");

        while(playing) {
            System.out.println();
            System.out.println("Number of mines: " + board.getNumberOfMines());
            System.out.println("Flags left: " + (board.getNumberOfMines() - board.countNumberOfFlags(board.getBoard())));
            board.printBoard(board.getBoard());

            System.out.println("Enter reveal/flag/unflag followed by the row-column coordinate (i.e. reveal 2-3)");

            String[] splitInput = new String[2];

            // input validation for command
            boolean validInput = false;
            while(!validInput) {
                String userInputStr = userInput.nextLine();
                splitInput = userInputStr.split(" ");

                try {
                    // use regex to check for valid input string
                    if (!userInputStr.matches("(reveal|flag|unflag)\\s\\d{1,2}-\\d{1,2}") || userInputStr.isEmpty()) {
                        System.out.println("Invalid input - please enter reveal/flag/unflag followed by the row-column coordinate (i.e. reveal 2-3)");
                    } else { // if regex accurate, check row-col values in board range
                        String[] coordinates = splitInput[1].split("-");
                        int row = Integer.parseInt(coordinates[0]);
                        int column = Integer.parseInt(coordinates[1]);

                        if (row < 0 || row >= board.getNumRows() || column < 0 || column >= board.getNumCols()) {
                            throw new ArrayIndexOutOfBoundsException();
                        } else {
                            validInput = true;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Invalid row-column coordinates - please make sure they're in range, from 0 to " + board.getNumRows());
                }

            }

            String command = splitInput[0];
            String[] coords = splitInput[1].split("-");
            int row = Integer.parseInt(coords[0]);

            int column = Integer.parseInt(coords[1]);
            Location loc = new Location();
            loc.setRow(row);
            loc.setColumn(column);
            switch (command) {
                case "flag":
                    board.getBoard()[row][column].flag(loc);
                    break;
                case "unflag":
                    board.getBoard()[row][column].unflag(loc);
                    break;
                case "reveal":
                    if (board.getBoard()[row][column].isMine()) {
                        board.revealAll(board.getBoard());
                        board.printBoard(board.getBoard());
                        System.out.println("You chose a mine - game over!");
                        playing = false;
                        break;
                    } else {
                        board.getBoard()[row][column].floodfill(loc, board.getBoard());
                        if (board.countNumberOfRevealedCells(board.getBoard()) == board.getNumberOfAccurateRevealedCells()) {
                            board.revealAll(board.getBoard());
                            System.out.println("\nSee the revealed board below:");
                            board.printBoard(board.getBoard());
                            System.out.println("Congrats - you won!");
                            playing = false;
                            break;
                        }
                    }
            }
        }
    }

    /**
     * Prompts the player to choose a difficulty level (1-easy,
     *                                                  2-medium,
     *                                                  3-hard).
     *
     * @return A new board based on the chosen difficulty level (easy: 8x8 with 10 mines,
     *                                                          medium: 14x14 with 40 mines,
     *                                                          hard: 20x20 with 99 mines).
     */
    public static Board chooseDifficulty() {
        // choose difficulty
        int difficulty;
        boolean validDifficulty = false;
        Board board = new Board(0,0,0);

        // input validation for difficulty
        while(!validDifficulty) {
            try {
                System.out.println("Please enter your difficulty level:");
                System.out.println("1 - easy (8x8 board with 10 mines)");
                System.out.println("2 - medium (14x14 board with 40 mines)");
                System.out.println("3 - hard (20x20 board with 99 mines)");
                difficulty = Integer.parseInt(userInput.nextLine());
                if (difficulty != 1 && difficulty != 2 && difficulty != 3) {
                    System.out.println("Please enter 1, 2 or 3.");
                } else {
                    if (difficulty == 1) {
                        board = new Board(8, 8, 10);
                        validDifficulty = true;
                    } else if (difficulty == 2) {
                        board = new Board(14, 14, 40);
                        validDifficulty = true;
                    } else {
                        board = new Board(20, 20, 99);
                        validDifficulty = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number - 1, 2 or 3.");
            }
        }
        return board;
    }
}
