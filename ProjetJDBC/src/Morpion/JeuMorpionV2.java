/**
 * 
 */
package Morpion;

/**
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JeuMorpionV2 extends JFrame implements ActionListener {
    private char joueurActuel = 'X';
    private JButton[][] boutons;

    public JeuMorpionV2() {
        boutons = new JButton[3][3];
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Morpion");
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boutons[i][j] = new JButton("");
                boutons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                boutons[i][j].setFocusPainted(false);
                boutons[i][j].addActionListener(this);
                add(boutons[i][j]);
            }
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton boutonClique = (JButton) e.getSource();

        if (boutonClique.getText().equals("")) {
            boutonClique.setText(String.valueOf(joueurActuel));

            if (aGagné()) {
                JOptionPane.showMessageDialog(this, "Le joueur " + joueurActuel + " a gagné !");
                reset();
            } else if (matchNul()) {
                JOptionPane.showMessageDialog(this, "Match nul !");
                reset();
            } else {
                changerJoueur();
            }
        }
    }

    private void changerJoueur() {
        joueurActuel = (joueurActuel == 'X') ? 'O' : 'X';
    }

    private boolean aGagné() {
        // Vérification des lignes, colonnes et diagonales
        for (int i = 0; i < 3; i++) {
            if (boutons[i][0].getText().equals(String.valueOf(joueurActuel))
                    && boutons[i][1].getText().equals(String.valueOf(joueurActuel))
                    && boutons[i][2].getText().equals(String.valueOf(joueurActuel))) {
                return true; // ligne gagnante
            }
            if (boutons[0][i].getText().equals(String.valueOf(joueurActuel))
                    && boutons[1][i].getText().equals(String.valueOf(joueurActuel))
                    && boutons[2][i].getText().equals(String.valueOf(joueurActuel))) {
                return true; // colonne gagnante
            }
        }
        if (boutons[0][0].getText().equals(String.valueOf(joueurActuel))
                && boutons[1][1].getText().equals(String.valueOf(joueurActuel))
                && boutons[2][2].getText().equals(String.valueOf(joueurActuel))) {
            return true; // diagonale (de gauche à droite) gagnante
        }
        if (boutons[0][2].getText().equals(String.valueOf(joueurActuel))
                && boutons[1][1].getText().equals(String.valueOf(joueurActuel))
                && boutons[2][0].getText().equals(String.valueOf(joueurActuel))) {
            return true; // diagonale (de droite à gauche) gagnante
        }
        return false;
    }

    private boolean matchNul() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boutons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void reset() {
        joueurActuel = 'X';
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boutons[i][j].setText("");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JeuMorpionV2();
        });
    }
}

