package br.com.ufrn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.ejb.Stateless;

import br.com.ufrn.domain.AccessToken;

/**
 * Classe de acesso a dados (DAO) da entidade de domínio
 * {@link br.com.ufrn.domain.AccessToken AcessToken}
 * 
 * @author Andreza Lima
 */

@Stateless
public class AccessTokenDAO extends GenericDAO {

	/**
	 * Método responsável por inserir um novo token no banco de dados.
	 * 
	 * @param accessToken
	 * @param clientDetailsId
	 */
	public void create(String accessToken, long clientDetailsId) {
		Connection con = null;
		PreparedStatement pst = null;
		String sql = "insert into AccessToken (id_clientDetails,token) values "
				+ "(?,?)";
		try {
			con = this.getConnection();
			pst = con.prepareStatement(sql);
			pst.setLong(1, clientDetailsId);
			pst.setString(2, accessToken);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(con, pst);
		}
	}

	/**
	 * Método responsável por buscar um objeto AccessToken a partir do token.
	 * 
	 * @param token
	 *            - token que irá ser utilizado para a busca.
	 * @return objeto AccessToken
	 */
	public AccessToken findAccessToken(String token) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getConnection();
		String sql = "select * from AccessToken where token = ?";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, token);
			pst.execute();
			rs = pst.getResultSet();
			if (rs.next()) {
				AccessToken accessToken = new AccessToken();
				accessToken.setId(rs.getInt("id"));
				accessToken.setExpirationTime(rs.getLong("expirationTime"));
				accessToken.setToken(rs.getString("token"));
				Date date = rs.getDate("created_at");
				accessToken.setCreatedAt(date);
				return accessToken;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Método responsável por buscar um id do cliente a partir do token.
	 * 
	 * @param token
	 *            - token a ser utilizado na busca.
	 * @return id do cliente.
	 */
	public Long findUserIdByToken(String token) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getConnection();
		String sql = "select id_clientDetails from AccessToken where token = ?";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, token);
			pst.execute();
			rs = pst.getResultSet();
			if (rs.next()) {
				long user_id = rs.getLong("id_clientDetails");
				return user_id;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String findTokenByClientIdAndClientSecret(String clientId,
			String clientSecret) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection con = getConnection();
		String sql = "select token from AccessToken at inner join ClientDetails on id_clientDetails"
				+ " where clientId = ? and clientSecret = ? order by at.created_at desc limit 1";
		try {
			pst = con.prepareStatement(sql);
			pst.setString(1, "clientId");
			pst.setString(2, "clientSecret");
			pst.execute();
			rs = pst.getResultSet();
			if (rs.next()) {
				return rs.getString("token");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(con, pst);
		}

		return null;

	}

}
