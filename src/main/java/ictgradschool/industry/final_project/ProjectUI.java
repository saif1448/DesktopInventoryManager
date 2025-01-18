package ictgradschool.industry.final_project;

import ictgradschool.industry.final_project.welcome_screen.WelcomeScreen;

import javax.swing.*;

public class ProjectUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomeScreen();
            }
        });
    }
}
