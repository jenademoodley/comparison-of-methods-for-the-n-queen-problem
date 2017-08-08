package simulated_annealing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SA {

	private int queens;
	private int tollerence;
	private double temperature;
	private int maxIterations;

	public SA(int queens, int tollerence, double temperature, int maxIterations) {
		this.queens = queens;
		this.tollerence = tollerence;
		this.temperature = temperature;
		this.maxIterations = maxIterations;
	}

	public void run() {
		long start = 0;
		long end = 0;
		NQueen nq;
		nq = new SimulatedAnnealing(queens, tollerence, temperature,
				maxIterations);
		start = System.currentTimeMillis();
		nq.solve();
		end = System.currentTimeMillis();
		double run = ((end * 1.0) - start) / 1000;
		BufferedWriter bw = null;
		FileWriter fw = null;
		String FILENAME = "Outputs/SAOutput";
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
			bw.write("Queens: " + queens);
			bw.newLine();
			bw.write(nq.show());
			bw.newLine();
			System.out.println("\nRun Time :" + run + " seconds");
			bw.write("Run Time :" + run + " seconds");
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
