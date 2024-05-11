import java.io.*;
import java.net.*;
import java.util.*;

public class server {
    private static final int PORT = 12345;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static List<TicTacToeGame> games = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Tic-Tac-Toe Server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;
        private String username;

        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                output.println("Welcome to Tic-Tac-Toe Server");
                output.println("Enter your username:");
                username = input.readLine();
                output.println("Hello, " + username + "!");

                String message;
                while ((message = input.readLine()) != null) {
                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                    if (message.equalsIgnoreCase("play")) {
                        output.println("Searching for an opponent...");
                        searchForOpponent();
                    }
                    if (message.startsWith("move")) {
                        int row = Integer.parseInt(message.split(" ")[1]);
                        int col = Integer.parseInt(message.split(" ")[2]);
                        int gameId = Integer.parseInt(message.split(" ")[3]);
                        TicTacToeGame game = games.get(gameId);
                        game.makeMove(row, col, this);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void searchForOpponent() {
            for (ClientHandler client : clients) {
                if (!client.equals(this)) {
                    output.println("Opponent found: " + client.username + ". Do you want to play? (yes/no)");
                    try {
                        String response = input.readLine();
                        if (response.equalsIgnoreCase("yes")) {
                            startGame(client);
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            output.println("No opponent found. Try again later.");
        }

        private void startGame(ClientHandler opponent) {
            TicTacToeGame game = new TicTacToeGame(this, opponent);
            games.add(game);
            opponent.output.println("Game started! You are X.");
            output.println("Game started! You are O.");
        }

        public void sendMessage(String message) {
            output.println(message);
        }
    }

    static class TicTacToeGame {
        private char[][] board = new char[3][3];
        private ClientHandler player1;
        private ClientHandler player2;
        private ClientHandler currentPlayer;
        private int moveCount = 0;

        public TicTacToeGame(ClientHandler player1, ClientHandler player2) {
            this.player1 = player1;
            this.player2 = player2;
            currentPlayer = player1;
        }

        public synchronized void makeMove(int row, int col, ClientHandler player) {
            if (player == currentPlayer && board[row][col] == 0) {
                char symbol = (player == player1) ? 'X' : 'O';
                board[row][col] = symbol;
                moveCount++;
                currentPlayer.sendMessage("move " + row + " " + col + " " + getGameId());
                if (checkWinner(row, col, symbol)) {
                    player.sendMessage("win");
                    currentPlayer = null;
                    player1 = null;
                    player2 = null;
                    games.remove(this);
                } else if (moveCount == 9) {
                    player1.sendMessage("draw");
                    player2.sendMessage("draw");
                    currentPlayer = null;
                    player1 = null;
                    player2 = null;
                    games.remove(this);
                } else {
                    currentPlayer = (currentPlayer == player1) ? player2 : player1;
                    currentPlayer.sendMessage("Your turn");
                }
            }
        }

        private boolean checkWinner(int row, int col, char symbol) {
            return (board[row][0] == symbol && board[row][1] == symbol && board[row][2] == symbol) ||
                   (board[0][col] == symbol && board[1][col] == symbol && board[2][col] == symbol) ||
                   (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                   (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
        }

        public int getGameId() {
            return games.indexOf(this);
        }
    }
}
