import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
    private static int PORT;
    private BufferedReader in;
    private PrintWriter out;

    SudokuHostServer(int port) {
        PORT = port;
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
                System.out.println("prepared to take input");

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

    private void gameLogic() throws IOException {
        out.println("Starting hash = " + "1..3.4.32.423.");
        while (true){
            String str = in.readLine();
            if(str.equals("END"))
                break;
            System.out.println("Echoing: " + str);
            out.println(str);
        }
    }

    public static void main(String[] args) {
        SudokuHostServer server = new SudokuHostServer(12345);
    }
}
