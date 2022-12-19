package org.example;

import java.util.ArrayList;
import java.util.List;

import static org.example.Properties.alphabet;

public class ValidateUtils {
    public static boolean validateKey(String key) {
        List<Character> lettersInKey = new ArrayList<>();

        key = key.toUpperCase();

        try {
            if (key.length() > 25)
                throw new IllegalArgumentException("Key cannot contain more than twenty-five characters");

            for (char character : key.toCharArray()) {
                if (Character.isSpaceChar(character))
                    throw new IllegalArgumentException("Key cannot contain white space");

                if (Character.isDigit(character))
                    throw new IllegalArgumentException("Key cannot contain digits");

                if (!Character.isAlphabetic(character))
                    throw new IllegalArgumentException("Key cannot contain punctuation or special characters");

                if (alphabet.indexOf(character) != -1)
                    lettersInKey.add(character);
            }

            if (lettersInKey.contains('I') && lettersInKey.contains('J'))
                throw new IllegalArgumentException("Key may contain 'I' or 'J', but not both");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public static boolean validateMessage(String message, Mode mode) {
        if (message.isEmpty())
            throw new IllegalArgumentException("Message cannot be empty");

        switch (mode) {
            case ENCRYPT:
                return validatePlainText(message);
            case DECRYPT:
                return validateCipherText(message);
            default:
                throw new IllegalArgumentException("Error mode");
        }
    }

    private static boolean validateCipherText(String message) {
        return message.matches("[A-Z]*");
    }

    private static boolean validatePlainText(String message) {
        char[] array = message.toCharArray();
        for (char c : array) {
            if (Character.isAlphabetic(c))
                return true;
        }
        new IllegalArgumentException("No letters present in the " + message).printStackTrace();
        return false;
    }
}
