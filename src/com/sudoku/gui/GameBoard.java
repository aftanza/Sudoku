package com.sudoku.gui;

import com.sudoku.multiplayer.SudokuClient;
import com.sudoku.multiplayer.SudokuHostServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Random;

public class GameBoard extends JPanel {
   // Name-constants for the game board properties
   public static final int GRID_SIZE = 9;    // Size of the board
   public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

   // Name-constants for UI sizes
   public static final int CELL_SIZE = 60;   // Cell width/height in pixels
   public static final int BOARD_WIDTH  = CELL_SIZE * GRID_SIZE;
   public static final int BOARD_HEIGHT = CELL_SIZE * GRID_SIZE;
   // Board width/height in pixels

   // The game board composes of 9x9 "Customized" JTextFields,
   private Cell[][] cells = new Cell[GRID_SIZE][GRID_SIZE];
   // It also contains a Puzzle
   public Puzzle puzzle = new Puzzle();
   public TextArea taDisplay;
   public int currentMode = 2;
   public int live = 3;

   // Constructor
   public GameBoard() {
      super.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));  // JPanel

      // Allocate the 2D array of Cell, and added into JPanel.
      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            cells[row][col] = new Cell(row, col);
            super.add(cells[row][col]);   // JPanel
         }
      }

      super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
   }

   public void setCurrentMode(int x){
      currentMode = x;
   }

   //Single player
   public void init(int level) {
      // Get a new puzzle
      switch (level) {
         case 3:
            puzzle.newPuzzle(SudokuDifficulty.EASY);
            break;
         case 2:
            puzzle.newPuzzle(SudokuDifficulty.LUKEWARM);
            break;
         case 1:
            puzzle.newPuzzle(SudokuDifficulty.MEDIUM);
            break;
         case 0:
            puzzle.newPuzzle(SudokuDifficulty.HARD);
            break;
         default:
            puzzle.newPuzzle(SudokuDifficulty.EASY);
            break;
      }

      CellInputListener listener = new CellInputListener();
//      puzzle.game.printTable();

      // Based on the puzzle, initialize all the cells.
      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            cells[row][col].init(puzzle.numbers[row][col], puzzle.isShown[row][col]);
         }
      }

      for (int row = 0; row < GRID_SIZE; row++) {
         for (int col = 0; col < GRID_SIZE; col++) {
            if (cells[row][col].isEditable()) {

               cells[row][col].addKeyListener(listener);   // For all editable rows and cols
            }
         }
      }
   }

   //Host
   public void init(int level, int port) throws IOException {
      init(level);

      String puzzleHash = puzzle.game.getHash();
      SudokuHostServer server = new SudokuHostServer(puzzle.game, port, puzzleHash);
      server.startServer();
      System.out.println(puzzleHash);
      server.sendHashToClient();
   }

   //Joining
   public void init(int port, String IP)throws IOException {
      SudokuClient client = new SudokuClient(IP, port);
      client.startClient();
      puzzle.setPuzzleUsingHash(client.game.getHash());

      CellInputListener listener = new CellInputListener();


      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            cells[row][col].init(puzzle.numbers[row][col], puzzle.isShown[row][col]);
         }
      }

      for (int row = 0; row < GRID_SIZE; row++) {
         for (int col = 0; col < GRID_SIZE; col++) {
            if (cells[row][col].isEditable()) {
               cells[row][col].addKeyListener(listener);   // For all editable rows and cols
            }
         }
      }
   }

   public void reset(){
      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            cells[row][col].init(puzzle.numbers[row][col], puzzle.isShown[row][col]);
         }
      }
   }

   public boolean isSolved() {
      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            if (cells[row][col].status == CellStatus.NO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
               return false;
            }
         }
      }
      return true;
   }

   public void submit(){
      Cell sourceCell;

      for(int row=0; row<GRID_SIZE; ++row){
         for(int col=0; col<GRID_SIZE; ++col){
            sourceCell = cells[row][col];

            if (cells[row][col].isEditable()) {

               if(sourceCell.getText().equals("")){
                  sourceCell.status = CellStatus.NO_GUESS;
               }
               else if (sourceCell.getText().equals(String.valueOf(sourceCell.number))) {
                  sourceCell.status = CellStatus.CORRECT_GUESS;
               } else if (Integer.parseInt(sourceCell.getText()) >= 1 && Integer.parseInt(sourceCell.getText()) <= 9) {
                  sourceCell.status = CellStatus.WRONG_GUESS;
               } else {
                  sourceCell.status = CellStatus.NO_GUESS;
               }
               sourceCell.paint();
               if (isSolved()) {
                  JOptionPane.showMessageDialog(null, "Congratulation!");
               }
            }
         }
      }
   }

   public void setEditableFalseAll(){
      for (int row = 0; row < GRID_SIZE; ++row) {
         for (int col = 0; col < GRID_SIZE; ++col) {
            cells[row][col].setEditable(false);
         }
      }
   }

   public void showRandomHint(){
      while (true){
         for(int i=0; i<GRID_SIZE; ++i){
            for(int j=0; j<GRID_SIZE; ++j){
               if(cells[i][j].isEditable() == true){
                  cells[i][j].setEditable(false);
                  cells[i][j].status = CellStatus.SHOWN;
                  cells[i][j].paint();
                  return;
               }
            }
         }
      }
   }

   public int getHowManyHints(){
      return puzzle.getHowManyHints();
   }

   private class CellInputListener implements KeyListener {
      @Override

      public void keyTyped(KeyEvent e) {
         // Get a reference of the JTextField that triggers this action event
         Cell sourceCell = (Cell)e.getSource();

         // Retrieve the int entered
         int numberIn = (int)e.getKeyChar()-'0';
         // For debugging
//         System.out.println("You entered " + numberIn);
         //taDisplay.append("You have typed " + numberIn + "\n");
//         String input  = sourceCell.row + " " + sourceCell.col + " " + numberIn;
         if (currentMode == 2) {
            if (numberIn == sourceCell.number) {
               sourceCell.status = CellStatus.CORRECT_GUESS;
            } else if (numberIn >= 1 && numberIn <= 9) {
               sourceCell.status = CellStatus.WRONG_GUESS;
            } else {
               sourceCell.status = CellStatus.NO_GUESS;
            }
            sourceCell.paint();
            if (isSolved()) {
               JOptionPane.showMessageDialog(null, "Congratulation!");
            }
         }
         else if(currentMode == 0){
            if (numberIn == sourceCell.number) {
               sourceCell.status = CellStatus.CORRECT_GUESS;
            } else if (numberIn >= 1 && numberIn <= 9) {
               --live;
               if(live == 0){
                  JOptionPane.showMessageDialog(null, "GAME OVER", "GAME OVER", JOptionPane.ERROR_MESSAGE);
                  setEditableFalseAll();
               }
               sourceCell.status = CellStatus.WRONG_GUESS;
            } else {
               sourceCell.status = CellStatus.NO_GUESS;
            }
            sourceCell.paint();
            if (isSolved()) {
               JOptionPane.showMessageDialog(null, "Congratulation!");
            }
         }


      }
      @Override public void keyPressed(KeyEvent evt) { }
      @Override public void keyReleased(KeyEvent evt) { }
   }


}


