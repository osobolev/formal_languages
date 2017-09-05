package task1.p2;

import task1.sm.StateMachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Реализация регулярного выражения [0-9]+ с помощью универсального конечного автомата (поиск чисел в тексте)
 */
public class RegMatchInteger2 {

    public static final int START = 0;
    public static final int DIGITS = 1;

    public static void main(String[] args) {
        List<Character> digits = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Set<Integer> endStates = Collections.singleton(DIGITS);
        StateMachine sm = new StateMachine(START, endStates);
        // Переход START -> DIGITS при '0'..'9' на входе
        sm.add(START, digits, DIGITS);
        // Переход DIGITS -> DIGITS при '0'..'9' на входе
        sm.add(DIGITS, digits, DIGITS);

        // Поиск всех вхождений в строке:
        String str = "abba 01.01.2017 xyzzy 02.02.2017";
        sm.findAll(str);
    }
}
