import java.util.Random;

public class TennerGridGenerator {
    Variable[][] grid; // grid of variables

    int[] sums;
    Random rand = new Random();
    final int rows = 3;
    final int totalSum = 45 * rows; // total sum for all columns need to be 135 to be feasible to solvee
    final int minSum = 3; // min possible column sum (0+1+2)
    final int maxSum = 24; // max possible column sum (7+8+9)

    public TennerGridGenerator() {
        // initialise the grid
        this.grid = new Variable[rows][10];
        this.sums = new int[10];

        generator();
    }

    /*
     * private void generateSum() {
     * int remainingSum = totalSum;
     * for (int i = 0; i < 10; i++) {
     * // last column use what is remaining
     * if (i == 9) {
     * sums[i] = remainingSum;
     * } else {
     * // makng sure that the remaining columns can reach the minimum sum
     * int maxCol = remainingSum - minSum * (9 - i);
     * int maxPos = Math.min(maxCol, maxSum);
     * 
     * // making sure each column can have at least the minimum sum
     * int minPos = Math.max(minSum, remainingSum - maxSum * (9 - i));
     * 
     * sums[i] = rand.nextInt(maxPos - minPos + 1) + minPos;
     * remainingSum -= sums[i];
     * }
     * }
     * }
     */

    public void generator() {
        int rowAssignment[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        // Fill the the first row with values randomaly
        shuffleArray(rowAssignment);
        for (int i = 0; i < 10; i++) {
            grid[0][i] = new Variable(rowAssignment[i]);
        }

        // fill the other rows
        for (int r = 1; r < rows; r++) {
            while (true) {
                // randomly generate an assignment for rows cells
                shuffleArray(rowAssignment);
                int c;
                /*
                 * check if the shuffled array satisfy constrains , the for loop will break if
                 * any values in the array does not satisfy it
                 * ex : row0 = 1 2 3 9 8 7 6 4 5 0
                 * rowAssignmet for row1 = 4 5 3 . . the loop will break at j=2 since grid[0][2]
                 * =rowAssignment[2]
                 * 
                 */

                for (c = 0; c < 10; c++) {
                    if (rowAssignment[c] == grid[r - 1][c].value)
                        break;

                    if (c > 0 && rowAssignment[c] == grid[r - 1][c - 1].value)
                        break;

                    if (c < 9 && rowAssignment[c] == grid[r - 1][c + 1].value)
                        break;

                }

                // c=10 means that the generated assignment satisfy our constrains -> break out
                // of
                // the while loop and assign row r to rowAssignment

                if (c == 10)
                    break;

            }

            // grid[r] = rowAssignment
            for (int j = 0; j < 10; j++)
                grid[r][j] = new Variable(rowAssignment[j]);

        }

        // calculate sums for each column

        for (int c = 0; c < 10; c++) {
            int colSum = 0;
            for (int r = 0; r < rows; r++) {
                colSum += grid[r][c].value;
            }
            sums[c] = colSum;

        }

        // assign -1 to % of the cells
        // between 0.5 to 0.85 chance of hidden cells

        double hideCellProb = 0.5 + (0.85 - 0.5) * rand.nextDouble();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < 10; c++)
                if (rand.nextDouble() < hideCellProb)
                    grid[r][c] = new Variable(-1);

        }

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
