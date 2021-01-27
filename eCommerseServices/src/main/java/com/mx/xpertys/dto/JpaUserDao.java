package com.mx.xpertys.dto;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class JpaUserDao extends JpaDao<User, Long> implements UserDao {
	
	/**
	 * Constructor de la clase JpaUserDao
	 */
	public JpaUserDao() {
		super(User.class);
	}

	/**
	 * Metodo que se utiliza para buscar un usuario por su nombre de usuario
	 * @param username - Nombre de usuario para la busqueda
	 * @return El usuario encontrado por nombre de usuario
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.findByName(username);
		if (null == user) {
			throw new UsernameNotFoundException("El usuario con nombre " + username + " no ha sido encontrado");
		}

		return user;
	}

	/**
	 * Metodo que se utiliza para buscar un usuario por su nombre de usuario
	 * @param name - Nombre de usuario para la busqueda
	 * @return El usuario encontrado por nombre de usuario
	 */
	@Override
	public User findByName(String name) {
		System.out.println("Entro al metodo de findByName");
		String clave = null;
		User usr = null;
		String password = "israel";
		if(password != null){
			StandardPasswordEncoder encod = new StandardPasswordEncoder("x9ajDR$#Qkr91");
			String claveCodificada = encod.encode(password);	
			
			usr = new User(name, claveCodificada);
		}
		return usr;
	}

}
