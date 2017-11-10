package task5;

public class NumberExpr extends ExprNode {

    public final Token number;

    public NumberExpr(Token number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return number.text;
    }
}
