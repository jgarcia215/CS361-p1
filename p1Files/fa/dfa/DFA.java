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
        finalStates = new HashMap<>();
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
        builder.append("Q = {");
        //StringJoiner statesJoiner = new StringJoiner();
        for (String stateName : dfa.keySet()) {
           builder.append(stateName);
        }
        //builder.append(statesJoiner.toString());
        builder.append("}\n");

        // Alphabet (Sigma)
        builder.append("Sigma = { ");
        StringJoiner sigmaJoiner = new StringJoiner(" ");
        for (char symbol : sigma) {
            sigmaJoiner.add(Character.toString(symbol));
        }
        builder.append(sigmaJoiner.toString());
        builder.append(" }\n");

        // Transition Function (delta)
        builder.append("delta =\n\t");
        StringJoiner headerJoiner = new StringJoiner("\t");
        for (char symbol : sigma) {
            headerJoiner.add(Character.toString(symbol));
        }
        builder.append("\t").append(headerJoiner.toString()).append("\n");

        for (String stateName : dfa.keySet()) {
            builder.append(stateName).append("\t");
            for (char symbol : sigma) {
                DFAState toState = dfa.get(stateName).getTransistion(symbol);
                String toStateName = toState != null ? toState.getName() : "-";
                builder.append("\t").append(toStateName);
            }
            builder.append("\n");
        }

        // Initial State (q0)
        if (initialState != null) {
            builder.append("q0 = ").append(initialState.getName()).append("\n");
        } else {
            builder.append("q0 = {}\n");
        }

        // Final States (F)
        builder.append("F = { ");
        StringJoiner finalStatesJoiner = new StringJoiner(" ");
        for (String stateName : finalStates.keySet()) {
            finalStatesJoiner.add(stateName);
        }
        builder.append(finalStatesJoiner.toString());
        builder.append(" }\n");

        return builder.toString();
        /**
         * Q = { a b }
         * Sigma = { 0 1 }
         * delta =
         *		0	1
         *	a	a	b
         *	b	a	b
         * q0 = a
         * F = { b }
         */

//        StringBuilder returnStr = new StringBuilder();
//        Iterator itr;
//
//        //States
//        returnStr.append("Q = { ");
//        itr = dfa.keySet().iterator();
//        while (itr.hasNext()) {
//            returnStr.append(itr.next());
//        }
//        returnStr.append(" }\n");   //End States
//
//
//        //Sigma
//        returnStr.append("Sigma = { ");
//        itr = sigma.iterator();
//        while (itr.hasNext()) {
//            returnStr.append(itr.next());
//        }
//        returnStr.append(" }\n");   //End sigma
//
//
//
//
//
//
//
//        //////////////////////////////////////////////////////
//        //////////////// NEED TO FINISH DELTA ////////////////
//
//
//        //Delta
//        returnStr.append("delta = \n\t\t");     //Add column values to table. Repeats the above steps since sigma is
//        itr = sigma.iterator();                 //the same as the column values.
//        while (itr.hasNext()) {
//            returnStr.append(itr.next()).append("\t");
//        }
//
//
//        Iterator stateIterator;     //Creating row values on table now. This is where we need to print out each transition for the states. ROW BY ROW.
//        itr = dfa.keySet().iterator();
//        DFAState state;
//
//        while (itr.hasNext()) {
//            //Add row value. For example: "a"
//            //returnStr.append(tempState + "\t");
//
//            // Add transition values \\
//            //tempState.getNextState();
//        }
//
//
//
//        //////////////// NEED TO FINISH DELTA ////////////////
//        //////////////////////////////////////////////////////
//
//
//
//
//
//
//
//        //Initial State
//        returnStr.append("q0 = " + initialState + '\n');
//
//
//        //Final States
//        itr = finalStates.keySet().iterator();
//        returnStr.append("F = { ");
//
//        while (itr.hasNext()) {
//            returnStr.append(itr.next());
//        }
//        returnStr.append(" }\n");   //End final states
//
//
//        //Return the string builder.
//        return returnStr.toString();
    }
}
