package br.com.ufrn.helper;

import javax.inject.Inject;

import br.com.ufrn.service.AccessTokenService;

public class AccessTokenHelper {


	@Inject
	public AccessTokenService accessTokenService;

	public static final String INVALID_CLIENT = "Client authentication failed (e.g., unknown client or client_secret, no client "
			+ "authentication included, or unsupported authentication method).";

	public static final String TOKEN_EXPIRED = "Requested token is expired, request a new one";
	
	public static final String INVALID_TOKEN = "Requested token was not found";

	/**
	 * Método para validar o token. Verifica se ele existe e se ainda não
	 * expirou.
	 *
	 * @param accessToken
	 *            - token a ser validado.
	 * @return Null se o token for válido caso contrário retorna uma mensagem de
	 *         erro.
	 */
	public String validateToken(String accessToken) {
		if (!accessTokenService.checkToken(accessToken))
			return INVALID_TOKEN;

		if (accessTokenService.isTokenExpired(accessToken))
			return TOKEN_EXPIRED;

		return null;
	}
	
}
