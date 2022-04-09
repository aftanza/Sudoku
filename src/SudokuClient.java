import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SudokuClient {
    private static String ADDRESS;
    private static int PORT;
    private BufferedReader in;
    private PrintWriter out;

    SudokuClient(String address, int port){
        PORT = port;
        ADDRESS = address;
    }

    public void startClient() throws IOException{
        InetAddress address = InetAddress.getByName(ADDRESS);
        Socket s = new Socket(ADDRESS, PORT);
        System.out.println("connected with host, address = " + ADDRESS + " port = " + PORT);
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

            String stringFromHost = in.readLine();
            System.out.println(stringFromHost);

            gameLogic();
        }
        finally {
            System.out.println("closing session...");
            s.close();
        }
    }

    private void gameLogic() throws IOException{
        Scanner input = new Scanner(System.in);
        String stringFromHost;
        while (true){
            try {
                /*-----------------Wait for host input------------------------*/
                do {
                    stringFromHost = in.readLine();
                }
                while (stringFromHost.equals(""));

                if(stringFromHost.equals("END"))
                    break;
                System.out.println(stringFromHost);

                /*-----------------Get client input----------------------*/
                String stringFromClient = input.nextLine();
                if(stringFromClient.equals("END"))
                    break;
                out.println(stringFromClient);
            }
            catch (NullPointerException e){
                break;
            }
        }
    }

    public static void main(String[] args)throws IOException {
        SudokuClient client = new SudokuClient("localhost", 12345);
        client.startClient();
    }

}
