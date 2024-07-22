package com.ibrahim;

import javax.swing.*;
import java.util.Objects;

public class Main {
    class SpaceInvadersFrame extends JFrame {
        public SpaceInvadersFrame() {
            setTitle("Space Invaders");

            ImageIcon icon = new ImageIcon(getClass().getResource("/graphics/alien-green.png")); // Your icon file
            setIconImage(icon.getImage());

            // Add your game panel to the frame
            add(new SpaceInvaders());

            // Set default close operation and frame size
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null); // Center the frame
        }
    }

    public static void main(String[] args) {
        int tileSize = 32;
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns;  // 512px
        int boardHeight = tileSize * rows; // 512px

        JFrame frame = new JFrame("Space Invaders");
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/graphics/icon.png"))); // Your icon file
        frame.setIconImage(icon.getImage());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders(); // Creating Custom Panel
        frame.add(spaceInvaders); // Adding Panel to Current Frame
        frame.pack();
        frame.setVisible(true);

    }
}