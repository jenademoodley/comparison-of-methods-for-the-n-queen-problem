package hill_climbing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HillClimbing {

	private int TOTAL_QUEENS = 5;
	private int[][] board;
	private int[] queenPositions;

	public HillClimbing(int queens) {
		this.TOTAL_QUEENS = queens;
	}

	public void run() {
		boolean climb = true;
		int climbCount = 0;
		long start = System.currentTimeMillis();
		long end = 0;
		// 5 restarts
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs/HillClimbingOutput";
		int t = 1;
		try {

			File f = new File(FILENAME + t + ".txt");

			// if file doesnt exists, then create it

			while (f.exists()) {
				t++;
				f = new File(FILENAME + t + ".txt");
			}
			f.createNewFile();

			// true = append file
			fw = new FileWriter(f.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			while (climb) {

				/*
				 * HillClimbing board = new HillClimbing( new
				 * int[TOTAL_QUEENS][TOTAL_QUEENS], new int[TOTAL_QUEENS]);
				 */

				int[][] board = new int[TOTAL_QUEENS][TOTAL_QUEENS];
				int[] positions = new int[TOTAL_QUEENS];

				setBoard(board);
				setPositions(positions);

				// randomly place queens
				placeQueens();
				System.out.println("Trial #: " + (climbCount + 1));
				System.out.println("Original board:");
				printBoard();
				System.out.println("# pairs of queens attacking each other: "
						+ h() + "\n");

				// score to be compared against
				int localMin = h();
				boolean best = false;
				// array to store best queen positions by row (array index is
				// column)
				int[] bestQueenPositions = new int[TOTAL_QUEENS];

				// iterate through each column
				for (int j = 0; j < board.length; j++) {
					System.out.println("Iterating through COLUMN " + j + ":");
					best = false;
					// iterate through each row
					for (int i = 0; i < board.length; i++) {

						// skip score calculated by original board
						if (i != queenPositions[j]) {

							// move queen
							moveQueen(i, j);
							printBoard();
							System.out.println();
							// calculate score, if best seen then store queen
							// position
							if (h() < localMin) {
								best = true;
								localMin = h();
								bestQueenPositions[j] = i;
							}
							// reset to original queen position
							resetQueen(i, j);

						}
					}

					// change 2 back to 1
					resetBoard(j);
					if (best) {
						// if a best score was found, place queen in this
						// position
						placeBestQueen(j, bestQueenPositions[j]);
						System.out.println("Best board found this iteration: ");
						finalBoard();
						bw.write("\nSolution: ");
						for (int i = 0; i < board.length; i++) {
							for (int k = 0; k < board[i].length; k++) {
								if (board[i][k] == 1)
									bw.write("(" + i + "," + k + ") ");
							}
						}
						bw.newLine();
						System.out
								.println("# pairs of queens attacking each other: "
										+ h() + "\n");
						bw.write("# pairs of queens attacking each other: "
								+ h());
						bw.newLine();
					} else {

						System.out.println("No better board found.");
						finalBoard();
						bw.write("\nSolution: ");
						for (int i = 0; i < board.length; i++) {
							for (int k = 0; k < board[i].length; k++) {
								if (board[i][k] == 1)
									bw.write("(" + i + "," + k + ") ");
							}
						}
						bw.newLine();
						System.out
								.println("# pairs of queens attacking each other: "
										+ h() + "\n");
						bw.write("# pairs of queens attacking each other: "
								+ h());
						bw.newLine();
						bw.newLine();
					}
				}

				// if score = 0, hill climbing has solved problem
				if (h() == 0)
					climb = false;

				climbCount++;

				// only 5 restarts
				if (climbCount == 6) {
					climb = false;
				}

				end = System.currentTimeMillis();
				double run = ((end * 1.0) - start) / 1000;
				System.out
						.println("Done in " + (climbCount - 1) + " restarts.");
				System.out.println("Run Time: " + run + " seconds");
				bw.write("Done in " + (climbCount - 1) + " restarts.");
				bw.newLine();
				bw.write("Run Time: " + run + " seconds");
				bw.newLine();
				bw.write("Queens: " + TOTAL_QUEENS);
			}
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}

	}

	public void setBoard(int[][] board) {
		this.board = board;
	}

	public void setPositions(int[] positions) {
		this.queenPositions = positions;
	}

	private int[] generateQueens() {

		List<Integer> randomPos = new ArrayList<Integer>();

		Random r = new Random();
		for (int i = 0; i < TOTAL_QUEENS; i++) {
			randomPos.add(r.nextInt(TOTAL_QUEENS));
		}

		int[] randomPositions = new int[TOTAL_QUEENS];

		for (int i = 0; i < randomPos.size(); i++) {
			randomPositions[i] = randomPos.get(i);
		}

		return randomPositions;
	}

	public void placeQueens() {

		queenPositions = generateQueens();

		for (int i = 0; i < board.length; i++) {
			board[queenPositions[i]][i] = 1;
		}

	}

	public int h() {

		int totalPairs = 0;

		// checking rows
		for (int i = 0; i < board.length; i++) {
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j] == 1) {
					pairs.add(true);
				}

			}
			if (pairs.size() != 0)
				totalPairs = totalPairs + (pairs.size() - 1);
		}

		// check diagonal from top left
		int rows = board.length;
		int cols = board.length;
		int maxSum = rows + cols - 2;

		for (int sum = 0; sum <= maxSum; sum++) {
			ArrayList<Boolean> pairs = new ArrayList<Boolean>();
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (i + j - sum == 0) {
						if (board[i][j] == 1) {
							pairs.add(true);
						}
					}
				}

			}
			if (pairs.size() != 0)
				totalPairs = totalPairs + (pairs.size() - 1);
		}

		// check mirrored diagonal. couldn't figure out algorithm so solved
		// brute force.
		int pairs = checkMirrorDiagonal();

		return totalPairs + pairs;
	}

	private int checkMirrorDiagonal() {

		ArrayList<ArrayList<Integer>> array = new ArrayList<>();

		int counter = 1;

		for (int i = 1; i < board.length; i++) {
			ArrayList<Integer> arr = new ArrayList<>();
			int row = i;

			while (row < board.length) {
				arr.add(board[row][row - counter]);
				row++;
			}
			counter++;
			array.add(arr);
		}

		counter = 1;

		for (int i = 1; i < board.length; i++) {
			ArrayList<Integer> arr = new ArrayList<>();
			int col = i;

			while (col < board.length) {
				arr.add(board[col - counter][col]);
				col++;
			}
			counter++;
			array.add(arr);
		}

		ArrayList<Integer> arr = new ArrayList<>();
		for (int i = 0; i < board.length; i++) {
			arr.add(board[i][i]);
		}

		array.add(arr);

		int totalPairs = 0;

		for (int i = 0; i < array.size(); i++)
			totalPairs += checkMirrorHorizontal(array.get(i));

		return totalPairs;

	}

	public void moveQueen(int row, int col) {

		// original queen will become a 2 and act as a marker
		board[queenPositions[col]][col] = 2;

		board[row][col] = 1;

	}

	private int checkMirrorHorizontal(ArrayList<Integer> b) {

		int totalPairs = 0;

		ArrayList<Boolean> pairs = new ArrayList<Boolean>();
		for (int i = 0; i < b.size(); i++) {
			if (b.get(i) == 1)
				pairs.add(true);

		}

		if (pairs.size() != 0)
			totalPairs = (pairs.size() - 1);

		return totalPairs;
	}

	public void resetQueen(int row, int col) {

		if (board[row][col] == 1)
			board[row][col] = 0;
	}

	public void resetBoard(int col) {

		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == 2)
				board[i][col] = 1;
		}
	}

	public void placeBestQueen(int col, int queenPos) {

		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == 1)
				board[i][col] = 2;

		}
		board[queenPos][col] = 1;
		for (int i = 0; i < board.length; i++) {
			if (board[i][col] == 2)
				board[i][col] = 0;

		}
	}

	public void finalBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1)
					System.out.print("Q ");
				else
					System.out.print(". ");
			}
			System.out.println();
		}

		System.out.print("\nSolution: ");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1)
					System.out.print("(" + i + "," + j + ") ");
			}
		}
		System.out.println();
	}

	public void printBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

}
