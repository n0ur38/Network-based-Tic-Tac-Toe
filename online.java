import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class online extends JFrame {
    private static final String SERVER_IP = "localhost"; 
    private static final int SERVER_PORT = 12345; 
    
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private JTextArea gameTextArea;
    private JButton[][] buttons;
    
    public online() {
        super("Tic-Tac-Toe Client");
        
        gameTextArea = new JTextArea(5, 30);
        gameTextArea.setEditable(false);
        add(new JScrollPane(gameTextArea), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 3));
        buttonPanel.setBackground(new Color(95, 158, 160)); // Set background color
        
        buttons = new JButton[3][3];
        ButtonListener buttonListener = new ButtonListener();
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].addActionListener(buttonListener);
                buttons[row][col].setPreferredSize(new Dimension(100, 100)); // Set preferred size
                buttonPanel.add(buttons[row][col]);
            }
        }
        
        JButton replayButton = new JButton("Replay"); // Create replay button
        replayButton.setPreferredSize(new Dimension(100, 100)); // Set preferred size
        replayButton.setFont(new Font(replayButton.getFont().getName(), Font.PLAIN, 30)); // Set font size
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        buttonPanel.add(replayButton, gbc); // Add replay button
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 530); // Increase height to accommodate the replay button
        setVisible(true);
        
        connectToServer();
    }
    
    private void connectToServer() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            Thread serverListenerThread = new Thread(new ServerListener());
            serverListenerThread.start();
            
            displayMessage("Connected to server\n");
        } catch (IOException e) {
            displayMessage("Error connecting to server\n");
        }
    }
    
    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> gameTextArea.append(message));
    }
    
    private void sendMove(int row, int col) {
        output.println(row + "," + col);
    }
    
    private void updateBoard(String boardState) {
        String[] values = boardState.split(",");
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText(values[row * 3 + col]);
                buttons[row][col].setEnabled(values[row * 3 + col].isEmpty());
            }
        }
    }
    
    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (button == buttons[row][col]) {
                        sendMove(row, col);
                        return;
                    }
                }
            }
        }
    }
    
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                String serverMessage;
                
                while ((serverMessage = input.readLine()) != null) {
                    if (serverMessage.startsWith("BOARD:")) {
                        String boardState = serverMessage.substring(6);
                        updateBoard(boardState);
                    } else {
                        displayMessage(serverMessage + "\n");
                    }
                }
            } catch (IOException e) {
                displayMessage("Error reading from server\n");
            }
        }
        
        public class TicTacToeClient {
    private char[][] board;
    private char currentPlayerSymbol;

    public TicTacToeClient() {
        board = new char[3][3];
        currentPlayerSymbol = 'X';
        initializeBoard();
    }

    // Initialize the game board
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    // Make a move on the board
    public boolean makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayerSymbol;
            return true;
        }
        return false;
    }

    // Check if a move is valid
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-';
    }

    // Switch the current player
    private void switchPlayer() {
        if (currentPlayerSymbol == 'X') {
            currentPlayerSymbol = 'O';
        } else {
            currentPlayerSymbol = 'X';
        }
    }

    // Update the board based on the received board state
    public void updateBoard(String boardState) {
        String[] values = boardState.split(",");

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = values[row * 3 + col].charAt(0);
            }
        }
    }

    // Get the current board state
    public String getBoardState() {
        StringBuilder boardState = new StringBuilder();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                boardState.append(board[row][col]);
                if (col < 2) {
                    boardState.append(",");
                }
            }
        }

        return boardState.toString();
    }

    // Check if the game is over (win or tie)
    public boolean isGameOver() {
        return checkWin('X') || checkWin('O') || isBoardFull();
    }

    // Check if the game is a tie
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    // Check if a player has won the game
    private boolean checkWin(char symbol) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == symbol && board[1][j] == symbol && board[2][j] == symbol) {
                return true;
            }
        }

        // Check diagonals
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
            return true;
        }

        return false;
    }
}
    }
    
 
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(online.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(online.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(online.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(online.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new online().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
