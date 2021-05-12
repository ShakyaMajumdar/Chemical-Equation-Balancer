package Balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

        int n = tokens.size() - 1;
        tokens.get(0).coefficient = 1;
        List<String> elementList = getElementList(tokens);

        int[][] equationCoefficients = new int[n][n];
        int[] rhs = new int[n];

        Set<List<Integer>> existingCoefficients = new HashSet<>();
        int f = 0;
        for (int i = 0; i < elementList.size(); i++) {
            String element = elementList.get(i);
            int[] newCoefficients = new int[n];

            for (int j = 0; j < tokens.size() - 1; j++) {
                Token token = tokens.get(j + 1);
                newCoefficients[j] = ((token.tokenType == TokenType.PRODUCT) ? -1 : 1) * token.components.getOrDefault(element, 0);
            }

            if (existingCoefficients.contains(Arrays.stream(simplify(newCoefficients)).boxed().collect(Collectors.toList()))) {
                f++;
                continue;
            }
            existingCoefficients.add(Arrays.stream(simplify(newCoefficients)).boxed().collect(Collectors.toList()));
            equationCoefficients[i - f] = newCoefficients;
            rhs[i - f] = -1 * tokens.get(0).components.getOrDefault(element, 0);
        }

        tokens.get(0).coefficient = det(equationCoefficients);

        int[] coefficients = new int[n + 1];
        coefficients[0] = det(equationCoefficients);

        System.arraycopy(solveEquations(equationCoefficients, rhs), 0, coefficients, 1, n);
        coefficients = simplify(coefficients);
        for (int i = 0; i < tokens.size(); i++) {
            tokens.get(i).coefficient = Math.abs(coefficients[i]);
        }

        System.out.println(equationString(tokens));
    }

    private static boolean isBalanced(List<Token> tokens) {
        return sumTokens(tokens, TokenType.REACTANT).equals(sumTokens(tokens, TokenType.PRODUCT));
    }

    private static Map<String, Integer> sumTokens(List<Token> tokens, TokenType type) {
        return tokens
                .stream()
                .filter(x -> x.tokenType == type)
                .map(x -> x.components)
                .reduce(new HashMap<>(), (sum, token) ->
                        Stream.concat(sum.entrySet().stream(), token.entrySet().stream())
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum)));
    }

    private static List<String> getElementList(List<Token> tokens) {
        Set<String> elementSet = new HashSet<>();
        tokens.forEach(token -> elementSet.addAll(token.components.keySet()));
        return new ArrayList<>(elementSet);
    }

    private static int[] solveEquations(int[][] coefficients, int[] rhs) {
        int n = coefficients.length;
        int[] solution = new int[n];
        for (int x = 0; x < n; x++) {
            int[][] numerator = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j == x) {
                        numerator[i][j] = rhs[i];
                    } else {
                        numerator[i][j] = coefficients[i][j];
                    }
                }
            }
            solution[x] = det(numerator);
        }
        return solution;
    }

    private static int det(int[][] matrix) {
        int dim = matrix.length;
        if (dim == 1) {
            return matrix[0][0];
        }
        int det = 0;
        for (int i = 0; i < dim; i++) {
            int sign = (i % 2 == 0) ? 1 : -1;
            int[][] subMatrix = new int[dim - 1][dim - 1];

            for (int j = 1; j < dim; j++) {
                int f = 0;
                for (int k = 0; k < dim; k++) {
                    if (k == i) {
                        f = 1;
                        continue;
                    }
                    subMatrix[j - 1][k - f] = matrix[j][k];
                }
            }
            det += sign * matrix[0][i] * det(subMatrix);
        }
        return det;
    }

    private static int[] simplify(int[] arr) {
        int gcd = Arrays.stream(arr).reduce(arr[0], Balancer::gcd);
        return Arrays.stream(arr).map(x -> x / gcd).toArray();
    }

    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        int l = Math.min(a, b);
        int g = Math.max(a, b);
        if (l == 0) {
            return g;
        }
        return gcd(g % l, l);
    }

    private static String equationString(List<Token> tokens) {
        return tokens
                .stream()
                .filter(x -> x.tokenType == TokenType.REACTANT)
                .map(x -> x.toString())
                .collect(Collectors.joining(" + "))
                + " -> " +
                tokens
                .stream()
                .filter(x -> x.tokenType == TokenType.PRODUCT)
                .map(x -> x.toString())
                .collect(Collectors.joining(" + "));
    }
}
