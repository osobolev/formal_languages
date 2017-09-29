package task2;

import java.util.HashMap;
import java.util.Map;
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
     * Попытка сопоставить текст, начиная с текущей позиции index, с
     * регулярным выражением.
     *
     * @param pattern регулярное выражение
     * @return -1, если если регулярное выражение не удалось найти в
     * текущей позиции; значение >= 0 - индекс первого символа,
     * следующего после найденной лексемы, соответствующей
     * регулярному выражению
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

    private Token matchNumber() {
        Pattern numberPattern = Pattern.compile("[0-9]+");
        int matched = match(numberPattern);
        if (matched < 0)
            return null;
        String numberText = str.substring(index, matched);
        return new Token(TokenType.NUMBER, numberText, index, matched);
    }

    private final Map<String, TokenType> SYMBOL_MAP = new HashMap<>();

    {
        SYMBOL_MAP.put("+", TokenType.ADD);
        SYMBOL_MAP.put("-", TokenType.SUB);
        SYMBOL_MAP.put("*", TokenType.MUL);
        SYMBOL_MAP.put("/", TokenType.DIV);
        SYMBOL_MAP.put("(", TokenType.LPAR);
        SYMBOL_MAP.put(")", TokenType.RPAR);
    }

    private Token matchAnySymbol() {
        for (Map.Entry<String, TokenType> entry : SYMBOL_MAP.entrySet()) {
            String key = entry.getKey();
            TokenType value = entry.getValue();
            Pattern symbolPattern = Pattern.compile(Pattern.quote(key));
            int matched = match(symbolPattern);
            if (matched < 0)
                continue;
            String symbolText = str.substring(index, matched);
            return new Token(value, symbolText, index, matched);
        }
        return null;
    }

    private Token matchSpaces() {
        int i = index;
        while (i < str.length()) {
            char ch = str.charAt(i);
            if (ch <= ' ') {
                i++;
            } else {
                break;
            }
        }
        if (i > index) {
            String spaces = str.substring(index, i);
            return new Token(TokenType.SPACES, spaces, index, i);
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
        Token numberToken = matchNumber();
        if (numberToken != null)
            return numberToken;
        Token symbolToken = matchAnySymbol();
        if (symbolToken != null)
            return symbolToken;
        // Символ в текущей позиции не подходит ни к одной из возможных лексем - ошибка:
        throw new ParseException(
            "Unexpected character '" + str.charAt(index) + "'", index
        );
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
            if (token.type != TokenType.SPACES) {
                // Непробельную лексему возвращаем:
                return token;
            }
        }
    }
}
