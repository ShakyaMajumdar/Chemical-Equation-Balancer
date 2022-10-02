package Balancer;

import java.util.*;
import java.util.stream.Collectors;

import static Balancer.MathUtils.*;

public class Equation {

    List<Token> reactants;
    List<Token> products;

    public Equation(List<Token> reactants, List<Token> products) {
        this.reactants = reactants;
        this.products = products;
    }

    public static Equation parseFrom(String equation) {
        String[] parts = equation.split("->");
        if (parts.length != 2) {
            throw new ParsingException("Required exactly one '->'.");
        }
        String[] reactants = parts[0].split("\\+");
        String[] products = parts[1].split("\\+");
        if (reactants.length == 0 || products.length == 0) {
            throw new ParsingException("Empty reactants/products list.");
        }
        return new Equation(
            Arrays.stream(reactants).map(String::strip).map(Token::parseFrom).collect(Collectors.toList()),
            Arrays.stream(products).map(String::strip).map(Token::parseFrom).collect(Collectors.toList())
        );
    }

    public void balance() {
        int n = reactants.size() + products.size() - 1;
        reactants.get(0).coefficient = 1;
        List<String> elementList = getElementList();

        int[][] equationCoefficients = new int[n][n];
        int[] rhs = new int[n];

        Set<List<Integer>> existingCoefficients = new HashSet<>();
        int f = 0;
        for (int i = 0; i < elementList.size(); i++) {
            String element = elementList.get(i);
            int[] newCoefficients = new int[n];
            int j = 0;
            for (int k = 1; k < reactants.size(); j++, k++) {
                newCoefficients[j] = reactants.get(k).components.getOrDefault(element, 0);
            }
            for (int k = 0; k < products.size(); j++, k++) {
                newCoefficients[j] = products.get(k).components.getOrDefault(element, 0);
            }

            if (existingCoefficients.contains(Arrays.stream(simplify(newCoefficients)).boxed().collect(Collectors.toList()))) {
                f++;
                continue;
            }
            if (i - f >= n) {
                break;
            }
            existingCoefficients.add(Arrays.stream(simplify(newCoefficients)).boxed().collect(Collectors.toList()));
            equationCoefficients[i - f] = newCoefficients;
            rhs[i - f] = -1 * reactants.get(0).components.getOrDefault(element, 0);
        }

        int[] coefficients = new int[n + 1];
        coefficients[0] = det(equationCoefficients);

        System.arraycopy(solveEquationSystem(equationCoefficients, rhs), 0, coefficients, 1, n);
        coefficients = simplify(coefficients);
        for (int i = 0; i < reactants.size(); i++) {
            reactants.get(i).coefficient = -coefficients[i];
        }
        for (int i = 0; i < products.size(); i++) {
            products.get(i).coefficient = coefficients[i+reactants.size()];
        }
    }

    public String toString() {
        String r = reactants.stream().map(Token::toString).collect(Collectors.joining(" + "));
        String p = products.stream().map(Token::toString).collect(Collectors.joining(" + "));

        return r + " -> " + p;
    }

    private List<String> getElementList() {
        Set<String> elementSet = new HashSet<>();
        reactants.forEach(reactant -> elementSet.addAll(reactant.components.keySet()));
        products.forEach(product -> elementSet.addAll(product.components.keySet()));
        return new ArrayList<>(elementSet);
    }
}
