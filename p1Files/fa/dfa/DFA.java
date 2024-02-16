package fa.dfa;

import fa.State;

import java.util.*;

public class DFA implements DFAInterface {

    private Map<String, DFAState> dfa;
    private DFAState initialState;
    private Map<String, DFAState> finalStates;
    private Set<Character> sigma;

    public DFA() {
        dfa = new LinkedHashMap<>();
        finalStates = new LinkedHashMap<>();
        initialState = null;
        sigma = new LinkedHashSet<>();
    }

    @Override
    public boolean addState(String name) {

        if (!dfa.containsKey(name)) {
            dfa.put(name, new DFAState(name));
            return true;
        }

        return false;
    }

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

    @Override
    public boolean setStart(String name) {

        if (dfa.containsKey(name)) {
            initialState = dfa.get(name);
            return true;
        }

        return false;
    }

    @Override
    public void addSigma(char symbol) {
        sigma.add(symbol);
    }

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

    @Override
    public boolean accepts(String s){
        if(s.isEmpty() || initialState == null) return false;
        return accepts(s, initialState);
    }

    @Override
    public Set<Character> getSigma() {
        return sigma;
    }

    @Override
    public State getState(String name) {

        if (dfa.containsKey(name)) {
            return dfa.get(name);
        }

        return null;
    }

    @Override
    public boolean isFinal(String name) {
        return finalStates.containsKey(name);
    }

    @Override
    public boolean isStart(String name) {
        return initialState.getName().equals(name);
    }

    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {

        if (dfa.containsKey(fromState) && dfa.containsKey(toState) && sigma.contains(onSymb)) {
            //Need more significant names lmao
            DFAState from = dfa.get(fromState);
            DFAState to = dfa.get(toState);

            from.createNewTransition(onSymb, to);
            return true;
        }

        return false;
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        DFA newDFA = new DFA();

        // Copy the alphabet and states, setting up the initial and final states as necessary
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

        // Iterate through the existing DFA to copy and swap transitions
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
     * toString method. Uses an iterator to parse through
     * each set/map. Uses a string builder as well to append.
     * Matches the string example in DFAInterface.
     *
     *
     * @return
     */
    public String toString() {

        StringBuilder builder = new StringBuilder();

        // States (Q)
        builder.append("Q={");
        for (String stateName : dfa.keySet()) {
            builder.append(stateName);
        }
        builder.append("}\n");

        // Alphabet (Sigma)
        builder.append("Sigma = {");
        for (char symbol : sigma) {
            builder.append(symbol).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}\n");

        // Transition Function (delta)
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

        // Table rows for the transition function
        for (String stateName : dfa.keySet()) {
            builder.append(stateName);
            for (char symbol : sigma) {
                DFAState toState = dfa.get(stateName).getTransistion(symbol);
                String toStateName = toState != null ? toState.getName() : "-";
                builder.append("\t").append(toStateName);
            }
            builder.append("\n");
        }

        // Initial State (q0)
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
        }
        builder.append("}\n");

        return builder.toString();
    }
}
