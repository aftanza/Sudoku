package com.sudoku.multiplayer;

import com.sudoku.sudokulogic.SudokuTest;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class SudokuClient{
    private final String ADDRESS;
    private final int PORT;
    public BufferedReader in;
    public PrintWriter out;
    public SudokuTest game = new SudokuTest();
    private boolean exit = false;
    public void exit(){
        exit = true;
    }
    public Socket s;

    public SudokuClient(String address, int port){
        PORT = port;
        ADDRESS = address;
    }

    public void startClient() throws IOException{
        InetAddress address = InetAddress.getByName(ADDRESS);
        s = new Socket(address, PORT);
        System.out.println("connected with host, address = " + address + " port = " + PORT);

        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

        getHostGameUsingHash();
    }

    public void getHostGameUsingHash() throws IOException{
        String hash = in.readLine();
        game.implementHash(hash);
        System.out.println(game.getHowManyHints());
//        game.printTable();
    }

//    public static void main(String[] args)throws IOException {
//        com.sudoku.sudokulogic.SudokuTest game = new com.sudoku.sudokulogic.SudokuTest();
//        com.sudoku.multiplayer.SudokuClient client = new com.sudoku.multiplayer.SudokuClient(game, "localhost", 12520);
//        client.startClient();
//    }

}