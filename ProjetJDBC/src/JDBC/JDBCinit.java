/**
 * 
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 */
public class JDBCinit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// informations de connexion à la base de donnée
		String jdbcUrl = "jdbc:mysql://localhost:3306/etudiant"; // url de la base
		String user = "root";
		String password = "";
		
		//try catch
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
			if (connection != null) {
                System.out.println("Connexion à la base de données réussie !");
            } else {
                System.out.println("La connexion à la base de données a échoué.");
            }

            // Étape 4 : Utiliser la connexion ici
            // Vous pouvez exécuter des requêtes SQL et effectuer des opérations sur la base de données à partir d'ici.

            // Étape 5 : Fermer la connexion lorsque vous avez fini
            // Il est important de fermer la connexion lorsque vous avez terminé pour libérer les ressources.
            connection.close();
        } catch (ClassNotFoundException e) {
            // Gestion des exceptions liées au chargement du pilote JDBC
            e.printStackTrace();
        } catch (SQLException e) {
            // Gestion des exceptions liées à la connexion à la base de données
            e.printStackTrace();
        }
    }
}