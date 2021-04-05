package Balancer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Token {
    TokenType tokenType; //reactant or product

    String literal;
    String substance;

    int coefficient;

    Map<String, Integer> components = new HashMap<>();

    Token(String literal, TokenType tokenType) {
        this.literal = literal;
        this.tokenType = tokenType;
    }

    public String toString() {
        return coefficient + " " + substance + " " + components + " " + literal + " " + tokenType;
    }

    public void parse() {

        if (!Character.isDigit(literal.charAt(0)))
            literal = 1 + literal;

        Matcher startsWithNumberMatcher = Pattern.compile("^\\d+").matcher(literal);
        if (startsWithNumberMatcher.find()) {
            coefficient = Integer.parseInt(startsWithNumberMatcher.group(0));
            substance = literal.substring(startsWithNumberMatcher.end());
        }

        StringBuilder element = new StringBuilder();
        int multiplier = coefficient;
        for (int i = 0; i < substance.length(); i++) {
            if (i == substance.length() - 1 ||
                    (Character.isAlphabetic(substance.charAt(i)) && Character.isUpperCase(substance.charAt(i + 1))) ||
                    Character.isDigit(substance.charAt(i)) ||
                    substance.charAt(i + 1) == '(' ||
                    substance.charAt(i + 1) == ')'
            ) {
                element.append(substance.charAt(i));
                addElement(element.toString(), multiplier);
                element = new StringBuilder();
            } else if (substance.charAt(i) == '(') {
                multiplier *= substance.charAt(substance.indexOf(')', i) + 1) - 48;
            } else if (substance.charAt(i) == ')') {
                multiplier = coefficient;
                i++;
            } else {
                element.append(substance.charAt(i));
            }

        }
    }

    private void addElement(String element, int multiplier) {
        if (element.equals(""))
            return;

        if (!Character.isDigit(element.charAt(element.length() - 1))) {
            element += "1";
        }

        Matcher endsWithNumberMatcher = Pattern.compile("\\d+$").matcher(element);

        int elementCoefficient = 1;

        if (endsWithNumberMatcher.find()) {
            elementCoefficient = Integer.parseInt(endsWithNumberMatcher.group(0));
            element = element.substring(0, endsWithNumberMatcher.start());
        }

        components.merge(element, elementCoefficient * multiplier, Integer::sum);
    }
}
//>> CH3COOH -> 2C + 2H2