package Balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Balancer {
    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print(">> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            balance(line);
        }
    }

    private static void balance(String unbalanced) {
        Lexer lexer = new Lexer(unbalanced);
        List<Token> tokens = lexer.getTokens();
        for (Token token: tokens) {
            token.parse();
            System.out.println(token);
        }
    }
}
