import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.util.*;
 
public class MazeInitial {
        
        // set board size here
	private static final int ROWS = 150;
	private static final int COLS = 150;
        
	private static final int ACTUAL_ROWS = ROWS - 1;
	private static final int ACTUAL_COLS = COLS - 1;
	private static final double PERCENTAGE = 0.7;
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private int[][] board = new int[ROWS][COLS];
	private String[][] valueboard = new String[ROWS][COLS];
	private int[][] colorboard = new int[ROWS][COLS];
 
	private Random rand = new Random();

 
	public void generate() {
            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {
                    double randomnumber = rand.nextDouble();
                    if (randomnumber < PERCENTAGE) {
                        board[x][y] = 0;
                        colorboard[x][y] = 0;
                        valueboard[x][y] = "░";
                    } else {
                        board[x][y] = 1;
                        colorboard[x][y] = 1;
                        valueboard[x][y] = "▓";
                    }
                }
            }

            for (int x = 0; x < ROWS; x++) {
                // top
                board[0][x] = 1;
                colorboard[0][x] = 1;
                valueboard[0][x] = "▓";
                // bottom
                board[ACTUAL_ROWS][x] = 1;
                colorboard[ACTUAL_ROWS][x] = 1;
                valueboard[ACTUAL_ROWS][x] = "▓";
                // left
                board[x][0] = 1;
                colorboard[x][0] = 1;
                valueboard[x][0] = "▓";
                // right
                board[x][ACTUAL_COLS] = 1;
                colorboard[x][ACTUAL_COLS] = 1;
                valueboard[x][ACTUAL_COLS] = "▓";
            }

            board[(ROWS / 2)][0] = 3;
            board[(ROWS / 2)][1] = 0;   // start
            colorboard[(ROWS / 2)][0] = 0;
            colorboard[(ROWS / 2)][1] = 0;
            valueboard[(ROWS / 2)][0] = "▒";
            valueboard[(ROWS / 2)][1] = "▒";
            board[(ROWS / 2)][ACTUAL_COLS] = 0;
            board[(ROWS / 2)][ACTUAL_COLS - 1] = 0; // for simplicity
            colorboard[(ROWS / 2)][ACTUAL_COLS] = 0;
            colorboard[(ROWS / 2)][ACTUAL_COLS - 1] = 0;
            valueboard[(ROWS / 2)][ACTUAL_COLS] = "░";
            valueboard[(ROWS / 2)][ACTUAL_COLS - 1] = "░";
	}
	
	public void printBoard() {
            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {
                    System.out.print(" " + board[x][y]);
                }
                System.out.println();
            }
            System.out.println("");
            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {
                    int h = colorboard[x][y];
                    String padded = String.format("%02d", h);
                    System.out.print(" " + padded);
                }
                System.out.println();
            }
	}
 
	public void draw() {
            JFrame GameBoard = new JFrame();
            GameBoard.setSize(400, 400);
            GameBoard.setTitle("Maze");
            GameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Container pane = GameBoard.getContentPane();
            pane.setLayout(new GridLayout(ROWS, COLS));
            Color checker;

            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {
                    if (board[x][y] == 0) {
                        checker = Color.white;
                    } 
                    else if (board[x][y] == 1) {
                        checker = Color.blue;
                    }
                    else if (board[x][y] == -1) {
                        checker = Color.red;
                    }
                    else {
                        checker = Color.white;
                    }

                    JPanel panel = new JPanel();
                    panel.setPreferredSize(new Dimension(WIDTH / ROWS, HEIGHT / COLS));
                    panel.setBackground(checker);
                    pane.add(panel);
                }
            }
            GameBoard.setVisible(true);
	}
 
        public void pathfind() { 
            for (int z = 0; z < (ROWS * COLS); z++) {
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        int val = board[i][j];
                        if (val > 1) {
                            if (i < ROWS - 1) {
                                if (board[i + 1][j] == 0) {
                                    board[i + 1][j] = val + 1;
                                }
                            }
                            if (i >= 1) {
                                if (board[i - 1][j] == 0) {
                                    board[i - 1][j] = val + 1;
                                }
                            }
                            if (j < COLS - 1) {
                                if (board[i][j + 1] == 0) {
                                    board[i][j + 1] = val + 1;
                                }
                            }
                            if (j >= 1) {
                                if (board[i][j - 1] == 0) {
                                    board[i][j - 1] = val + 1;
                                }
                            }
                        }
                    }
                }
            }
            goBack();
        }

        public void goBack() {

            int max = board[(ROWS / 2)][ACTUAL_COLS];
            int currentR = ROWS / 2;
            int currentC = 0; 
            
            if (board[(ROWS / 2)][ACTUAL_COLS] != 0) {
                max = board[(ROWS / 2)][ACTUAL_COLS];
                currentR = ROWS / 2;
                currentC = ACTUAL_COLS;
            }
            else {
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        if (board[i][j] > max) {
                            max = board[i][j];
                            currentR = i;
                            currentC = j;
                        }
                    }
                }
            }
            
            board[currentR][currentC] = -1;

            int currentVal = max;
            for (int i = 0; i < max; i++) {
                Boolean did = false;
                if (currentR >= 1 && did == false) {
                    if (board[currentR - 1][currentC] == currentVal - 1) {
                        board[currentR - 1][currentC] = -1;
                        currentVal--;
                        currentR--;
                        did = true;
                    }
                }
                if (currentC < COLS - 1 && did == false) {
                    if (board[currentR][currentC + 1] == currentVal - 1) {
                        board[currentR][currentC + 1] = -1;
                        currentVal--;
                        currentC++;
                        did = true;
                    }
                }
                if (currentC >= 1 && did == false) {
                    if (board[currentR][currentC - 1] == currentVal - 1) {
                        board[currentR][currentC - 1] = -1;
                        currentVal--;
                        currentC--;
                        did = true;
                }
                }
                if (currentR < ROWS -1 && did == false) {
                    if (board[currentR + 1][currentC] == currentVal - 1) {
                        board[currentR + 1][currentC] = -1;
                        currentVal--;
                        currentR++;
                        did = true;
                    }
                }
            }
        }
        
        
	public static void main(String[] args) {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            MazeInitial maze = new MazeInitial();
            maze.generate();
            maze.pathfind();
            //maze.printBoard();
            maze.draw();
	}
}
