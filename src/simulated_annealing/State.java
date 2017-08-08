package simulated_annealing;

abstract class State {

	int boardSize;
	int cost;
	protected Queen q[];

	public State(int boardSize) {
		// int i;
		this.boardSize = boardSize;
		q = new Queen[boardSize];

		cost = 0;
	}

	public State(int boardSize, Queen q[]) {
		this.boardSize = boardSize;
		this.q = q;
		cost = 0;
	}

	abstract public State getNextState();

	public void calculateCost() {

		int fitness = 0;
		int[][] board = new int[boardSize][boardSize];

		for (int i = 0; i < q.length; i++)
			board[q[i].indexOfX][q[i].indexOfY] = 1;

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
		// this is due to double counting
		cost = fitness;

	}

	public int getCost() {
		calculateCost();
		return cost;
	}

	public Queen[] getQueens() {
		return q;
	}
}
