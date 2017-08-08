package particle_swarm_optimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PSO {
	// Writer logWriter;
	private ParticleSwarmOptimization pso;
	private int queens;
	private double runtime;
	private double maxVelocity;
	private int maxEpoch;
	private int population;
	private int epochs;
	private ArrayList<Particle> particles;

	/*
	 * Instantiates the PSO class
	 */
	public PSO(int queens, int population, double maxVelocity, int maxEpoch) {
		this.queens = queens;
		this.maxVelocity = maxVelocity;
		this.maxEpoch = maxEpoch;
		this.population = population;
		runtime = 0;
		particles = new ArrayList<>();
		epochs = 0;
	}

	/*
	 * Test method accepts the N/max length, and parameters mutation rate and
	 * max epoch to set for the PSO accordingly.
	 * 
	 * @param: max length/n
	 * 
	 * @param: max velocity for PSO
	 * 
	 * @param: max epoch for PSO
	 */
	public void run() {
		pso = new ParticleSwarmOptimization(queens, population); // instantiate
																	// and
		// define params for
		// PSO here
		pso.setVMax(maxVelocity);
		pso.setMaxEpoch(maxEpoch);

		long startTime = 0;
		long endTime = 0;

		startTime = System.currentTimeMillis();
		if (pso.algorithm()) {
			epochs = pso.getEpoch();
			endTime = System.currentTimeMillis();
			runtime = ((endTime * 1.0) - startTime) / 1000;

			particles = pso.getSolutions();

		} else {
			epochs = pso.getEpoch();
			endTime = System.currentTimeMillis();
			runtime = ((endTime * 1.0) - startTime) / 1000;

			particles = pso.getSolutions();
		}

		printRuntimes();
	}

	/*
	 * Prints the runtime summary in the console
	 */
	public void printRuntimes() {
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs/PSOOutput";
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
			System.out.println();
			bw.write("Queens: " + queens);
			bw.newLine();
			System.out.println("Epochs: " + epochs);
			bw.write("Epochs: " + epochs);
			bw.newLine();
			bw.newLine();
			System.out.println("Best Solution(s): ");
			bw.write("Best Solution(s): ");
			bw.newLine();
			for (int j = 0; j < particles.size(); j++) {
				for (int k = 0; k < queens; k++) {
					System.out.print("(" + k + ","
							+ particles.get(j).getData(k) + ")");
					bw.write("(" + k + "," + particles.get(j).getData(k) + ")");
					if (k != (queens - 1)) {
						System.out.print(" ");
						bw.write(" ");
					}
				}
				System.out.println();
				bw.newLine();
				System.out.println("Conflicts: "
						+ particles.get(j).getConflicts());
				bw.write("Conflicts: " + particles.get(j).getConflicts());
				bw.newLine();
			}
			System.out.println();
			bw.newLine();
			System.out.println("Run Time: " + runtime + " seconds");
			bw.write("Run Time: " + runtime + " seconds");
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
