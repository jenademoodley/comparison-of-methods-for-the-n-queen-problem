package hybrid_ica_tabu;

import java.util.Arrays;

import org.jacop.constraints.Alldifferent;
import org.jacop.constraints.XplusCeqZ;
import org.jacop.core.IntDomain;
import org.jacop.core.IntVar;
import org.jacop.core.Store;

import tabu_search.BestCandidate;
import tabu_search.NeighborHood;
import tabu_search.TabuList;

public class HybridTabuSearch {
	private Store store;
	private IntVar[] Q; // main variables: Q[i] represents the column of the
						// queen on the i-th row
	private int maxIteration = 100;
	private int tabuSize = 25;
	private int[] solution;
	private int count;

	public HybridTabuSearch(int[] solution, int maxIteration, int tabuSize,
			int count) {
		this.solution = solution;
		int n = solution.length;
		this.maxIteration = maxIteration;
		this.tabuSize = tabuSize;
		this.count = count;
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

		// Generate a first solution
		IntDomain[] domains = getDomains();
		int[] currentSol = solution;
		int[] bestSol = currentSol; // best known solution
		TabuList tabuList = new TabuList(tabuSize);

		// Cost of the current solution
		int currentCost = fitness(currentSol);
		int bestCost = currentCost;
		int nbIte = count;

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

		System.out.println("Final Solution : " + Arrays.toString(bestSol));
		finalBoard(bestSol);
		solution = bestSol;
		count = nbIte;
		return bestCost == 0;

	}

	public int getCount() {
		return count;
	}

	public int[] getSolution() {
		return solution;
	}

	public void run() {
		boolean result = tabuSearch(maxIteration, tabuSize);
		if (result) {
			System.out.println("An Optimal Solution was Found!");
		} else {
			System.out.println("A Non-Optimal Solution was Found!");
		}
	}

	public void finalBoard(int[] board) {
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

		System.out.print("\nSolution: ");
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == 1)
					System.out.print("(" + i + "," + j + ") ");
			}
		}
		System.out.println();
	}

}
