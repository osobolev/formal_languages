package task1.sm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Конечный автомат, описывающий регулярное выражение
 */
public class StateMachine {

    private final int startState;
    private final Set<Integer> endStates;
    private final Map<Integer, Map<Character, Integer>> transitions = new HashMap<>();

    /**
     * @param startState начальное состояние
     * @param endStates множество успешных конечных состояний
     */
    public StateMachine(int startState, Set<Integer> endStates) {
        this.startState = startState;
        this.endStates = endStates;
    }

    public void add(Integer from, Character ch, Integer to) {
        Map<Character, Integer> fromMap = transitions.computeIfAbsent(from, k -> new HashMap<>());
        fromMap.put(ch, to);
    }

    public void add(Integer from, List<Character> chars, Integer to) {
        for (Character ch : chars) {
            add(from, ch.charValue(), to);
        }
    }

    public Integer getNext(Integer state, Character ch) {
        Map<Character, Integer> stateTransitions = transitions.get(state);
        return stateTransitions.get(ch);
    }

    /**
     * Сопоставление подстроки с регулярным выражением.
     *
     * @param str строка
     * @param from индекс в строке, с которого начинается сопоставление
     * @return -1 если подстрока начиная с позиции from не сопоставлена с регулярным выражением; неотрицательное
     * значение, если подстрока сопоставлена с регулярным выражением - индекс в строке символа, следующего за
     * сопоставленной подстрокой
     */
    public int match(String str, int from) {
        int state = startState;
        int i = from;
        while (i < str.length()) {
            char ch = str.charAt(i);
            Integer nextState = getNext(state, ch);
            if (nextState == null)
                break;
            state = nextState.intValue();
            i++;
        }
        if (endStates.contains(state)) {
            return i;
        } else {
            return -1;
        }
    }

    /**
     * Поиск всех сопоставлений в строке
     */
    public void findAll(String str) {
        int i = 0;
        while (i < str.length()) {
            int match = match(str, i);
            if (match < 0) {
                i++;
            } else {
                System.out.println(str.substring(i, match));
                i = match;
            }
        }
    }
}
