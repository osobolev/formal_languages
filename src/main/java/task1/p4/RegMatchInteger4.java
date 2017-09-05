package task1.p4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Использование стандартных классов java.util.regex для поиска по регулярному выражению [0-9]+ (поиск чисел в тексте)
 */
public class RegMatchInteger4 {

    private static void findAll(String str, Pattern regexp) {
        Matcher matcher = regexp.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    public static void main(String[] args) {
        // Поиск всех вхождений в строке:
        String str = "abba 01.01.2017 xyzzy 02.02.2017 ???";
        findAll(str, Pattern.compile("[0-9]+"));
    }
}
