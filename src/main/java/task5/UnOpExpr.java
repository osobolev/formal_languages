package task5;

public class UnOpExpr extends ExprNode {

    public final Token op;
    public final ExprNode right;

    public UnOpExpr(Token op, ExprNode right) {
        this.op = op;
        this.right = right;
    }

    @Override
    public String toString() {
        if (op.type == TokenType.EXCLAM) {
            return right.toString() + op.text;
        } else {
            return op.text + right.toString();
        }
    }
}
