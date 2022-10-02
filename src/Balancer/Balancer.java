package Balancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Balancer {
    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while (true) {
            System.out.print(">> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            try {
                Equation equation = Equation.parseFrom(line);
                equation.balance();
                System.out.println(equation);
            } catch (ParsingException e) {
                System.out.println("Could not parse");
            } catch (BalancingException e) {
                System.out.println("Could not balance");
            }
        }
    }
}
