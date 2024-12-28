import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToeAI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private JLabel statusLabel;
    private boolean againstAI = true;

    public TicTacToeAI() {
        // Set up the main frame
        setTitle("Tic Tac Toe (vs AI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLayout(new BorderLayout());

        // Create the game board
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        Font buttonFont = new Font("Arial", Font.BOLD, 60);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(buttonFont);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }

        // Create the status label
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Add components to the frame
        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Inner class to handle button clicks
    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!buttons[row][col].getText().equals("") || (againstAI && currentPlayer == 'O')) {
                return;
            }

            buttons[row][col].setText(String.valueOf(currentPlayer));
            buttons[row][col].setEnabled(false);

            if (checkWinner(currentPlayer)) {
                statusLabel.setText("Player " + currentPlayer + " wins!");
                disableAllButtons();
                return;
            }

            if (isBoardFull()) {
                statusLabel.setText("It's a tie!");
                return;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText("Player " + currentPlayer + "'s turn");

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
                // Try to win or block
                if (!tryToWinOrBlock('O') && !tryToWinOrBlock('X')) {
                    makeRandomMove();
                }

                if (checkWinner('O')) {
                    statusLabel.setText("Player O (AI) wins!");
                    disableAllButtons();
                    return;
                }

                if (isBoardFull()) {
                    statusLabel.setText("It's a tie!");
                    return;
                }

                currentPlayer = 'X';
                statusLabel.setText("Player X's turn");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Function to try to win or block the opponent
    private boolean tryToWinOrBlock(char player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(String.valueOf(player));
                    if (checkWinner(player)) {
                        if (player == 'O') {
                            return true; // AI wins
                        }
                        buttons[i][j].setText(""); // Reset and block
                        buttons[i][j].setEnabled(true);
                        buttons[i][j].setText("O"); // Block opponent
                        buttons[i][j].setEnabled(false);
                        return true;
                    }
                    buttons[i][j].setText("");
                }
            }
        }
        return false;
    }

    // Function to make a random move
    private void makeRandomMove() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));

        buttons[row][col].setText("O");
        buttons[row][col].setEnabled(false);
    }

    // Function to check if the current player has won
    private boolean checkWinner(char player) {
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
        return false;
    }

    // Function to check if the board is full
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    // Function to disable all buttons
    private void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        new TicTacToeAI();
    }
}
