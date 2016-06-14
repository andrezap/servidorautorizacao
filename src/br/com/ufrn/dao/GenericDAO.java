package br.com.ufrn.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Classe genérica responsável por implementar o método de conexão do banco
 * 
 * @author Andreza Lima
 *
 */
public class GenericDAO {

	/**
	 * Endereço do banco de dados.
	 */
//	private String DB_URL = "jdbc:mysql://localhost:3306/auth";

	private String DB_URL = "jdbc:mysql://localhost:3306/oauthserver";

	/**
	 * Usuário para acessar o banco de dados.
	 */
	private String DB_USER = "root";
	
	/**
	 * Senha para acessar o banco de dados.
	 */
	private String DB_PASSWORD = "2158";

	/**
	 * Método responsável por realizar a conexão com o banco de dados.
	 *
	 * @return conexão se realizada.
	 */
	public Connection getConnection() {
		Connection con = null;

		String url = DB_URL;
		String user = DB_USER;
		String password = DB_PASSWORD;
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		return con;
	}
	
	/**
	 * Método responsável por encerrar a conexão com o banco de dados.
	 * 
	 * @param con
	 *            - conexão a ser encerrada.
	 * @param pst
	 *            - o statement que estava sendo executado a ser encerrado.
	 */
	public void close(Connection con, PreparedStatement pst) {

		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
