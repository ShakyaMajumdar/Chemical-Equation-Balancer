package Balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Balancer {
    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (; ; ) {
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
        tokens.forEach(x -> x.parse());
        System.out.println("Is balanced: " + isBalanced(tokens));
    }

    private static boolean isBalanced(List<Token> tokens) {
        Map<String, Integer> reactantSum = tokens
                .stream()
                .filter(x -> x.tokenType == TokenType.REACTANT)
                .map(x -> x.components)
                .reduce(new HashMap<>(), (sum, token) -> Stream.concat(sum.entrySet().stream(), token.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum)));

        Map<String, Integer> productSum = tokens
                .stream()
                .filter(x -> x.tokenType == TokenType.PRODUCT)
                .map(x -> x.components)
                .reduce(new HashMap<>(), (sum, token) -> Stream.concat(sum.entrySet().stream(), token.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum)));

        return reactantSum.equals(productSum);
    }
}
