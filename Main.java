public class Main {
    public static void main(String[] args) {

        TennerGridGenerator generator = new TennerGridGenerator();

        generator.printSums();
        TennerGridSolver solver = new TennerGridSolver(3, generator.sums, generator.grid);
        // TennerGridSolver solver = new TennerGridSolver(3, generator.getSums());
        System.out.println("Grid before solving");
        solver.printGrid();
        generator.printSums();
        if (solver.solve()) {
            System.out.println("solved Tenner grid");
            solver.printGrid();
            generator.printSums();
        } else {
            System.out.println("failed to solve the tenner grid");
        }
    }
}
