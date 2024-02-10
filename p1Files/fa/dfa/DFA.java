package fa.dfa;

import fa.DFAState;
import fa.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DFA implements DFAInterface {

    private Map<String, DFAState> dfa;
    private DFAState initialState;

    public DFA() {
        dfa = new HashMap<>();
        initialState = null;
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

        if (dfa.containsKey(name)) {
            dfa.get(name).makeFinalState();
            return true;
        }

        return false;
    }

    @Override
    public boolean setStart(String name) {

        if (initialState == null && dfa.containsKey(name)) {
            initialState = dfa.get(name);
            return true;
        }

        return false;
    }

    @Override
    public void addSigma(char symbol) {

    }

    @Override
    public boolean accepts(String s) {
        return false;
    }

    @Override
    public Set<Character> getSigma() {
        return null;
    }

    @Override
    public State getState(String name) {
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        return false;
    }

    @Override
    public boolean isStart(String name) {
        return false;
    }

    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        return false;
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        return null;
    }
}
