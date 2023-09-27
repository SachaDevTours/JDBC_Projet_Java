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

public class JeuV2 extends JPanel implements ActionListener {
    private int playerX = 50;
    private int playerY = 300;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private Timer timer;

    private List<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();

    public JeuV2() {
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

        // Ajouter des obstacles initiaux
        for (int i = 0; i < 5; i++) {
            obstacles.add(new Obstacle(800 + i * 200, 325, 50, 25));
        }
    }

    private void jump() {
        if (!isJumping) {
            isJumping = true;
            playerVelocityY = -15; // R�glage de la vitesse du saut
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
        // Appliquer la gravit�
        if (playerY < 300) {
            playerVelocityY += 1; // Gravit�
        }

        playerY += playerVelocityY;

        if (playerY >= 300) {
            playerY = 300;
            isJumping = false;
        }

        // Mettre � jour la position des obstacles
        for (Obstacle obstacle : obstacles) {
            obstacle.move();
            if (obstacle.getX() + obstacle.getWidth() < 0) {
                // L'obstacle a quitt� l'�cran, le r�initialiser
                obstacle.setX(800);
            }
        }

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("sdqsfe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JeuV2());
            frame.pack();
            frame.setVisible(true);
        });
    }

    // Classe pour repr�senter les obstacles
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
            x -= 5; // D�placement vers la gauche
        }

        public void setX(int x) {
            this.x = x;
        }
    }
}

