/*----------------------------------------------------------------
 *  Author:        Vadim Babaev
 *  Written:       25.01.2017
 *  Last updated:  25.01.2017
 *
 *  Implementation of Percolation class.
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte OPEN = 1;
    private static final byte CONNECTED_TO_TOP = 2;
    private static final byte CONNECTED_TO_BOTTOM = 4;
    private static final byte CONNECTED_BOTH = 6;
    private static final byte DIRS_LENGTH = 4;
    private static final byte[][] DIRS = new byte[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private boolean isPercolates = false;
    private WeightedQuickUnionUF field;     //  representation of field
    private int width;                      //  width of field
    private byte[] status;                  //  array with status of cells
    private int openedCount;                //  quantity of opened cells

    /**
     *   Class constructor specifying width of field
     *
     *   @param n the integer representing of width of field
     *
     *   @throws IllegalArgumentException unless {@code n > 0}
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N should be more than 1");
        }

        int count = n*n;
        width = n;
        field = new WeightedQuickUnionUF(count);
        status = new byte[count];
        for (int i = 0; i < count; i++) {
            status[i] = 0;
        }
    }

    /**
     *   Open cell on field.
     *
     *   @param row the integer representing row number of cell
     *   @param col the integer representing column number of cell
     *
     *   @throws IndexOutOfBoundsException unless
     *         both {@code 0 < row < width} and {@code 0 < col < width}

     */
    public void open(int row, int col) {
        validate(row, col);
        int current = coordsToIndex(row, col);
        if ((status[current] & OPEN) == 1) {
            return;
        }

        byte stat = 0;
        for (int i = 0; i < DIRS_LENGTH; i++) {
            int neighRow = row + DIRS[i][0];
            int neighCol = col + DIRS[i][1];
            if (neighRow > 0 && neighRow <= width && neighCol > 0 && neighCol <= width) {
                int neighbour = coordsToIndex(neighRow, neighCol);
                if ((status[neighbour] & OPEN) == 1) {
                    int root = field.find(neighbour);
                    stat |= status[root];
                    field.union(current, neighbour);
                }
            }
        }

        if (row == 1) {
            stat |= CONNECTED_TO_TOP;
        }

        if (row == width) {
            stat |= CONNECTED_TO_BOTTOM;
        }

        stat |= OPEN;
        status[current] = stat;
        status[field.find(current)] |= stat;

        if ((stat & CONNECTED_BOTH) == CONNECTED_BOTH) {
            isPercolates = true;
        }

        openedCount++;
    }

    /**
     *   Check if cell is open.
     *
     *   @param row the integer representing row number of cell
     *   @param col the integer representing column number of cell
     *
     *   @return {@code true} if cell is open, otherwise {@code false}
     *   @throws IndexOutOfBoundsException unless
     *         both {@code 0 < row < width} and {@code 0 < col < width}

     */
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return (status[coordsToIndex(row, col)] & OPEN) == 1;
    }

    /**
     *   Check if cell is full.
     *
     *   @param row the integer representing row number of cell
     *   @param col the integer representing column number of cell
     *
     *   @return {@code true} if cell is full, otherwise {@code false}
     *   @throws IndexOutOfBoundsException unless
     *         both {@code 0 < row < width} and {@code 0 < col < width}
     */
    public boolean isFull(int row, int col) {
        validate(row, col);
        int root = field.find(coordsToIndex(row, col));
        return (status[root] & CONNECTED_TO_TOP) == CONNECTED_TO_TOP;
    }

    /**
     *   Return number of open sites
     *
     *   @return {@code openedCount}
     */
    public int numberOfOpenSites() {
        return openedCount;
    }

    /**
     *   Check if system percolates
     *
     *   @return {@code true} if system percolates, otherwise {@code false}
     */
    public boolean percolates() {
        return isPercolates;
    }

    /**
     *   Convert 2D representation to 1d representation
     *
     *   @param row the integer representing row number of cell
     *   @param col the integer representing column number of cell
     *   @return 1D representation index of cell
     */
    private int coordsToIndex(int row, int col) {
        return (row - 1) * width + col - 1;
    }

    /**
     *   Check if row and col are valid.
     *
     *   @param row the integer representing row number of cell
     *   @param col the integer representing column number of cell
     *
     *   @throws IndexOutOfBoundsException unless
     *         both {@code 0 < row < width} and {@code 0 < col < width}
     */
    private void validate(int row, int col) {
        if (row <= 0 || row > width) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }

        if (col <= 0 || col > width) {
            throw new IndexOutOfBoundsException("col index i out of bounds");
        }
    }

    public static void main(String[] args) {
        Percolation testField = new Percolation(4);
        testField.open(1, 1);
        testField.open(2, 1);
        testField.open(3, 1);
        testField.open(4, 1);
        System.out.println("Field is percolated: " + testField.percolates());
    }
}
