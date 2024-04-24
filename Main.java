public class Main {
    public static void main(String[] args) {

        TennerGridGenerator generator = new TennerGridGenerator();
        System.out.println("generated column sums for the tenner grid:");
        generator.printSums();

        TennerGridSolver solver = new TennerGridSolver(3, generator.getSums());
        if (solver.solve()) {
            System.out.println("solved Tenner grid");
            solver.printGrid();
        } else {
            System.out.println("failed to solve the tenner grid");
        }
    }
}
