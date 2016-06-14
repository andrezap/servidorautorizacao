package br.com.ufrn.domain;

import java.util.Date;

/**
 * Classe responsável pelo modelo de dominio de um Token de Acesso.
 * @author Andreza Lima
 *
 */

public class AccessToken {

	/** 
	 * Identificador da tabela AccessToken, gerado automaticamente quando o mesmo é
	 * persistido no banco de dados
	 */	
	private long id;

	/**
	 * Tempo em segundos que o toke irá expirar. 
	 */
	private long expirationTime;
	
	/**
	 * Id do client dono do token.
	 */
	private long idClientDetails; 
	
	/**
	 * Token de acesso a api.
	 */
	private String token;
	
	/**
	 * Timestamp da criação do token.
	 */
	private Date createdAt;

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getIdClientDetails() {
		return idClientDetails;
	}

	public void setIdClientDetails(long idClientDetails) {
		this.idClientDetails = idClientDetails;
	}



	
}
