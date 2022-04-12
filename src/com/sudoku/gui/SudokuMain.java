package com.sudoku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuMain extends JFrame {
    // private variables
    GameBoard board = new GameBoard();
    private Label lblHintCount;    // Declare a Label component
    private TextField tfHintCount; // Declare a TextField component


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


        JPanel hintCount = new JPanel(new FlowLayout());

        lblHintCount = new Label("Hint count");  // construct the Label component
        hintCount.add(lblHintCount);                    // "super" Frame container adds Label component

        tfHintCount = new TextField(""); // construct the TextField component with initial text
        tfHintCount.setEditable(false);       // set to read-only

        hintCount.add(tfHintCount);
        cp.add(hintCount,BorderLayout.SOUTH);


        JButton btnNewGame = new JButton("New Game");
        btnNewGame.setSize(40, 20);
        btnPanel.add(btnNewGame);
        btnNewGame.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent evt) {
                Object[] options = { "HARD", "MEDIUM", "LUKEWARM",  "EASY" };
                int difficultyLevel = JOptionPane.showOptionDialog(null, "Select difficulty level", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[3]);
                board.init(difficultyLevel);
                tfHintCount.setText(String.valueOf(board.getHowManyHints()));
            }
        });

        cp.add(btnPanel,BorderLayout.EAST);

        board.init(3);
        tfHintCount.setText(String.valueOf(board.getHowManyHints()));

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku");
        setVisible(true);
        this.setLocationRelativeTo(null);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuMain(); // Let the constructor do the job
            }
        });
    }
}


