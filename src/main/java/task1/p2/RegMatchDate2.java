package task1.p2;

import task1.sm.StateMachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Реализация регулярного выражения [0-9]+\.[0-9]+\.[0-9]+ с помощью универсального конечного автомата (поиск дат в тексте)
 */
public class RegMatchDate2 {

    public static final int START = 0;
    public static final int DIGIT1 = 1;
    public static final int DOT1 = 2;
    public static final int DIGIT2 = 3;
    public static final int DOT2 = 4;
    public static final int DIGIT3 = 5;

    public static void main(String[] args) {
        List<Character> digits = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Set<Integer> endStates = Collections.singleton(DIGIT3);
        StateMachine sm = new StateMachine(START, endStates);
        sm.add(START, digits, DIGIT1);
        sm.add(DIGIT1, digits, DIGIT1);
        sm.add(DIGIT1, '.', DOT1);
        sm.add(DOT1, digits, DIGIT2);
        sm.add(DIGIT2, digits, DIGIT2);
        sm.add(DIGIT2, '.', DOT2);
        sm.add(DOT2, digits, DIGIT3);
        sm.add(DIGIT3, digits, DIGIT3);

        // Поиск всех вхождений в строке:
        String str = "abba 01.01.2017 xyzzy 02.02.2017";
        sm.findAll(str);
    }
}
