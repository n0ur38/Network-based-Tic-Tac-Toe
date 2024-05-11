import java.io.*;
import java.net.*;

public class client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            String serverResponse;
            while ((serverResponse = input.readLine()) != null) {
                System.out.println(serverResponse);
                if (serverResponse.startsWith("Hello")) {
                    System.out.println("Type 'play' to start a game.");
                }
                if (serverResponse.startsWith("Game started")) {
                    playGame(input, output, consoleInput);
                }
                if (serverResponse.startsWith("move")) {
                    displayBoard(serverResponse);
                    System.out.println("Enter your move (row column):");
                    String move = consoleInput.readLine();
                    output.println("move " + move);
                }
                if (serverResponse.startsWith("win")) {
                    System.out.println("Congratulations! You win!");
                    break;
                }
                if (serverResponse.startsWith("draw")) {
                    System.out.println("It's a draw!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playGame(BufferedReader input, PrintWriter output, BufferedReader consoleInput) throws IOException {
        String serverResponse;
        while ((serverResponse = input.readLine()) != null) {
            if (serverResponse.startsWith("move")) {
                displayBoard(serverResponse);
                System.out.println("Enter your move (row column):");
                String move = consoleInput.readLine();
                output.println("move " + move);
            }
            if (serverResponse.startsWith("Your turn")) {
                System.out.println("Waiting for opponent's move...");
            }
            if (serverResponse.startsWith("win")) {
                System.out.println("Congratulations! You win!");
                break;
            }
            if (serverResponse.startsWith("draw")) {
                System.out.println("It's a draw!");
                break;
            }
        }
    }

    private static void displayBoard(String serverResponse) {
        String[] parts = serverResponse.split(" ");
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(parts[i * 3 + j + 1] + " | ");
            }
            System.out.println("\n-------------");
        }
    }
}