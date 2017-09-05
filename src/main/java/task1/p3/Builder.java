package task1.p3;

import task1.sm.StateMachine;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Построение конечного автомата для простых регулярных выражений, содержащих только буквы и метасимвол '+'.
 */
public class Builder {

    public static final int START = 0;

    private final Set<Integer> endStates = new HashSet<>();
    private final StateMachine sm = new StateMachine(START, endStates);
    private int state = START;

    public Builder nextOneOf(List<Character> chars) {
        int nextState = state + 1;
        sm.add(state, chars, nextState);
        state = nextState;
        return this;
    }

    public Builder next(Character ch) {
        int nextState = state + 1;
        sm.add(state, ch, nextState);
        state = nextState;
        return this;
    }

    public Builder nextMultipleOf(List<Character> chars) {
        int nextState = state + 1;
        sm.add(state, chars, nextState);
        sm.add(nextState, chars, nextState);
        state = nextState;
        return this;
    }

    public StateMachine getStateMachine() {
        endStates.add(state);
        return sm;
    }
}
