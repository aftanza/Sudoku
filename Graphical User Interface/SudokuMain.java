package sudoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * The main Sudoku program
 */
public class SudokuMain extends JFrame {
   // private variables
   GameBoard board = new GameBoard();
   JButton btnNewGame = new JButton("New Game");

   // Constructor
   public SudokuMain() {
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());

      cp.add(board, BorderLayout.CENTER);
      cp.add(btnNewGame, BorderLayout.EAST);

      board.init();

      pack();   
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
      setTitle("Sudoku");
      setVisible(true);
   }

   /** The entry main() entry method */
   public static void main(String[] args) {
	   SwingUtilities.invokeLater(new Runnable() {
	         @Override
	         public void run() {
	            new SudokuMain(); // Let the constructor do the job
	         }
	      });
   }
}