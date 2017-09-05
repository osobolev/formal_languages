package task1.p3;

import task1.sm.StateMachine;

import java.util.Arrays;
import java.util.List;

/**
 * Реализация регулярного выражения [0-9]+ с помощью универсального конечного автомата (поиск чисел в тексте)
 */
public class RegMatchInteger3 {

    public static void main(String[] args) {
        List<Character> digits = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
        Builder builder = new Builder();
        builder.nextMultipleOf(digits);
        StateMachine sm = builder.getStateMachine();

        // Поиск всех вхождений в строке:
        String str = "abba 01.01.2017 xyzzy 02.02.2017 ???";
        sm.findAll(str);
    }
}
