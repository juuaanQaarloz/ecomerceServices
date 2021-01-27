package com.mx.xpertys.security.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class TokenUtils {

	/** Constante MAGIC_KEY. */
	public static final String MAGIC_KEY = "obfuscate";

	/**
	 * Metodo para genenra Tokens seguros.
	 * 
	 * @param userDetails
	 * @return String Token de Seguridad
	 */
	public static String createToken(UserDetails userDetails) {
		String [] userNodo = userDetails.getUsername().split("_");
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
		Date resultdate = new Date(System.currentTimeMillis());
		/* Expires in one hour */
		long expires = System.currentTimeMillis() + (86400000);//15 min realizar verificar tiempo
		Date fec = new Date(expires);
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder.append(userDetails.getUsername());
		tokenBuilder.append(":");
		tokenBuilder.append(expires);
		tokenBuilder.append(":");
		tokenBuilder.append(TokenUtils.computeSignature(userDetails, expires));
		return tokenBuilder.toString();
	}

	/**
	 * @param userDetails
	 *            -Objeto de detalles de Usuario
	 * @param expires
	 *            -Cuando expira el
	 * @return String -cadena segmentada de token
	 */
	public static String computeSignature(UserDetails userDetails, long expires) {
		StandardPasswordEncoder encoder = new StandardPasswordEncoder("x9ajDR$#Qkr91");
		String password = "israel";
		//easpayb	
		if (password != null) {
			String[] userNodo = userDetails.getUsername().split("_");
			StringBuilder signatureBuilder = new StringBuilder();
			signatureBuilder.append(userDetails.getUsername());
			signatureBuilder.append(":");
			signatureBuilder.append(expires);
			signatureBuilder.append(":");
			if (encoder.matches(password, userDetails.getPassword())) {
				signatureBuilder.append(userDetails.getUsername() + expires);
			}
			signatureBuilder.append(":");
			signatureBuilder.append(TokenUtils.MAGIC_KEY);
			MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("No esta disponible el Algotimo MD5!", e);
			}
			return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
		}
		return password;
	}

	/**
	 * Metodo que obtiene Nombre de Usuario del token
	 * 
	 * @param authToken
	 *            Token de Autenticacion
	 * @return String parte del token
	 */
	public static String getUserNameFromToken(String authToken) {
		if (null == authToken) {
			return null;
		}
		String[] parts = authToken.split(":");
		return parts[0];
	}

	/**
	 * @param authToken
	 * @param userDetails
	 * @return bandera de autenticacion
	 */
	public static boolean validateToken(String authToken, UserDetails userDetails) {
		String[] parts = authToken.split(":");
		long expires = Long.parseLong(parts[1]);
		String signature = parts[2];
		if (expires < System.currentTimeMillis()) {
			return false;
		}
		return signature.equals(TokenUtils.computeSignature(userDetails, expires));
	}
}