package br.com.ufrn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ejb.Stateless;

import br.com.ufrn.domain.ClientDetails;

/**
 * Classe de acesso a dados (DAO) da entidade de domínio
 * {@link br.com.ufrn.domain.ClientDetails ClientDetails}
 * 
 * @author Andreza Lima
 */
@Stateless
public class ClientDetailsDAO extends GenericDAO {

	/**
	 * Método responsável por buscar o id de um usuario pelo clientID.
	 * 
	 * @param clientID
	 * @return o id do usuário.
	 */
	public Long findIdByClientID(String clientID) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getConnection();
		String sql = "select id from ClientDetails where clientId = ?";
		Long id = null;
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, clientID);
			pst.execute();
			rs = pst.getResultSet();
			if (rs.next()) {
				id = rs.getLong("id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Método responsável por validar um usuário segundo seu clientID e
	 * clientSecret
	 * 
	 * @param clientID
	 * @param clientSecret
	 * @return TRUE se for valido, FALSE caso contrário.
	 * 
	 */
	public boolean validateClient(String clientID, String clientSecret) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getConnection();
		String sql = "select * from ClientDetails where clientId = ? and clientSecret = ?";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, clientID);
			pst.setString(2, clientSecret);
			pst.execute();
			rs = pst.getResultSet();
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}

	public ClientDetails findUserById(long id) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getConnection();
		String sql = "select * from ClientDetails where id = ?";
		try {
			pst = con.prepareStatement(sql);
			pst.setLong(1, id);
			pst.execute();
			rs = pst.getResultSet();
			ClientDetails user = new ClientDetails();
			if (rs.next()) {
				 user.setUser(rs.getString("user"));
				 user.setClientId(rs.getString("clientId"));
				 user.setClientSecret(rs.getString("clientSecret"));
			}
			
			return user;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void create(String clientId, String clientSecret, String user) {
		Connection con = null;
		PreparedStatement pst = null;
		String sql = "insert into ClientDetails (clientId,clientSecret,user) values "
				+ "(?,?,?)";
		try {
			con = this.getConnection();
			pst = con.prepareStatement(sql);
			pst.setString(1, clientId);
			pst.setString(2, clientSecret);
			pst.setString(3, user);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(con, pst);
		}
	}

}
