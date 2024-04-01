
    public static void main(String[] args) {
        TennerGridGenerator generator = new TennerGridGenerator();
        System.out.println("Generated column sums for the Tenner Grid:");
        generator.printSums();

        TennerGridSolver solver = new TennerGridSolver(generator.getSums());
        if (solver.solve()) {
            System.out.println("Solved Tenner Grid:");
            solver.printGrid();
        } else {
            System.out.println("Failed to solve the Tenner Grid.");
        }
    }
}
