package genetic_algorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {

	private int queens;
	private double crossRate;
	private double mutateRate;
	private int popSize;
	private int iterations;

	public GeneticAlgorithm(int queens, int popSize, double crossRate,
			double mutateRate, int iterations) {
		this.queens = queens;
		this.crossRate = crossRate;
		this.mutateRate = mutateRate;
		this.popSize = popSize;
		this.iterations = iterations;
	}

	public Gene tournamentSelection(ArrayList<Gene> pool, int size,
			Random random) {
		Gene[] participants = new Gene[size];
		for (int i = 0; i < participants.length; i++) {
			int index = random.nextInt(pool.size());
			participants[i] = pool.get(index);
		}

		Gene winner = participants[0];

		for (int i = 1; i < participants.length; i++) {
			if (winner.getFitness() > participants[i].getFitness())
				winner = participants[i];
		}

		Gene survivor = new Gene(winner);
		return survivor;

	}

	public void crossover(ArrayList<Gene> pool, int size, int queens,
			Random random) {
		int a = random.nextInt(queens);
		int b = random.nextInt(queens);

		int min = Math.min(a, b);
		int max = Math.max(a, b);

		for (int i = 0; i < size; i = i + 2) {
			Gene parent1 = tournamentSelection(pool, 4, random);
			Gene parent2 = tournamentSelection(pool, 4, random);
			cross(parent1, parent2, min, max, random, pool);
		}
	}

	private void cross(Gene originalParent1, Gene originalParent2, int min,
			int max, Random random, ArrayList<Gene> tempPool) {

		Gene parent1 = new Gene(originalParent1);
		Gene parent2 = new Gene(originalParent2);
		int[] parent1Queens = parent1.getQueens();
		int[] parent2Queens = parent2.getQueens();

		ArrayList<Integer> parent1String = new ArrayList<>();
		ArrayList<Integer> parent2String = new ArrayList<>();

		ArrayList<Integer> tempparent1String = new ArrayList<>();
		ArrayList<Integer> tempparent2String = new ArrayList<>();

		for (int i = min; i <= max; i++) {
			parent1String.add(parent1Queens[i]);
			parent2String.add(parent2Queens[i]);
		}

		for (int i = 0; i < parent1Queens.length; i++) {
			if (i < min || i > max) {
				tempparent1String.add(parent1Queens[i]);
				tempparent2String.add(parent2Queens[i]);
			}
		}

		int tempMin = min;
		int tempMax = max;

		int[] offspring1 = new int[parent1Queens.length];
		int[] offspring2 = new int[parent1Queens.length];

		int index = 0;
		while (tempMin <= tempMax) {
			offspring1[tempMin] = parent2String.get(index);
			offspring2[tempMin] = parent1String.get(index);
			tempMin++;
			index++;
		}

		index = 0;

		for (int i = 0; i < parent1Queens.length; i++) {
			if (i < min || i > max) {
				if (parent2String.contains(parent1Queens[i]))
					offspring1[i] = -1;
				else {
					offspring1[i] = parent1Queens[i];
					parent2String.add(parent1Queens[i]);
				}
			}
		}

		for (int i = 0; i < parent2Queens.length; i++) {
			if (i < min || i > max) {
				if (parent1String.contains(parent2Queens[i]))
					offspring2[i] = -1;
				else {
					offspring2[i] = parent2Queens[i];
					parent1String.add(parent2Queens[i]);
				}
			}
		}

		for (int i = 0; i < offspring1.length; i++) {
			if (offspring1[i] == -1) {
				int temp = random.nextInt(offspring1.length);
				while (parent2String.contains(temp))
					temp = random.nextInt(offspring1.length);
				offspring1[i] = temp;
				parent2String.add(temp);
			}

			if (offspring2[i] == -1) {
				int temp = random.nextInt(offspring2.length);
				while (parent1String.contains(temp))
					temp = random.nextInt(offspring2.length);
				offspring2[i] = temp;
				parent1String.add(temp);
			}

		}

		parent2.setQueens(offspring2);
		parent1.setQueens(offspring1);

		tempPool.add(parent2);
		tempPool.add(parent1);
	}

	public void mutation(ArrayList<Gene> pool, int size, int queens,
			Random random) {
		int a = random.nextInt(queens);
		int b = random.nextInt(queens);

		int min = Math.min(a, b);
		int max = Math.max(a, b);

		for (int i = 0; i < size; i++) {
			Gene parent = tournamentSelection(pool, 4, random);
			mutate(parent, min, max, pool);
		}
	}

	private void mutate(Gene originalParent, int min, int max,
			ArrayList<Gene> tempPool) {
		Gene parent = new Gene(originalParent);
		int[] parentQueens = parent.getQueens();

		int var1 = parentQueens[min];
		int var2 = parentQueens[max];
		parentQueens[min] = var2;
		parentQueens[max] = var1;

		parent.setQueens(parentQueens);
		tempPool.add(parent);

	}

	public void reproduction(ArrayList<Gene> pool, int size, int queens,
			Random random) {

		for (int i = 0; i < size; i++) {
			Gene parent = tournamentSelection(pool, 4, random);
			pool.add(new Gene(parent));
		}
	}

	public void survival(ArrayList<Gene> pool, int size) {

		for (int i = 0; i < size; i++) {
			Gene gene = pool.get(0);
			int index = 0;

			for (int j = 1; j < pool.size(); j++) {
				if (gene.getFitness() < pool.get(j).getFitness()) {
					gene = pool.get(j);
					index = j;
				}

			}

			pool.remove(index);
		}

	}

	public Gene getBestGene(ArrayList<Gene> pool) {
		Gene best = pool.get(0);

		for (int i = 1; i < pool.size(); i++) {
			if (best.getFitness() > pool.get(i).getFitness())
				best = pool.get(i);
		}

		return best;
	}

	public void printBoard(int[] queens) {

		String board[][] = new String[queens.length][queens.length];

		for (int x = 0; x < queens.length; x++) {
			board[x][queens[x]] = "Q";
		}

		// Display the board.
		System.out.println("Board:");
		for (int y = 0; y < queens.length; y++) {
			for (int x = 0; x < queens.length; x++) {
				if (board[y][x] == "Q") {
					System.out.print("Q ");
				} else {
					System.out.print(". ");
				}
			}
			System.out.print("\n");
		}
	}

	public void run() {
		long start = System.currentTimeMillis();
		Random random = new Random();
		ArrayList<Gene> pool = new ArrayList<>();

		int crossSize = (int) (popSize * 1.0 * crossRate);
		if (crossSize % 2 != 0)
			crossSize++;

		int mutateSize = (int) (popSize * 1.0 * mutateRate);

		int repSize = popSize - crossSize - mutateSize;

		for (int i = 0; i < popSize; i++)
			pool.add(new Gene(queens, random));

		int count = 0;
		Gene best = getBestGene(pool);

		while (best.getFitness() > 0 && count < iterations) {
			if (count % 5 == 0) {
				System.out.println("Generation: " + count);
				System.out.println("Best: " + best.toString());
				System.out.println("Fitness: " + best.getFitness());
				System.out.println();
			}
			crossover(pool, crossSize, queens, random);
			mutation(pool, mutateSize, queens, random);
			reproduction(pool, repSize, queens, random);
			survival(pool, popSize);
			Gene newbest = getBestGene(pool);
			if (newbest.getFitness() < best.getFitness())
				best = newbest;
			count++;
		}

		printBoard(best.getQueens());
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs/GeneticAlgorithmOutput";
		int t = 1;
		try {

			File f = new File(FILENAME + t + ".txt");

			// if file doesnt exists, then create it

			while (f.exists()) {
				t++;
				f = new File(FILENAME + t + ".txt");
			}
			f.createNewFile();

			fw = new FileWriter(f.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);

			long end = System.currentTimeMillis();

			double run = ((end * 1.0) - start) / 1000;

			bw.write("Queens: " + queens);
			bw.newLine();
			System.out.println("\nBest: " + best.toString() + "\nFitness: "
					+ best.getFitness() + "\nGenerations: " + (count));
			bw.write("Best: " + best.toString());
			bw.newLine();

			bw.write("Fitness: " + best.getFitness());
			bw.newLine();

			bw.write("Generations: " + (count));
			bw.newLine();

			System.out.println("Run Time: " + run + " seconds");
			bw.write("Run Time: " + run + " seconds");
			bw.newLine();
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

}
