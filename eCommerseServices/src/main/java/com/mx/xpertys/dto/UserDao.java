package com.mx.xpertys.dto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDao extends Dao<User, Long>, UserDetailsService {
	/**
	 * Metodo para poder encontrar un usuario por su nombre
	 * @param name - Nombre del usuario
	 */
	User findByName(String name);
}
