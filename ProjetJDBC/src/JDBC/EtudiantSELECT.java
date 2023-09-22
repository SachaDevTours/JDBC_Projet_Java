/**
 * 
 */
package JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class EtudiantSELECT {
    public static void main(String[] args) {
        // Connexion � la base de donn�es
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etudiant", "root", "");

            // Cr�ation d'une instruction SQL
            Statement statement = connection.createStatement();

            // Ex�cution de la requ�te SQL pour r�cup�rer les informations des �tudiants
            String query = "SELECT * FROM etudiants";
            ResultSet resultSet = statement.executeQuery(query);

            // Parcourir les r�sultats et afficher les informations
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                // Vous pouvez ajouter d'autres colonnes selon votre table

                System.out.println("ID : " + id);
                System.out.println("Nom : " + nom);
                System.out.println("Pr�nom : " + prenom);
                // Affichez les autres colonnes ici
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
