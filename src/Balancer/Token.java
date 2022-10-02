package Balancer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Token {
    String substance;

    int coefficient;

    Map<String, Integer> components;

    public Token(String substance, int coefficient, Map<String, Integer> components) {
        this.substance = substance;
        this.coefficient = coefficient;
        this.components = components;
    }

    public static Token parseFrom(String token) {
        String substance;
        int coefficient;
        Map<String, Integer> components = new HashMap<>();

        if (!Character.isDigit(token.charAt(0)))
            token = 1 + token;

        Matcher startsWithNumberMatcher = Pattern.compile("^\\d+").matcher(token);
        if (startsWithNumberMatcher.find()) {
            coefficient = Integer.parseInt(startsWithNumberMatcher.group(0));
            substance = token.substring(startsWithNumberMatcher.end());
        } else {
            throw new AssertionError("unreachable");
        }

        StringBuilder element = new StringBuilder();
        int multiplier = 1;
        for (int i = 0; i < substance.length(); i++) {
            if (i == substance.length() - 1 ||
                    (Character.isAlphabetic(substance.charAt(i)) && Character.isUpperCase(substance.charAt(i + 1))) ||
                    Character.isDigit(substance.charAt(i)) ||
                    substance.charAt(i + 1) == '(' ||
                    substance.charAt(i + 1) == ')'
            ) {
                element.append(substance.charAt(i));
                addElement(components, element.toString(), multiplier);
                element = new StringBuilder();
            } else if (substance.charAt(i) == '(') {
                multiplier *= substance.charAt(substance.indexOf(')', i) + 1) - 48;
            } else if (substance.charAt(i) == ')') {
                multiplier = 1;
                i++;
            } else {
                element.append(substance.charAt(i));
            }

        }
        return new Token(substance, coefficient, components);
    }

    public String toString() {
        return (coefficient == 1 ? "" : coefficient) + "" + substance;
    }

    private static void addElement(Map<String, Integer> components, String element, int multiplier) {
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