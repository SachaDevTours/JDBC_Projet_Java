/**
 * 
 */
package JDBC;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class EtudiantSwingINSERTv2 {
    private static Connection connection; // D�claration de la connexion comme variable de classe
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        // Connexion � la base de donn�es
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etudiant", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Cr�ation d'une fen�tre Swing pour afficher le tableau et le formulaire
        JFrame frame = new JFrame("Gestion des �tudiants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Cr�ation d'un mod�le de tableau pour stocker les donn�es
        tableModel = new DefaultTableModel();

        // Ajout des colonnes au mod�le
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Pr�nom");
        tableModel.addColumn("Date de Naissance");
        tableModel.addColumn("Email");
        tableModel.addColumn("Matricule");

        // Cr�ation du tableau avec le mod�le de donn�es
        JTable table = new JTable(tableModel);

        // Ajout du tableau � un JScrollPane pour une meilleure gestion de l'affichage
        JScrollPane scrollPane = new JScrollPane(table);

        // Bouton de rafra�chissement des donn�es
        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Efface toutes les lignes du mod�le
                tableModel.setRowCount(0);

                try {
                    Statement statement = connection.createStatement();
                    String query = "SELECT * FROM etudiants";
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nom = resultSet.getString("nom");
                        String prenom = resultSet.getString("prenom");
                        String dateNaissance = resultSet.getString("date_naissance");
                        String email = resultSet.getString("email");
                        String matricule = resultSet.getString("matricule");

                        tableModel.addRow(new Object[]{id, nom, prenom, dateNaissance, email, matricule});
                    }

                    resultSet.close();
                    statement.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Bouton de modification des donn�es
        JButton modifyButton = new JButton("Modification");
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Parcourez les lignes du tableau
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    String nom = (String) tableModel.getValueAt(row, 1);
                    String prenom = (String) tableModel.getValueAt(row, 2);
                    String dateNaissance = (String) tableModel.getValueAt(row, 3);
                    String email = (String) tableModel.getValueAt(row, 4);
                    String matricule = (String) tableModel.getValueAt(row, 5);

                    // Mettez � jour les donn�es dans la base de donn�es
                    try {
                        String query = "UPDATE etudiants SET nom=?, prenom=?, date_naissance=?, email=?, matricule=? WHERE id=?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, nom);
                        preparedStatement.setString(2, prenom);
                        preparedStatement.setString(3, dateNaissance);
                        preparedStatement.setString(4, email);
                        preparedStatement.setString(5, matricule);
                        preparedStatement.setInt(6, id);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        // Cr�ation d'un panneau pour le formulaire d'ajout
        JPanel inputPanel = new JPanel(new FlowLayout());

        // Cr�ation des champs de texte pour le nouvel �tudiant
        JTextField nomField = new JTextField(15);
        JTextField prenomField = new JTextField(15);
        JTextField dateNaissanceField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField matriculeField = new JTextField(15);

        // Cr�ation des libell�s pour les champs
        JLabel nomLabel = new JLabel("Nom:");
        JLabel prenomLabel = new JLabel("Pr�nom:");
        JLabel dateNaissanceLabel = new JLabel("Date de Naissance:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel matriculeLabel = new JLabel("Matricule:");

        // Cr�ation du bouton pour ajouter un nouvel �tudiant
        JButton addButton = new JButton("Ajouter");

        // Ajout d'un gestionnaire d'�v�nements pour le bouton d'ajout
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String dateNaissance = dateNaissanceField.getText();
                String email = emailField.getText();
                String matricule = matriculeField.getText();

                // Insertion des donn�es dans la base de donn�es
                try {
                    String query = "INSERT INTO etudiants (nom, prenom, date_naissance, email, matricule) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, nom);
                    preparedStatement.setString(2, prenom);
                    preparedStatement.setString(3, dateNaissance);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, matricule);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    // Rafra�chissement du tableau
                    refreshButton.doClick();  // Appelle le gestionnaire d'�v�nements du bouton Actualiser
                    nomField.setText("");
                    prenomField.setText("");
                    dateNaissanceField.setText("");
                    emailField.setText("");
                    matriculeField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Ajout des composants au panneau de saisie
        inputPanel.add(nomLabel);
        inputPanel.add(nomField);
        inputPanel.add(prenomLabel);
        inputPanel.add(prenomField);
        inputPanel.add(dateNaissanceLabel);
        inputPanel.add(dateNaissanceField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(matriculeLabel);
        inputPanel.add(matriculeField);
        inputPanel.add(addButton);

        // Ajout du bouton Actualiser et du bouton Modification
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(modifyButton);

        // Ajout du bouton Actualiser et du JScrollPane � la fen�tre
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Ajustement de la taille de la fen�tre
        frame.setSize(800, 400);

        // Affichage de la fen�tre
        frame.setVisible(true);

        // Initialement, chargez les donn�es dans le tableau
        refreshButton.doClick();
    }
}

