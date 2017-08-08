package app;

import genetic_algorithm.GeneticAlgorithm;
import hill_climbing.HillClimbing;
import hybrid_ga_tabu.HybridGA;
import hybrid_ica_tabu.HybridICA;
import imperialist_competitive_algorithm.ICA;

import java.io.IOException;
import java.util.Scanner;

import particle_swarm_optimization.PSO;
import simulated_annealing.SA;
import tabu_search.TabuSearch;
import brute_force.BruteForce;

public class Runnable {

	static Scanner sc;

	public static void main(String[] args) {
		sc = new Scanner(System.in);
		int queens = 0;
		System.out.println("Optimizing the N-Queens Problem");
		System.out.println("Enter Choice (Number Only)");

		System.out.println("\nBrute Force Technique");
		System.out.println("---------------------");
		System.out.println("1 - Brute Force");

		System.out.println("\nPopulation-Based Techniques");
		System.out.println("---------------------------");
		System.out.println("2 - Genetic Algorithm");
		System.out.println("3 - Imperialist Competitive Algorithm");
		System.out.println("4 - Particle Swarm Optimization");

		System.out.println("\nLocal Search Techniques");
		System.out.println("-----------------------");
		System.out.println("5 - Hill-Climbing");
		System.out.println("6 - Tabu Search");
		System.out.println("7 - Simulated Annealing");

		System.out.println("\nHybrid Techniques");
		System.out.println("-----------------");
		System.out.println("8 - ICA-Tabu Hybrid");
		System.out.println("9 - GA-Tabu Hybrid");
		System.out.println();
		System.out.println("0 - Exit");
		int choice = sc.nextInt();

		while (choice < 0 || choice > 9) {
			System.out.println("Option Not Available");
			System.out.println("Enter Choice (Number Only)");
			System.out.println("\nBrute Force Technique");
			System.out.println("---------------------");
			System.out.println("1 - Brute Force");

			System.out.println("\nPopulation-Based Techniques");
			System.out.println("---------------------------");
			System.out.println("2 - Genetic Algorithm");
			System.out.println("3 - Imperialist Competitive Algorithm");
			System.out.println("4 - Particle Swarm Optimization");

			System.out.println("\nLocal Search Techniques");
			System.out.println("-----------------------");
			System.out.println("5 - Hill-Climbing");
			System.out.println("6 - Tabu Search");
			System.out.println("7 - Simulated Annealing");

			System.out.println("\nHybrid Techniques");
			System.out.println("-----------------");
			System.out.println("8 - ICA-Tabu Hybrid");
			System.out.println("9 - GA-Tabu Hybrid");
			System.out.println();
			System.out.println("0 - Exit");
			choice = sc.nextInt();
		}

		switch (choice) {
		case 1:
			System.out.println("Brute Force");
			queens = 8;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t" + queens);

			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			int use = sc.nextInt();

			if (use == 1)
				queens = checkQueen();

			BruteForce bf = new BruteForce(queens);
			bf.run();
			break;

		case 2:
			System.out.println("Genetic Algorithm");
			queens = 8;
			int pop = 33;
			double cross = 0.6;
			double mutation = 0.1;
			int gen = 1000;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t\t" + queens);
			System.out.println("Population Size:\t\t" + pop);
			System.out.println("Crossover Rate:\t\t\t" + cross);
			System.out.println("Mutation Rate:\t\t\t" + mutation);
			System.out.println("Max Number of Generations:\t" + gen);
			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Population Size");
				pop = sc.nextInt();

				System.out.println("Enter Crossover Rate");
				cross = sc.nextDouble();

				System.out.println("Enter Mutation Rate");
				mutation = sc.nextDouble();

				System.out.println("Enter Max Number of Generations");
				gen = sc.nextInt();
			}

			GeneticAlgorithm ga = new GeneticAlgorithm(queens, pop, cross,
					mutation, gen);
			ga.run();
			break;

		case 3:
			System.out.println("Imperialist Competitive Algorithm");
			queens = 8;
			pop = 100;
			double rate = 0.4;
			int noOfEmpires = 10;
			int iterations = 1000;
			int crossover = 1;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t\t" + queens);
			System.out.println("Population Size:\t\t" + pop);
			System.out.println("Revolution Rate:\t\t" + rate);
			System.out.println("Number of Empires:\t\t" + noOfEmpires);
			System.out.println("Max Number of Iterations:\t" + iterations);

			System.out.print("Crossover Type:\t\t\t");
			if (crossover == 1)
				System.out.println("Partial Match Crossover");
			else
				System.out.println("Order Crossover");
			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Population Size");
				pop = sc.nextInt();

				System.out.println("Enter Revolution Rate");
				rate = sc.nextDouble();

				System.out.println("Enter Number of Empires");
				noOfEmpires = sc.nextInt();

				System.out.println("Enter Max Number of Iterations");
				iterations = sc.nextInt();

				System.out
						.println("Enter Crossover Type:\n1 - Partial Match Crossover\n2 - Order Crossover");
				crossover = sc.nextInt();
			}

			ICA ica = new ICA(queens, pop, rate, noOfEmpires, iterations,
					crossover);
			ica.run();
			break;

		case 4:
			System.out.println("Particle Swarm Optimization");
			queens = 8;
			int population = 40;
			double maxVelocity = 4;
			int maxEpoch = 1000;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t" + queens);
			System.out.println("Population Size:\t" + population);
			System.out.println("Maximum Velocity:\t" + maxVelocity);
			System.out.println("Maximum Epoch:\t\t" + maxEpoch);

			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Population Size");
				population = sc.nextInt();

				System.out.println("Enter Maximum Velocity");
				maxVelocity = sc.nextDouble();

				System.out.println("Enter Maximum Epoch");
				maxEpoch = sc.nextInt();
			}

			PSO pso = new PSO(queens, population, maxVelocity, maxEpoch);
			pso.run();
			break;

		case 5:
			System.out.println("Hill-Climbing");
			queens = 8;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t" + queens);

			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1)
				queens = checkQueen();

			HillClimbing hc = new HillClimbing(queens);
			hc.run();
			break;

		case 6:
			System.out.println("Tabu Search");
			queens = 100;
			int maxIteration = 300;
			int tabuSize = 25;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t\t" + queens);
			System.out
					.println("Maximum Number of Iterations:\t" + maxIteration);
			System.out.println("Tabu Size:\t\t\t" + tabuSize);

			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Maximum Number of Iterations");
				maxIteration = sc.nextInt();
				System.out.println("Enter Tabu Size");
				tabuSize = sc.nextInt();
			}
			TabuSearch ts = new TabuSearch(queens, maxIteration, tabuSize);
			ts.run();
			break;

		case 7:
			System.out.println("Simulated Annealing");
			queens = 8;
			int tollerence = 0;
			double temperature = 1000;
			int maxIterations = 1000;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t" + queens);
			System.out.println("Tollerence:\t\t" + tollerence);
			System.out.println("Temperature:\t\t" + temperature);
			System.out.println("Max Iterations:\t\t" + maxIterations);
			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Tollerence");
				tollerence = sc.nextInt();

				System.out.println("Enter Temperature");
				temperature = sc.nextDouble();

				System.out.println("Enter Max Iterations");
				maxIterations = sc.nextInt();
			}

			SA sa = new SA(queens, tollerence, temperature, maxIterations);
			sa.run();
			break;

		case 8:
			System.out.println("ICA-Tabu Hybrid");
			queens = 8;
			pop = 100;
			rate = 0.4;
			noOfEmpires = 10;
			iterations = 1000;
			crossover = 1;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t\t" + queens);
			System.out.println("Population Size:\t\t" + pop);
			System.out.println("Revolution Rate:\t\t" + rate);
			System.out.println("Number of Empires:\t\t" + noOfEmpires);
			System.out.println("Max Number of Iterations:\t" + iterations);

			System.out.print("Crossover Type:\t\t\t");
			if (crossover == 1)
				System.out.println("Partial Match Crossover");
			else
				System.out.println("Order Crossover");
			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Population Size");
				pop = sc.nextInt();

				System.out.println("Enter Revolution Rate");
				rate = sc.nextDouble();

				System.out.println("Enter Number of Empires");
				noOfEmpires = sc.nextInt();

				System.out.println("Enter Max Number of Iterations");
				iterations = sc.nextInt();

				System.out
						.println("Enter Crossover Type:\n1 - Partial Match Crossover\n2 - Order Crossover");
				crossover = sc.nextInt();
			}

			HybridICA hica = new HybridICA(queens, pop, rate, noOfEmpires,
					iterations, crossover);
			hica.run();
			break;

		case 9:
			System.out.println("GA-Tabu Hybrid");
			queens = 8;
			pop = 33;
			cross = 0.6;
			mutation = 0.1;
			gen = 1000;
			System.out.println("Default Values");
			System.out.println("--------------");
			System.out.println("Number of Queens:\t\t" + queens);
			System.out.println("Population Size:\t\t" + pop);
			System.out.println("Crossover Rate:\t\t\t" + cross);
			System.out.println("Mutation Rate:\t\t\t" + mutation);
			System.out.println("Max Number of Generations:\t" + gen);
			System.out.println("\nUse Default Values?");
			System.out.println("1-NO\n2-YES");
			use = sc.nextInt();

			if (use == 1) {
				queens = checkQueen();

				System.out.println("Enter Population Size");
				pop = sc.nextInt();

				System.out.println("Enter Crossover Rate");
				cross = sc.nextDouble();

				System.out.println("Enter Mutation Rate");
				mutation = sc.nextDouble();

				System.out.println("Enter Max Number of Generations");
				gen = sc.nextInt();
			}

			HybridGA hga = new HybridGA(queens, pop, cross, mutation, gen);
			hga.run();
			break;

		case 0:
			System.out.println("Thank You");
			System.exit(0);
			break;
		}

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int checkQueen() {
		System.out.println("Enter Number of Queens");
		int queens = sc.nextInt();

		while (queens < 4) {
			System.out.println("Enter Number of Queens");
			queens = sc.nextInt();
		}

		return queens;
	}

}
