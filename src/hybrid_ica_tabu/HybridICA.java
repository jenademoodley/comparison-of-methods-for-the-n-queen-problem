package hybrid_ica_tabu;

import imperialist_competitive_algorithm.Country;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class HybridICA {

	private int queens;
	private int popSize;
	private double rate;
	private int noOfEmpires;
	private int iterations;
	private int crossover;

	public HybridICA(int queens, int popSize, double rate, int noOfEmpires,
			int iterations, int crossover) {
		this.queens = queens;
		this.popSize = popSize;
		this.rate = rate;
		this.noOfEmpires = noOfEmpires;
		this.iterations = iterations;

	}

	private Country getBestColony(ArrayList<ArrayList<Country>> empires) {
		Country best = empires.get(0).get(0);
		for (int i = 0; i < empires.size(); i++) {
			for (int j = 0; j < empires.get(i).size(); j++) {
				if (best.getFitness() > empires.get(i).get(j).getFitness())
					best = empires.get(i).get(j);
			}
		}

		return best;
	}

	private void PMX(ArrayList<Country> empire, int min, int max, int country,
			ArrayList<Country> tempEmpires, Random random) {
		Country originalImperialist = empire.get(0);
		Country imperialist = new Country(originalImperialist);
		int[] imperialistQueens = imperialist.getQueens();

		Country originalColony = empire.get(country);
		Country colony = new Country(originalColony);

		tempEmpires.add(originalColony);

		int[] colonyQueens = colony.getQueens();

		ArrayList<Integer> imperialistString = new ArrayList<>();
		ArrayList<Integer> colonyString = new ArrayList<>();

		ArrayList<Integer> tempimperialistString = new ArrayList<>();
		ArrayList<Integer> tempcolonyString = new ArrayList<>();

		for (int i = min; i <= max; i++) {
			imperialistString.add(imperialistQueens[i]);
			colonyString.add(colonyQueens[i]);
		}

		for (int i = 0; i < imperialistQueens.length; i++) {
			if (i < min || i > max) {
				tempimperialistString.add(imperialistQueens[i]);
				tempcolonyString.add(colonyQueens[i]);
			}
		}

		int tempMin = min;
		int tempMax = max;

		int[] offspring1 = new int[imperialistQueens.length];
		int[] offspring2 = new int[imperialistQueens.length];

		int index = 0;
		while (tempMin <= tempMax) {
			offspring1[tempMin] = colonyString.get(index);
			offspring2[tempMin] = imperialistString.get(index);
			tempMin++;
			index++;
		}

		index = 0;

		for (int i = 0; i < imperialistQueens.length; i++) {
			if (i < min || i > max) {
				if (colonyString.contains(imperialistQueens[i]))
					offspring1[i] = -1;
				else {
					offspring1[i] = imperialistQueens[i];
					colonyString.add(imperialistQueens[i]);
				}
			}
		}

		for (int i = 0; i < colonyQueens.length; i++) {
			if (i < min || i > max) {
				if (imperialistString.contains(colonyQueens[i]))
					offspring2[i] = -1;
				else {
					offspring2[i] = colonyQueens[i];
					imperialistString.add(colonyQueens[i]);
				}
			}
		}

		for (int i = 0; i < offspring1.length; i++) {
			if (offspring1[i] == -1) {
				int temp = random.nextInt(offspring1.length);
				while (colonyString.contains(temp))
					temp = random.nextInt(offspring1.length);
				offspring1[i] = temp;
				colonyString.add(temp);
			}

			if (offspring2[i] == -1) {
				int temp = random.nextInt(offspring2.length);
				while (imperialistString.contains(temp))
					temp = random.nextInt(offspring2.length);
				offspring2[i] = temp;
				imperialistString.add(temp);
			}

		}

		colony.setQueens(offspring2);
		imperialist.setQueens(offspring1);
		tempEmpires.add(colony);
		tempEmpires.add(imperialist);
	}

	private void partialMatchCrossover(ArrayList<ArrayList<Country>> empires,
			int queens, Random random) {
		int a = random.nextInt(queens);
		int b = random.nextInt(queens);

		int min = Math.min(a, b);
		int max = Math.max(a, b);

		for (int i = 0; i < empires.size(); i++) {
			ArrayList<Country> tempEmpires = new ArrayList<>();
			for (int j = 1; j < empires.get(i).size(); j++) {
				PMX(empires.get(i), min, max, j, tempEmpires, random);
			}
			for (int j = 1; j < empires.get(i).size(); j++) {
				Country bestColony = tempEmpires.get(0);
				int index = 0;
				for (int k = 0; k < tempEmpires.size(); k++) {
					if (bestColony.getFitness() > tempEmpires.get(k)
							.getFitness()) {
						bestColony = tempEmpires.get(k);
						index = k;
					}
				}

				empires.get(i).set(j, new Country(bestColony));
				tempEmpires.remove(index);

			}
		}
	}

	private void orderCrossover(ArrayList<ArrayList<Country>> empires,
			int queens, Random random) {

		int a = random.nextInt(queens);
		int b = random.nextInt(queens);

		int min = Math.min(a, b);
		int max = Math.max(a, b);

		for (int i = 0; i < empires.size(); i++) {
			ArrayList<Country> tempEmpires = new ArrayList<>();
			for (int j = 1; j < empires.get(i).size(); j++) {
				// System.out.println(j);
				OX(empires.get(i), min, max, j, random, tempEmpires);
			}

			for (int j = 1; j < empires.get(i).size(); j++) {
				Country bestColony = tempEmpires.get(0);
				int index = 0;
				for (int k = 0; k < tempEmpires.size(); k++) {
					if (bestColony.getFitness() > tempEmpires.get(k)
							.getFitness()) {
						bestColony = tempEmpires.get(k);
						index = k;
					}
				}

				empires.get(i).set(j, new Country(bestColony));
				tempEmpires.remove(index);

			}
		}

	}

	private void OX(ArrayList<Country> empire, int min, int max, int country,
			Random random, ArrayList<Country> tempEmpires) {
		Country originalImperialist = empire.get(0);
		Country imperialist = new Country(originalImperialist);
		int[] imperialistQueens = imperialist.getQueens();

		Country originalColony = empire.get(country);
		Country colony = new Country(originalColony);

		tempEmpires.add(originalColony);

		int[] colonyQueens = colony.getQueens();

		int[] subArray = new int[(max - min) + 1];
		int[] tempArray = new int[colonyQueens.length - subArray.length];
		int minVar = min;
		int maxVar = max;

		HashSet<Integer> set = new HashSet<>();

		int index = 0;

		index = 0;
		while (minVar <= maxVar) {
			subArray[index] = imperialistQueens[minVar];
			set.add(subArray[index]);
			minVar++;
			index++;
		}
		index = 0;
		for (int i = 0; i < colonyQueens.length; i++) {
			if (!set.contains(colonyQueens[i])) {
				tempArray[index] = colonyQueens[i];
				index++;
			}
		}

		int indexVar = 0;
		index = 0;
		int[] offspring = new int[colonyQueens.length];

		for (int i = 0; i < offspring.length; i++) {
			if (i < min || i > max) {
				offspring[i] = tempArray[index];
				index++;
			} else {
				offspring[i] = subArray[indexVar];
				indexVar++;
			}
		}
		colony.setQueens(offspring);
		tempEmpires.add(colony);

	}

	private void revolution(ArrayList<ArrayList<Country>> empires, int queens,
			double rate, Random random) {
		int a = random.nextInt(queens);
		int b = random.nextInt(queens);

		int min = Math.min(a, b);
		int max = Math.max(a, b);

		for (int i = 0; i < empires.size(); i++) {
			int revolutionPopulation = (int) (rate * empires.get(i).size());
			// System.out.println(revolutionPopulation + " "
			// + empires.get(i).size());
			for (int j = 0; j < revolutionPopulation; j++) {
				int country = random.nextInt(empires.get(i).size() - 1) + 1;
				rev(empires.get(i), min, max, country, random);
			}
		}
	}

	private void rev(ArrayList<Country> empire, int index1, int index2,
			int country, Random random) {
		// System.out.println("Revolution");
		// System.out.println("Colony:\t" + empire.get(country).toString());
		int[] colony = empire.get(country).getQueens();

		int var1 = colony[index1];
		int var2 = colony[index2];

		// System.out.println(index1);
		// System.out.println(index2);

		colony[index1] = var2;
		colony[index2] = var1;

		empire.get(country).setQueens(colony);
		// System.out.println("Colony:\t" + empire.get(country).toString());
	}

	private void powerStruggle(ArrayList<ArrayList<Country>> empires) {
		for (int i = 0; i < empires.size(); i++) {
			Country colony = empires.get(i).get(0);
			Country imperial = empires.get(i).get(0);
			int index = 0;
			for (int j = 1; j < empires.get(i).size(); j++) {
				if (empires.get(i).get(j).getFitness() < colony.getFitness()) {
					index = j;
					colony = empires.get(i).get(j);
				}
			}
			empires.get(i).set(0, colony);
			empires.get(i).set(index, imperial);
		}
	}

	private void imperialisticCompetition(ArrayList<ArrayList<Country>> empires) {
		double[] power = new double[empires.size()];

		for (int i = 0; i < empires.size(); i++) {
			power[i] = calculatePower(empires.get(i));
		}

		double minPow = power[0];
		double maxPow = power[0];
		int indexMin = 0;
		int indexMax = 0;

		for (int i = 1; i < power.length; i++) {
			if (minPow > power[i]) {
				minPow = power[i];
				indexMin = i;
			}

			if (maxPow < power[i]) {
				maxPow = power[i];
				indexMax = i;
			}
		}

		Country colonyToMove = empires.get(indexMax).get(0);
		int index = 0;
		for (int i = 1; i < empires.get(indexMax).size(); i++) {
			if (colonyToMove.getFitness() > empires.get(indexMax).get(i)
					.getFitness()) {
				colonyToMove = empires.get(indexMax).get(i);
				index = i;
			}
		}

		empires.get(indexMin).add(new Country(colonyToMove));
		empires.get(indexMax).remove(index);
	}

	public int getQueens() {
		return queens;
	}

	public void setQueens(int queens) {
		this.queens = queens;
	}

	public int getPopSize() {
		return popSize;
	}

	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getNoOfEmpires() {
		return noOfEmpires;
	}

	public void setNoOfEmpires(int noOfEmpires) {
		this.noOfEmpires = noOfEmpires;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getCrossover() {
		return crossover;
	}

	public void setCrossover(int crossover) {
		this.crossover = crossover;
	}

	private double calculatePower(ArrayList<Country> empire) {
		double power = 0;

		power += empire.get(0).getFitness();

		for (int i = 1; i < empire.size(); i++)
			power += (0.1 * empire.get(i).getFitness()) / empire.size();

		return power;
	}

	private void removeEmptyColonies(ArrayList<ArrayList<Country>> empires) {
		int index = -1;
		for (int i = 0; i < empires.size(); i++) {
			if (empires.get(i).size() == 0)
				index = i;
		}

		if (index >= 0)
			empires.remove(index);
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
		Random random = new Random();
		long start = System.currentTimeMillis();
		ArrayList<Country> population = new ArrayList<>();

		for (int i = 0; i < popSize; i++)
			population.add(new Country(queens, random));

		ArrayList<ArrayList<Country>> empires = new ArrayList<>();

		for (int i = 0; i < noOfEmpires; i++) {
			ArrayList<Country> empire = new ArrayList<>();
			Country imperialist = population.get(0);
			int index = 0;

			for (int j = 0; j < population.size(); j++) {
				if (imperialist.getFitness() > population.get(j).getFitness()) {
					imperialist = population.get(j);
					index = j;
				}
			}

			empire.add(imperialist);
			empires.add(empire);
			population.remove(index);
		}

		int noOfColonies = population.size() / noOfEmpires;

		for (int i = 0; i < noOfEmpires - 1; i++) {
			while (empires.get(i).size() < noOfColonies + 1) {
				int index = random.nextInt(population.size());
				empires.get(i).add(population.get(index));
				population.remove(index);
			}
		}

		for (int i = 0; i < population.size(); i++)
			empires.get(noOfEmpires - 1).add(population.get(i));

		population.clear();

		Country xbest = getBestColony(empires);
		Country best = new Country(xbest);
		int count = 0;
		System.out.println("Iteration:\t" + count);
		System.out.println("Best Fitness:\t" + best.getFitness());
		System.out.println();

		while (best.getFitness() > 0 && count < iterations) {

			if (crossover == 1)
				partialMatchCrossover(empires, queens, random);
			else
				orderCrossover(empires, queens, random);

			revolution(empires, queens, rate, random);
			powerStruggle(empires);
			imperialisticCompetition(empires);
			removeEmptyColonies(empires);

			Country newbest = getBestColony(empires);

			if (best.getFitness() > newbest.getFitness())
				best = new Country(newbest);

			count++;
			if (count % 5 == 0) {
				System.out.println("Iteration:\t" + (count));
				System.out.println("Best Fitness:\t" + best.getFitness());
				System.out.println();
			}
		}

		printBoard(best.getQueens());
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs/HybridTabuICAOutput";
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

			int tabuCount = 0;
			if (best.getFitness() > 0) {
				HybridTabuSearch ts = new HybridTabuSearch(best.getQueens(),
						300, 25, 0);
				ts.run();
				best = new Country(ts.getSolution(), random);
				System.out.println("Fitness: " + best.getFitness());
				tabuCount = ts.getCount();
			}

			bw.write("Queens: " + queens);
			bw.newLine();
			System.out.println("\nBest: " + best.toString() + "\nFitness: "
					+ best.getFitness() + "\nIterations on ICA: " + (count));
			System.out.println("Iterations on Tabu: " + tabuCount);
			bw.write("Best: " + best.toString());
			bw.newLine();

			bw.write("Fitness: " + best.getFitness());
			bw.newLine();

			bw.write("Iterations on ICA: " + (count));
			bw.newLine();
			bw.write("Iterations on Tabu: " + (tabuCount));
			bw.newLine();

			long end = System.currentTimeMillis();
			double run = ((end * 1.0) - start) / 1000;

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
