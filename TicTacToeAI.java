import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * A simple Tic Tac Toe game with an AI opponent using Java Swing for GUI.
 * Players can play against an AI, which tries to win, block, or make random moves.
 */
public class TicTacToeAI extends JFrame {
    private JButton[][] buttons = new JButton[3][3]; // 3x3 grid for the game board
    private char currentPlayer = 'X'; // Keeps track of the current player ('X' or 'O')
    private JLabel statusLabel; // Label to show the game's current status
    private boolean againstAI = true; // Flag to indicate if the game is Player vs AI

    // Constructor: Initializes the game UI
    public TicTacToeAI() {
        // Set up the main frame
        setTitle("Tic Tac Toe (vs AI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());

        // Create the game board as a 3x3 grid
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        Font buttonFont = new Font("Arial", Font.BOLD, 60);

        // Initialize the buttons for the grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(""); // Empty button initially
                buttons[i][j].setFont(buttonFont);
                buttons[i][j].setFocusPainted(false); // Remove focus border
                buttons[i][j].addActionListener(new ButtonClickListener(i, j)); // Add action listener
                boardPanel.add(buttons[i][j]);
            }
        }

        // Create the status label to display whose turn it is
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Add the board and status label to the main frame
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true); // Display the frame
    }

    // Inner class to handle button clicks
    private class ButtonClickListener implements ActionListener {
        private int row; // Row index of the clicked button
        private int col; // Column index of the clicked button

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Ignore clicks on already-filled buttons or when it's AI's turn
            if (!buttons[row][col].getText().equals("") || (againstAI && currentPlayer == 'O')) {
                return;
            }

            // Mark the button with the current player's symbol
            buttons[row][col].setText(String.valueOf(currentPlayer));
            buttons[row][col].setEnabled(false);

            // Check if the current player has won
            if (checkWinner(currentPlayer)) {
                statusLabel.setText("Player " + currentPlayer + " wins!");
                disableAllButtons(); // Disable all buttons when the game ends
                return;
            }

            // Check if the board is full (tie)
            if (isBoardFull()) {
                statusLabel.setText("It's a tie!");
                return;
            }

            // Switch to the other player
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText("Player " + currentPlayer + "'s turn");

            // If playing against AI and it's AI's turn, make an AI move
            if (againstAI && currentPlayer == 'O') {
                aiMove();
            }
        }
    }

    // AI logic for making a move
    private void aiMove() {
        // Delay AI's move for better user experience
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Try to win first, then block the player, or make a random move
                if (!tryToWinOrBlock('O') && !tryToWinOrBlock('X')) {
                    makeRandomMove();
                }

                // Check if AI won after making a move
                if (checkWinner('O')) {
                    statusLabel.setText("Player O (AI) wins!");
                    disableAllButtons();
                    return;
                }

                // Check if the board is full (tie)
                if (isBoardFull()) {
                    statusLabel.setText("It's a tie!");
                    return;
                }

                // Switch back to Player X
                currentPlayer = 'X';
                statusLabel.setText("Player X's turn");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Try to win or block the opponent
    private boolean tryToWinOrBlock(char player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    // Simulate placing the player's symbol
                    buttons[i][j].setText(String.valueOf(player));
                    if (checkWinner(player)) {
                        if (player == 'O') {
                            return true; // AI wins
                        }
                        // If blocking the player, revert the move and block
                        buttons[i][j].setText("");
                        buttons[i][j].setEnabled(true);
                        buttons[i][j].setText("O");
                        buttons[i][j].setEnabled(false);
                        return true;
                    }
                    buttons[i][j].setText(""); // Revert move
                }
            }
        }
        return false; // No winning or blocking move found
    }

    // Make a random move for AI
    private void makeRandomMove() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().equals("")); // Ensure the move is on an empty cell

        buttons[row][col].setText("O");
        buttons[row][col].setEnabled(false);
    }

    // Check if a player has won
    private boolean checkWinner(char player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(String.valueOf(player)) &&
                buttons[i][1].getText().equals(String.valueOf(player)) &&
                buttons[i][2].getText().equals(String.valueOf(player))) {
                return true;
            }
            if (buttons[0][i].getText().equals(String.valueOf(player)) &&
                buttons[1][i].getText().equals(String.valueOf(player)) &&
                buttons[2][i].getText().equals(String.valueOf(player))) {
                return true;
            }
        }

        // Check diagonals
        if (buttons[0][0].getText().equals(String.valueOf(player)) &&
            buttons[1][1].getText().equals(String.valueOf(player)) &&
            buttons[2][2].getText().equals(String.valueOf(player))) {
            return true;
        }
        if (buttons[0][2].getText().equals(String.valueOf(player)) &&
            buttons[1][1].getText().equals(String.valueOf(player)) &&
            buttons[2][0].getText().equals(String.valueOf(player))) {
            return true;
        }

        return false; // No winner found
    }

    // Check if the board is full (tie condition)
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false; // Empty cell found
                }
            }
        }
        return true; // No empty cells
    }

    // Disable all buttons after the game ends
    private void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        new TicTacToeAI(); // Create and display the game
    }
}
