package com.mx.xpertys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@javax.persistence.Entity
public class User implements UserDetails, Serializable {

	/**
	 * Numero de serie de la clase
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Propiedad id del usuario
	 */
	private Long id;
	/**
	 * Propiedad nombre del usuario
	 */
	private String name;
	/**
	 * Propiedad contraseña del usuario
	 */
	private String password;
	/**
	 * Propiedad cadena set de roles
	 */
	private Set<String> roles = new HashSet<String>();

	/**
	 * Constructor del usuario
	 */
	protected User() {
		/* Reflection instantiation */
	}

	/**
	 * Asignar el nombre y la contraseña
	 * 
	 * @param name
	 *            - nombre del usuario
	 * @param passwordHash
	 *            - Contraseña
	 */
	public User(String name, String passwordHash) {
		this.name = name;
		this.password = passwordHash;
	}

	/**
	 * 
	 * @return Id del usuario
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Asignar el id del usuario
	 * 
	 * @param id
	 *            - Id del usuario
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return name Regresa el nombre del Usuario
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Asignar el nombre del usuario
	 * 
	 * @param name
	 *            - Nombre del usuario
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Los roles del usuario
	 */
	public Set<String> getRoles() {
		return this.roles;
	}

	/**
	 * Asignar los roles del usuario
	 * 
	 * @param roles
	 *            - Roles del usuario
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	/**
	 * Asignar un nuevo role de usuario
	 * 
	 * @param role
	 *            - Role de usuario
	 */
	public void addRole(String role) {
		this.roles.add(role);
	}

	/**
	 * 
	 * @return La contraseña del usuario
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/**
	 * Asignar la contraseña del usuario
	 * 
	 * @param password
	 *            - Contraseña del usuario
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	// public Collection<? extends GrantedAuthority> getAuthorities() {
	// Set<String> roles = this.getRoles();
	//
	// if (roles == null) {
	// return Collections.emptyList();
	// }
	//
	// Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
	// for (String role : roles) {
	// authorities.add(new SimpleGrantedAuthority(role));
	// }
	//
	// return authorities;
	// }
	/**
	 * 
	 * @return Una coleccion de autorizacion
	 */
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		grantedAuthorities.add(new SimpleGrantedAuthority("user"));
		return grantedAuthorities;
	}

	/**
	 * 
	 * @return El nombre del usuario
	 */
	@Override
	public String getUsername() {
		return this.name;
	}

	/**
	 * 
	 * @return Un valor boleano
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 
	 * @return Un valor boleano
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 
	 * @return Un valor boleano
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 
	 * @return Un valor boleano
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
