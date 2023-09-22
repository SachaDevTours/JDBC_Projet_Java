/**
 * 
 */
package JDBC;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EtudiantSELECTSwing {
    public static void main(String[] args) {
        // Connexion � la base de donn�es
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etudiant", "root", "");

            // Cr�ation d'une instruction SQL
            Statement statement = connection.createStatement();

            // Ex�cution de la requ�te SQL pour r�cup�rer les informations des �tudiants
            String query = "SELECT * FROM etudiants";
            ResultSet resultSet = statement.executeQuery(query);

            // Cr�ation d'un mod�le de tableau pour stocker les donn�es
            DefaultTableModel tableModel = new DefaultTableModel();

            // Ajout des colonnes au mod�le
            tableModel.addColumn("ID");
            tableModel.addColumn("Nom");
            tableModel.addColumn("Pr�nom");
            // Ajoutez d'autres colonnes ici

            // Remplissage du mod�le avec les donn�es de la base de donn�es
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                // Vous pouvez ajouter d'autres colonnes selon votre table

                tableModel.addRow(new Object[]{id, nom, prenom});
            }

            // Cr�ation d'une fen�tre Swing pour afficher le tableau
            JFrame frame = new JFrame("Table des �tudiants");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Cr�ation du tableau avec le mod�le de donn�es
            JTable table = new JTable(tableModel);

            // Ajout du tableau � un JScrollPane pour une meilleure gestion de l'affichage
            JScrollPane scrollPane = new JScrollPane(table);

            // Ajout du JScrollPane � la fen�tre
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            // Ajustement de la taille de la fen�tre
            frame.setSize(600, 400);

            // Affichage de la fen�tre
            frame.setVisible(true);

            // Fermeture des ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

