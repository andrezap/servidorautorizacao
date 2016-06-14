package br.com.ufrn.domain;

import java.util.Date;

/**
 * Classe responsável pelo modelo de domínio de um Cliente.
 * @author Andreza Lima
 *
 */
public class ClientDetails {

	/** 
	 * Identificador da tabela ClientDetails, gerado automaticamente quando o mesmo é
	 * persistido no banco de dados
	 */
	private long id;
	
	/**
	 * Id do cliente no qual faz parte das credenciais para requisitar acesso ao servidor de autorização.
	 */
	private String clientId;
	

	/**
	 * Usuário do client que está registrado na ecodif
	 */
	private String user;
	
	/**
	 * Segredo do cliente no qual faz parte das credenciais para requisitar acesso ao servidor de autorização.
	 */
	private String clientSecret;

	/**
	 * Timestamp da criação de um cliente.
	 */
	private Date createdAt;
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	


	
}
