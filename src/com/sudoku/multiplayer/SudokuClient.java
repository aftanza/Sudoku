package com.sudoku.multiplayer;

import com.sudoku.sudokulogic.SudokuTest;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SudokuClient {
    private final String ADDRESS;
    private final int PORT;
    private BufferedReader in;
    private PrintWriter out;
    private final SudokuTest game;

    public SudokuClient(SudokuTest game, String address, int port){
        PORT = port;
        ADDRESS = address;
        this.game = game;
    }

    public void startClient() throws IOException{
        InetAddress address = InetAddress.getByName(ADDRESS);
        Socket s = new Socket(address, PORT);
        System.out.println("connected with host, address = " + address + " port = " + PORT);
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

            gameLogic();
        }
        finally {
            System.out.println("closing session...");
            s.close();
        }
    }

    boolean getClientInput(){
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
    boolean getHostInput(){
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
        else if (str.equals("SKIP")) {
            game.printTable();
            return false;
        }
        else{
            game.implementHash(str);
            game.printTable();
            return false;
        }
    }

    void gameLogic()throws IOException{
        String hash = in.readLine();
        game.implementHash(hash);
        System.out.println(game.getHowManyHints());
        game.printTable();

        while (true){

            System.out.println("At any point, if you want to check ans just put -1 as the only number");
            System.out.println("Waiting for other player.....");

            if(getHostInput())
                break;

            System.out.println("Enter row, col, and num as such: x x x");

            if(getClientInput())
                break;
        }
        System.out.println("Congrats!");
    }

//    public static void main(String[] args)throws IOException {
//        com.sudoku.sudokulogic.SudokuTest game = new com.sudoku.sudokulogic.SudokuTest();
//        com.sudoku.multiplayer.SudokuClient client = new com.sudoku.multiplayer.SudokuClient(game, "localhost", 12520);
//        client.startClient();
//    }

}