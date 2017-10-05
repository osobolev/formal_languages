package task3;

import task2.Lexer;
import task2.ParseException;
import task2.Token;
import task2.TokenType;

import java.util.List;

/**
 * Грамматический разбор грамматики
 * выражение ::= ЧИСЛО ('+' ЧИСЛО)*
 */
public class Parser1 {

    /**
     * Список лексем
     */
    private final List<Token> tokens;
    /**
     * Индекс текущей лексемы
     */
    private int index = 0;

    public Parser1(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Проверка типа текущей лексемы.
     *
     * @param type предполагаемый тип лексемы
     * @return не null, если текущая лексема предполагаемого типа (при этом текущи индекс сдвигается на 1);
     * null, если текущая лексема другого типа
     */
    private Token match(TokenType type) {
        if (index >= tokens.size())
            return null;
        Token token = tokens.get(index);
        if (token.type != type)
            return null;
        index++;
        return token;
    }

    /**
     * Сообщение об ошибке с указанием текущей позиции в тексте.
     *
     * @param message текст сообщения
     */
    private void error(String message) throws ParseException {
        // Позиция ошибки в тексте
        int errorPosition;
        if (index >= tokens.size()) {
            // Мы стоим в конце текста
            if (tokens.isEmpty()) {
                // Лексем не было вообще - текст пустой; указываем на начало текста
                errorPosition = 0;
            } else {
                // Берем координату после последней лексемы
                errorPosition = tokens.get(tokens.size() - 1).to;
            }
        } else {
            // Берем координату текущей лексемы
            Token token = tokens.get(index);
            errorPosition = token.from;
        }
        throw new ParseException(message, errorPosition);
    }

    /**
     * Грамматический разбор выражения по грамматике
     * выражение ::= ЧИСЛО ('+' ЧИСЛО)*
     */
    public void matchExpression() throws ParseException {
        // В начале должно быть ЧИСЛО:
        Token n1 = match(TokenType.NUMBER);
        if (n1 == null) {
            error("Missing number");
        }
        while (true) {
            // Пока есть символ '+'...
            if (match(TokenType.ADD) != null) {
                // Требуем после плюса снова ЧИСЛО:
                Token n2 = match(TokenType.NUMBER);
                if (n2 == null) {
                    error("Missing number");
                }
            } else {
                break;
            }
        }
    }

    /**
     * Проверка грамматического разбора выражения
     */
    public static void main(String[] args) throws ParseException {
        String expression = "1 + 2 + 3";
        Lexer lexer = new Lexer(expression);
        List<Token> allTokens = lexer.getAllTokens();
        Parser1 parser = new Parser1(allTokens);
        parser.matchExpression();
    }
}
