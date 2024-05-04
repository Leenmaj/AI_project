import java.util.Random;

public class TennerGridGenerator {
    Variable[][] grid; // grid of variables

    int[] sums;
    Random rand = new Random();
    final int rows = 3;
    final int totalSum = 45 * rows; // total sum for all columns need to be 135 to be feasible to solvee

    public TennerGridGenerator() {
        // initialise the grid
        this.grid = new Variable[rows][10];
        this.sums = new int[10];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < 10; j++) {
                this.grid[i][j] = new Variable(-1);
            }

        int rowAssignment[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        shuffleArray(rowAssignment);

        generatorBT(0, 0, rowAssignment);
        hideCells();

    }

    public boolean generatorBT(int row, int col, int[] rowAssignment) {
        if (row == rows)
            return calculateSums();
        if (col == 10) {
            shuffleArray(rowAssignment);
            return generatorBT(row + 1, 0, rowAssignment);

        }

        // shuffleArray(rowAssignment);
        for (int i = 0; i < rowAssignment.length; i++) {
            int val = rowAssignment[i];
            if (isSafe(row, col, val)) {
                grid[row][col] = new Variable(val);
                if (generatorBT(row, col + 1, rowAssignment))
                    return true;
                grid[row][col] = new Variable(-1);
                ; // backtracking step
            }
        }
        return false;

    }

    boolean isSafe(int r, int c, int val) {

        for (int i = 0; i < 10; i++) {
            if (grid[r][i].value == val)
                return false;
        }
        if (r > 0 && val == grid[r - 1][c].value)
            return false;

        if (r > 0 && c > 0 && val == grid[r - 1][c - 1].value)
            return false;

        if (r > 0 && c < 9 && val == grid[r - 1][c + 1].value)
            return false;

        return true;

    }

    boolean calculateSums() {
        for (int c = 0; c < 10; c++) {
            int colSum = 0;
            for (int r = 0; r < rows; r++) {
                colSum += grid[r][c].value;
            }
            sums[c] = colSum;

        }
        return true;
    }

    private void shuffleArray(int[] ar) {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void hideCells() {
        /*
         * Randomly hide cells within the grid, with a likelihood ranging from 40% to
         * 70%. This selection is arbitrary,
         * aimed at achieving a diverse array of grids that avoid extreme levels of
         * crowding or emptiness most of the.
         */

        double hideCellProb = 0.4 + (0.7 - 0.4) * rand.nextDouble();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < 10; c++)
                if (rand.nextDouble() < hideCellProb)
                    grid[r][c] = new Variable(-1);

        }
    }

    public void printGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 10; col++) {
                if (grid[row][col].value == -1)
                    System.out.printf("%4s", "-");
                else

                    System.out.printf("%4d", grid[row][col].value);
            }
            System.out.println();

        }
        System.out.println("  - - - - - - - - - - - - - - - - - - - - ");

        printSums();
    }

    public void printSums() {

        for (int summation : sums) {
            System.out.printf("%4d", summation);
        }
        System.out.println("\n");
    }

}
