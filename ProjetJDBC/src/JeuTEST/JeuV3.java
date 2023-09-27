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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JeuV3 extends JPanel implements ActionListener {
    private int playerX = 50;
    private int playerY = 300;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private Timer timer;

    private List<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();

    private boolean doubleJumpReady = true; // Indique si le double saut est prêt

    public JeuV3() {
        timer = new Timer(10, this);
        timer.start();

        setPreferredSize(new Dimension(800, 400));
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter() {
            private int clickCount = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickCount == 0) {
                    // Premier clic
                    jump();
                    clickCount++;
                } else if (clickCount == 1 && doubleJumpReady) {
                    // Deuxième clic pour le double saut
                    doubleJump();
                    clickCount = 0; // Réinitialise le compteur de clics
                }
            }
        });

        // Ajouter des obstacles initiaux
        for (int i = 0; i < 5; i++) {
            obstacles.add(new Obstacle(800 + i * 200, 325, 50, 25));
        }
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            playerVelocityY = -15; // Réglage de la vitesse du saut
        }
    }

    private void doubleJump() {
        if (doubleJumpReady) {
            playerVelocityY = -15; // Réglage de la vitesse du double saut
            doubleJumpReady = false; // Désactive le double saut jusqu'à ce qu'il soit réinitialisé
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, 50, 50);
        g.setColor(Color.GREEN);
        g.fillRect(0, 350, 800, 50);

        // Dessiner les obstacles
        g.setColor(Color.RED);
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }
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
            doubleJumpReady = true; // Réactive le double saut lorsque le joueur touche le sol
        }

        // Mettre à jour la position des obstacles
        for (Obstacle obstacle : obstacles) {
            obstacle.move();
            if (obstacle.getX() + obstacle.getWidth() < 0) {
                // L'obstacle a quitté l'écran, le réinitialiser
                obstacle.setX(800);
            }
        }

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JeuV3");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JeuV3());
            frame.pack();
            frame.setVisible(true);
        });
    }

    // Classe pour représenter les obstacles
    private class Obstacle {
        private int x;
        private int y;
        private int width;
        private int height;

        public Obstacle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void move() {
            x -= 5; // Déplacement vers la gauche
        }

        public void setX(int x) {
            this.x = x;
        }
    }
}
