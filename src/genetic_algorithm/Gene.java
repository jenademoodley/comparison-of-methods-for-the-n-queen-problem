package genetic_algorithm;

import java.util.HashSet;
import java.util.Random;

public class Gene {

	private int[] queens;
	private int numberOfQueens;
	private int fitness;
	private Random random;

	public Gene() {

	}

	public Gene(Gene c) {
		this.queens = c.queens.clone();
		this.numberOfQueens = c.numberOfQueens;

		computeFitness();

	}

	public Gene(int numberOfQueens, Random random) {
		this.numberOfQueens = numberOfQueens;
		this.random = random;
		queens = new int[numberOfQueens];
		randomQueens();
		computeFitness();
	}

	public Gene(int[] queens, Random random) {
		this.queens = queens;
		this.numberOfQueens = queens.length;
		this.random = random;
		computeFitness();
	}

	public int[] getQueens() {
		return queens;
	}

	public void setQueens(int[] queens) {
		this.queens = queens;
		computeFitness();
	}

	public int getNumberOfQueens() {
		return numberOfQueens;
	}

	public void setNumberOfQueens(int numberOfQueens) {
		this.numberOfQueens = numberOfQueens;
	}

	public int getFitness() {

		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	public void randomQueens() {
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < queens.length; i++) {
			int index = random.nextInt(numberOfQueens);

			while (set.contains(index))
				index = random.nextInt(numberOfQueens);

			set.add(index);
			queens[i] = index;
		}
	}

	public void computeFitness() {
		fitness = 0;
		int[][] board = new int[numberOfQueens][numberOfQueens];

		for (int i = 0; i < queens.length; i++)
			board[i][queens[i]] = 1;

		int numberOfQueens = 0;

		for (int i = (board.length - 1); i > 0; i--) {
			int index = i;
			for (int j = 0; j < (board.length - i); j++) {
				if (board[index][j] == 1) {
					numberOfQueens++;
					if (numberOfQueens > 1)
						fitness++;
				}
				index++;
			}
			numberOfQueens = 0;
		}

		numberOfQueens = 0;
		for (int i = (board.length - 1); i > 0; i--) {
			int index = i;
			for (int j = 0; j < (board.length - i); j++) {

				if (board[j][index] == 1) {
					numberOfQueens++;
					if (numberOfQueens > 1)
						fitness++;
				}
				index++;
			}
			numberOfQueens = 0;
		}

		numberOfQueens = 0;

		for (int i = 0; i < board.length; i++) {
			if (board[i][i] == 1) {
				numberOfQueens++;
				if (numberOfQueens > 1)
					fitness++;
			}
		}

		// ////////////////////////////////////////////////////////////////
		numberOfQueens = 0;
		for (int i = (board.length - 1); i > 0; i--) {
			int index = i;

			for (int j = (board.length - 1); j >= i; j--) {

				if (board[index][j] == 1) {
					numberOfQueens++;
					if (numberOfQueens > 1)
						fitness++;
				}
				index++;
			}
			numberOfQueens = 0;
		}

		numberOfQueens = 0;
		for (int i = 0; i <= (board.length - 2); i++) {
			int index = i;
			for (int j = 0; j <= i; j++) {

				if (board[index][j] == 1) {
					numberOfQueens++;
					if (numberOfQueens > 1)
						fitness++;
				}
				index--;
			}
			numberOfQueens = 0;
		}

		numberOfQueens = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i][board.length - 1 - i] == 1) {
				numberOfQueens++;
				if (numberOfQueens > 1)
					fitness++;

			}
		}
		numberOfQueens = 0;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == 1) {
					numberOfQueens++;
					if (numberOfQueens > 1)
						fitness++;
				}
			}
			numberOfQueens = 0;
		}

		numberOfQueens = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[j][i] == 1) {
					numberOfQueens++;
					if (numberOfQueens > 1)
						fitness++;
				}
			}
			numberOfQueens = 0;
		}
	}

	public String toString() {
		String temp = "";
		for (int i = 0; i < queens.length; i++)
			temp += queens[i] + " ";
		return temp;
	}

}
