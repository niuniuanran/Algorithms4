import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final double[] openSiteRates;
    private final int trialTime;
    private final double alfa;
    private double mean;
    private double stddev;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        mean = -1;
        stddev = -1;
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("Illegal trial requirement!");
        }

        openSiteRates = new double[trials];
        trialTime = trials;
        alfa = 1.96;
        for (int i = 0; i < trials; i++) {
            Percolation currentPercolation = new Percolation(n);
            while (!currentPercolation.percolates()) {
                currentPercolation.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
            }
            openSiteRates[i] = currentPercolation.numberOfOpenSites() * 1.0 / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean != -1) {
            return mean;
        }
        mean = StdStats.mean(openSiteRates);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev != -1) {
            return stddev;
        }
        stddev = StdStats.stddev(openSiteRates);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        if (mean == -1) {
            mean = StdStats.mean(openSiteRates);
        }
        if (stddev == -1) {
            stddev = StdStats.stddev(openSiteRates);
        }
        return mean - alfa * stddev / Math.sqrt(trialTime);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (mean == -1) {
            mean = StdStats.mean(openSiteRates);
        }
        if (stddev == -1) {
            stddev = StdStats.stddev(openSiteRates);
        }
        return mean + alfa * stddev / Math.sqrt(trialTime);
    }

    // test client
    // include a main() method that takes two command-line arguments n and T,
    // performs T independent computational experiments (discussed above) on
    // an n-by-n grid, and prints the sample mean, sample standard deviation,
    // and the 95% confidence interval for the percolation threshold.
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        if (n < 1 || t < 1) {
            throw new IllegalArgumentException("Illegal trial requirement!");
        }

        PercolationStats trials = new PercolationStats(n, t);
        StdOut.printf("mean                    = %f\n", trials.mean());
        StdOut.printf("stddev                  = %f\n", trials.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n\n", trials.confidenceLo(),
                      trials.confidenceHi());

    }

}
