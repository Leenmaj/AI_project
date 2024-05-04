public class TennerGridSolver {
    Variable[][] grid; // grid of variables
    int[] sums; // goal sums for each column
    int rows;
    int consistency = 0;
    int assignments = 0;

    public TennerGridSolver(int rows, int[] Sums, Variable[][] unsolvedGrid) {
        this.rows = rows;
        this.grid = new Variable[rows][10];
        this.sums = Sums;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < 10; j++) {
                this.grid[i][j] = new Variable(unsolvedGrid[i][j].value, unsolvedGrid[i][j].domain,
                        unsolvedGrid[i][j].domSize, unsolvedGrid[i][j].filledCell);
            }

    }

    public boolean solve(char type) {
        long startTime = 0;

        long endTime = 0;

        boolean solved = false;

        switch (type) {
            case 'B':
                assignments = 0;
                startTime = System.nanoTime();
                solved = backtrack(0, 0);
                endTime = System.nanoTime();
                break;

            case 'F':
                assignments = 0;
                startTime = System.nanoTime();
                solved = backtrackWithForwardChecking(0, 0);
                endTime = System.nanoTime();
                break;
            case 'M':
                assignments = 0;
                int indices[] = findMRV();
                startTime = System.nanoTime();
                solved = ForwardCheckingwithMRV(indices[0], indices[1]);
                endTime = System.nanoTime();
                break;
            default:
                break;
        }

        if (!solved)
            System.out.println("No solution found.");

        else {
            System.out.println("Time solving :  " + (endTime - startTime) / 1000000.0 + " milliseconds");

            System.out.println("Number of assignments :  " + assignments);
            System.out.println("Number of consistency checks :  " + consistency);

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
                assignments++;
                if (backtrack(row, col + 1))
                    return true;
                grid[row][col].value = -1; // backtracking step
                assignments++;
            }
        }

        return false;
    }

    public boolean backtrackWithForwardChecking(int row, int col) {
        if (row == rows)
            return verifySums();
        if (col == 10)
            return backtrackWithForwardChecking(row + 1, 0);
        if (grid[row][col].filledCell)
            return backtrackWithForwardChecking(row, col + 1);
        for (int i = 0; i < grid[row][col].domSize; i++) {
            int val = grid[row][col].domain[i];
            Variable[][] gridClone = new Variable[rows][10];
            copygrid(gridClone, grid);

            if (isSafe(row, col, val)) {
                grid[row][col].value = val;
                assignments++;
                if (ForwardChecking(row, col)) {
                    if (backtrackWithForwardChecking(row, col + 1))
                        return true;
                    copygrid(grid, gridClone);
                    assignments++;

                }

            }
        }

        return false;
    }

    public boolean ForwardChecking(int row, int col) {
        int value = grid[row][col].value;
        // check that no varible would have domain size =0
        for (int i = 0; i < 10; i++) {
            if (grid[row][i].value == -1 && i != col)
                if (grid[row][i].noPossibleAssignment(value))
                    return false;

        }

        int[][] directions = { { 1, -1 }, { 1, 0 }, { 1, 1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };

        for (int[] d : directions) {
            int drow = row + d[0];
            int dcol = col + d[1];
            if (drow >= 0 && drow < rows && dcol >= 0 && dcol < 10) {
                if (grid[drow][dcol].noPossibleAssignment(value))
                    return false;
            }
        }
        // remove
        for (int i = 0; i < 10; i++) {
            if (grid[row][i].value == -1 && i != col)
                grid[row][i].removeFromDomain(value);
        }
        for (int[] d : directions) {
            int drow = row + d[0];
            int dcol = col + d[1];
            if (drow >= 0 && drow < rows && dcol >= 0 && dcol < 10) {
                if (grid[drow][dcol].value == -1)
                    grid[drow][dcol].removeFromDomain(value);
            }
        }

        return true;

    }

    public boolean ForwardCheckingwithMRV(int row, int col) {

        int unfilled = 0;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < 10; j++)
                if (grid[i][j].value == -1)
                    unfilled++;

        if (unfilled == 0) {
            return verifySums();
        }
        // Find cell with minimum remaining values

        Variable[][] gridClone = new Variable[rows][10];

        copygrid(gridClone, grid);
        // Try all remaining values for the MRV cell
        int size = grid[row][col].domSize;
        for (int i = 0; i < size; i++) {

            int val = grid[row][col].domain[i];
            if (isSafe(row, col, val)) {
                grid[row][col].value = val;
                assignments++;
                if (ForwardChecking(row, col)) { // Call ForwardChecking here
                    int[] indices = findMRV();
                    int mrvRow = indices[0];
                    int mrvCol = indices[1];
                    if (ForwardCheckingwithMRV(mrvRow, mrvCol))
                        return true;
                    copygrid(grid, gridClone);

                }

                assignments++;
            }

        }

        return false;
    }

    public boolean isSafe(int row, int col, int value) {

        if (row == 0 && value == sums[col]) {
            consistency++;
            return false;
        }
        for (int i = 0; i < 10; i++) {

            if (grid[row][i].value == value) {
                consistency++;
                return false;
            }
        }

        int[][] directions = { { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 } };
        for (int[] d : directions) {
            int drow = row + d[0];
            int dcol = col + d[1];
            if (drow >= 0 && drow < rows && dcol >= 0 && dcol < 10) {

                if (grid[drow][dcol].value == value) {
                    consistency++;
                    return false;
                }
            }
        }
        if (row > 0 && grid[row - 1][col].value == value) {
            consistency++;
            return false;
        }
        if (row < rows - 1 && grid[row + 1][col].value == value) {
            consistency++;
            return false;
        }

        int tempSum = value;
        for (int i = 0; i < rows; i++) {
            if (i != row) {
                tempSum += (grid[i][col].value != -1) ? grid[i][col].value : 0;
            }
        }
        if (tempSum > sums[col]) {

            return false;
        }

        int maxPossSum = tempSum + ((rows - 1 - row) * 9);
        if (maxPossSum < sums[col]) {

            return false; // if even the max possible sum is less than the target it's not safe
        }

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

    void copygrid(Variable[][] gridClone, Variable[][] grid) {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < 10; j++) {
                gridClone[i][j] = new Variable(grid[i][j].value, grid[i][j].domain,
                        grid[i][j].domSize, grid[i][j].filledCell);
            }
    }

    int[] findMRV() {
        int MRV = Integer.MAX_VALUE;
        int mrvRow = -1, mrvCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 10; j++) {
                if (grid[i][j].value == -1) {
                    int min = grid[i][j].domSize;
                    if (min < MRV) {
                        MRV = min;
                        mrvRow = i;
                        mrvCol = j;
                    }
                }
            }
        }

        int indices[] = { mrvRow, mrvCol };
        return indices;

    }

}
