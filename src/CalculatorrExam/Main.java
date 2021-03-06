package CalculatorrExam;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static boolean isExit = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Calculater cal = new Calculater();

        System.out.println("Welcome to Calculator");
        while (true) {
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                continue;
            }
            if (input.equals("/exit")) {
                System.out.println("Bye!");
                break;
            }
            if(input.equals("/prev"))
            {
                cal.callPreviousHistory();
                continue;
            }
            if(input.equals("/help"))
            {
                System.out.println("/exit for end program");
                System.out.println("/prev for call previous history");
                continue;
            }
            if (input.contains("=")) {
                cal.setAssignValue(input);
            }
            else{
                cal.setExpression(input);
            }
        }
    }



}

