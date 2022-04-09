import java.io.IOException;
import java.util.Scanner;

public class SudokuRun {

    void doMultiplayer(SudokuTest game) throws IOException{
        Scanner input = new Scanner(System.in);
        String userInput;
        int port;

        System.out.print("Host or join? h/j: ");
        userInput = input.nextLine();

        if(userInput.equals("h")) {
            String hash = game.getHash();
            System.out.print("Enter port: ");
            port = input.nextInt();
            SudokuHostServer server = new SudokuHostServer(game, port, hash);
            server.startServer();
        }
        else {
            System.out.print("Enter IP address: ");
            String IP = input.nextLine();

            System.out.print("Enter port: ");
            port = input.nextInt();
            SudokuClient client = new SudokuClient(game,IP, port);
            client.startClient();
        }
    }

    void doSinglePlayer(SudokuTest game){
        System.out.println(game.getHowManyHints());
        game.printTable();

        Scanner input = new Scanner(System.in);
        int r, c, x;
        while (true){
            System.out.println("At any point, if you want to check ans just put -1 as the only number");
            System.out.println("Enter row, col, and num as such: x x x");
            r = input.nextInt();
            if(r == -1) {
                if(game.checkTable()) {
                    game.printTable();
                    break;
                }
                else {
                    game.printTable();
                    continue;
                }
            }
            c = input.nextInt();
            x = input.nextInt();
            game.setTile(r, c, x);
            game.printTable();
        }
        System.out.println("Congrats!");
    }


    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        SudokuTest game = new SudokuTest();

//        game.implementHash("002030108040906051400050207090108130809011305060712040201070519081304060504090601130207080603080412071509010907041803010615020308120706050401190005060904020803071");
        game.setUpSudoku(0, 35);

        System.out.print("Multiplayer game? y/n: ");
        String userInput = input.nextLine();

        if(userInput.equals("y"))
            new SudokuRun().doMultiplayer(game);
        else
            new SudokuRun().doSinglePlayer(game);
    }
}
