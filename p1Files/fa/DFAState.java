package fa;

import fa.dfa.DFA;

import java.util.HashMap;
import java.util.Map;

public class DFAState extends State {
    private boolean isFinal;
    private boolean isStart;
    private Map<String, DFAState> transitions; // Transition function for this state

    /**
     * Constructor for a DFAState with a specified name.
     * Initializes the state with no transitions.
     *
     * @param name The unique name for this state.
     */
    public DFAState(String name) {
        super(name); // Call the superclass constructor to set the name
        this.isStart = false;
        this.isFinal = false; // By default, states are not accepting states
        this.transitions = new HashMap<>(); // Initialize the transition function
    }

    /**
     * Retrieves the next state for a given input symbol.
     *
     * @param symbol The input symbol.
     * @return The next state based on the input symbol, or null if no transition exists for the symbol.
     */
    public DFAState getNextState(char symbol) {
        return transitions.get(symbol);
    }

    public void setNextState(String name, DFAState state) {
        transitions.put(name, state);
    }
    //test
    public void makeFinalState() {
        this.isFinal = true;
    }
}
