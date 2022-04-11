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
    private BufferedReader in;
    private PrintWriter out;
    private final String hash;
    private final SudokuTest game;

    public SudokuHostServer(SudokuTest game, int port, String hash) {
        PORT = port;
        this.hash = hash;
        this.game = game;
    }

    public void startServer() throws IOException{
        ServerSocket ss = new ServerSocket(PORT);
        System.out.println("server starting in port " + PORT);
        try {
            Socket s = ss.accept();
            System.out.println("connection established with " + s.getInetAddress());
            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

                gameLogic();

            }
            finally {
                s.close();
            }
        }
        finally {
            ss.close();
            System.out.println("server closing...");
        }
    }

    boolean getHostInput(){
        Scanner input = new Scanner(System.in);
        int r, c, x;

        r = input.nextInt();
        if (r == -1) {
            if (game.checkTable()) {
                game.printTable();
                out.println("WIN");
                return true;
            } else {
                game.printTable();
                out.println("SKIP");
                return false;
            }
        }

        c = input.nextInt();
        x = input.nextInt();
        game.setTile(r, c, x);
        game.printTable();
        out.println(game.getHash());
        return false;
    }
    boolean getClientInput(){
        String str = "";
        do{
            try {
                str = in.readLine();
            }
            catch (IOException ignore){}
        }
        while (str.equals(""));

        if(str.equals("WIN")) {
            game.printTable();
            return true;
        }
        else if (str.equals("SKIP")){
            game.printTable();
            return false;
        }
        else{
            game.implementHash(str);
            game.printTable();
            return false;
        }
    }

    void gameLogic(){
        game.implementHash(hash);
        out.println(hash);

        System.out.println(game.getHowManyHints());
        game.printTable();

        while (true){

            System.out.println("At any point, if you want to check ans just put -1 as the only number");
            System.out.println("Enter row, col, and num as such: x x x");

            if(getHostInput())
                break;

            System.out.println("Waiting for other player.....");

            if(getClientInput())
                break;
        }
        System.out.println("Congrats!");
    }


//    public static void main(String[] args) throws IOException {
//        com.sudoku.multiplayer.SudokuHostServer server = new com.sudoku.multiplayer.SudokuHostServer(12520);
//        server.startServer();
//    }
}
