/**
 * 
 */
package JeuTEST;

/**
 * Cercle + rotation
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JeuV7 extends JPanel implements ActionListener {
    private int playerX = 50;
    private int playerY = 300;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private boolean canDoubleJump = true;
    private long lastDoubleJumpTime = 0;
    private static final long DOUBLE_JUMP_COOLDOWN = 5000; // Cooldown de 5 secondes
    private boolean isGameOver = false;
    private JButton replayButton;
    private int score = 0; // Variable pour le score
    private Timer scoreTimer; // Timer pour incr�menter le score
    private Timer rotationTimer; // Timer pour g�rer la rotation

    private Timer timer;

    private List<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();

    private double rotationAngle = 0.0; // Angle de rotation du joueur

    public JeuV7() {
        timer = new Timer(10, this);
        timer.start();

        // Ajoutez un Timer pour incr�menter le score toutes les 100 millisecondes
        scoreTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    score++;
                }
            }
        });
        scoreTimer.start(); // D�marrez le Timer du score

        // Timer pour g�rer la rotation
        rotationTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotationAngle += Math.toRadians(10); // Augmentez l'angle de rotation
                if (rotationAngle >= Math.toRadians(360)) {
                    rotationAngle = 0.0;
                }
            }
        });
        rotationTimer.start(); // D�marrez le Timer de rotation

        setPreferredSize(new Dimension(800, 400));
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isGameOver) {
                    if (!isJumping) {
                        jump();
                    } else if (canDoubleJump && isCooldownOver()) {
                        doubleJump();
                    }
                }
            }
        });

        // G�n�rer les obstacles initiaux
        generateObstacles();
    }

    private void generateObstacles() {
        // Ajoutez les obstacles avec des positions en Y sp�cifiques et des largeurs al�atoires
    	obstacles.add(new Obstacle(800, 325, getRandomWidth(), 25)); // Obstacle sur le sol
        obstacles.add(new Obstacle(1000, 200, getRandomWidth(), 25)); // Obstacle en l'air
        obstacles.add(new Obstacle(1200, 325, getRandomWidth(), 25)); // Obstacle sur le sol
        obstacles.add(new Obstacle(1400, 200, getRandomWidth(), 25)); // Obstacle en l'air
        obstacles.add(new Obstacle(1600, 325, getRandomWidth(), 25)); // Obstacle sur le sol
    }
    
    private int getRandomWidth() {
        // G�n�rez une largeur al�atoire entre 25 (la largeur actuelle) et la largeur maximale souhait�e
        int maxWidth = 50; // Largeur maximale souhait�e
        return random.nextInt(maxWidth - 25 + 1) + 25;
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

    private void addReplayButton() {
        replayButton = new JButton("Rejouer");
        replayButton.setBounds(350, 200, 100, 50);
        replayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isGameOver = false;
                remove(replayButton);
                obstacles.clear();
                playerX = 50;
                playerY = 300;
                playerVelocityY = 0;
                isJumping = false;
                canDoubleJump = true;
                lastDoubleJumpTime = 0;
                score = 0; // R�initialisez le score

                // G�n�rer � nouveau les obstacles
                generateObstacles();

                repaint();
            }
        });
        add(replayButton);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Perdu", 350, 150);

            // Affichez le score une fois la partie termin�e
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 350, 200);
        } else {
            Graphics2D g2d = (Graphics2D) g;

            g.setColor(Color.GREEN);
            g.fillRect(0, 350, 800, 50);

            g.setColor(Color.RED);
            for (Obstacle obstacle : obstacles) {
                g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            }

            // Dessinez le joueur sous forme de cercle avec rotation en fonction de son �tat
            Ellipse2D.Double playerCircle = new Ellipse2D.Double(playerX, playerY, 50, 50);

            g2d.setColor(Color.BLUE);
            g2d.rotate(rotationAngle, playerX + 25, playerY + 25); // Rotation du joueur
            g2d.fill(playerCircle);

            // R�tablissez la transformation pr�c�dente
            g2d.rotate(-rotationAngle, playerX + 25, playerY + 25);

            // Affichez le score en haut � droite
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + score, 700, 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
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
                if (playerX + 50 > obstacle.getX() && playerX < obstacle.getX() + obstacle.getWidth()
                        && playerY + 50 > obstacle.getY() && playerY < obstacle.getY() + obstacle.getHeight()) {
                    isGameOver = true;
                    addReplayButton();
                    return;
                }
                obstacle.move();
                if (obstacle.getX() + obstacle.getWidth() < 0) {
                    obstacle.setX(800);
                }
            }

            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JeuV7");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JeuV7());
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
            x -= 5; // D�placement vers la gauche
        }

        public void setX(int x) {
            this.x = x;
        }
    }
}


