package com.sg.flooringmastery.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Implementation of UserIO interface
 *
 * @author Narish
 */
public class UserIOImpl implements UserIO {

    final private Scanner input = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(input.nextLine());
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter integer: ");
            userInput = Integer.parseInt(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    @Override
    public double readDouble(String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(input.nextLine());
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter double: ");
            userInput = Double.parseDouble(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    @Override
    public float readFloat(String prompt) {
        System.out.print(prompt);
        return Float.parseFloat(input.nextLine());
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter float: ");
            userInput = Float.parseFloat(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    @Override
    public long readLong(String prompt) {
        System.out.print(prompt);
        return Long.parseLong(input.nextLine());
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long userInput;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter long: ");
            userInput = Long.parseLong(input.nextLine());
        } while (userInput < min || userInput > max);

        return userInput;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        boolean hasErrors;
        BigDecimal userInput = null;

        System.out.println(prompt);

        do {
            try {
                userInput = new BigDecimal(input.nextLine());
                userInput.setScale(2, RoundingMode.HALF_UP);
                hasErrors = false;
            } catch (NumberFormatException e) {
                hasErrors = true;
            }
        } while (hasErrors);

        return userInput;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        boolean hasErrors;
        BigDecimal userInput = null;

        System.out.println(prompt);
        System.out.println("min = " + min);
        System.out.println("max = " + max);
        do {
            System.out.print("Enter number: ");
            try {
                userInput = new BigDecimal(input.nextLine());
                userInput.setScale(2, RoundingMode.HALF_UP);
                hasErrors = false;
            } catch (NumberFormatException e) {
                hasErrors = true;
            }
        } while (hasErrors || userInput.compareTo(min) < 0 || userInput.compareTo(max) > 0);

        return userInput;
    }

    @Override
    public LocalDate readLocalDate(String prompt) {
        boolean hasErrors;
        LocalDate date = LocalDate.now(); //just for initialization

        System.out.println(prompt);
        do {
            try {
                date = LocalDate.parse(readString("Enter date in MM-dd-yyyy format: "), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                hasErrors = false;
            } catch (DateTimeParseException e) {
                hasErrors = true;
            }
        } while (hasErrors);

        return date;
    }

    @Override
    public LocalDate readLocalDate(String prompt, LocalDate earliest, LocalDate latest) {
        boolean hasErrors;
        DateTimeFormatter mmddyyyy = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate date = LocalDate.now(); //just for initialization

        System.out.println(prompt);
        do {
            try {
                date = LocalDate.parse(readString("Enter date in MM-dd-yyyy format between " + earliest.format(mmddyyyy)
                        + " and " + latest.format(mmddyyyy) + ": "), mmddyyyy);

                hasErrors = date.isBefore(earliest) || date.isAfter(latest);
            } catch (DateTimeParseException e) {
                hasErrors = true;
            }
        } while (hasErrors);

        return date;
    }

}
