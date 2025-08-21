import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class IBANChecker {
    private static final Map<String, Integer> chars = new HashMap<>();

    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }

    public static void main(String[] args) {
        String iban = "DE227902007600279131";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }

    public static boolean validate(String iban) {
        if (iban == null) {
            return false;
        }
        String normalized = iban.replaceAll("\\s+", "");
        if (normalized.length() < 4) {
            return false;
        }
        if (!isAlphanumeric(normalized)) {
            return false;
        }
        if (!checkLength(normalized)) {
            return false;
        }
        String rearrangedIban = rearrangeIban(normalized);
        String convertedIban = convertToInteger(rearrangedIban);
        if (convertedIban.isEmpty()) {
            return false;
        }
        List<String> segments = createSegments(convertedIban);
        return calculate(segments) == 1;
    }

    private static int calculate(List<String> segments) {
        long n = 0;
        for (String segment : segments) {
            if (segment.isEmpty()) {
                continue;
            }
            String current = (n == 0) ? segment : (n + segment);
            n = Long.parseLong(current) % 97;
        }
        return (int) n;
    }

    private static boolean checkLength(String iban) {
        if (iban == null || iban.length() < 2) {
            return false;
        }
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) == iban.length();
    }

    private static String convertToInteger(String iban) {
        StringBuilder convertedIban = new StringBuilder();
        String upperIban = iban.toUpperCase();
        for (char c : upperIban.toCharArray()) {
            if (Character.isDigit(c)) {
                convertedIban.append(c);
            }
            if (Character.isLetter(c)) {
                convertedIban.append(c - 55);
            }
        }
        return convertedIban.toString();
    }

    private static List<String> createSegments(String iban) {
        List<String> segments = new ArrayList<>();
        String remainingIban = iban;
        boolean first = true;
        while (remainingIban.length() > 0) {
            int size = first ? Math.min(9, remainingIban.length()) : Math.min(7, remainingIban.length());
            segments.add(remainingIban.substring(0, size));
            remainingIban = remainingIban.substring(size);
            first = false;
        }
        return segments;
    }
//this comment enables me to hopefully commit and push this project to github
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }

    private static boolean isAlphanumeric(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isLetterOrDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}