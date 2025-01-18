package ictgradschool.industry.final_project.utils;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

public class Utils {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void styleButton(JButton button, String colorHex, Font font, Dimension dimension) {
        button.setBackground(Color.decode(colorHex)); // Set background color
        button.setFont(font);
        button.setFocusPainted(false); // Remove focus border
        button.setPreferredSize(dimension);

    }

    public static String generateUniqueId() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }


}
