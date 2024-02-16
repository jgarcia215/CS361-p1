/**
 * Represent a state for a DFA. Is an abstraction
 * of a finite automata state.
 *
 * @author Josh Miller and Jack Garcia
 */


package fa.dfa;

import fa.State;
import java.util.HashMap;
import java.util.Map;

public class DFAState extends State {
    private boolean isFinal;
    private Map<Character, DFAState> transitions; // Transition function for this state

    /**
     * Constructor for a DFAState with a specified name.
     * Initializes the state with no transitions.
     *
     * @param name The unique name for this state.
     */
    public DFAState(String name) {
        super(name); // Call the superclass constructor to set the name
        this.isFinal = false; // By default, states are not accepting states
        this.transitions = new HashMap<>(); // Initialize the transition function
    }

    /**
     * Retrieves the next state for a given input symbol.
     *
     * @param symbol The input symbol.
     * @return The next state based on the input symbol, or null if no transition exists for the symbol.
     */
    public DFAState getTransistion(char symbol) {
        return transitions.get(symbol);
    }

    /**
     * Creates a new transition and adds it to
     * the map of transitions so the state can
     * track it.
     *
     * @param name
     * @param state
     */
    public void createNewTransition(char name, DFAState state) {
        transitions.put(name, state);
    }

    /**
     * Makes a state a final state.
     *
     */
    public void makeFinalState() {
        this.isFinal = true;
    }
}
