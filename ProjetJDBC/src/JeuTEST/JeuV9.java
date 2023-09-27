package JeuTEST;
//impl�mentation bug des triangles 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JeuV9 extends JPanel implements ActionListener {
    private int playerX = 50;
    private int playerY = 300;
    private int playerVelocityY = 0;
    private boolean isJumping = false;
    private boolean canDoubleJump = true;
    private long lastDoubleJumpTime = 0;
    private static final long DOUBLE_JUMP_COOLDOWN = 3000; // Cooldown de 3 secondes
    private boolean isGameOver = false;
    private JButton replayButton;
    private int score = 0; // Variable pour le score
    private Timer scoreTimer; // Timer pour incr�menter le score
    private Timer rotationTimer; // Timer pour g�rer la rotation

    private Timer timer;

    private List<Obstacle> obstacles = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>(); // Liste pour les triangles
    private Random random = new Random();

    private double rotationAngle = 0.0; // Angle de rotation du joueur

    private boolean collisionDisabled = false;
    private long collisionDisableStartTime = 0;
    private static final long COLLISION_DISABLE_DURATION = 10000; // 10 secondes

    public JeuV9() {
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

        // Initialisez lastDoubleJumpTime (le cooldown est d�j� �coul�)
        lastDoubleJumpTime = System.currentTimeMillis() - DOUBLE_JUMP_COOLDOWN;
        System.out.println(lastDoubleJumpTime);

        // G�n�rer les obstacles initiaux
        generateObstacles();

        // G�n�rer les triangles initiaux
        generateTriangles();
    }

    private void generateObstacles() {
        // Ajoutez les obstacles avec des positions en Y sp�cifiques et des largeurs al�atoires
        obstacles.add(new Obstacle(800, 325, getRandomWidth(), 25)); // Obstacle sur le sol
        obstacles.add(new Obstacle(1000, 200, getRandomWidth(), 25)); // Obstacle en l'air
        obstacles.add(new Obstacle(1200, 325, getRandomWidth(), 25)); // Obstacle sur le sol
        obstacles.add(new Obstacle(1400, 200, getRandomWidth(), 25)); // Obstacle en l'air
        obstacles.add(new Obstacle(1600, 325, getRandomWidth(), 25)); // Obstacle sur le sol
    }

    private void generateTriangles() {
        // Ajoutez les triangles avec des positions en Y sur le sol
        triangles.add(new Triangle(900, 325, 30, -2)); // Triangle sur le sol, d�placement de droite � gauche
        triangles.add(new Triangle(1100, 325, 30, -3)); // Triangle sur le sol, d�placement de droite � gauche
        triangles.add(new Triangle(1300, 325, 30, -2)); // Triangle sur le sol, d�placement de droite � gauche
        triangles.add(new Triangle(1500, 325, 30, -3)); // Triangle sur le sol, d�placement de droite � gauche
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

                // G�n�rer � nouveau les triangles
                generateTriangles();

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
            g.drawString("Score: " + score, 50, 50); // D�calage du score vers la gauche
        } else {
            Graphics2D g2d = (Graphics2D) g;

            g.setColor(Color.GREEN);
            g.fillRect(0, 350, 800, 50);

            g.setColor(Color.RED);
            for (Obstacle obstacle : obstacles) {
                g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            }

            // Dessinez les triangles sur le sol
            g.setColor(Color.YELLOW);
            for (Triangle triangle : triangles) {
                int[] xPoints = triangle.getXPoints();
                int[] yPoints = triangle.getYPoints();
                g.fillPolygon(xPoints, yPoints, 3);
            }

            // Dessinez le joueur sous forme de cercle avec rotation en fonction de son �tat
            Ellipse2D.Double playerCircle = new Ellipse2D.Double(playerX, playerY, 50, 50);

            g2d.setColor(Color.BLUE);
            g2d.rotate(rotationAngle, playerX + 25, playerY + 25); // Rotation du joueur
            g2d.fill(playerCircle);

            // R�tablissez la transformation pr�c�dente
            g2d.rotate(-rotationAngle, playerX + 25, playerY + 25);

            // Affichez le score � gauche
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + score, 50, 30); // D�calage du score vers la gauche

            // Affichez le compte � rebours du cooldown � gauche
            long remainingCooldown = getRemainingDoubleJumpCooldown();
            if (remainingCooldown < 0) {
                String cooldownText = "Cooldown : 0s";
                g.drawString(cooldownText, 50, 60); // D�calage du cooldown vers la gauche
            } else {
                String cooldownText = "Cooldown: " + (remainingCooldown / 1000) + "s";
                g.drawString(cooldownText, 50, 60); // D�calage du cooldown vers la gauche
            }
        }
    }

    private long getRemainingDoubleJumpCooldown() {
        long currentTime = System.currentTimeMillis();
        return lastDoubleJumpTime + DOUBLE_JUMP_COOLDOWN - currentTime;
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
                if (!collisionDisabled && playerX + 50 > obstacle.getX() && playerX < obstacle.getX() + obstacle.getWidth()
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

            // V�rifiez les collisions avec les triangles sur le sol
            for (Triangle triangle : triangles) {
                if (!collisionDisabled && triangle.intersects(playerX, playerY, 50, 50)) {
                    isGameOver = true;
                    addReplayButton();

                    // D�sactivez les collisions pour 10 secondes
                    collisionDisabled = true;
                    collisionDisableStartTime = System.currentTimeMillis();

                    return;
                }
                triangle.move(); // D�placez les triangles horizontalement
            }

            if (collisionDisabled && System.currentTimeMillis() - collisionDisableStartTime >= COLLISION_DISABLE_DURATION) {
                collisionDisabled = false;
            }

            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JeuV9");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new JeuV9());
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

    private class Triangle {
        private int x;
        private int y;
        private int size;
        private int dx; // D�placement horizontal

        public Triangle(int x, int y, int size, int dx) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.dx = dx; // Vitesse de d�placement horizontal
        }

        public int[] getXPoints() {
            return new int[]{x, x + size / 2, x + size};
        }

        public int[] getYPoints() {
            return new int[]{y + size, y, y + size};
        }

        public boolean intersects(int px, int py, int pw, int ph) {
            Polygon playerPolygon = new Polygon(
                new int[]{px, px + pw / 2, px + pw, px},
                new int[]{py, py + ph, py, py},
                4
            );

            Polygon trianglePolygon = new Polygon(getXPoints(), getYPoints(), 3);

            return playerPolygon.intersects(trianglePolygon.getBounds2D());
        }

        public void move() {
            x += dx; // D�placez le triangle horizontalement
            if (x + size < 0) {
                // R�initialisez la position lorsque le triangle sort de l'�cran
                x = 800;
            }
        }
    }
}
