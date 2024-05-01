public class Main {
    public static void main(String[] args) {

        TennerGridGenerator generator = new TennerGridGenerator();

        // Back Tracking
        TennerGridSolver solver = new TennerGridSolver(3, generator.sums, generator.grid);
        // Forward Checking
        TennerGridSolver solver2 = new TennerGridSolver(3, generator.sums, generator.grid);
        // MRV
        TennerGridSolver solver3 = new TennerGridSolver(3, generator.sums, generator.grid);
        System.out.println("Grid before solving");
        solver.printGrid();
        
        if (solver.solve('B')) {
            System.out.println("Solved Tenner grid with backtracking");
            solver.printGrid();
        
        } else {
            System.out.println("failed to solve the tenner grid  backtracking");
        }

        if (solver2.solve('F')) {
            System.out.println("Solved Tenner grid with forward Checking");
            solver2.printGrid();
            
        } else {
            System.out.println("failed to solve the tenner grid with forward Checking");
        }
    }
}
