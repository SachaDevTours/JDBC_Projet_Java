/**
 * 
 */
package Morpion;

/**
 * 
 */
import java.util.Scanner;

public class JeuMorpionV1 {
    private char[][] tableau;
    private char joueurActuel;

    public JeuMorpionV1() {
    	//création d'un tableau
        tableau = new char[3][3];
        joueurActuel = 'X';
        initialiserTableau();
    }

    //boucle pou
    public void jouer() {
        boolean jeuTermine = false;
        Scanner scanner = new Scanner(System.in);

        while (!jeuTermine) {
            afficherTableau();
            int ligne, colonne;
            
            do {
                System.out.println("Joueur " + joueurActuel + ", entrez la ligne (0, 1, ou 2) et la colonne (0, 1, ou 2) : ");
                ligne = scanner.nextInt();
                colonne = scanner.nextInt();
            } while (!coupValide(ligne, colonne));

            tableau[ligne][colonne] = joueurActuel;

            if (aGagne()) {
                afficherTableau();
                System.out.println("Le joueur " + joueurActuel + " a gagné !");
                jeuTermine = true;
            } else if (matchNul()) {
                afficherTableau();
                System.out.println("Match nul !");
                jeuTermine = true;
            } else {
                changerJoueur();
            }
        }

        scanner.close();
    }

    private void initialiserTableau() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tableau[i][j] = ' ';
            }
        }
    }

    //afficher un tableau  "joli dans la console"
    private void afficherTableau() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(tableau[i][j]);
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (i < 2) {
                System.out.println("---------");
            }
        }
    }

    //vérification de la position
    private boolean coupValide(int ligne, int colonne) {
        if (ligne < 0 || ligne >= 3 || colonne < 0 || colonne >= 3 || tableau[ligne][colonne] != ' ') {
            System.out.println("Coup invalide. Réessayez.");
            return false;
        }
        return true;
    }

    //changer de joueur
    private void changerJoueur() {
        if (joueurActuel == 'X') {
            joueurActuel = 'O';
        } else {
            joueurActuel = 'X';
        }
    }

    //func vérification en cas de victoire
    private boolean aGagne() {
        // Vérification des lignes, colonnes et diagonales
        for (int i = 0; i < 3; i++) {
            if (tableau[i][0] == joueurActuel && tableau[i][1] == joueurActuel && tableau[i][2] == joueurActuel) {
                return true; // ligne gagnante
            }
            if (tableau[0][i] == joueurActuel && tableau[1][i] == joueurActuel && tableau[2][i] == joueurActuel) {
                return true; // colonne gagnante
            }
        }
        if (tableau[0][0] == joueurActuel && tableau[1][1] == joueurActuel && tableau[2][2] == joueurActuel) {
            return true; // diagonale (de gauche à droite) gagnante
        }
        if (tableau[0][2] == joueurActuel && tableau[1][1] == joueurActuel && tableau[2][0] == joueurActuel) {
            return true; // diagonale (de droite à gauche) gagnante
        }
        return false;
    }

    //func vérification en cas de match nul
    private boolean matchNul() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tableau[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        JeuMorpionV1 jeu = new JeuMorpionV1();
        jeu.jouer();
    }
}

