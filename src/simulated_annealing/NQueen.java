package simulated_annealing;

abstract class NQueen {

	protected int boardSize;
	protected State currentState, nextState;
	protected int tollerenceCost;
	protected int count;

	public NQueen(int boardSize, int tollrence) {
		this.boardSize = boardSize;
		this.tollerenceCost = tollrence;
		if (boardSize < 4)
			throw new UnsupportedOperationException(
					"No of N should be more than 3 to solve the problem");
	}

	abstract public void solve();

	public void showSolution() {
		Queen q[] = currentState.getQueens();

		System.out.print("Solution: ");
		for (int k = 0; k < boardSize; k++)
			System.out.print("(" + q[k].getIndexOfX() + ","
					+ q[k].getIndexOfY() + ") ");

		System.out.println("\nTotal Cost of " + currentState.getCost());
		System.out.println();
	}

	public String show() {
		String temp = "";
		Queen q[] = currentState.getQueens();
		boolean queen = false;

		System.out.println("Board State");
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				for (int k = 0; k < boardSize; k++) {
					if (i == q[k].getIndexOfX() && j == q[k].getIndexOfY()) {
						queen = true;
						// temp = k;
						break;

					}
				}

				if (queen) {
					System.out.print("Q ");
					queen = false;
				} else {
					System.out.print(". ");
				}
			}

			System.out.println();
		}
		System.out.println("Iteration: " + count);
		System.out.print("Solution: ");
		for (int k = 0; k < boardSize; k++) {
			System.out.print("(" + q[k].getIndexOfX() + ","
					+ q[k].getIndexOfY() + ") ");
		}
		System.out.println("\nTotal Cost of " + currentState.getCost());
		temp += "Iteration: " + count + "\n";
		temp += "Solution: ";
		for (int k = 0; k < boardSize; k++) {
			temp += ("(" + q[k].getIndexOfX() + "," + q[k].getIndexOfY() + ") ");
		}

		temp += ("\nTotal Cost of " + currentState.getCost());
		return temp;
	}

	protected boolean isSolvedPossition(State s) {
		if (s.getCost() <= tollerenceCost) {
			return true;
		}
		return false;
	}
}
