package com.mx.xpertys.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private final UserDetailsService userService;
	public AuthenticationTokenProcessingFilter(UserDetailsService userService) {
		this.userService = userService;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		String userName = TokenUtils.getUserNameFromToken(authToken);
		if (userName != null) {
			UserDetails userDetails = this.userService
					.loadUserByUsername(userName);
			if (TokenUtils.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(
						authentication);
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * Metodo de Peticiones.
	 * 
	 * @param request -Servlet Reques
	 * @return http request
	 */
	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Esperando una Peticion HTTP");
		}
		return (HttpServletRequest) request;
	}

	/**
	 * Extract de uutenticacion de request.
	 * 
	 * @param httpRequest - httpRequest de la peticion
	 * @return String token de autenticacion
	 */
	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
		/* Get token from header */
		String authToken = httpRequest.getHeader("x-auth-token");
		/* If token not found get it from request parameter */
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}
		return authToken;
	}
}