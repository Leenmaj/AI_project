import java.util.Random;

public class TennerGridGenerator {
    int[] sums;
    Random rand = new Random();
    final int rows = 3;
    final int totalSum = 45 * rows; // total sum for all columns need to be 135 to be feasible to solvee
    final int minSum = 3; // min possible column sum (0+1+2)
    final int maxSum = 24; // max possible column sum (7+8+9)

    public TennerGridGenerator() {
        this.sums = new int[10];
        generateSum();
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
                int maxPos= Math.min(maxCol, maxSum);

                //making sure each column can have at least the minimum sum
                int minPos= Math.max(minSum, remainingSum - maxSum * (9 - i));

                sums[i] = rand.nextInt(maxPos - minPos+ 1) + minPos;
                remainingSum -= sums[i];
            }
        }
    }

    public int[] getSums() {
        return sums;
    }

    public void printSums() {
        System.out.println("Sums");
        for (int summation : sums) {
            System.out.print(summation + " ");
        }
        System.out.println("\n");
    }
}
