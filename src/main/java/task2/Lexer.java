package task2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Лексический анализатор
 */
public class Lexer {

    /**
     * Входная строка
     */
    private final String str;
    /**
     * Текущая позиция во входной строке
     */
    private int index = 0;

    public Lexer(String str) {
        this.str = str;
    }

    /**
     * Попытка сопоставить текст, начиная с текущей позиции index, с регулярным выражением.
     *
     * @param pattern регулярное выражение
     * @return -1, если если регулярное выражение не удалось найти в текущей позиции;
     * значение >= 0 - индекс первого символа, следующего после найденной лексемы, соответствующей регулярному выражению
     */
    private int match(Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        // Устанавливаем регион поиска - начиная с текущей позиции:
        matcher.region(index, str.length());
        if (matcher.lookingAt()) {
            // Да, в текущей позиции найдено регулярное выражение - возвращаем индекс символа _после_ найденной лексемы
            return matcher.end();
        } else {
            // Не найдено совпадения - возвращаем -1
            return -1;
        }
    }

    /**
     * Попытка найти следующую лексему, начиная с текущей позиции index.
     *
     * @param type тип лексемы
     * @param pattern регулярное выражение, описывающее лексему
     * @return null, если регулярное выражение не удалось найти в текущей позиции;
     * иначе лексему, соответствующую регулярному выражению
     */
    private Token matchToken(TokenType type, Pattern pattern) {
        // Сопоставляем текст, начиная с текущей позиции, с регулярным выражением:
        int end = match(pattern);
        if (end < 0) {
            // Не удалось сопоставить
            return null;
        }
        // Удалось сопоставить, конец текста лексемы - в переменной end:
        return new Token(type, str.substring(index, end), index, end);
    }

    /**
     * Попытка найти целое число в текущей позиции. Целое число описывается регулярным выражением \d+.
     *
     * @return null, если в текущей позиции нет целого числа
     */
    private Token matchInt() {
        return matchToken(TokenType.INT, Pattern.compile("\\d+"));
    }

    /**
     * Попытка найти символ операции в текущей позиции.
     *
     * @return null, если в текущей позиции нет символа операции
     */
    private Token matchSymbol() {
        // Сопоставляем текст, начиная с текущей позиции, с регулярным выражением:
        int end = match(Pattern.compile("[+\\-*/()]"));
        if (end < 0) {
            // Не удалось сопоставить
            return null;
        }
        // Сопоставленный текст:
        String op = str.substring(index, end);
        TokenType type;
        switch (op) {
        case "+":
            type = TokenType.PLUS;
            break;
        case "-":
            type = TokenType.MINUS;
            break;
        case "*":
            type = TokenType.MULTIPLY;
            break;
        case "/":
            type = TokenType.DIVIDE;
            break;
        case "(":
            type = TokenType.LEFT_PARENS;
            break;
        case ")":
            type = TokenType.RIGHT_PARENS;
            break;
        default:
            // Такого не может быть:
            throw new IllegalStateException();
        }
        return new Token(type, op, index, end);
    }

    /**
     * Пропуск всех пробельных символов во входной строке, начиная с текущей позиции.
     * После вызова этого метода текущая позиция стоит либо в конце строки, либо на непробельном символе.
     *
     * @return лексема, содержащая пробельные символы, либо null, если пробелов в текущей позиции не было.
     * Обычно пробельные лексемы не используются, но могут использоваться некоторыми инструментами, для которых
     * наличие пробелов существенно, например утилитами форматирования кода.
     */
    private Token matchSpaces() {
        int i = index;
        while (i < str.length()) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch))
                break;
            i++;
        }
        if (i > index) {
            String spaces = str.substring(index, i);
            return new Token(TokenType.WHITESPACE, spaces, index, i);
        } else {
            return null;
        }
    }

    /**
     * Получение лексемы, стоящей в текущей позиции.
     *
     * @return null, если в строке больше нет лексем
     */
    private Token matchAnyToken() throws ParseException {
        // Мы стоим в конце строки - больше нет лексем:
        if (index >= str.length())
            return null;
        // Перебираем все возможные типы лексем:
        Token spacesToken = matchSpaces();
        if (spacesToken != null)
            return spacesToken;
        Token intToken = matchInt();
        if (intToken != null)
            return intToken;
        Token symbolToken = matchSymbol();
        if (symbolToken != null)
            return symbolToken;
        // Символ в текущей позиции не подходит ни к одной из возможных лексем - ошибка:
        throw new ParseException("Unexpected character '" + str.charAt(index) + "'", index);
    }

    /**
     * Получение лексемы, стоящей в текущей позиции и перемещение текущей позиции дальше.
     *
     * @return null, если в строке больше нет лексем
     */
    public Token nextToken() throws ParseException {
        while (true) {
            Token token = matchAnyToken();
            if (token == null) {
                // Строка закончилась, больше нет лексем:
                return null;
            }
            // Перемещаем текущую позицию после найденной лексемы:
            index = token.to;
            if (token.type == TokenType.WHITESPACE) {
                // Пропускаем пробельные лексемы - в данном случае они нам неинтересны:
                continue;
            }
            // Непробельную лексему возвращаем:
            return token;
        }
    }
}
