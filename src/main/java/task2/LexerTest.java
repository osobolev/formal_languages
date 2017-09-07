package task2;

public class LexerTest {

    public static void main(String[] args) throws ParseException {
        Lexer lexer = new Lexer("1   +   234 \n * 567 \n\n / 8");
        while (true) {
            Token token = lexer.nextToken();
            if (token == null)
                break;
            System.out.println(token);
        }
    }
}
