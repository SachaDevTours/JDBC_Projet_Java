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
import javax.swing.table.TableColumnModel;

public class EtudiantSwingINSERTv4 {

    private static Connection connection;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etudiant", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        JFrame frame = new JFrame("Gestion des étudiants");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setIconImage(new ImageIcon("icon.png").getImage());

        tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class; // Colonne de cases à cocher
                }
                return super.getColumnClass(columnIndex);
            }
        };
        tableModel.addColumn("Supprimer");
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Prénom");
        tableModel.addColumn("Date de Naissance");
        tableModel.addColumn("Email");
        tableModel.addColumn("Matricule");

        JTable table = new JTable(tableModel);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(0).setMaxWidth(30);
        columnModel.getColumn(0).setMinWidth(30);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton refreshButton = new JButton("Actualiser"); //,new ImageIcon("Actualiser.png")
        JButton modifyButton = new JButton("Modification");
        JButton deleteRecordButton = new JButton("Supprimer");

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

                        tableModel.addRow(new Object[]{false, id, nom, prenom, dateNaissance, email, matricule});
                    }

                    resultSet.close();
                    statement.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    int id = (int) tableModel.getValueAt(row, 1);
                    String nom = (String) tableModel.getValueAt(row, 2);
                    String prenom = (String) tableModel.getValueAt(row, 3);
                    String dateNaissance = (String) tableModel.getValueAt(row, 4);
                    String email = (String) tableModel.getValueAt(row, 5);
                    String matricule = (String) tableModel.getValueAt(row, 6);

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

        deleteRecordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int row = tableModel.getRowCount() - 1; row >= 0; row--) {
                    boolean selected = (boolean) tableModel.getValueAt(row, 0);
                    if (selected) {
                        int id = (int) tableModel.getValueAt(row, 1);
                        try {
                            String query = "DELETE FROM etudiants WHERE id=?";
                            PreparedStatement preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setInt(1, id);
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        tableModel.removeRow(row);
                    }
                }
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());

        JTextField nomField = new JTextField(15);
        JTextField prenomField = new JTextField(15);
        JTextField dateNaissanceField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField matriculeField = new JTextField(15);

        JLabel nomLabel = new JLabel("Nom:");
        JLabel prenomLabel = new JLabel("Prénom:");
        JLabel dateNaissanceLabel = new JLabel("Date de Naissance:");
        JLabel emailLabel = new JLabel("Email");
        JLabel matriculeLabel = new JLabel("Matricule:");

        JButton addButton = new JButton("Ajouter");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String dateNaissance = dateNaissanceField.getText();
                String email = emailField.getText();
                String matricule = matriculeField.getText();

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

                    refreshButton.doClick();
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteRecordButton);

        JPanel tableAndButtonPanel = new JPanel(new BorderLayout());
        tableAndButtonPanel.add(scrollPane, BorderLayout.CENTER);
        tableAndButtonPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(tableAndButtonPanel, BorderLayout.CENTER);

        frame.setSize(800, 400);
        frame.setVisible(true);

        refreshButton.doClick();
    }
}

