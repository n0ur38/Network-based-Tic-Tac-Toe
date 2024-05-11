import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class withpc extends javax.swing.JFrame {
    
        
    // Game board
    private char[][] board;
    // Player and computer symbols
    private char playerSymbol, computerSymbol;
    // Buttons for the game board
    private JButton[][] buttons;
    // Replay button
    private JButton replayButton;
    
    public withpc() {
        
        
        board = new char[3][3];
        playerSymbol = 'X';
        computerSymbol = 'O';
        buttons = new JButton[3][3];
        
        initializeBoard();
        initializeGUI();
    }
    
       
    // Initialize the game board
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }
    
    // Initialize the graphical user interface
    private void initializeGUI() {
    setTitle("Tic-Tac-Toe");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Set the background color for the entire frame
    getContentPane().setBackground(new Color(95, 158, 160));
    
    // Use BorderLayout to organize components
    setLayout(new BorderLayout());
    
    JPanel gamePanel = new JPanel(new GridLayout(3, 3)); // Panel for the game board
    gamePanel.setBackground(new Color(95, 158, 160)); // Set background color for the game panel
    
    JPanel buttonPanel = new JPanel(new GridLayout(1, 2)); // Panel for buttons (Replay and return)
    buttonPanel.setBackground(new Color(95, 158, 160)); // Set background color for the button panel

    buttons = new JButton[3][3];

    // Create buttons for the game board
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            JButton button = new JButton();
            button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 80));
            button.addActionListener(new ButtonClickListener(i, j));
            buttons[i][j] = button;
            gamePanel.add(button);
        }
    }

    // Create replay button
    replayButton = new JButton("Replay");
    replayButton.setBackground(Color.WHITE);
    replayButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
    replayButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    });
    replayButton.setEnabled(false);
    buttonPanel.add(replayButton);

    // Create return to choose page button
    JButton choosePageButton = new JButton("Return");
    choosePageButton.setBackground(Color.WHITE);
    choosePageButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
    choosePageButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Dispose of the current frame
            dispose();
            // Open the choose page frame
            choose choosePage = new choose();
           
            choosePage.setVisible(true);
        }
    });
  
    buttonPanel.add(choosePageButton);

    // Add panels to the frame
    add(gamePanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(null);

    // Set the size of the frame
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int width = 550;
    int height = 520;
    setSize(new Dimension(width, height));

    setVisible(true);
}

    
      // Check if the game board is full
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
    
    // Check if the current player has won the game
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
    
     // Make a move on the board
    private void makeMove(int row, int col, char symbol) {
        board[row][col] = symbol;
        buttons[row][col].setText(Character.toString(symbol));
        buttons[row][col].setEnabled(false);
    }
    
    // Computer's turn
    private void computerTurn() {
        // Generate random moves until a valid move is found
        while (true) {
            int row = (int) (Math.random() * 3);
            int col = (int) (Math.random() * 3);
            if (isValidMove(row, col)) {
                makeMove(row, col, computerSymbol);
                break;
            }
        }
        
    }
    
    // Check if a move is valid
    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == '-';
    }

   

    // Button click listener
    private class ButtonClickListener implements ActionListener {
        private int row, col;
        
        public ButtonClickListener(int row, int col) {
            this.row =row;
            this.col = col;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isValidMove(row, col)) {
                makeMove(row, col, playerSymbol);
                if (checkWin(playerSymbol)) {
                    JOptionPane.showMessageDialog(null, "Congratulations! You win!");
                    endGame();
                    return;
                }
                if (isBoardFull()) {
                    JOptionPane.showMessageDialog(null, "It's a tie!");
                    endGame();
                    return;
                }
                computerTurn();
                if (checkWin(computerSymbol)) {
                    JOptionPane.showMessageDialog(null, "Computer wins! Better luck next time!");
                    endGame();
                    return;
                }
                if (isBoardFull()) {
                    JOptionPane.showMessageDialog(null, "It's a tie!");
                    endGame();
                    return;
                }
            }
        }
    }
    
    // Replay button click listener
    private class ReplayButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    }
    
    // End the game
    private void endGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
        replayButton.setEnabled(true);
    }
    
    // Reset the game
    private void resetGame() {
        initializeBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        replayButton.setEnabled(false);
    }
    
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(150, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(153, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(withpc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(withpc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(withpc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(withpc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new withpc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
