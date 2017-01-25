/*----------------------------------------------------------------
 *  Author:        Vadim Babaev
 *  Written:       25.01.2017
 *  Last updated:  25.01.2017
 *
 *  Implementation of PercolationStats class.
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] percolationThresholds;  //  array holding results of experiments
    private int width;                       //  width of experimental field
    private int count;                       //  quantity of field cells
    private double meanVal;                  //  mean of percolation threshold
    private double stddevVal;                //  standard deviation of percolation threshold
    private double confLo;                   //  low  endpoint of 95% confidence interval
    private double confHi;                   //  high endpoint of 95% confidence interval

    /**
     *   Class constructor specifying width of field
     *   and number of trials.
     *
     *   @param n the integer representing of width of field
     *   @param trials the integer representing of number of experiments
     *
     *   @throws IllegalArgumentException unless
     *      both {@code n > 0} and {@code trials > 0}
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("N should be more than 1");
        }

        width = n;
        count = n * n;
        percolationThresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            percolationThresholds[i] = trial();
        }

        meanVal   = StdStats.mean(percolationThresholds);
        stddevVal = StdStats.stddev(percolationThresholds);
        confLo    = meanVal - (1.96 * stddevVal / Math.sqrt(trials));
        confHi    = meanVal + (1.96 * stddevVal / Math.sqrt(trials));
    }

    /**
     *   Perform trial.
     *
     *   @return percolation threshold
     */
    private double trial() {
        Percolation field = new Percolation(width);
        int i = 0;
        int row;
        int col;
        while (!field.percolates()) {
            int rndField;
            do {
                rndField = StdRandom.uniform(count);
                row = rndField / width + 1;
                col = 1 + rndField % width;
            } while (field.isOpen(row, col));

            field.open(row, col);
            i++;
        }

        return ((double) i)/count;
    }

    /**
     *   Gets the mean value of percolation thresholds
     *
     *   @return {@code meanVal}
     */
    public double mean() {
        return meanVal;
    }

    /**
     *   Gets the standard deviation of percolation thresholds
     *
     *   @return {@code stddevVal}
     */
    public double stddev() {
        return stddevVal;
    }

    /**
     *   Gets the low  endpoint of 95% confidence interval
     *
     *   @return {@code confLo}
     */
    public double confidenceLo() {
        return confLo;
    }

    /**
     *   Gets the high endpoint of 95% confidence interval
     *
     *   @return {@code confHi}
     */
    public double confidenceHi() {
        return confHi;
    }

    public static void main(String[] args) {
        PercolationStats perc = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("args                    = " + args[0] + ", " + args[1]);
        System.out.println("mean                    = " + perc.mean());
        System.out.println("stddev                  = " + perc.stddev());
        System.out.println("95% confidence interval = " + perc.confidenceLo() + ", " + perc.confidenceHi());
    }
}
