package task4;

import java.util.List;

/**
 * Грамматический разбор грамматики
 * выражение ::= слагаемое (('+'|'-') слагаемое)*
 * слагаемое ::= множитель (('*'|'/') множитель)*
 * множитель ::= ('-')? ЧИСЛО | '(' выражение ')' ('!')?
 * с построением дерева разбора.
 */
public class Parser5 {

    /**
     * Список лексем
     */
    private final List<Token> tokens;
    /**
     * Индекс текущей лексемы
     */
    private int index = 0;

    public Parser5(List<Token> tokens) {
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
     * Проверка типа текущей лексемы.
     *
     * @param types возможные типы лексем
     * @return не null, если текущая лексема одного из предполагаемых типов (при этом текущи индекс сдвигается на 1);
     * null, если текущая лексема другого типа
     */
    private Token matchAny(TokenType... types) {
        for (TokenType type : types) {
            Token matched = match(type);
            if (matched != null)
                return matched;
        }
        return null;
    }

    /**
     * Метод для нетерминального символа 'множитель'.
     *
     * @return узел дерева, соответствующий множителю
     */
    private ExprNode1 matchMnozhitel() throws ParseException {
        // В начале может стоять унарный минус:
        Token minus = match(TokenType.SUB);
        Token number = match(TokenType.NUMBER);
        ExprNode1 result;
        if (number != null) {
            // Если это ЧИСЛО, то результат - узел для числа:
            result = new ExprNode1(number);
        } else if (match(TokenType.LPAR) != null) {
            // Если это открывающая скобка, то вызываем разбор выражения в скобках:
            ExprNode1 nested = matchExpression();
            if (match(TokenType.RPAR) == null) {
                error("Missing ')'");
            }
            result = nested;
        } else {
            // Иначе ошибка - других вариантов кроме числа и скобки быть не может:
            error("Number or '(' expected");
            return null;
        }
        // В конце может стоять факториал:
        Token factorial = match(TokenType.EXCLAM);
        if (factorial != null) {
            result = new ExprNode1(null, factorial, result);
        }
        if (minus != null) {
            result = new ExprNode1(null, minus, result);
        }
        return result;
    }

    /**
     * Метод для нетерминального символа 'слагаемое'.
     *
     * @return узел дерева, соответствующий слагаемому
     */
    private ExprNode1 matchSlagaemoe() throws ParseException {
        // В начале должен быть множитель:
        ExprNode1 leftNode = matchMnozhitel();
        while (true) {
            // Пока есть символ '*' или '/'...
            Token op = matchAny(TokenType.MUL, TokenType.DIV);
            if (op != null) {
                // Требуем после умножения/деления снова множитель:
                ExprNode1 rightNode = matchMnozhitel();
                // Из двух множителей формируем дерево с двумя поддеревьями:
                leftNode = new ExprNode1(leftNode, op, rightNode);
            } else {
                break;
            }
        }
        return leftNode;
    }

    /**
     * Грамматический разбор выражения по грамматике
     * выражение ::= слагаемое (('+'|'-') слагаемое)*
     * слагаемое ::= множитель (('*'|'/') множитель)*
     * множитель ::= ('-')? ЧИСЛО | '(' выражение ')' ('!')?
     *
     * @return дерево разбора выражения
     */
    public ExprNode1 matchExpression() throws ParseException {
        // В начале должно быть слагаемое:
        ExprNode1 leftNode = matchSlagaemoe();
        while (true) {
            // Пока есть символ '+' или '-'...
            Token op = matchAny(TokenType.ADD, TokenType.SUB);
            if (op != null) {
                // Требуем после плюса/минуса снова слагаемое:
                ExprNode1 rightNode = matchSlagaemoe();
                // Из двух слагаемых формируем дерево с двумя поддеревьями:
                leftNode = new ExprNode1(leftNode, op, rightNode);
            } else {
                break;
            }
        }
        return leftNode;
    }

    private static long factorial(long n) {
        long result = 1;
        for (long i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private static double eval(ExprNode1 expr) {
        if (expr.isNumber) {
            String text = expr.number.text;
            return Double.parseDouble(text);
        } else {
            if (expr.left == null) {
                double rightValue = eval(expr.right);
                switch (expr.op.type) {
                case SUB: return -rightValue;
                case EXCLAM: return factorial((long) rightValue);
                }
            } else {
                double leftValue = eval(expr.left);
                double rightValue = eval(expr.right);
                switch (expr.op.type) {
                case ADD: return leftValue + rightValue;
                case SUB: return leftValue - rightValue;
                case MUL: return leftValue * rightValue;
                case DIV: return leftValue / rightValue;
                }
            }
            throw new IllegalStateException();
        }
    }

    /**
     * Проверка грамматического разбора выражения
     */
    public static void main(String[] args) throws ParseException {
        String expression = "-1.23 - .2 * 3!";
        Lexer lexer = new Lexer(expression);
        List<Token> allTokens = lexer.getAllTokens();
        Parser5 parser = new Parser5(allTokens);
        ExprNode1 exprTreeRoot = parser.matchExpression();
        System.out.println(exprTreeRoot.toString());
        System.out.println(eval(exprTreeRoot));
    }
}
