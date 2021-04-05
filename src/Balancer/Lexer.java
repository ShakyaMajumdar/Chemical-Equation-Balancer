package Balancer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Lexer {
    private final String source;

    Lexer(String source) {
        this.source = source;
    }

    List<Token> getTokens() {

        String[] temp = source.split("->");

        List<Token> reactants = Arrays.stream(temp[0].split("\\+"))
                .map(x -> new Token(x.strip(), TokenType.REACTANT))
                .collect(Collectors.toList());

        List<Token> products = Arrays.stream(temp[1].split("\\+"))
                .map(x -> new Token(x.strip(), TokenType.PRODUCT))
                .collect(Collectors.toList());

        return Stream.concat(reactants.stream(), products.stream()).collect(Collectors.toList());
    }
}
