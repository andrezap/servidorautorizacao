package br.com.ufrn.helper;

import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;

import br.com.ufrn.service.ClientDetailsService;

public class ClientDetailsHelper {

	@Inject
	private ClientDetailsService clientDetailsService;

	public String[] createClientDetails(HttpServletRequest request)
			throws Exception {
		String user = request.getHeader("user");
		if(user == null)
			throw new Exception("User not found in the request head");
		String clientId = user + "Id";
		String clientSecret = this.generateClientSecret(clientId);
		clientDetailsService.create(clientId, clientSecret, user);
		String values[] = new String[2];
		values[0] = clientId;
		values[1] = clientSecret;
		return values;

	}

	private String generateClientSecret(String clientId) throws Exception {
		Random gerador = new Random();
		int numero = gerador.nextInt(10);
		String clientSecret = clientId + String.valueOf(numero);
		MD5Generator  md5 = new MD5Generator();
		String value = md5.generateValue(clientSecret);
		return value.substring(0,7);
	}

	/**
	 * Método para validar um cliente baseado no clientID e clientSecret passado
	 * no request
	 *
	 * @param oauthRequest
	 *            - request contendo o clientID e clientSecret
	 * @return TRUE se o cliente for validado, FALSE caso contrário.
	 */
	public boolean validateClient(OAuthTokenRequest oauthRequest) {
		String clientid = oauthRequest.getClientId();
		String clientSecret = oauthRequest.getClientSecret();
		if (!clientDetailsService.validateClient(clientid, clientSecret)) {
			return false;
		}
		return true;

	}
		
}
