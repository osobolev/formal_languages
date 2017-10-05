package task3;

import task2.Lexer;
import task2.ParseException;
import task2.Token;
import task2.TokenType;

import java.util.List;

/**
 * Грамматический разбор грамматики
 * выражение ::= слагаемое ('+' слагаемое)*
 * слагаемое ::= ЧИСЛО
 * с построением дерева разбора
 */
public class Parser2 {

    /**
     * Список лексем
     */
    private final List<Token> tokens;
    /**
     * Индекс текущей лексемы
     */
    private int index = 0;

    public Parser2(List<Token> tokens) {
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
     * Метод для нетерминального символа 'слагаемое'.
     *
     * @return узел дерева, соответствующий слагаемому
     */
    private ExprNode1 matchSlagaemoe() throws ParseException {
        // Должно быть ЧИСЛО:
        Token number = match(TokenType.NUMBER);
        if (number == null) {
            error("Missing number");
        }
        // Преобразуем лексему в узел дерева:
        return new ExprNode1(number);
    }

    /**
     * Грамматический разбор выражения по грамматике
     * выражение ::= слагаемое ('+' слагаемое)*
     * слагаемое ::= ЧИСЛО
     *
     * @return дерево разбора выражения
     */
    public ExprNode1 matchExpression() throws ParseException {
        // В начале должно быть слагаемое:
        ExprNode1 leftNode = matchSlagaemoe();
        while (true) {
            // Пока есть символ '+'...
            Token plus = match(TokenType.ADD);
            if (plus != null) {
                // Требуем после плюса снова слагаемое:
                ExprNode1 rightNode = matchSlagaemoe();
                // Из двух слагаемых формируем дерево с двумя поддеревьями:
                leftNode = new ExprNode1(leftNode, plus, rightNode);
            } else {
                break;
            }
        }
        return leftNode;
    }

    /**
     * Проверка грамматического разбора выражения
     */
    public static void main(String[] args) throws ParseException {
        String expression = "1 + 2 + 3";
        Lexer lexer = new Lexer(expression);
        List<Token> allTokens = lexer.getAllTokens();
        Parser2 parser = new Parser2(allTokens);
        ExprNode1 exprTreeRoot = parser.matchExpression();
        System.out.println(exprTreeRoot.toString());
    }
}
