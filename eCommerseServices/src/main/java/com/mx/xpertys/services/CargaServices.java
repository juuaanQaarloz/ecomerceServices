package com.mx.xpertys.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mx.xpertys.dto.AlumnoAsistencia;
import com.mx.xpertys.exception.AccesoException;

@Service("CargaServices")
public class CargaServices {

	public List<AlumnoAsistencia> leerArchivoExcel( 
			MultipartFile archivo, String indConfirma, String materia, 
			String fecha, String idProfesor) throws IOException {
		List<AlumnoAsistencia> resultado = new ArrayList<AlumnoAsistencia>();
		if (archivo != null) {
			Workbook wb = null;
			try {
				InputStream targetStream = new BufferedInputStream(archivo.getInputStream());
				wb = WorkbookFactory.create(targetStream);
				Sheet mySheet = wb.getSheetAt(0);
				Iterator<Row> rowIter = mySheet.rowIterator();
				Row row = null;
				Integer cont = 0;
			
				while (rowIter.hasNext()) {
					row = rowIter.next();
					if (cont >= 5 && row != null && row.getCell(0) != null) {
						row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
						if (!row.getCell(0).getStringCellValue().trim().isEmpty()) {
							AlumnoAsistencia alumno = new AlumnoAsistencia();
							if(row.getCell(0) != null) {
								row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
								alumno.setMatricula(row.getCell(0) + "");
							}
							if(row.getCell(1) != null) {
								row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
								alumno.setNombreAlumno(row.getCell(1) + "");
							}
							if(row.getCell(2) != null) {
								row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
								alumno.setIndAsistencia(row.getCell(2) + "");
							} else {
								alumno.setIndAsistencia("N");
							}
							if(indConfirma != null && indConfirma.equals("S")){
								alumno.setFechaAsistencia(fecha);
								alumno.setIdProfesor(idProfesor);
								alumno.setMateria(materia);
								alumno.setIdAsistencia(new Date().getTime() + "");
							}
							resultado.add(alumno);
						}
					}
					
					row = null;
					cont++;
				}
				targetStream.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new AccesoException(e.getMessage(), e);
			}
		} 
		return resultado;

	}
}
