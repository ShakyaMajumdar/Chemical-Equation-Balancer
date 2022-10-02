package Balancer;

import java.util.Arrays;

public class MathUtils {
    public static int[] simplify(int[] arr) {
        int gcd = Arrays.stream(arr).reduce(arr[0], MathUtils::gcd);
        return Arrays.stream(arr).map(x -> x / gcd).toArray();
    }

    public static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        int l = Math.min(a, b);
        int g = Math.max(a, b);
        if (l == 0) {
            return g;
        }
        return gcd(g % l, l);
    }

    public static int det(int[][] matrix) {
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

    public static int[] solveEquationSystem(int[][] coefficients, int[] rhs) {
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
}
