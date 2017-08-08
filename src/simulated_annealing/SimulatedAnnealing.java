package simulated_annealing;

public class SimulatedAnnealing extends NQueen {

	private double temperature;
	private int maxIteration;

	public SimulatedAnnealing(int boardSize, int tollerence,
			double temperature, int maxIteration) {
		super(boardSize, tollerence);
		this.temperature = temperature;
		this.maxIteration = maxIteration;
		currentState = new SimulatedAnnealingState(boardSize);
	}

	@Override
	public void solve() {
		count = 0;
		while (!isSolvedPossition(currentState) && count < maxIteration) {
			count++;
			if (count % 5 == 0) {
				System.out.println("Iteration: " + count);
				showSolution();
			}
			double temperature;
			double delta;
			double probability;
			double rand;

			for (temperature = this.temperature; (temperature > 0)
					&& (currentState.getCost() != 0); temperature--) {
				nextState = currentState.getNextState();
				delta = currentState.getCost() - nextState.getCost();
				probability = Math.exp(delta / temperature);
				rand = Math.random();

				if (delta > 0) {
					currentState = nextState;
				} else if (rand <= probability) {
					currentState = nextState;
				}
			}
		}
	}
}
