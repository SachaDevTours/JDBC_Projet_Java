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
		
		// informations de connexion � la base de donn�e
				String jdbcUrl = "jdbc:mysql://localhost:3306/etudiant"; // url de la base
				String user = "root";
				String password = "";
				
		// les donn�es � insert
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
				System.out.println("Succ�s de l'insertion");
			}else {
				System.out.println("Echec de l'insertion");
			}
			//close connexion
			preparedStatement.close();
			connection.close();
			
		}catch (ClassNotFoundException e) {
            // Gestion des exceptions li�es au chargement du pilote JDBC
            e.printStackTrace();
        } catch (SQLException e) {
            // Gestion des exceptions li�es � la connexion � la base de donn�es
            e.printStackTrace();
        }
	}

}
