import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 750;

    JFrame frame = new JFrame("🔥 Tic-Tac-Toe 🔥");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel historyPanel = new JPanel();
    JTextArea historyArea = new JTextArea();

    JButton[][] board = new JButton[3][3];
    String playerP = "P";
    String playerG = "G";
    String nameP, nameG;
    String currentPlayer = playerP;

    boolean gameOver = false;
    int turns = 0;
    int scoreP = 0;
    int scoreG = 0;
    ArrayList<String> history = new ArrayList<>();

    TicTacToe() {
        nameP = JOptionPane.showInputDialog("Enter name for Player P:");
        nameG = JOptionPane.showInputDialog("Enter name for Player G:");

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.black);
        textLabel.setForeground(Color.red);
        textLabel.setFont(new Font("Impact", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("🔥 " + nameP + " vs " + nameG + " 🔥");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(Color.red);
        restartButton.setForeground(Color.white);
        restartButton.addActionListener(e -> resetGame());
        textPanel.add(restartButton, BorderLayout.EAST);

        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.black);
        frame.add(boardPanel, BorderLayout.CENTER);

        historyPanel.setLayout(new BorderLayout());
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyArea.setBackground(Color.black);
        historyArea.setForeground(Color.orange);
        historyPanel.add(new JLabel("Game History:"), BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        frame.add(historyPanel, BorderLayout.SOUTH);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.black);
                tile.setForeground(Color.orange);
                tile.setFont(new Font("Impact", Font.BOLD, 120));
                tile.setFocusable(false);
                tile.setBorder(BorderFactory.createLineBorder(Color.red, 3));

                tile.addActionListener(e -> {
                    if (gameOver) return;
                    JButton clicked = (JButton) e.getSource();
                    if (clicked.getText().equals("")) {
                        clicked.setText(currentPlayer);
                        playSound("click.wav");
                        turns++;
                        checkWinner();
                        if (!gameOver) {
                            currentPlayer = currentPlayer.equals(playerP) ? playerG : playerP;
                            textLabel.setText((currentPlayer.equals(playerP) ? nameP : nameG) + "'s turn 🔥");
                        }
                    }
                });

                tile.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (tile.getText().equals("") && !gameOver) tile.setBackground(Color.darkGray);
                    }
                    public void mouseExited(MouseEvent e) {
                        if (tile.getText().equals("") && !gameOver) tile.setBackground(Color.black);
                    }
                });
            }
        }
    }

    void checkWinner() {
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().equals("")) continue;
            if (board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                for (int i = 0; i < 3; i++) setWinner(board[r][i]);
                gameOver = true;
                return;
            }
        }

        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().equals("")) continue;
            if (board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                for (int i = 0; i < 3; i++) setWinner(board[i][c]);
                gameOver = true;
                return;
            }
        }

        if (!board[0][0].getText().equals("") &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            setWinner(board[0][0]); setWinner(board[1][1]); setWinner(board[2][2]);
            gameOver = true;
            return;
        }

        if (!board[0][2].getText().equals("") &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            setWinner(board[0][2]); setWinner(board[1][1]); setWinner(board[2][0]);
            gameOver = true;
            return;
        }

        if (turns == 9) {
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    setTie(board[r][c]);
            gameOver = true;
        }
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.red);
        String winnerName = currentPlayer.equals(playerP) ? nameP : nameG;
        textLabel.setText(winnerName + " wins! 🔥");
        playSound("win.wav");
        history.add(winnerName + " won");
        updateHistory();
        if (currentPlayer.equals(playerP)) scoreP++;
        else scoreG++;
    }

    void setTie(JButton tile) {
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        textLabel.setText("It's a tie! 🤝");
        playSound("tie.wav");
        history.add("Tie");
        updateHistory();
    }

    void resetGame() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(Color.black);
                board[r][c].setForeground(Color.orange);
            }
        currentPlayer = playerP;
        textLabel.setText("🔥 " + nameP + " vs " + nameG + " 🔥 | Score: " + nameP + " " + scoreP + " - " + scoreG + " " + nameG);
        gameOver = false;
        turns = 0;
    }

    void updateHistory() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            sb.append("Game ").append(i + 1).append(": ").append(history.get(i)).append("\n");
        }
        historyArea.setText(sb.toString());
    }

    void playSound(String filename) {
        try {
            File soundFile = new File(filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new TicTacToe();
    }
}
