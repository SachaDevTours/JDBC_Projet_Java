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
        // Connexion à la base de données
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etudiant", "root", "");

            // Création d'une instruction SQL
            Statement statement = connection.createStatement();

            // Exécution de la requête SQL pour récupérer les informations des étudiants
            String query = "SELECT * FROM etudiants";
            ResultSet resultSet = statement.executeQuery(query);

            // Création d'un modèle de tableau pour stocker les données
            DefaultTableModel tableModel = new DefaultTableModel();

            // Ajout des colonnes au modèle
            tableModel.addColumn("ID");
            tableModel.addColumn("Nom");
            tableModel.addColumn("Prénom");
            // Ajoutez d'autres colonnes ici

            // Remplissage du modèle avec les données de la base de données
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                // Vous pouvez ajouter d'autres colonnes selon votre table

                tableModel.addRow(new Object[]{id, nom, prenom});
            }

            // Création d'une fenêtre Swing pour afficher le tableau
            JFrame frame = new JFrame("Table des étudiants");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Création du tableau avec le modèle de données
            JTable table = new JTable(tableModel);

            // Ajout du tableau à un JScrollPane pour une meilleure gestion de l'affichage
            JScrollPane scrollPane = new JScrollPane(table);

            // Ajout du JScrollPane à la fenêtre
            frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

            // Ajustement de la taille de la fenêtre
            frame.setSize(600, 400);

            // Affichage de la fenêtre
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

