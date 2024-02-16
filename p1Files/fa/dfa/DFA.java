/**
 * Represent a Definite Finite Automata. Includes methods
 * that creates a state, adds a transition, declares final/start state,
 * swap the states, etc.
 *
 * @author Josh Miller and Jack Garcia
 */


package fa.dfa;
import fa.State;
import java.util.*;

public class DFA implements DFAInterface {

    private Map<String, DFAState> dfa;  //Map that represents our DFA
    private DFAState initialState;  //Initial State. There can only be one.
    private Map<String, DFAState> finalStates;  //Map with our final states.
    private Set<Character> sigma;   //Represents our alphabet

    /**
     * Constructor for new DFA
     */
    public DFA() {
        dfa = new LinkedHashMap<>();
        finalStates = new LinkedHashMap<>();
        initialState = null;
        sigma = new LinkedHashSet<>();
    }

    /**
     * Adds a state to the state machine
     *
     * @param name is the label of the state
     * @return boolean based on success of the addition
     */
    @Override
    public boolean addState(String name) {

        if (!dfa.containsKey(name)) {
            dfa.put(name, new DFAState(name));
            return true;
        }

        return false;
    }

    /**
     * Sets a state as a final state.
     *
     * @param name is the label of the state
     * @return true unless the state was not set
     */
    @Override
    public boolean setFinal(String name) {
        DFAState finalState = null;

        if (dfa.containsKey(name)) {
            finalState = dfa.get(name);
            finalState.makeFinalState();
            finalStates.put(name, finalState);
            return true;
        }

        return false;
    }

    /**
     * Set the start state
     *
     * @param name is the label of the start state
     * @return true if the start state was set successfully
     */
    @Override
    public boolean setStart(String name) {

        if (dfa.containsKey(name)) {
            initialState = dfa.get(name);
            return true;
        }

        return false;
    }

    /**
     * Adds a symbol to the alphabet
     *
     * @param symbol to add to the alphabet set
     */
    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

    /**
     * Function determining if a state is accepted or not
     *
     * @param s
     * @param state
     * @return a boolean based on the acceptance of a state
     */
    public boolean accepts(String s, DFAState state) {
        if(state == null)
        {
            return false;
        }
        else if(s.length() == 1){
            return finalStates.containsValue(state.getTransistion(s.charAt(0)));
        }
        else{
            return accepts(s.substring(1), state.getTransistion(s.charAt(0)));
        }
    }

    /**
     * checks if the DFA will accept the input string
     *
     * @param s - the input string
     * @return
     */
    @Override
    public boolean accepts(String s){
        if(s.isEmpty() || initialState == null) return false;
        return accepts(s, initialState);
    }

    /**
     * returns the alphabet character
     *
     * @return the character
     */
    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    /**
     * gets the name of the state
     *
     * @param name of a state
     * @return the name of the state
     */
    @Override
    public State getState(String name) {

        if (dfa.containsKey(name)) {
            return dfa.get(name);
        }

        return null;
    }

    /**
     * Checks if the state is the final state
     *
     * @param name the name of the state
     * @return true or false
     */
    @Override
    public boolean isFinal(String name) {
        return finalStates.containsKey(name);
    }

    /**
     * Checks if the state is the start state or not
     *
     * @param name the name of the state
     * @return true or false
     */
    @Override
    public boolean isStart(String name) {
        return initialState.getName().equals(name);
    }

    /**
     * Adds a transition to the DFA
     *
     * @param fromState is the label of the state where the transition starts
     * @param toState is the label of the state where the transition ends
     * @param onSymb is the symbol from the DFA's alphabet.
     * @return true if the transition was added and false otherwise
     */
    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {

        if (dfa.containsKey(fromState) && dfa.containsKey(toState) && sigma.contains(onSymb)) {
            DFAState from = dfa.get(fromState);
            DFAState to = dfa.get(toState);

            from.createNewTransition(onSymb, to);
            return true;
        }

        return false;
    }

    /**
     * Swaps two states
     *
     * @param symb1
     * @param symb2
     * @return a copy of the dfa with the switched states
     */
    @Override
    public DFA swap(char symb1, char symb2) {
        DFA newDFA = new DFA();

        //Copy the alphabet and states, setting up the initial and final states as necessary
        for (String stateName : this.dfa.keySet()) {
            newDFA.addState(stateName);
            if (this.finalStates.containsKey(stateName)) {
                newDFA.setFinal(stateName);
            }
            if (this.initialState != null && this.initialState.getName().equals(stateName)) {
                newDFA.setStart(stateName);
            }
        }
        newDFA.sigma = new HashSet<>(this.sigma);

        //Iterate through the existing DFA to copy and swap transitions
        for (Map.Entry<String, DFAState> entry : this.dfa.entrySet()) {
            String fromStateName = entry.getKey();
            DFAState fromState = entry.getValue();

            for (char symbol : this.sigma) {
                DFAState toState = fromState.getTransistion(symbol);
                if (toState != null) {
                    // Determine the correct symbol for the transition in the new DFA
                    char transitionSymbol = symbol;
                    if (symbol == symb1) {
                        transitionSymbol = symb2;
                    } else if (symbol == symb2) {
                        transitionSymbol = symb1;
                    }

                    newDFA.addTransition(fromStateName, toState.getName(), transitionSymbol);
                }
            }
        }

        return newDFA;
    }



    /**
     * toString method. Parses through the different
     * sets and maps and populates the string
     * to the specified string requirement.
     *
     *
     * @return a string
     */
    public String toString() {

        StringBuilder builder = new StringBuilder();

        //States (Q)
        builder.append("Q={");
        for (String stateName : dfa.keySet()) {
            builder.append(stateName);
        }
        builder.append("}\n");

        //Alphabet (Sigma)
        builder.append("Sigma = {");
        for (char symbol : sigma) {
            builder.append(symbol).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);     //Needed to remove extra whitespace.
        builder.append("}\n");

        //Delta columns
        builder.append("delta =\n\t");
        boolean first = true;
        for (char symbol : sigma) {
            if (first) {
                first = false;
            } else {
                builder.append("\t");
            }
            builder.append(symbol);
        }
        builder.append("\n");

        //Delta rows
        for (String stateName : dfa.keySet()) {
            builder.append(stateName);
            for (char symbol : sigma) {
                DFAState toState = dfa.get(stateName).getTransistion(symbol);
                String toStateName = toState != null ? toState.getName() : "-";
                builder.append("\t").append(toStateName);
            }
            builder.append("\n");
        }

        //Initial State
        builder.append("q0 = ");
        if (initialState != null) {
            builder.append(initialState.getName());
        } else {
            builder.append("{}");
        }
        builder.append("\n");

        // Final States (F)
        builder.append("F = {");
        for (String stateName : finalStates.keySet()) {
            builder.append(stateName);
            builder.append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);     //Needed to remove extra whitespace.
        builder.append("}\n");

        return builder.toString();
    }
}
