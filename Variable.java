public class Variable {
    int value; // Current value assigned to the variable (-1 for unassigned)
    int[] domain; // Possible values the variable can take
    int domSize; // Size of the domain (number of possible values)

    public Variable() {
        this.value = -1; // -1 represents an unassigned variable
        this.domain = new int[10]; // assuming values can be from 0 to 9
        this.domSize = 10; // in the start all values from 0 to 9 are possible
        for (int i = 0; i < 10; i++) {
            this.domain[i] = i; // initialise domain with all possible values
        }
    }
}
