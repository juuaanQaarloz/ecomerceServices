package com.mx.xpertys.utils;

public class Utils {

	public static String crearUrlDataBase() {
		String strColum = "jdbc:mysql://localhost:3306/ecommers?user=" + ConstantesGeneral.USER_DATABASE + "&pasword=" + ConstantesGeneral.PASSWORD_DATABASE;
		return strColum;
	}
	
	public static String prepararDatosUpdate(String datos){
		String [] list = datos.split("\\|");
		String cadena = "";
		int count = 1;
		for (String data : list ) {
			String [] valores = data.split("\\::");
			if(count == list.length) {
				cadena += valores[0] + " = ? ";
			} else cadena += valores[0] + " = ?, ";
			count++;
		}
		
		return cadena;
	}
}
