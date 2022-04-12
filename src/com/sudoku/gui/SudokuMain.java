package com.sudoku.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class SudokuMain extends JFrame {
    // private variables
    GameBoard board = new GameBoard();
    private Label lblHintCount;    // Declare a Label component
    private TextField tfHintCount; // Declare a TextField component
    public int port;
    public String IP;


    // Constructor
    public SudokuMain() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        cp.add(board, BorderLayout.CENTER);

        JPanel hintCount = new JPanel(new FlowLayout());

        lblHintCount = new Label("Hint count");  // construct the Label component
        hintCount.add(lblHintCount);                    // "super" Frame container adds Label component

        tfHintCount = new TextField(""); // construct the TextField component with initial text
        tfHintCount.setEditable(false);       // set to read-only

        hintCount.add(tfHintCount);
        cp.add(hintCount,BorderLayout.SOUTH);



        JPanel btnPanel = new JPanel(new FlowLayout());


        JButton btnReset = new JButton("Reset");
        JButton btnNewGame = new JButton("New Game");
        JButton btnSubmit = new JButton("Submit");

        btnSubmit.setVisible(false);

        btnPanel.add(btnReset);
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                board.reset();
            }
        });


        btnNewGame.setSize(40, 20);
        btnPanel.add(btnNewGame);
        btnNewGame.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent evt) {
                Object[] mode = { "MULTIPLAYER", "SINGLEPLAYER"};
                int modeSelected = JOptionPane.showOptionDialog(null, "Select mode", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, mode, mode[1]);
                if (modeSelected == 1)
                {
                    Object[] options = { "HARD", "MEDIUM", "LUKEWARM",  "EASY" };
                    int difficultyLevel = JOptionPane.showOptionDialog(null, "Select difficulty level", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[3]);
                    board.init(difficultyLevel);
                    btnSubmit.setVisible(false);
                }
                else
                {
                    Object[] selectHost = { "JOIN", "HOST"};
                    int hostSelected = JOptionPane.showOptionDialog(null, "Host or join", "Warning",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, selectHost, selectHost[1]);
                    if (hostSelected == 1)
                    {
                        port = Integer.valueOf(JOptionPane.showInputDialog("Enter port"));

                        Object[] options = { "HARD", "MEDIUM", "LUKEWARM",  "EASY" };
                        int difficultyLevel = JOptionPane.showOptionDialog(null, "Select difficulty level", "Warning",
                                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, options, options[3]);
                        try {
                            board.init(difficultyLevel, port);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        btnSubmit.setVisible(false);
                    }
                    else
                    {
                        IP = JOptionPane.showInputDialog("Enter IP");
                        System.out.println(IP);
                        port = Integer.valueOf(JOptionPane.showInputDialog("Enter port"));
                        System.out.println(port);
                        try {
                            board.init(port, IP);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    btnSubmit.setVisible(true);
                }
                tfHintCount.setText(String.valueOf(board.getHowManyHints()));
            }
        });


        btnPanel.add(btnSubmit);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                board.reset(); //t5e4rthjdrgthjddryjtrtyjfrtyjdrtyjdtyjdrtjrtjdrtyjdtyj
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







