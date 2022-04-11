package com.sudoku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main Sudoku program
 */
public class SudokuMain extends JFrame {
    // private variables
    GameBoard board = new GameBoard();


    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());

        JButton btnReset = new JButton("Reset");
        btnPanel.add(btnReset);
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                board.reset();
            }
        });

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.setSize(40, 20);
        btnPanel.add(btnNewGame);
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                board.init();
            }
        });

        cp.add(btnPanel,BorderLayout.EAST);
        JPanel hintCount = new JPanel(new FlowLayout());

        board.init();

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setVisible(true);
        this.setLocationRelativeTo(null);
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