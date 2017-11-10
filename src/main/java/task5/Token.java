package task5;

/**
 * Описание лексемы
 */
public class Token {

    /**
     * Тип лексемы
     */
    public final TokenType type;
    /**
     * Текст лексемы
     */
    public final String text;
    /**
     * Индекс начала лексемы во входной строке (от 0)
     */
    public final int from;
    /**
     * Индекс символа, идущего после лексемы во входной строке (от 0). Таким образом, длина текста лексемы = to - from.
     */
    public final int to;

    public Token(TokenType type, String text, int from, int to) {
        this.type = type;
        this.text = text;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return type + ": " + text;
    }
}
