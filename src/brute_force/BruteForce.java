package brute_force;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Given nxn board place n queen on this board so that they dont attack each
 * other. One solution is to find any placement of queens which do not attack
 * each other. Other solution is to find all placements of queen on the board.
 *
 * Time complexity O(n*n) Space complexity O(n*n)
 */
public class BruteForce {

	private int queens;

	public BruteForce(int queens) {
		this.queens = queens;
	}

	public void run() {
		long start = System.currentTimeMillis();
		Position[] positions = solveNQueenOneSolution(queens);
		long end = System.currentTimeMillis();
		double run = ((end * 1.0) - start) / 1000;
		for (int i = 0; i < positions.length; i++) {
			for (int j = 0; j < positions.length; j++) {
				if (positions[i].getRow() == i && positions[i].getCol() == j)
					System.out.print("Q ");
				else
					System.out.print(". ");
			}
			System.out.println();
		}
		System.out.print("\nSolution: ");
		for (int i = 0; i < positions.length; i++) {
			System.out.print("(" + positions[i].getRow() + " "
					+ positions[i].getCol() + ") ");
		}
		System.out.println("\nRun Time: " + run + " seconds");
	}

	public Position[] solveNQueenOneSolution(int n) {
		Position[] positions = new Position[n];
		boolean hasSolution = solveNQueenOneSolutionUtil(n, 0, positions);
		if (hasSolution) {
			return positions;
		} else {
			return new Position[0];
		}
	}

	private boolean solveNQueenOneSolutionUtil(int n, int row,
			Position[] positions) {
		if (n == row) {
			return true;
		}
		int col;
		for (col = 0; col < n; col++) {
			boolean foundSafe = true;

			// check if this row and col is not under attack from any previous
			// queen.
			for (int queen = 0; queen < row; queen++) {
				if (positions[queen].getCol() == col
						|| positions[queen].getRow()
								- positions[queen].getCol() == row - col
						|| positions[queen].getRow()
								+ positions[queen].getCol() == row + col) {
					foundSafe = false;
					break;
				}
			}
			if (foundSafe) {
				positions[row] = new Position(row, col);
				if (solveNQueenOneSolutionUtil(n, row + 1, positions)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * Solution for https://leetcode.com/problems/n-queens/
	 */
	public List<List<String>> solveNQueens(int n) {
		List<List<String>> result = new ArrayList<>();
		Position[] positions = new Position[n];
		solve(0, positions, result, n);
		return result;
	}

	public void solve(int current, Position[] positions,
			List<List<String>> result, int n) {
		if (n == current) {
			StringBuffer buff = new StringBuffer();
			List<String> oneResult = new ArrayList<>();
			for (Position p : positions) {
				for (int i = 0; i < n; i++) {
					if (p.getCol() == i) {
						buff.append("Q");
					} else {
						buff.append(".");
					}
				}
				oneResult.add(buff.toString());
				buff = new StringBuffer();

			}
			result.add(oneResult);
			return;
		}

		for (int i = 0; i < n; i++) {
			boolean foundSafe = true;
			for (int j = 0; j < current; j++) {
				if (positions[j].getCol() == i
						|| positions[j].getCol() - positions[j].getRow() == i
								- current
						|| positions[j].getRow() + positions[j].getCol() == i
								+ current) {
					foundSafe = false;
					break;
				}
			}
			if (foundSafe) {
				positions[current] = new Position(current, i);
				solve(current + 1, positions, result, n);
			}
		}
	}

}
