import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.util.*;
 
public class MazeV3 {
 
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
                    } 
                    else {
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

            board[(ROWS / 2)][0] = 0;
            colorboard[(ROWS / 2)][0] = 0;
            valueboard[(ROWS / 2)][0] = "▒";
            board[(ROWS / 2)][ACTUAL_COLS] = 0;
            colorboard[(ROWS / 2)][ACTUAL_COLS] = 0;
            valueboard[(ROWS / 2)][ACTUAL_COLS] = "░";
	}
	
	public void printBoard() {
            for (int x = 0; x < ROWS; x++) {
                for (int y = 0; y < COLS; y++) {
                    System.out.print(" " + valueboard[x][y]);
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
                    else {
                        checker = Color.red;
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
            Stack<int[]> currentCheck = new Stack();
            Stack<int[]> nextCheck = new Stack();
            int incr = 2;
            colorboard[ROWS/2][0] = 2;
            int[] start = {(ROWS/2), 0};
            currentCheck.push(start);
            
            while (!currentCheck.isEmpty()) {   
                incr++;
                while (!currentCheck.isEmpty()) {
                    int[] current = currentCheck.pop();  
                    lookAround(current, nextCheck, incr);
                }
                currentCheck = (Stack<int[]>) nextCheck.clone();
                nextCheck.clear();
            }
            currentCheck.clear();
            nextCheck.clear();
            int[] backStart = {(ROWS/2), (COLS-1)};
            currentCheck.push(backStart);
            
            for (int i = 2; i <= incr; i++) {
                while (!currentCheck.isEmpty()) {
                    int[] current = currentCheck.pop();
                    goBack(current, nextCheck);
                }
                
                currentCheck = (Stack<int[]>) nextCheck.clone();
                nextCheck.clear();
            }
        }
        
        private void lookAround(int[] current, Stack nextCheck, int incr) {
            int rowCord = current[0];
            int colCord = current[1];
            
            int left = 1;
            int right = 1;
            int[] leftCord = new int[2];
            int[] rightCord = new int[2];
            
            int up = colorboard[rowCord - 1][colCord];
            int[] upCord = {(rowCord - 1), colCord};
            
            int down = colorboard[rowCord + 1][colCord];
            int[] downCord = {(rowCord + 1), colCord};
            
            if (colCord != 0) {
                left = colorboard[rowCord][colCord - 1];
                leftCord[0] = rowCord;
                leftCord[1] = colCord - 1;
            }
            if (colCord != ACTUAL_COLS) {
                right = colorboard[rowCord][colCord + 1];
                rightCord[0] = rowCord;
                rightCord[1] = colCord + 1;
            }
            
            if (up == 0) {
                nextCheck.push(upCord);
                colorboard[rowCord - 1][colCord] = incr;
            }
            if (down == 0) {
                nextCheck.push(downCord);
                colorboard[rowCord + 1][colCord] = incr;
            }
            if (left == 0) {
                nextCheck.push(leftCord);
                colorboard[rowCord][colCord - 1] = incr;
            }
            if (right == 0) {
                nextCheck.push(rightCord);
                colorboard[rowCord][colCord + 1] = incr;
            }
        }
        
        public void goBack(int[] current, Stack nextCheck) {
            int rowCord = current[0];
            int colCord = current[1];
            int currentValue = colorboard[rowCord][colCord];
            board[rowCord][colCord] = -1;
            if (colCord == 0) {
                return;
            }
            int left = 1;
            int right = 1;
            int[] leftCord = new int[2];
            int[] rightCord = new int[2];

            int up = colorboard[rowCord - 1][colCord];
            int[] upCord = {(rowCord - 1), colCord};

            int down = colorboard[rowCord + 1][colCord];
            int[] downCord = {(rowCord + 1), colCord};

            if (colCord != 0) {
                left = colorboard[rowCord][colCord - 1];
                leftCord[0] = rowCord;
                leftCord[1] = colCord - 1;
            }
            if (colCord != ACTUAL_COLS) {
                right = colorboard[rowCord][colCord + 1];
                rightCord[0] = rowCord;
                rightCord[1] = colCord + 1;
            }

            if (up == currentValue - 1) {
                nextCheck.push(upCord);
            }
            else if (down == currentValue - 1) {
                nextCheck.push(downCord);
            }
            else if (left == currentValue - 1) {
                nextCheck.push(leftCord);
            }
            else if (right == currentValue - 1) {
                nextCheck.push(rightCord);
            }
        }
        
        
	public static void main(String[] args) {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            MazeV3 maze = new MazeV3();
            maze.generate();
            maze.pathfind();
            //maze.printBoard();
            maze.draw();
	}
}
