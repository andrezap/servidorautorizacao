package br.com.ufrn.service;

import javax.inject.Inject;

import br.com.ufrn.dao.ClientDetailsDAO;
import br.com.ufrn.domain.ClientDetails;

/**
 * Classe responsável por fazer a ligação com o DAO dos detalhes de um cliente.
 * @author Andreza Lima.
 *
 */
public class ClientDetailsService {

	/**
	 * Referência a classe de acesso a dados associado aos detalhes de um cliente.
	 * @see br.com.ufrn.dao.ClientDetailsDAO
	 */
	@Inject
	private ClientDetailsDAO clientDetailsDAO;
	
	/**
	 * Referência a classe de serviço para acesso ao DAO de acesso aos tokens
	 * @see br.com.ufrn.service.AccessTokenService
	 */
	@Inject
	private AccessTokenService accessTokenService;

	/**
	 * Método pra validar um cliente.
	 * @param clientID - clientId de um usuário.
	 * @param clientSecret - clientSecret de um usuário.
	 * @return TRUE se válido, FALSE caso contrário.
	 */
	public boolean validateClient(String clientID, String clientSecret) {
		return clientDetailsDAO.validateClient(clientID, clientSecret);
	}
	
	/**
	 * Método responsável por buscar um id de um usuário a partir do seu clientId
	 * @param clientID - clientId do usuário
	 * @return id do usuário.
	 */
	public Long findIdByClientID(String clientID){
		return clientDetailsDAO.findIdByClientID(clientID); 
	}
	
	/**
	 * Método responsável por buscar um usuário a partir de um token.
	 * @param accessToken  - token que será utilizado pela busca.
	 * @return o usuário dono do token.
	 */
	public ClientDetails findUserByToken(String accessToken) {
		long user_id = accessTokenService.findUserIdByToken(accessToken);
		return clientDetailsDAO.findUserById(user_id);
	}
	
	public void create(String clientId, String clientSecret, String user) {
		clientDetailsDAO.create(clientId, clientSecret, user);
	}
	
}	
