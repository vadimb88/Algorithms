import java.util.ArrayList;

public class Board {
    private final int[][] blocks;
    private final int dim;

    public Board(int[][] blocks) {
        dim = blocks.length;
        this.blocks = copyBlocks(blocks);
    }

    public int dimension() {
        return dim;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0, k = 1; i < dim; i++)
            for (int j = 0; j < dim; j++, k++) {
                if (blocks[i][j] != 0 && blocks[i][j] != k) {
                    count++;
                }
            }

        return count;
    }

    public int manhattan() {
        int count = 0;
        for (int i = 0, k = 1; i < dim; i++)
            for (int j = 0; j < dim; j++, k++) {
                int block = blocks[i][j];
                if (block != 0 && block != k)
                    count += Math.abs(i - ((block - 1) / dim)) + Math.abs(j - (block - 1) % dim);

            }

        return count;
    }

    private int[][] copyBlocks(int[][] oldBlocks) {
        int[][] newBlocks = new int[dim][dim];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                newBlocks[i][j] = oldBlocks[i][j];

        return newBlocks;
    }

    private Board swap(int row1, int col1, int row2, int col2) {
        int[][] newBlocks = copyBlocks(blocks);
        int temp = newBlocks[row1][col1];
        newBlocks[row1][col1] = newBlocks[row2][col2];
        newBlocks[row2][col2] = temp;
        return new Board(newBlocks);
    }

    public boolean isGoal() {
        for (int i = 0, k = 1; i < dim; i++)
            for (int j = 0; j < dim; j++, k++) {
                if (blocks[i][j] != 0 && blocks[i][j] != k) {
                    return false;
                }
            }

        return true;
    }

    public Board twin() {
        for (int i = 0; i < dim; i++)
            for (int j = 1; j < dim; j++)
                if (blocks[i][j] != 0 && blocks[i][j - 1] != 0)
                    return swap(i, j, i, j - 1);

        throw new RuntimeException();
    }

    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;

        int[][] yBlocks = ((Board) y).blocks;
        if (dim != yBlocks.length) return false;

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (blocks[i][j] != yBlocks[i][j])
                    return false;

        return true;
    }

    private int[] getZeroPos() {
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (blocks[i][j] == 0)
                    return new int[] {i, j};

        throw new RuntimeException();
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighb = new ArrayList<Board>();
        final int[] zeroPos = getZeroPos();
        final int row = zeroPos[0];
        final int col = zeroPos[1];

        if (row > 0)         neighb.add(swap(row, col, row - 1, col));
        if (row < dim - 1)   neighb.add(swap(row, col, row + 1, col));
        if (col > 0)         neighb.add(swap(row, col, row, col - 1));
        if (col < dim - 1)   neighb.add(swap(row, col, row, col + 1));

        return neighb;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++)
                str.append(String.format("%3d ", blocks[i][j]));

            str.append("\n");
        }

        return str.toString();
    }

    public static void main(String[] args) {
        Board b = new Board(new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        Board c = new Board(new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.equals(c));
        System.out.println(b);
    }
}
