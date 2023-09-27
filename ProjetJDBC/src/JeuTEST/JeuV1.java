/**
 * 
 */
package JeuTEST;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JeuV1 extends JPanel implements ActionListener {
    private int playerX = 50;
    private int playerY = 300;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private Timer timer;

    public JeuV1() {
        timer = new Timer(10, this);
        timer.start();

        setPreferredSize(new Dimension(800, 400));
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isJumping) {
                    jump();
                }
            }
        });
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            playerVelocityY = -15; // Réglage de la vitesse du saut
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, 50, 50);
        g.setColor(Color.GREEN);
        g.fillRect(0, 350, 800, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Appliquer la gravité
        if (playerY < 300) {
            playerVelocityY += 1; // Gravité
        }

        playerY += playerVelocityY;

        if (playerY >= 300) {
            playerY = 300;
            isJumping = false;
        }

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JeuCubeV1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JeuV1());
            frame.pack();
            frame.setVisible(true);
        });
    }
}