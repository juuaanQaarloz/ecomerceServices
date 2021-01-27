package com.mx.xpertys.controllers;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mx.xpertys.dto.CategoriaProductos;
import com.mx.xpertys.dto.Categorias;
import com.mx.xpertys.dto.Promocion;
import com.mx.xpertys.dto.RowSetObject;
import com.mx.xpertys.dto.Usuario;
import com.mx.xpertys.dto.Ventas;
import com.mx.xpertys.exception.AccesoException;
import com.mx.xpertys.utils.ConstantesGeneral;
import com.mx.xpertys.utils.Utils;
import com.mysql.cj.xdevapi.Type;

@RestController
@RequestMapping(ConstantesGeneral.URL_CONTROLLER_WS)
public class AccesoPublicoController {

	@RequestMapping(value = ConstantesGeneral.URL_GET_USUARIOS, method = RequestMethod.POST)
	public List<Usuario> consultarUsuarios(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		List<Usuario> usuarios = new ArrayList<Usuario>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String where = "";
				if (rowSet.getPnIdUsuario() != null) {
					where = "where id_usuario = " + rowSet.getPnIdUsuario();
				}
				if (rowSet.getPcUsuario() != null && rowSet.getPcPassword() != null) {
					where = "where email = '" + rowSet.getPcUsuario() + "' and password = '" + rowSet.getPcPassword()+"'";
				}
				ResultSet rs = null;
				String query = "SELECT * FROM per_usuario " + where;
				System.out.println(" querys --> " + query);
				Statement st = conexion1.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
					Usuario user = new Usuario();
					user.setCalle(rs.getString("calle"));
					user.setCiudad(rs.getString("ciudad"));
					user.setCodPos(rs.getString("codpos"));
					user.setEmail(rs.getString("email"));
					user.setEstado(rs.getString("estado"));
					user.setFecRegistro(rs.getString("fecregistro"));
					user.setIdUsuario(rs.getString("id_usuario"));
					user.setMunicipio(rs.getString("municipio"));
					user.setNoExt(rs.getString("noext"));
					user.setNoInt(rs.getString("noint"));
					user.setNombreUsuario(rs.getString("nombre"));
					user.setPais(rs.getString("pais"));
					user.setPassword(rs.getString("password"));
					user.setReferencia(rs.getString("referencia"));
					user.setTipoUsuario(rs.getString("tipousuario"));
					user.setColonia(rs.getString("colonia"));
					usuarios.add(user);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();
		}

		if (rowSet.getPcUsuario() != null && rowSet.getPcPassword() != null && usuarios.isEmpty()) {
			throw new AccesoException(" El Correo electrónico o la contraseña son invalidos");
		}

		return usuarios;
	}

	@RequestMapping(value = ConstantesGeneral.URL_NGET_CATEGORIAS, method = RequestMethod.POST)
	public List<Categorias> consultarCategorias(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		List<Categorias> categorias = new ArrayList<Categorias>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String where = "";
				if (rowSet.getPnIdGenerico() != null) {
					where = "where id_categoria = " + rowSet.getPnIdGenerico();
				} else if (rowSet.getPcIndVigente() != null) {
					where = "where indvigente = " + rowSet.getPcIndVigente();
				}
				ResultSet rs = null;
				String query = "SELECT * FROM pro_categoria " + where;
				Statement st = conexion1.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
					Categorias cat = new Categorias();
					cat.setDescripcion(rs.getString("descripcion"));
					cat.setFecRegistro(rs.getString("fecregistro"));
					cat.setIdCategoria(rs.getString("id_categoria"));
					cat.setIndVigente(rs.getString("indvigente"));
					cat.setNombreCategoria(rs.getString("nombrecategoria"));
					cat.setImagen(rs.getString("imagen"));
					categorias.add(cat);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();

		}
		return categorias;
	}

	@RequestMapping(value = ConstantesGeneral.URL_GET_PRODUCTOS, method = RequestMethod.POST)
	public List<CategoriaProductos> consultarCategoriasProducto(@RequestBody RowSetObject rowSet)
			throws ClassNotFoundException {
		List<CategoriaProductos> categorias = new ArrayList<CategoriaProductos>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String where = "";
				if (rowSet.getPnIdGenerico() != null) {
					where = "where id_categoria = " + rowSet.getPnIdGenerico();
				}
				if (rowSet.getPnIdProducto() != null) {
					where = "where id_producto = " + rowSet.getPnIdProducto();
				}
				ResultSet rs = null;
				String query = "SELECT * FROM pro_categoria_producto " + where;
				Statement st = conexion1.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
					CategoriaProductos cat = new CategoriaProductos();
					cat.setDescripcion(rs.getString("descripcion"));
					cat.setFecRegistro(rs.getString("fecregistro"));
					cat.setIdCategoria(rs.getString("id_categoria"));
					cat.setIdProducto(rs.getString("id_producto"));
					cat.setImagen(rs.getString("imagen"));
					cat.setIndVigente(rs.getString("indvigente"));
					cat.setNoExistencias(rs.getString("no_existencias"));
					cat.setNombreProducto(rs.getString("nombreproducto"));
					cat.setNoVendidas(rs.getString("no_vendidas"));
					cat.setOrden(rs.getString("orden"));
					cat.setPrecio(rs.getString("precio"));
					categorias.add(cat);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();

		}
		return categorias;
	}

	@RequestMapping(value = ConstantesGeneral.URL_GET_PRODUCTOS_ACTIVOS, method = RequestMethod.POST)
	public List<CategoriaProductos> consultarCategoriasProductoActs(@RequestBody RowSetObject rowSet)
			throws ClassNotFoundException {
		List<CategoriaProductos> categorias = new ArrayList<CategoriaProductos>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String where = null;
				where = "where indvigente = 'S'";
				if (rowSet.getPnIdGenerico() != null) {
					where += "and id_categoria = " + rowSet.getPnIdGenerico();
				}
				if (rowSet.getPnIdProducto() != null) {
					where += "and id_producto = " + rowSet.getPnIdProducto();
				}
				if (rowSet.getPcTextBuscar() != null) {
					where += "and UPPER(NOMBREPRODUCTO) LIKE '%" + rowSet.getPcTextBuscar() + "%'";
				}
				if (rowSet.getPcRangoPrecioAl() != null && rowSet.getPcRangoPrecioDe() != null) {
					where += "and precio BETWEEN " + rowSet.getPcRangoPrecioDe() + " AND " + rowSet.getPcRangoPrecioAl();
				}
				ResultSet rs = null;
				String query = "SELECT * FROM pro_categoria_producto " + where;
				Statement st = conexion1.createStatement();
				System.out.println( " query --> " + query);
				rs = st.executeQuery(query);
				while (rs.next()) {
					CategoriaProductos cat = new CategoriaProductos();
					cat.setDescripcion(rs.getString("descripcion"));
					cat.setFecRegistro(rs.getString("fecregistro"));
					cat.setIdCategoria(rs.getString("id_categoria"));
					cat.setIdProducto(rs.getString("id_producto"));
					cat.setImagen(rs.getString("imagen"));
					cat.setIndVigente(rs.getString("indvigente"));
					cat.setNoExistencias(rs.getString("no_existencias"));
					cat.setNombreProducto(rs.getString("nombreproducto"));
					cat.setNoVendidas(rs.getString("no_vendidas"));
					cat.setOrden(rs.getString("orden"));
					cat.setPrecio(rs.getString("precio"));
					categorias.add(cat);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();

		}
		return categorias;
	}

	@RequestMapping(value = ConstantesGeneral.URL_GET_PROMOCIONES, method = RequestMethod.POST)
	public List<Promocion> consultarPromocion(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		List<Promocion> promociones = new ArrayList<Promocion>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String where = "";
				ResultSet rs = null;
				String query = "SELECT * FROM mkt_promocion ";
				Statement st = conexion1.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
					Promocion cat = new Promocion();
					cat.setDescripcion(rs.getString("descripcion"));
					cat.setIdProducto(rs.getString("id_producto"));
					cat.setIdPromocion(rs.getString("id_promocion"));
					cat.setImagen(rs.getString("imagen"));
					cat.setIndVigente(rs.getString("indvigente"));
					cat.setNombrePromocion(rs.getString("nombrepromocion"));
					cat.setOrden(rs.getString("orden"));
					promociones.add(cat);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();

		}
		return promociones;
	}

	@RequestMapping(value = ConstantesGeneral.URL_GET_PROMOCIONES_ACTIVO, method = RequestMethod.POST)
	public List<Promocion> consultarPromocionActivo(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		List<Promocion> promociones = new ArrayList<Promocion>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				ResultSet rs = null;
				String query = "SELECT * FROM mkt_promocion WHERE indvigente = 'S'";
				Statement st = conexion1.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
					Promocion cat = new Promocion();
					cat.setDescripcion(rs.getString("descripcion"));
					cat.setIdProducto(rs.getString("id_producto"));
					cat.setIdPromocion(rs.getString("id_promocion"));
					cat.setImagen(rs.getString("imagen"));
					cat.setIndVigente(rs.getString("indvigente"));
					cat.setNombrePromocion(rs.getString("nombrepromocion"));
					cat.setOrden(rs.getString("orden"));
					promociones.add(cat);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();

		}
		return promociones;
	}

	@RequestMapping(value = ConstantesGeneral.URL_GET_VENTAS, method = RequestMethod.POST)
	public List<Ventas> consultarVentas(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		List<Ventas> ventas = new ArrayList<Ventas>(0);
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String where = "";
				if (rowSet.getPnIdGenerico() != null) {
					where = "where id_venta = " + rowSet.getPnIdGenerico();
				}
				if (rowSet.getPnIdProducto() != null) {
					where = "where id_producto = " + rowSet.getPnIdProducto();
				}
				if (rowSet.getPnIdUsuario() != null) {
					where = "where id_usuario = " + rowSet.getPnIdUsuario();
				}
				ResultSet rs = null;
				String query = "SELECT * FROM vnt_venta " + where;
				Statement st = conexion1.createStatement();
				rs = st.executeQuery(query);
				while (rs.next()) {
					Ventas cat = new Ventas();
					cat.setCalle(rs.getString("calle"));
					cat.setCiudad(rs.getString("ciudad"));
					cat.setCodPos(rs.getString("codpos"));
					cat.setColonia(rs.getString("colonia"));
					cat.setEstado(rs.getString("estado"));
					cat.setFecVenta(rs.getString("fecventa"));
					cat.setIdProducto(rs.getString("id_producto"));
					cat.setIdUsuario(rs.getString("id_usuario"));
					cat.setIdVenta(rs.getString("id_venta"));
					cat.setMtoVenta(rs.getString("mtoventa"));
					cat.setMunicipio(rs.getString("municipio"));
					cat.setNoExt(rs.getString("noext"));
					cat.setNoInt(rs.getString("noint"));
					cat.setPais(rs.getString("pais"));
					cat.setReferencia(rs.getString("referencia"));
					ventas.add(cat);
				}
				st.close();
			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();

		}
		return ventas;
	}

	@RequestMapping(value = ConstantesGeneral.URL_CRUD_TBL_USUARIOS, method = RequestMethod.POST)
	public String crudTblUser(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String query = "";

				if (rowSet.getPcAccion() != null) {

					if (rowSet.getPcAccion().equals("INSERT")) {
						System.out.println(" REALIZA EL INSERT ");
						query = "INSERT INTO per_usuario (ID_USUARIO, FECREGISTRO, NOMBRE, EMAIL, PASSWORD, TIPOUSUARIO, CALLE, NOEXT, NOINT, CODPOS, COLONIA, MUNICIPIO, CIUDAD, ESTADO, PAIS, REFERENCIA) VALUES (";
						query += "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
						String[] datos = rowSet.getPcTextoAdd().split("\\,");

						// create a sql date object so we can use it in our
						// INSERT statement
						Calendar calendar = Calendar.getInstance();
						java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

						PreparedStatement pst = conexion1.prepareStatement(query);

						pst.setString(1, datos[0]);
						pst.setDate(2, startDate);
						pst.setString(3, datos[2]);
						pst.setString(4, datos[3]);
						pst.setString(5, datos[4]);
						pst.setString(6, datos[5]);

						if(datos[6].equals("null")) {
							pst.setNull(7, java.sql.Types.VARCHAR);		
						} else pst.setString(7, datos[6]);
						if(datos[7].equals("null")) {
							pst.setNull(8, java.sql.Types.VARCHAR);		
						} else pst.setString(8, datos[7]);
						if(datos[8].equals("null")) {
							pst.setNull(9, java.sql.Types.VARCHAR);		
						} else pst.setString(9, datos[8]);
						if(datos[9].equals("null")) {
							pst.setNull(10, java.sql.Types.VARCHAR);		
						} else pst.setString(10, datos[9]);
						if(datos[10].equals("null")) {
							pst.setNull(11, java.sql.Types.VARCHAR);		
						} else pst.setString(11, datos[10]);
						if(datos[11].equals("null")) {
							pst.setNull(12, java.sql.Types.VARCHAR);		
						} else pst.setString(12, datos[11]);
						if(datos[12].equals("null")) {
							pst.setNull(13, java.sql.Types.VARCHAR);		
						} else pst.setString(13, datos[12]);
						if(datos[13].equals("null")) {
							pst.setNull(14, java.sql.Types.VARCHAR);		
						} else pst.setString(14, datos[13]);
						if(datos[14].equals("null")) {
							pst.setNull(15, java.sql.Types.VARCHAR);		
						} else pst.setString(15, datos[14]);
						if(datos[15].equals("null")) {
							pst.setNull(16, java.sql.Types.VARCHAR);		
						} else pst.setString(16, datos[15]);
						/*
						pst.setString(7, datos[6]);
						pst.setString(8, datos[7]);
						pst.setString(9, datos[8]);
						pst.setString(10, datos[9]);
						pst.setString(11, datos[10]);
						pst.setString(12, datos[11]);
						pst.setString(13, datos[12]);
						pst.setString(14, datos[13]);
						pst.setString(15, datos[14]);
						pst.setString(16, datos[15]);
						*/
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("added");
						} else {
							System.out.println("failed to add");
						}

					} else if (rowSet.getPcAccion().equals("UPDATE")) {

						System.out.println(" REALIZA EL UPDATE ");
						query = "UPDATE per_usuario SET " + Utils.prepararDatosUpdate(rowSet.getPcTextoEdit())
								+ " WHERE per_usuario.ID_USUARIO = " + rowSet.getPnIdUsuario();

						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoEdit().split("\\|");
						int count = 1;
						for (String data : datos) {
							String[] valores = data.split("\\::");
							String[] value = valores[1].split("\\&");
							if (value[0].equals("N")) {
								if(value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.INTEGER);		
								} else pst.setInt(count, Integer.parseInt(value[1]));
							} else if (value[0].equals("D")) {
								if(value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.DOUBLE);		
								} else pst.setDouble(count, Double.parseDouble(value[1]));
							} else if (value[0].equals("S")) {
								if(value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.VARCHAR);		
								} else pst.setString(count, value[1]);
							}
							count++;
						}
						System.out.println("query edit --> " + query);
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si modifico");
						} else {
							System.out.println("failed to modify");
						}

					} else {

						System.out.println(" REALIZA EL DELETE ");

						query = "DELETE FROM `per_usuario` WHERE `per_usuario`.`ID_USUARIO` = ? ";
						PreparedStatement pst = conexion1.prepareStatement(query);
						pst.setInt(1, Integer.parseInt(rowSet.getPnIdUsuario()));
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si delete");
						} else {
							System.out.println("failed to delete");
						}

					}

					System.out.println("QUERY A EJECUTAR --> " + query);

				} else {
					return "{\"status\":\"N\"}";
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AccesoException(e.getMessage(), e);
		}
		return "{\"status\":\"S\"}";
	}

	@RequestMapping(value = ConstantesGeneral.URL_CRUD_TBL_CATEGORIA, method = RequestMethod.POST)
	public String crudTblCategoria(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String query = "";

				if (rowSet.getPcAccion() != null) {

					if (rowSet.getPcAccion().equals("INSERT")) {
						System.out.println(" REALIZA EL INSERT ");
						query = "INSERT INTO pro_categoria (ID_CATEGORIA, NOMBRECATEGORIA, DESCRIPCION, FECREGISTRO, INDVIGENTE, IMAGEN) VALUES ( ?,?,?,?,?,? )";
						
						Calendar calendar = Calendar.getInstance();
						java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoAdd().split("\\,");
						
						pst.setString(1, datos[0]);
						pst.setString(2, datos[1]);
						pst.setString(3, datos[2]);
						pst.setDate(4, startDate);
						pst.setString(5, datos[3]);
						pst.setString(6, datos[4]);
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("added");
						} else {
							System.out.println("failed to add");
						}

					} else if (rowSet.getPcAccion().equals("UPDATE")) {
						System.out.println(" REALIZA EL UPDATE ");
						query = "UPDATE pro_categoria SET " + Utils.prepararDatosUpdate(rowSet.getPcTextoEdit())
								+ " WHERE pro_categoria.ID_CATEGORIA = " + rowSet.getPnIdUsuario();
						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoEdit().split("\\|");
						int count = 1;
						for (String data : datos) {
							String[] valores = data.split("\\::");
							String[] value = valores[1].split("\\&");
							if (value[0].equals("N")) {
								if(value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.INTEGER);		
								} else pst.setInt(count, Integer.parseInt(value[1]));
							} else if (value[0].equals("D")) {
								if(value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.DOUBLE);		
								} else pst.setDouble(count, Double.parseDouble(value[1]));
							} else if (value[0].equals("S")) {
								if(value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.VARCHAR);		
								} else pst.setString(count, value[1]);
							}
							count++;
						}
						System.out.println("query edit --> " + query);
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si modifico");
						} else {
							System.out.println("failed to modify");
						}

					} else {
						System.out.println(" REALIZA EL DELETE ");
						query = "DELETE FROM pro_categoria WHERE pro_categoria.ID_CATEGORIA = ? ";
						PreparedStatement pst = conexion1.prepareStatement(query);
						pst.setInt(1, Integer.parseInt(rowSet.getPnIdUsuario()));
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si delete");
						} else {
							System.out.println("failed to delete");
						}
					}

				} else {
					return "{\"status\":\"N\"}";
				}

			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();
			throw new AccesoException(e.getMessage(), e);
		}
		return "{\"status\":\"S\"}";
	}

	@RequestMapping(value = ConstantesGeneral.URL_CRUD_TBL_CATEGORIA_PRODUCTO, method = RequestMethod.POST)
	public String crudTblCategoriaProducto(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String query = "";

				if (rowSet.getPcAccion() != null) {

					if (rowSet.getPcAccion().equals("INSERT")) {
						System.out.println(" REALIZA EL INSERT ");
						query = "INSERT INTO pro_categoria_producto (ID_PRODUCTO, ID_CATEGORIA, ORDEN, NOMBREPRODUCTO, DESCRIPCION, FECREGISTRO, NO_EXISTENCIAS, NO_VENDIDAS, IMAGEN, INDVIGENTE, PRECIO ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
						Calendar calendar = Calendar.getInstance();
						java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoAdd().split("\\,");
						
						pst.setString(1, datos[0]);
						pst.setInt(2, Integer.parseInt(datos[1]));
						pst.setInt(3, Integer.parseInt(datos[2]));
						pst.setString(4, datos[3]);
						pst.setString(5, datos[4]);
						pst.setDate(6, startDate);
						pst.setInt(7, Integer.parseInt(datos[5]));
						pst.setInt(8, Integer.parseInt(datos[6]));
						pst.setString(9, datos[7]);
						pst.setString(10, datos[8]);
						pst.setDouble(11, Double.parseDouble(datos[9]));
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("added");
						} else {
							System.out.println("failed to add");
						}

					} else if (rowSet.getPcAccion().equals("UPDATE")) {

						System.out.println(" REALIZA EL UPDATE ");
						query = "UPDATE pro_categoria_producto SET " + Utils.prepararDatosUpdate(rowSet.getPcTextoEdit())
								+ " WHERE pro_categoria_producto.ID_PRODUCTO = " + rowSet.getPnIdUsuario();
						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoEdit().split("\\|");
						int count = 1;
						for (String data : datos) {
							String[] valores = data.split("\\::");
							String[] value = valores[1].split("\\&");
							if (value[0].equals("N")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.INTEGER);
								} else
									pst.setInt(count, Integer.parseInt(value[1]));
							} else if (value[0].equals("D")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.DOUBLE);
								} else
									pst.setDouble(count, Double.parseDouble(value[1]));
							} else if (value[0].equals("S")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.VARCHAR);
								} else
									pst.setString(count, value[1]);
							}
							count++;
						}
						System.out.println("query edit --> " + query);
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si modifico");
						} else {
							System.out.println("failed to modify");
						}

					} else {

						System.out.println(" REALIZA EL DELETE ");

						query = "DELETE FROM per_usuario WHERE per_usuario.ID_USUARIO = " + rowSet.getPnIdUsuario();
					}

				} else {
					return "{\"status\":\"N\"}";
				}

			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();
			throw new AccesoException(e.getMessage(), e);
		}
		return "{\"status\":\"S\"}";
	}

	@RequestMapping(value = ConstantesGeneral.URL_CRUD_TBL_PROMOCION, method = RequestMethod.POST)
	public String crudTblPromocion(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String query = "";

				if (rowSet.getPcAccion() != null) {

					if (rowSet.getPcAccion().equals("INSERT")) {
						System.out.println(" REALIZA EL INSERT ");
						query = "INSERT INTO mkt_promocion (ID_PROMOCION, ORDEN, NOMBREPROMOCION, DESCRIPCION, IMAGEN, INDVIGENTE, ID_PRODUCTO) VALUES (";
						query += "?,?,?,?,?,?,? )";
						
						System.out.println("query insert --> " + query);
						
						String[] datos = rowSet.getPcTextoAdd().split("\\,");
						PreparedStatement pst = conexion1.prepareStatement(query);

						pst.setString(1, datos[0]);
						pst.setString(2, datos[1]);
						pst.setString(3, datos[2]);
						pst.setString(4, datos[3]);
						pst.setString(5, datos[4]);
						pst.setString(6, datos[5]);
						if (datos[6].equals("null")) {
							pst.setNull(7, java.sql.Types.INTEGER);
						} else
							pst.setInt(7, Integer.parseInt(datos[6]));
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("added");
						} else {
							System.out.println("failed to add");
						}

					} else if (rowSet.getPcAccion().equals("UPDATE")) {

						System.out.println(" REALIZA EL UPDATE ");
						query = "UPDATE mkt_promocion SET " + Utils.prepararDatosUpdate(rowSet.getPcTextoEdit())
						+ " WHERE mkt_promocion.ID_PROMOCION = " + rowSet.getPnIdUsuario();

						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoEdit().split("\\|");
						int count = 1;
						for (String data : datos) {
							String[] valores = data.split("\\::");
							String[] value = valores[1].split("\\&");
							if (value[0].equals("N")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.INTEGER);
								} else
									pst.setInt(count, Integer.parseInt(value[1]));
							} else if (value[0].equals("D")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.DOUBLE);
								} else
									pst.setDouble(count, Double.parseDouble(value[1]));
							} else if (value[0].equals("S")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.VARCHAR);
								} else
									pst.setString(count, value[1]);
							}
							count++;
						}
						System.out.println("query edit --> " + query);
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si modifico");
						} else {
							System.out.println("failed to modify");
						}

					} else {

						System.out.println(" REALIZA EL DELETE ");

						query = "DELETE FROM mkt_promocion WHERE mkt_promocion.ID_PROMOCION = ? ";
						PreparedStatement pst = conexion1.prepareStatement(query);
						pst.setInt(1, Integer.parseInt(rowSet.getPnIdUsuario()));
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si delete");
						} else {
							System.out.println("failed to delete");
						}
					}
				} else {
					return "{\"status\":\"N\"}";
				}

			}
		} catch (SQLException e) {
			System.out.println(
					"Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();
			throw new AccesoException(e.getMessage(), e);
		}
		return "{\"status\":\"S\"}";
	}

	@RequestMapping(value = ConstantesGeneral.URL_CRUD_TBL_VENTA, method = RequestMethod.POST)
	public String crudTblVenta(@RequestBody RowSetObject rowSet) throws ClassNotFoundException {
		Connection conexion1 = null;
		try {
			Class.forName(ConstantesGeneral.NAME_DRIVE_DATABASE);
			conexion1 = DriverManager.getConnection(Utils.crearUrlDataBase());
			if (conexion1 != null) {
				String query = "";

				if (rowSet.getPcAccion() != null) {

					if (rowSet.getPcAccion().equals("INSERT")) {
						System.out.println(" REALIZA EL INSERT ");
						query = "INSERT INTO vnt_venta (ID_VENTA, ID_USUARIO, FECVENTA, ID_PRODUCTO, MTOVENTA, CALLE, NOEXT, NOINT, CODPOS, COLONIA, MUNICIPIO, CIUDAD, ESTADO, PAIS, REFERENCIA) VALUES (";
						query += "?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";

						Calendar calendar = Calendar.getInstance();
						java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

						PreparedStatement pst = conexion1.prepareStatement(query);

						String[] datos = rowSet.getPcTextoAdd().split("\\,");
						
						pst.setString(1, datos[0]);
						pst.setInt(2, Integer.parseInt(datos[1]));
						pst.setDate(3, startDate);
						pst.setInt(4, Integer.parseInt(datos[2]));
						pst.setDouble(5, Double.parseDouble(datos[3]));
						pst.setString(6, datos[4]);
						pst.setString(7, datos[5]);
						if (datos[6].equals("null")) {
							pst.setNull(8, java.sql.Types.VARCHAR);
						} else
							pst.setString(8, datos[6]);
						pst.setString(9, datos[7]);
						pst.setString(10, datos[8]);
						pst.setString(11, datos[9]);
						pst.setString(12, datos[10]);
						pst.setString(13, datos[11]);
						pst.setString(14, datos[12]);
						pst.setString(15, datos[13]);
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("added");
						} else {
							System.out.println("failed to add");
						}

					} else if (rowSet.getPcAccion().equals("UPDATE")) {

						System.out.println(" REALIZA EL UPDATE ");

						query = "UPDATE vnt_venta SET " + Utils.prepararDatosUpdate(rowSet.getPcTextoEdit())
						+ " WHERE vnt_venta.ID_VENTA = " + rowSet.getPnIdUsuario();

						PreparedStatement pst = conexion1.prepareStatement(query);
						String[] datos = rowSet.getPcTextoEdit().split("\\|");
						int count = 1;
						for (String data : datos) {
							String[] valores = data.split("\\::");
							String[] value = valores[1].split("\\&");
							if (value[0].equals("N")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.INTEGER);
								} else
									pst.setInt(count, Integer.parseInt(value[1]));
							} else if (value[0].equals("D")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.DOUBLE);
								} else
									pst.setDouble(count, Double.parseDouble(value[1]));
							} else if (value[0].equals("S")) {
								if (value[1].equals("null")) {
									pst.setNull(count, java.sql.Types.VARCHAR);
								} else
									pst.setString(count, value[1]);
							}
							count++;
						}
						System.out.println("query edit --> " + query);
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si modifico");
						} else {
							System.out.println("failed to modify");
						}

					} else {

						System.out.println(" REALIZA EL DELETE ");
						query = "DELETE FROM vnt_venta WHERE vnt_venta.ID_VENTA = ? ";
						PreparedStatement pst = conexion1.prepareStatement(query);
						pst.setInt(1, Integer.parseInt(rowSet.getPnIdUsuario()));
						
						int i = pst.executeUpdate();
						if (i != 0) {
							System.out.println("si delete");
						} else {
							System.out.println("failed to delete");
						}
						
					}

				} else {
					return "{\"status\":\"N\"}";
				}

			}
		} catch (SQLException e) {
			System.out.println( "Error en la conexión, verifique, su usuario y password o el nombre de la base a la que intenta conectarse");
			e.printStackTrace();
			throw new AccesoException(e.getMessage(), e);
		}
		return "{\"status\":\"S\"}";
	}

	
	@RequestMapping(value = ConstantesGeneral.URL_CRUD_DOCTOS, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA)
	@ResponseStatus(value = HttpStatus.OK)
	public void crudDoctoGeneral(@RequestParam("ruta") String ruta, @RequestParam("metodo") String metodo,
			@RequestParam(value = "file", required = false) MultipartFile archivo) {
		try {

			if (metodo.equals("delete")) {
				File archivoFisico = new File(System.getProperty("com.sun.aas.instanceRoot") + "/docroot/" + ruta);
				if (archivoFisico.exists()) {
					boolean a = archivoFisico.delete();
				}
			} else {
				File carpeta = new File( System.getProperty("com.sun.aas.instanceRoot") + "/docroot/" + ruta);
				if (!carpeta.exists()) {
					carpeta.mkdirs();
				}
				File documento = new File( System.getProperty("com.sun.aas.instanceRoot") + "/docroot/" + ruta + "/" + archivo.getOriginalFilename());
				archivo.transferTo(documento);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new AccesoException(e.getMessage(), e);
		}
	}

}
