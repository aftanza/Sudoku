package com.sudoku.multiplayer;

import com.sudoku.sudokulogic.SudokuTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/*HOST MULTIPLAYER
* Goal: to fill up Sudoku by Co-Op mode, each player taking turns
* Restrictions:
* Can only run in LAN
* Can only connect to 1 other device
*
* Logistics:
*   Two devices connect to each other, one will be the host and the other will be the client
*       The host is responsible for making the initial table
*       The host will host the server, and the client just have to connect
*   For each change made to the table, the table will be relayed to the other device in the form of a hash
*   The device receives the hash and updates its table accordingly
*   The game will end if the sudoku is completed or any player gives up
*
*/

public class SudokuHostServer {
    private final int PORT;
    public BufferedReader in;
    public PrintWriter out;
    private final String hash;
    private final SudokuTest game;
    private boolean exit = false;
    public void exit(){
        exit = true;
    }

    public SudokuHostServer(SudokuTest game, int port, String hash) {
        PORT = port;
        this.hash = hash;
        this.game = game;
    }

    public void startServer() throws IOException{
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("server starting in port " + PORT);

        Socket s = ss.accept();
        System.out.println("connection established with " + s.getInetAddress());
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
//        sendHashToClient();
    }

    public void sendHashToClient(){
        out.println(hash);
    }

    public void gameLogic(){
        System.out.println(game.getHowManyHints());
        game.printTable();
    }


//    public static void main(String[] args) throws IOException {
//        com.sudoku.multiplayer.SudokuHostServer server = new com.sudoku.multiplayer.SudokuHostServer(12520);
//        server.startServer();
//    }
}
