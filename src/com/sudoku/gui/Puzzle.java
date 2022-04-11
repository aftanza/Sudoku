package com.sudoku.gui;
import com.sudoku.sudokulogic.SudokuTest;

/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle{
   // All variables have package access
   int[][] numbers = new int[GameBoard.GRID_SIZE][GameBoard.GRID_SIZE];

   boolean[][] isShown = new boolean[GameBoard.GRID_SIZE][GameBoard.GRID_SIZE];

   // Constructor
   public Puzzle() {
      super();  // JPanel
   }

   public void newPuzzle(SudokuDifficulty difficulty) {
      // Hardcoded here for simplicity.

      SudokuTest game = new SudokuTest();

      switch (difficulty){
         case HARD -> game.setUpSudoku(0, 35);
         case MEDIUM -> game.setUpSudoku(36, 51);
         case LUKEWARM -> game.setUpSudoku(52, 68);
         case EASY -> game.setUpSudoku(69, 81);
      }

      game.printTable();
      game.solveTable();
      game.printTable();

      for (int row = 0; row < GameBoard.GRID_SIZE; ++row) {
         for (int col = 0; col < GameBoard.GRID_SIZE; ++col) {
            numbers[row][col] = game.getArrNumAt(row, col);
            isShown[row][col] = game.getArrStateAt(row, col) == 1;
         }
      }

   }

   //(For advanced students) use singleton design pattern for this class
}