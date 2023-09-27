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

public class JeuV4 extends JPanel implements ActionListener {
    private int playerX = 50;
    private int playerY = 300;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private boolean canDoubleJump = true;
    private long lastDoubleJumpTime = 0;
    private static final long DOUBLE_JUMP_COOLDOWN = 5000; // Cooldown de 5 secondes
    private Timer timer;

    private List<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();

    public JeuV4() {
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
                } else if (canDoubleJump && isCooldownOver()) {
                    doubleJump();
                }
            }
        });

        // Ajouter des obstacles initiaux
        for (int i = 0; i < 5; i++) {
            obstacles.add(new Obstacle(800 + i * 200, 325, 50, 25));
        }
    }

    private void jump() {
        isJumping = true;
        playerVelocityY = -15;
    }

    private void doubleJump() {
        playerVelocityY = -15;
        canDoubleJump = false;
        lastDoubleJumpTime = System.currentTimeMillis();
    }

    private boolean isCooldownOver() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastDoubleJumpTime >= DOUBLE_JUMP_COOLDOWN;
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
        if (playerY < 300) {
            playerVelocityY += 1;
        }

        playerY += playerVelocityY;

        if (playerY >= 300) {
            playerY = 300;
            isJumping = false;
            canDoubleJump = true;
        }

        for (Obstacle obstacle : obstacles) {
            obstacle.move();
            if (obstacle.getX() + obstacle.getWidth() < 0) {
                obstacle.setX(800);
            }
        }

        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JeuV4");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JeuV4());
            frame.pack();
            frame.setVisible(true);
        });
    }

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
            x -= 5;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
}
