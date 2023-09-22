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
        // Connexion à la base de données
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etudiant", "root", "");

            // Création d'une instruction SQL
            Statement statement = connection.createStatement();

            // Exécution de la requête SQL pour récupérer les informations des étudiants
            String query = "SELECT * FROM etudiants";
            ResultSet resultSet = statement.executeQuery(query);

            // Parcourir les résultats et afficher les informations
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                // Vous pouvez ajouter d'autres colonnes selon votre table

                System.out.println("ID : " + id);
                System.out.println("Nom : " + nom);
                System.out.println("Prénom : " + prenom);
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
