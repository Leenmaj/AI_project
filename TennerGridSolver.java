public class TennerGridSolver {
    Variable[][] grid; // grid of variables
    int[] sums; // goal sums for each column
    int rows;

    public TennerGridSolver(int rows, int[] Sums, Variable[][] grid) {
        this.rows = rows;
        this.grid = grid;
        this.sums = Sums;

    }

    public boolean solve() {
        boolean solved = backtrackWithForwardChecking(0, 0);
        // boolean solved = backtrack(0, 0);
        if (solved) {
            // printGrid(); // p rint the grid if solved
        } else {
            System.out.println("No solution found.");
        }
        return solved;
    }

    public boolean backtrack(int row, int col) {

        if (row == rows)
            return verifySums();
        if (col == 10)
            return backtrack(row + 1, 0);
        if (grid[row][col].filledCell)
            return backtrack(row, col + 1);

        for (int i = 0; i < grid[row][col].domSize; i++) {
            int val = grid[row][col].domain[i];

            if (isSafe(row, col, val)) {
                grid[row][col].value = val;
                if (backtrack(row, col + 1))
                    return true;
                grid[row][col].value = -1; // backtracking step
            }
        }

        return false;
    }

    public boolean backtrackWithForwardChecking(int row, int col) {
        if (row == rows)
            return verifySums();
        if (col == 10)
            return backtrack(row + 1, 0);

        for (int i = 0; i < grid[row][col].domSize; i++) {
            int val = grid[row][col].domain[i];
            ForwardChecking(row, col);

            if (isSafe(row, col, val)) {
                grid[row][col].value = val;
                if (backtrack(row, col + 1))
                    return true;
                grid[row][col].value = -1; // backtracking step
            }
        }

        return false;
    }

    public boolean ForwardChecking(int row, int col) {
        int value = grid[row][col].value;
        int[][] directions = { { 1, -1 }, { 1, 0 }, { 1, 1 } };
        for (int i = col + 1; i < 10; i++) {
            grid[row][i].removeFromDomain(value);

            if (grid[row][i].domSize == 0)
                return false;

        }
        for (int[] d : directions) {
            int drow = row + d[0];
            int dcol = col + d[1];
            if (drow >= 0 && drow < rows && dcol >= 0 && dcol < 10) {
                grid[drow][dcol].removeFromDomain(value);
                if (grid[drow][dcol].domSize == 0)
                    return false;
            }
        }

        return true;

    }

    public void printCurrentState() {
        System.out.println("Current grid state:");
        // Iterate through each row and column of the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 10; col++) {
                /*
                 * print the value of each variable in the grid
                 * if the value is -1 print underscore
                 */
                System.out.print((grid[row][col].value >= 0 ? grid[row][col].value : "_") + "  ");
            }
            System.out.println();
        }
        System.out.println("-----------------------------");

        // calculate and print the current sum for each column
        for (int col = 0; col < 10; col++) {
            int currSum = 0;
            for (int row = 0; row < rows; row++) {
                if (grid[row][col].value >= 0) { // only add up assigned values
                    currSum += grid[row][col].value;
                }
            }
            System.out.print(currSum + " "); // the current sum for the column
        }
        System.out.println();

        // print the goal sum for each column
        System.out.println("goal sums:");
        for (int i = 0; i < sums.length; i++) {
            System.out.print(sums[i] + " ");
        }
        System.out.println("\n-----------------------------");
    }

    public boolean isSafe(int row, int col, int value) {
        if (row == 0 && value == sums[col])
            return false;
        // row constraintthe value is not already ther in same row
        for (int i = 0; i < 10; i++) {
            if (grid[row][i].value == value)
                return false;
        }

        // check diagonal cells
        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } }; // directions
        for (int[] d : directions) {
            int drow = row + d[0];
            int dcol = col + d[1];
            // make sure the new position is within the grid bounds
            if (drow >= 0 && drow < rows && dcol >= 0 && dcol < 10) {
                if (grid[drow][dcol].value == value)
                    return false;
            }
        }
        if (row > 0 && grid[row - 1][col].value == value)
            return false;
        // checking the cell directly below onlyy if not in the last row
        if (row < rows - 1 && grid[row + 1][col].value == value)
            return false;

        // making sure adding doesn't exceed the goal sum for the column
        int tempSum = value;
        for (int i = 0; i < rows; i++) {
            if (i != row)
                tempSum += (grid[i][col].value != -1) ? grid[i][col].value : 0;
        }
        if (tempSum > sums[col])
            return false;

        // future possibility check for column sum
        int maxPossSum = tempSum; // Start with the current sum including 'value'
        int remainingCells = rows - 1 - row; // cells below the current one
        // calculate the maximum possible sum for the column assuming remaining cells
        // take the highest value which is9
        maxPossSum += remainingCells * 9;
        if (maxPossSum < sums[col])
            return false; // if even the max possible sum is less than the target its not safe

        return true;
    }

    public boolean verifySums() {
        for (int col = 0; col < 10; col++) {
            int sum = 0;
            for (int row = 0; row < rows; row++) {
                sum += grid[row][col].value;
            }
            if (sum != sums[col])
                return false;
        }
        return true;
    }

    public void printGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < 10; col++) {
                System.out.printf("%4d", grid[row][col].value);
            }
            System.out.println();
        }
    }

}