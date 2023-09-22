/**
 * 
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * 
 */
public class EtudiantTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// informations de connexion à la base de donnée
				String jdbcUrl = "jdbc:mysql://localhost:3306/etudiant"; // url de la base
				String user = "root";
				String password = "";
				
		// les données à insert
		String nom ="Benoit";
		String prenom ="David";
		String dateNaissance = "1982-12-10";
		String email = "benoitdavid@gmail.com";
		String matricule = "E240";
		
		try {
			//connexion
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
			
			String sql = "INSERT INTO etudiants (nom, prenom, date_naissance, email, matricule) "
					+ "VALUES (?, ?, ?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, prenom);
			preparedStatement.setString(3, dateNaissance);
			preparedStatement.setString(4, email);
			preparedStatement.setString(5, matricule);
			
			//executer
			int ligneAffectees = preparedStatement.executeUpdate();
			if(ligneAffectees >0) {
				System.out.println("Succès de l'insertion");
			}else {
				System.out.println("Echec de l'insertion");
			}
			//close connexion
			preparedStatement.close();
			connection.close();
			
		}catch (ClassNotFoundException e) {
            // Gestion des exceptions liées au chargement du pilote JDBC
            e.printStackTrace();
        } catch (SQLException e) {
            // Gestion des exceptions liées à la connexion à la base de données
            e.printStackTrace();
        }
	}

}
