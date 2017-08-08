package tabu_search;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.jacop.constraints.Alldifferent;
import org.jacop.constraints.XplusCeqZ;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;
import org.jacop.core.ValueEnumeration;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMedian;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

public class TabuSearch {
	private Store store;
	private IntVar[] Q; // main variables: Q[i] represents the column of the
						// queen on the i-th row
	private int maxIteration = 100;
	private int tabuSize = 25;

	public TabuSearch(int n, int maxIteration, int tabuSize) {
		this.maxIteration = maxIteration;
		this.tabuSize = tabuSize;

		store = new Store();
		Q = new IntVar[n];
		IntVar[] y = new IntVar[n];
		IntVar[] z = new IntVar[n];

		for (int i = 0; i < n; ++i) {
			Q[i] = new IntVar(store, "Q" + i, 0, n - 1);
			y[i] = new IntVar(store, "y" + i, -i, n - 1 - i);
			z[i] = new IntVar(store, "z" + i, i, n - 1 + i);

			store.impose(new XplusCeqZ(Q[i], i, z[i]));
			store.impose(new XplusCeqZ(y[i], i, Q[i]));
		}

		// all different: no attack on columns
		store.impose(new Alldifferent(Q));
		store.impose(new Alldifferent(y));
		store.impose(new Alldifferent(z));
	}

	/**
	 * 
	 * @return tab The domain of the main variables
	 */
	public IntDomain[] getDomains() {
		IntDomain[] tab = new IntDomain[Q.length];
		for (int i = 0; i < Q.length; ++i) {
			tab[i] = Q[i].domain;
			System.out.println("Domain[" + i + "] : " + tab[i]);
		}
		return tab;
	}

	/**
	 * Generate randomly a solution within the domains
	 * 
	 * @param domains
	 *            The domain in which we generate randomly a solution
	 * @return solution The generated ramdomly solution
	 */
	public int[] generateSolution(IntDomain[] domains) {
		Random rand = new Random();
		int[] solution = new int[domains.length];

		for (int i = 0; i < domains.length; ++i) {
			ValueEnumeration values = domains[i].valueEnumeration();
			int r = rand.nextInt(domains[i].getSize()); // 0 .. getSize()-1

			for (int j = 0; j <= r; ++j) {
				solution[i] = values.nextElement(); // only the r-th is relevant
			}
		}

		// Print for display
		System.out.print("Generated Solution : [" + solution[0]);
		for (int i = 1; i < solution.length; i++) {
			System.out.print(", ");
			System.out.print(solution[i]);
		}
		System.out.print("]");
		System.out.println("\n");

		return solution;
	}

	/**
	 * Cost or fitness of an alldifferent constraint
	 * 
	 * @param sol
	 * @return
	 */
	public int costAllDifferent(int[] sol) {
		int n = 0;
		for (int i = 0; i < sol.length; ++i) {
			for (int j = i + 1; j < sol.length; ++j) {
				if (sol[i] == sol[j])
					++n;
			}
		}
		return n;
	}

	// Fitness of a solution for the n-queens problem
	public int fitness(int[] sol) {
		int n = 0;

		// allDifferent on Q
		n += costAllDifferent(sol);

		// allDifferent on y
		int[] aux = new int[sol.length];
		for (int i = 0; i < sol.length; ++i) {
			aux[i] = sol[i] + i;
		}
		n += costAllDifferent(aux);

		// allDifferent on z
		for (int i = 0; i < sol.length; ++i) {
			aux[i] = sol[i] - i;
		}
		n += costAllDifferent(aux);

		return n;
	}

	// Display a solution
	public static void printSolution(int[] sol) {
		System.out.print("{");
		for (int i = 0; i < sol.length; ++i) {
			if (i != 0)
				System.out.print(", ");
			System.out.print(sol[i]);
		}
		System.out.print("}");
	}

	public boolean tabuSearch(int maxIteration, int tabuSize) {
		long start = System.currentTimeMillis();
		// Generate a first solution
		IntDomain[] domains = getDomains();
		int[] currentSol = generateSolution(domains);
		int[] bestSol = currentSol; // best known solution
		TabuList tabuList = new TabuList(tabuSize);

		// Cost of the current solution
		int currentCost = fitness(currentSol);
		int bestCost = currentCost;
		int nbIte = 0;

		while (currentCost > 0 && nbIte < maxIteration) {
			nbIte++;
			NeighborHood neighborHood = new NeighborHood(currentSol, domains);

			// Check
			try {
				neighborHood.determineNeighboor();
			} catch (Exception e) {
				continue;
			}
			neighborHood.reduceNeighborHood(tabuList);

			// Get BestCandidate from Neighborhood class
			BestCandidate bestCandidate = neighborHood
					.getBestPossibleSolution();
			currentCost = bestCandidate.getCost();
			currentSol = bestCandidate.getSolution();

			// Add the move to tabuList
			tabuList.addTabuElement(bestCandidate.getMove());

			if (currentCost < bestCost) {
				bestSol = currentSol;
				bestCost = currentCost;
			}

			System.out
					.println("*****************************************************************************");
			System.out.println("Best Solution : " + Arrays.toString(bestSol));
			System.out.println("Cost : " + bestCost);
			System.out.println("Number of Iterations : " + nbIte);

		}
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs/TabuOutput";
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
			bw.write("Queens: " + bestSol.length);
			bw.newLine();
			bw.write("Number of Iterations : " + nbIte);
			bw.newLine();
			System.out.println("Final Solution : " + Arrays.toString(bestSol));
			bw.write("Final Solution : " + Arrays.toString(bestSol));
			bw.newLine();
			bw.write("Cost : " + bestCost);
			bw.newLine();
			long end = System.currentTimeMillis();
			double run = ((end * 1.0) - start) / 1000;
			finalBoard(bestSol, bestCost, run);
			bw.write("Run Time : " + run + " seconds");
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
		return bestCost == 0;

	}

	public boolean completeSearch() {
		DepthFirstSearch<IntVar> search = new DepthFirstSearch<IntVar>();

		search.getSolutionListener().searchAll(true);
		search.getSolutionListener().recordSolutions(true);

		SelectChoicePoint<IntVar> select = new SimpleSelect<IntVar>(Q,
				new SmallestDomain<IntVar>(), new IndomainMedian<IntVar>());

		boolean result = search.labeling(store, select);

		for (int i = 1; i <= search.getSolutionListener().solutionsNo(); i++) {
			System.out.print("Solution " + i + ": [");
			for (int j = 0; j < search.getSolution(i).length; j++) {
				if (j != 0)
					System.out.print(", ");
				System.out.print(search.getSolution(i)[j]);
			}
			System.out.println("]");
		}

		return result;
	}

	public void run() {
		boolean result = tabuSearch(maxIteration, tabuSize);
		if (result) {
			System.out.println("An Optimal Solution was Found!");
		} else {
			System.out.println("A Non-Optimal Solution was Found!");
		}

	}

	public void finalBoard(int[] board, int bestCost, double run) {
		int[][] table = new int[board.length][board.length];

		for (int i = 0; i < board.length; i++)
			table[i][board[i]] = 1;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (table[i][j] == 1)
					System.out.print("Q ");
				else
					System.out.print(". ");
			}
			System.out.println();
		}

		System.out.print("\nSolution: " + Arrays.toString(board));
		System.out.println("\nCost: " + bestCost);
		System.out.println("Run Time: " + run + " seconds");
	}

}
