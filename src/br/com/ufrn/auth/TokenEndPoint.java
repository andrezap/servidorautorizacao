package br.com.ufrn.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import br.com.ufrn.domain.ClientDetails;
import br.com.ufrn.helper.AccessTokenHelper;
import br.com.ufrn.helper.ClientDetailsHelper;
import br.com.ufrn.service.AccessTokenService;
import br.com.ufrn.service.ClientDetailsService;

/**
 * Servlet responsável pelos serviços de validar token e registrar um novo.
 *
 * @author Andreza Lima
 *
 */
public class TokenEndPoint extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private ClientDetailsHelper clientDetailsHelper;

	@Inject
	private AccessTokenHelper accessTokenHelper;

	@Inject
	private AccessTokenService accessTokenService;

	@Inject
	private ClientDetailsService clientDetailsService;

	public static final String INVALID_CLIENT = "Client authentication failed (e.g., unknown client or client_secret, no client "
			+ "authentication included, or unsupported authentication method).";

	public static final String INVALID_TOKEN = "Requested token was not found";

	/**
	 * Método POST do servlet que cria um client ID, um client Secret e um token
	 * para o usuário que está tentando acessa-lo.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			String[] clientInfo = clientDetailsHelper.createClientDetails(request);
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());
			Long id = clientDetailsService.findIdByClientID(clientInfo[0]);
			String accessToken = oauthIssuerImpl.accessToken();
			accessTokenService.create(accessToken, id);
			response.setHeader("token", accessToken);
			response.setHeader("clientId", clientInfo[0]);
			response.setHeader("clientSecret", clientInfo[1]);
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Método PUT do servlet que utiliza a biblioteca Apache Oltu para criar um
	 * token para o usuário que está tentando acessa-lo.
	 *
	 * @param request
	 *            - clientID e clienteSecret para serem validados e gerar um
	 *            novo token.
	 * @param reponse
	 *            - token gerado
	 */
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		OAuthResponse r = null;

		OAuthTokenRequest oauthRequest = null;

		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

		try {
			oauthRequest = new OAuthTokenRequest(request);

			if (!clientDetailsHelper.validateClient(oauthRequest)) {
				r = OAuthASResponse
						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
						.setErrorDescription(INVALID_CLIENT).buildJSONMessage();
			} else {

				String accessToken = oauthIssuerImpl.accessToken();
				String clientid = oauthRequest.getClientId();
				long user_id = clientDetailsService.findIdByClientID(clientid);
				accessTokenService.create(accessToken, user_id);

				r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
						.setAccessToken(accessToken).setExpiresIn("7884000")
						.buildJSONMessage();

			}

			response.setStatus(r.getResponseStatus());
			PrintWriter pw = response.getWriter();
			pw.print(r.getBody());
			pw.flush();
			pw.close();

		} catch (OAuthProblemException ex) {
			try {
				r = OAuthResponse.errorResponse(401).error(ex)
						.buildJSONMessage();
				response.setStatus(r.getResponseStatus());

				PrintWriter pw = response.getWriter();
				pw.print(r.getBody());
				pw.flush();
				pw.close();

				response.sendError(401);
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			}
		} catch (OAuthSystemException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Método GET do servlet para buscar o usuário relacionado ao token
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("token") != null) {
			String accessToken = request.getParameter("token");
			OAuthResponse r = null;
			try {
				String msg = accessTokenHelper.validateToken(accessToken);

				if (msg != null) {
					r = OAuthResponse.errorResponse(401).setError(msg)
							.buildJSONMessage();
					response.setStatus(r.getResponseStatus());
				} else {
					ClientDetails user = clientDetailsService
							.findUserByToken(accessToken);
					response.setContentType("text/plain");
					response.setHeader("user", user.getUser());
//					response.setHeader("clientID", user.getClientId());
//					response.setHeader("clientSecret", user.getClientSecret());
					response.setStatus(200);
				}
			} catch (OAuthSystemException e) {
				e.printStackTrace();
			}

		}
	}

}