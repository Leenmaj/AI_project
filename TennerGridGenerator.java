import java.util.Random;

public class TennerGridGenerator {
    Variable[][] grid; // grid of variables
    // change this
    int[] sums = { 13, 10, 8, 7, 19, 16, 11, 19, 15, 17 };
    Random rand = new Random();
    final int rows = 3;
    final int totalSum = 45 * rows; // total sum for all columns need to be 135 to be feasible to solvee
    final int minSum = 3; // min possible column sum (0+1+2)
    final int maxSum = 24; // max possible column sum (7+8+9)

    public TennerGridGenerator() {
        // initialise the grid with variables
        this.grid = new Variable[rows][10];
        int testGrid[][] = {
                { -1, 6, 2, 0, -1, -1, -1, 8, 5, 7 },
                { -1, 0, 1, 7, 8, -1, -1, -1, 9, -1 },
                { -1, 4, -1, -1, 2, -1, 3, 7, -1, 8 } };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 10; j++) {
                this.grid[i][j] = new Variable(testGrid[i][j]);
            }

        }

    }

    private void generateSum() {
        int remainingSum = totalSum;
        for (int i = 0; i < 10; i++) {
            // last column use what is remaining
            if (i == 9) {
                sums[i] = remainingSum;
            } else {
                // makng sure that the remaining columns can reach the minimum sum
                int maxCol = remainingSum - minSum * (9 - i);
                int maxPos = Math.min(maxCol, maxSum);

                // making sure each column can have at least the minimum sum
                int minPos = Math.max(minSum, remainingSum - maxSum * (9 - i));

                sums[i] = rand.nextInt(maxPos - minPos + 1) + minPos;
                remainingSum -= sums[i];
            }
        }
    }

    public int[] getSums() {
        return sums;
    }

    public void printSums() {

        for (int summation : sums) {
            System.out.printf("%4d", summation);
        }
        System.out.println("\n");
    }
}
