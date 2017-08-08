package particle_swarm_optimization;

public class Particle implements Comparable<Particle> {
	private int MAX_LENGTH;
	private int data[];
	private double velocity; // fitness
	private int conflicts; // pBest

	/*
	 * Instantiate a particle.
	 * 
	 * @param: size of n
	 */
	public Particle(int n) {
		MAX_LENGTH = n;
		data = new int[MAX_LENGTH];
		this.velocity = 0.0;
		this.conflicts = 0;
		initData();
	}

	/*
	 * Compares two particles.
	 * 
	 * @param: a particle to compare with
	 */
	public int compareTo(Particle p) {
		return this.conflicts - p.getConflicts();
	}

	/*
	 * Computes the conflicts in the nxn board.
	 */
	public void computeConflicts() { // compute the number of conflicts to
										// calculate fitness
		int fitness = 0;
		int[][] board = new int[MAX_LENGTH][MAX_LENGTH];

		plotQueens(board);

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

		this.conflicts = fitness;

	}

	/*
	 * Plots the queens in the board.
	 * 
	 * @param: a nxn board
	 */
	public void plotQueens(int[][] board) {
		for (int i = 0; i < MAX_LENGTH; i++) {
			board[i][data[i]] = 1;
		}
	}

	/*
	 * Clears the board.
	 * 
	 * @param: a nxn board
	 */
	public void clearBoard(String[][] board) {
		for (int i = 0; i < MAX_LENGTH; i++) {
			for (int j = 0; j < MAX_LENGTH; j++) {
				board[i][j] = "";
			}
		}
	}

	/*
	 * Initializes the particle into diagonal queens.
	 */
	public void initData() {
		for (int i = 0; i < MAX_LENGTH; i++) {
			data[i] = i;
		}
	}

	/*
	 * Gets the data on a specified index.
	 * 
	 * @param: index of data
	 * 
	 * @return: position of queen
	 */
	public int getData(int index) {
		return this.data[index];
	}

	/*
	 * Sets the data on a specified index.
	 * 
	 * @param: index of data
	 * 
	 * @param: new position of queen
	 */
	public void setData(int index, int value) {
		this.data[index] = value;
	}

	/*
	 * Gets the conflicts of the particle.
	 * 
	 * @return: number of conflicts of the particle
	 */
	public int getConflicts() {
		return this.conflicts;
	}

	/*
	 * Sets the conflicts of the particle.
	 * 
	 * @param: new number of conflicts
	 */
	public void setConflicts(int conflicts) {
		this.conflicts = conflicts;
	}

	/*
	 * Gets the velocity of the particle.
	 * 
	 * @return: velocity of particle
	 */
	public double getVelocity() {
		return this.velocity;
	}

	/*
	 * Sets the velocity of the particle.
	 * 
	 * @param: new velocity of particle
	 */
	public void setVelocity(double velocityScore) {
		this.velocity = velocityScore;
	}

	/*
	 * Gets the max length.
	 * 
	 * @return: max length
	 */
	public int getMaxLength() {
		return MAX_LENGTH;
	}
	// end particle
}
