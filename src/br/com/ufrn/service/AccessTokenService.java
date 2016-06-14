package br.com.ufrn.service;

import java.util.Calendar;

import javax.inject.Inject;

import br.com.ufrn.dao.AccessTokenDAO;
import br.com.ufrn.domain.AccessToken;


/**
 * Classe responsável por fazer a ligação com o DAO dos tokens de acesso.
 * @author Andreza Lima
 *
 */
public class AccessTokenService {

	/**
	 * Referência a classe de acesso a dados associado aos tokens de acesso.
	 * @see br.com.ufrn.dao.AccessTokenDAO
	 */
	@Inject
	private AccessTokenDAO accessTokenDAO;

	/**
	 * Método responsável pela criação de um novo Token de acesso no banco de dados.
	 * @param accessToken - token que será salvo no banco
	 * @param clientDetailsId - id do cliente que será registrado o token.
	 */
	public void create(String accessToken, long clientDetailsId) {
		accessTokenDAO.create(accessToken, clientDetailsId);
	}

	/**
	 * Método responsável por buscar um objeto AccessToken a partir de um token 
	 * @param accessToken - token que será utilizado na busca.
	 * @return objeto AccessToken
	 */
	private AccessToken findToken(String accessToken) {
		return accessTokenDAO.findAccessToken(accessToken);
	}

	/**
	 * Método que verifica se um token expirou.
	 * @param accessToken - token que será utilizado para a verificação
	 * @return TRUE se expirou, FALSE caso contrário.
	 */
	public boolean isTokenExpired(String accessToken) {		
		AccessToken accessTokenObj  = findToken(accessToken);
		if (accessTokenObj != null) {
			return isTokenExpired(accessTokenObj);
		}
		return false;
	}
	
	/**
	 * Método que verifica se um token é valido
	 * @param accessToken - token a ser validado.
	 * @return TRUE se for válido, FALSE caso contrário.
	 */
	public boolean checkToken(String accessToken) {
		if(findToken(accessToken) != null) 
			return true;
		else
			return false;
	}

	/**
	 * Método para auxiliar na verificação do tempo de expiração de um token.
	 * @param accessToken - token a ser verificado.
	 * @return TRUE se expirou, FALSE caso contrário.
	 */
	private boolean isTokenExpired(AccessToken accessToken) {
		Calendar calendar = Calendar.getInstance();
		long created = accessToken.getCreatedAt().getTime();
		long today = calendar.getTimeInMillis();
		long expires_in = accessToken.getExpirationTime() * 1000;
		if ((expires_in + created - today) > 0) {
			return false;
		}
		return true;
	}
	
	public long findUserIdByToken(String accessToken) {
		return accessTokenDAO.findUserIdByToken(accessToken);
	}
	
	public String findTokenByClientIdAndClientSecret(String clientId, String clientSecret) {
		return accessTokenDAO.findTokenByClientIdAndClientSecret(clientId, clientSecret);
	}
}
