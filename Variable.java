public class Variable {
    int value; // Current value assigned to the variable (-1 for unassigned)
    int[] domain; // Possible values the variable can take
    int domSize; // Size of the domain (number of possible values)
    Boolean filledCell;// Was a value assigned to the cell during the grid generation process?

    public Variable() {
        this.value = -1; // -1 represents an unassigned variable
        this.domain = new int[10]; // assuming values can be from 0 to 9
        this.domSize = 10; // in the start all values from 0 to 9 are possible
        for (int i = 0; i < 10; i++) {
            this.domain[i] = i; // initialise domain with all possible values
        }
    }

    public Variable(int value, int[] domain, int domSize, boolean filledCell) {
        this.value = value;
        this.domSize = domSize;
        this.domain = new int[domSize];
        for (int i = 0; i < domSize; i++) {
            this.domain[i] = domain[i];

        }

        this.filledCell = filledCell;

    }

    public Variable(int x) {

        this.value = x;// -1 represents an unassigned variable
        if (value == -1) {
            this.domain = new int[10]; // assuming values can be from 0 to 9
            for (int i = 0; i < 10; i++)
                this.domain[i] = i; // initialise domain with all possible values
            this.domSize = 10;
            filledCell = false;
        } else {
            this.domain = new int[1];
            this.domain[0] = x;
            this.domSize = 1;
            filledCell = true;
        }
    }

    void removeFromDomain(int x) {
        for (int i = 0; i < domSize; i++)
            if (domain[i] == x) {
                for (int j = i + 1; j < domSize; j++) {
                    domain[j - 1] = domain[j];
                }
                domSize--;
                break;
            }

    }

    boolean noPossibleAssignment(int x) {
        if (domain[0] == x && domSize == 1 && !filledCell) {
            return true;
        }

        return false;

    }

}
