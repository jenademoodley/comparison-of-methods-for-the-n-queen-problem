package particle_swarm_optimization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ParticleSwarmOptimization {
	/* PSO PARAMETERS */
	private int MAX_LENGTH; // N number of queens
	private int PARTICLE_COUNT; // population of particles
	private double V_MAX; // Maximum velocity change allowed
	private int MAX_EPOCHS;
	private int TARGET; // 0 conflicts; Number for algorithm to find.
	private int SHUFFLE_RANGE_MIN; // used for initializing particles randomly
	private int SHUFFLE_RANGE_MAX;

	private Random rand;
	private ArrayList<Particle> particles;
	private ArrayList<Particle> solutions;
	private int epoch;

	/*
	 * Instantiates the particle swarm optimization algorithm along with its
	 * parameters.
	 * 
	 * @param: size of n queens
	 */
	public ParticleSwarmOptimization(int n, int population) {
		MAX_LENGTH = n;
		PARTICLE_COUNT = population;
		V_MAX = 4; /* 4 8 12 16 20 */
		MAX_EPOCHS = 5000; /* 1000 5000 10000 50000 100000 */
		TARGET = 0;
		SHUFFLE_RANGE_MIN = 8;
		SHUFFLE_RANGE_MAX = 20;
		epoch = 0;
	}

	/*
	 * Starts the particle swarm optimization algorithm solving for n queens.
	 */
	public boolean algorithm() {
		particles = new ArrayList<Particle>();
		solutions = new ArrayList<Particle>();
		rand = new Random();
		epoch = 0;
		boolean done = false;
		Particle aParticle = null;

		initialize();

		while (!done) {
			if (epoch < MAX_EPOCHS) {
				for (int i = 0; i < PARTICLE_COUNT; i++) {
					aParticle = particles.get(i);
					aParticle.computeConflicts();
					if (aParticle.getConflicts() == TARGET) {
						done = true;
					}
				} // i

				Collections.sort(particles); // sort particles by their
												// conflicts scores, best to
												// worst.

				getVelocity();

				updateParticles();

				epoch++;
				System.out.println("Epoch: " + epoch);
			} else {
				done = true;
			}
		}

		if (epoch == MAX_EPOCHS) {
			System.out.println("No Solution Found");
			Particle best = particles.get(0);
			for (Particle p : particles) {
				if (p.getConflicts() < best.getConflicts())
					best = p;
			}
			solutions.add(best);
			printSolution(best);
			done = false;
		}

		for (Particle p : particles) { // prints the solutions if found within
										// mnc
			if (p.getConflicts() == TARGET) {
				System.out.println("\nSolution Found");
				solutions.add(p);
				printSolution(p);
			}
		}

		return done;
	}

	/*
	 * Updates each partilce according to its velocity.
	 */
	public void updateParticles() {
		Particle source = null;
		Particle destination = null;

		// Best is at index 0, so start from the second best.
		for (int i = 1; i < PARTICLE_COUNT; i++) {
			// The higher the velocity score, the more changes it will need.
			source = particles.get(i - 1);
			destination = particles.get(i);

			int changes = (int) Math.floor(Math.abs(destination.getVelocity()));

			for (int j = 0; j < changes; j++) {
				if (new Random().nextBoolean()) { // exploration
					randomlyArrange(i);
				}
				// Push it closer to it's best neighbor.
				copyFromParticle(source, destination); // exploitation
			} // j

			// Update conflicts value.
			destination.computeConflicts();
			;
		} // i
	}

	/*
	 * Particle movement. Moves the data of the destination particle closer to
	 * the best particle.
	 * 
	 * @param: particle with better standing in the sorted population
	 * 
	 * @param: current particle
	 */
	public void copyFromParticle(Particle best, Particle destination) {
		// push destination's data points closer to source's data points.
		int targetA = getRandomNumber(0, MAX_LENGTH - 1); // particle to target.
		int targetB = 0;
		int indexA = 0;
		int indexB = 0;
		int tempIndex = 0;

		// targetB will be source's neighbor immediately succeeding targetA
		// (circular).
		int i = 0;
		for (; i < MAX_LENGTH; i++) {
			if (best.getData(i) == targetA) {
				if (i == MAX_LENGTH - 1) {
					targetB = best.getData(0); // if end of array, take from
												// beginning.
				} else {
					targetB = best.getData(i + 1);
				}
				break;
			}
		}

		// Move targetB next to targetA by switching values.
		for (int j = 0; j < MAX_LENGTH; j++) {
			if (destination.getData(j) == targetA) {
				indexA = j;
			}
			if (destination.getData(j) == targetB) {
				indexB = j;
			}
		}

		// get temp index succeeding indexA.
		if (indexA == MAX_LENGTH - 1) {
			tempIndex = 0;
		} else {
			tempIndex = indexA + 1;
		}

		// Switch indexB value with tempIndex value.
		int temp = destination.getData(tempIndex);
		destination.setData(tempIndex, destination.getData(indexB));
		destination.setData(indexB, temp);

	}

	/*
	 * Calculates the velocity of each particle.
	 */
	public void getVelocity() {
		double worstResults = 0;
		double vValue = 0.0;
		Particle aParticle = null;

		// after sorting, worst will be last in list.
		worstResults = particles.get(PARTICLE_COUNT - 1).getConflicts();

		for (int i = 0; i < PARTICLE_COUNT; i++) {
			aParticle = particles.get(i);
			vValue = (V_MAX * aParticle.getConflicts()) / worstResults;

			if (vValue > V_MAX) {
				aParticle.setVelocity(V_MAX);
			} else if (vValue < 0.0) {
				aParticle.setVelocity(0.0);
			} else {
				aParticle.setVelocity(vValue);
			}
		}
	}

	/*
	 * Initializes all of the particles' placement of queens in ramdom
	 * positions.
	 */
	public void initialize() {
		int newParticleIndex = 0;
		int shuffles = 0;

		for (int i = 0; i < PARTICLE_COUNT; i++) {
			Particle newParticle = new Particle(MAX_LENGTH);

			particles.add(newParticle);
			newParticleIndex = particles.indexOf(newParticle);

			shuffles = getRandomNumber(SHUFFLE_RANGE_MIN, SHUFFLE_RANGE_MAX);

			for (int j = 0; j < shuffles; j++) {
				randomlyArrange(newParticleIndex);
			}

			particles.get(newParticleIndex).computeConflicts();
		} // i
	}

	/*
	 * Changes a position of the queens in a particle by swapping a randomly
	 * selected position
	 * 
	 * @param: index of the particle
	 */
	public void randomlyArrange(int index) { // randomly swap 2 positions
		int positionA = getRandomNumber(0, MAX_LENGTH - 1);
		int positionB = getExclusiveRandomNumber(MAX_LENGTH - 1, positionA);
		Particle thisParticle = particles.get(index);
		int temp = thisParticle.getData(positionA);
		thisParticle.setData(positionA, thisParticle.getData(positionB));
		thisParticle.setData(positionB, temp);
	}

	/*
	 * Gets a random number in the range of the parameters
	 * 
	 * @param: the minimum random number
	 * 
	 * @param: the maximum random number
	 * 
	 * @return: random number
	 */
	public int getRandomNumber(int low, int high) {
		return (int) Math.round((high - low) * rand.nextDouble() + low);
	}

	/*
	 * Gets a random number with the exception of the parameter
	 * 
	 * @param: the maximum random number
	 * 
	 * @param: number to to be chosen
	 * 
	 * @return: random number
	 */
	public int getExclusiveRandomNumber(int high, int except) {
		boolean done = false;
		int getRand = 0;

		while (!done) {
			getRand = rand.nextInt(high);
			if (getRand != except) {
				done = true;
			}
		}

		return getRand;
	}

	/*
	 * Prints the nxn board with the queens
	 * 
	 * @param: a particle
	 */
	public void printSolution(Particle solution) {
		String board[][] = new String[MAX_LENGTH][MAX_LENGTH];

		// Clear the board.
		for (int x = 0; x < MAX_LENGTH; x++) {
			for (int y = 0; y < MAX_LENGTH; y++) {
				board[x][y] = "";
			}
		}

		for (int x = 0; x < MAX_LENGTH; x++) {
			board[x][solution.getData(x)] = "Q";
		}

		// Display the board.
		System.out.println("Board:");
		for (int y = 0; y < MAX_LENGTH; y++) {
			for (int x = 0; x < MAX_LENGTH; x++) {
				if (board[y][x] == "Q") {
					System.out.print("Q ");
				} else {
					System.out.print(". ");
				}
			}
			System.out.print("\n");
		}
	}

	/*
	 * gets the solutions
	 * 
	 * @return: solutions
	 */
	public ArrayList<Particle> getSolutions() {
		return solutions;
	}

	/*
	 * gets the epoch
	 * 
	 * @return: epoch
	 */
	public int getEpoch() {
		return epoch;
	}

	/*
	 * gets the population size
	 * 
	 * @return: pop size
	 */
	public int getPopSize() {
		return particles.size();
	}

	/*
	 * gets the particle count
	 * 
	 * @return: particle count
	 */
	public int getParticleCount() {
		return PARTICLE_COUNT;
	}

	/*
	 * gets the v max
	 * 
	 * @return: v max
	 */
	public double getVmax() {
		return V_MAX;
	}

	/*
	 * gets the max epoch
	 * 
	 * @return: max epoch
	 */
	public int getMaxEpoch() {
		return MAX_EPOCHS;
	}

	/*
	 * gets the min shuffle
	 * 
	 * @return: min shuffle
	 */
	public int getShuffleMin() {
		return SHUFFLE_RANGE_MIN;
	}

	/*
	 * gets the max shuffle
	 * 
	 * @return: max shuffle
	 */
	public int getShuffleMax() {
		return SHUFFLE_RANGE_MAX;
	}

	/*
	 * sets the max epochs
	 * 
	 * @return: new max epochs
	 */
	public void setMaxEpoch(int newMaxEpochs) {
		this.MAX_EPOCHS = newMaxEpochs;
	}

	/*
	 * sets the max velocity
	 * 
	 * @param: new max velocity
	 */
	public void setVMax(double newMaxVelocity) {
		this.V_MAX = newMaxVelocity;
	}
}