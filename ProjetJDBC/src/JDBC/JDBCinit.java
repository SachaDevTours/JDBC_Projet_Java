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
		
		// informations de connexion � la base de donn�e
		String jdbcUrl = "jdbc:mysql://localhost:3306/etudiant"; // url de la base
		String user = "root";
		String password = "";
		
		//try catch
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
			if (connection != null) {
                System.out.println("Connexion � la base de donn�es r�ussie !");
            } else {
                System.out.println("La connexion � la base de donn�es a �chou�.");
            }

            // �tape 4 : Utiliser la connexion ici
            // Vous pouvez ex�cuter des requ�tes SQL et effectuer des op�rations sur la base de donn�es � partir d'ici.

            // �tape 5 : Fermer la connexion lorsque vous avez fini
            // Il est important de fermer la connexion lorsque vous avez termin� pour lib�rer les ressources.
            connection.close();
        } catch (ClassNotFoundException e) {
            // Gestion des exceptions li�es au chargement du pilote JDBC
            e.printStackTrace();
        } catch (SQLException e) {
            // Gestion des exceptions li�es � la connexion � la base de donn�es
            e.printStackTrace();
        }
    }
}