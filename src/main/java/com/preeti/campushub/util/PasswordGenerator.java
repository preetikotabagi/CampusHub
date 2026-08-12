package com.preeti.campushub.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    private static final String UPPER =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String LOWER =
            "abcdefghijklmnopqrstuvwxyz";

    private static final String DIGITS =
            "0123456789";

    private static final String SPECIAL =
            "@#$%&";

    private static final String ALL =
            UPPER + LOWER + DIGITS + SPECIAL;

    private static final SecureRandom random =
            new SecureRandom();

    public static String generatePassword(int length) {

        if (length < 8) {
            throw new IllegalArgumentException(
                    "Password length must be at least 8");
        }

        List<Character> password = new ArrayList<>();

        password.add(randomChar(UPPER));
        password.add(randomChar(LOWER));
        password.add(randomChar(DIGITS));
        password.add(randomChar(SPECIAL));

        while (password.size() < length) {
            password.add(randomChar(ALL));
        }

        Collections.shuffle(password, random);

        StringBuilder builder = new StringBuilder();

        for (char c : password) {
            builder.append(c);
        }

        return builder.toString();
    }

    private static char randomChar(String characters) {

        return characters.charAt(
                random.nextInt(characters.length())
        );
    }
}