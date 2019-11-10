package Projet;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Class that holds different functions that are too general too be implemented in a specific class
 */
public class Utilities {
    /**
     * Convert an object into a string of length by adding '0' at beginning
     * @param n Object to convert
     * @param length Integer Length desired for string
     * @return String
     */
    public static String convertIntDateToString(Object n, Integer length) {
        StringBuilder s = new StringBuilder(n.toString());
        while (s.length() < length) {
            s.insert(0, "0");
        }
        return s.toString();
    }

    /**
     * Verify if a date is valid
     * @param year Integer Year (4 digits)
     * @param month Integer Month number
     * @param day Integer Day
     * @return Validity of date
     */
    public static boolean verifyDate(Integer year, Integer month, Integer day) {
        Integer[] longMonth = {1, 3, 5, 7, 8, 10, 12};

        if (day > 31 || day < 1 || month < 1 || month > 12) {
            return false;
        }

        if (Arrays.asList(longMonth).contains(month)) {
            return true;
        } else if (month == 2) {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                if (day <= 29) {
                    return true;
                }
            }
            return day <= 28;
        } else return day <= 30;

    }

    /**
     * Check if two period are overlapping
     * @param start1 LocalDateTime Start of period 1
     * @param end1 LocalDateTime End of period 1
     * @param start2 LocalDateTime Start of period 2
     * @param end2 LocalDateTime End of period 2
     * @return boolean true if period are overlapping
     */
    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Set an array of string from a number to another
     * @param b Integer Starting number
     * @param e Integer Ending number
     * @return Array of string
     */
    public static String[] listNbToString(Integer b, Integer e) {
        String[] y = new String[e - b + 1];
        for(int i = 0; i < e - b + 1; i++) {
            y[i] = String.valueOf(i + b);
        }
        return y;
    }

    /**
     * Check if a string is a numeric value
     * @param strNum String to check
     * @return boolean
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Round double
     * @param value Double to round
     * @param places Integer Number of decimal
     * @return Double rounded
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Verify if a string is an email address
     * @param email String to verify
     * @return boolean of validity
     */
    public static boolean isValidMail(String email) {
        // Obviously not my expression, but I needed a strong regex to be sure the verification would be reliable
        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        return email.matches(regex);
    }
}
