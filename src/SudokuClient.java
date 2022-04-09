import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

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

            gameLogic();
        }
        finally {
            System.out.println("closing session...");
            s.close();
        }
    }

    private void gameLogic() throws IOException{
        for(int i=1; i<=5; ++i){
            out.println("test " + i);
            String str = in.readLine();
            System.out.println(str);
        }
        out.println("END");
    }

}
