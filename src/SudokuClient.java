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

    SudokuClient(SudokuTest game, String address, int port){
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

            gameLogic2();
        }
        finally {
            System.out.println("closing session...");
            s.close();
        }
    }

    boolean getClientInput() throws IOException{
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

    void gameLogic2()throws IOException{
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

    public static void main(String[] args)throws IOException {
        SudokuTest game = new SudokuTest();
        SudokuClient client = new SudokuClient(game, "localhost", 12520);
        client.startClient();
    }

}
//    private void gameLogic3() throws IOException{
//        Scanner input = new Scanner(System.in);
//        String stringFromHost = in.readLine();
//        System.out.println(stringFromHost);
//        System.out.flush();
//
//        int intFromHost = 0;
//        while (true){
//            try {
//                /*-----------------Wait for host input------------------------*/
//                do {
//                    intFromHost = in.read() - 48;
//                }
//                while (intFromHost == 0);
//
//                if(intFromHost == -1) {
//                    System.out.println("Host terminated session...");
//                    break;
//                }
//                System.out.println(intFromHost);
//
//                /*-----------------Get client input----------------------*/
//                int stringFromClient = input.nextInt();
//                if(stringFromClient == -1)
//                    break;
//                out.println(stringFromClient);
//            }
//            catch (NullPointerException e){
//                break;
//            }
//        }
//    }
//
//    private void gameLogic() throws IOException{
//        Scanner input = new Scanner(System.in);
//        String stringFromHost;
//
//        stringFromHost = in.readLine();
//        System.out.println(stringFromHost);
//        while (true){
//            try {
//                /*-----------------Wait for host input------------------------*/
//                do {
//                    stringFromHost = in.readLine();
//                }
//                while (stringFromHost.equals(""));
//
//                if(stringFromHost.equals("END")) {
//                    System.out.println("Host terminated session...");
//                    break;
//                }
//                System.out.println(stringFromHost);
//
//                /*-----------------Get client input----------------------*/
//                String stringFromClient = input.nextLine();
//                if(stringFromClient.equals("END"))
//                    break;
//                out.println(stringFromClient);
//            }
//            catch (NullPointerException e){
//                break;
//            }
//        }
//    }

//    boolean getHostInput() throws IOException{
//        String str;
//        int r, c, x;
//        do {
//            str = in.readLine();
//            System.out.println(str);
//        }
//        while (str.equals(""));
//
//        System.out.println(str);
//        r = str.charAt(0);
//        if (r == 'x') {
//            if (game.checkTable()) {
//                game.printTable();
//                return true;
//            } else {
//                game.printTable();
//                return false;
//            }
//        }
//
//        c = str.charAt(2);
//        x = str.charAt(4);
//        game.setTile((int)r-48, (int)c-48, (int)x-48);
//        game.printTable();
//        return false;
//    }
//
//    boolean getClientInput() throws IOException{
//        int r, c, x;
//        r = Integer.parseInt(in.readLine());
//
//        if (r == -1) {
//            if (game.checkTable()) {
//                game.printTable();
//                return true;
//            } else {
//                game.printTable();
//                return false;
//            }
//        }
//
//        c = in.read();
//        x = in.read();
//        game.setTile(r, c, x);
//        game.printTable();
//        return false;
//    }