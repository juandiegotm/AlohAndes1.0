package generadorDatos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.Buffer;
import java.util.Random;

public class Generador {

	public final static String MIEMBRO_ARRENDADOR_APARTAMENTO = "MiembroArrendadorApartamento";
	public final static String ARRENDADOR_HABITACION = "ArrendadorHabitacion";
	public final static String HOTEL = "Hotel";
	public final static String HOSTAL = "Hostal";
	public final static String EMPRESA_VIVIENDA_UNIVERSITARIA = "EmpresaViviendaUniversitaria";
	public final static String ARRENDADOR_ESPORADICO = "ArrendadorEsporadico";
	
	private static String[] tiposOferta = {"OFERTAAPARTAMENTO","OFERTAHABITACION", "OFERTAHOSTAL", "OFERTAHOTEL", "OFERTAVIVIENDAESPORADICA",
			"OFERTAVIVIENDAUNIVERSITARIA"};

	public static void main(String[] args) {
		//cambiarConstante("./data/data.csv", "./data/data1.csv", "change1"); //darConstanteOferta()
		//cambiarConstante("./data/data1.csv", "./data/data2.csv", "change2"); //(r.nextInt(99807)+1)+"");
		cambiarConstante("./data/data2.csv", "./data/data3.csv", "change3"); darConstanteHabilitado();
	}

	public static void cambiarConstante(String archivoEntrada, String archivoSalida, String nombreConstante) {
		try {

			FileReader reader = new FileReader(new File(archivoEntrada));
			BufferedReader bf= new BufferedReader(reader);

			FileWriter writter = new FileWriter(new File(archivoSalida));
			BufferedWriter bw = new BufferedWriter(writter);

			String linea = bf.readLine();
			bw.write(linea+"\n");
			Random r = new Random();
			
			while(linea != null) {
				String nuevaLinea = linea.replace(nombreConstante, darConstanteHabilitado());
				bw.write(nuevaLinea+"\n");

				linea = bf.readLine();
			}
		}

		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String darConstanteHabilitado() {
		String constante = "";
		Random r = new Random();
		Integer n = r.nextInt(2);
		
		return n==0? "habilitado": "deshabilitado";
	}

	public static String darConstanteOperador() {
		String constante = "";
		Random r = new Random();	
		Integer n = r.nextInt(6);
		switch(n) {
		case 0: constante=MIEMBRO_ARRENDADOR_APARTAMENTO; break;
		case 1: constante=ARRENDADOR_ESPORADICO; break;
		case 2: constante=ARRENDADOR_HABITACION; break;
		case 3: constante=HOSTAL;break;
		case 4: constante=HOTEL;break;
		case 5: constante=EMPRESA_VIVIENDA_UNIVERSITARIA; break;
		default: break;
		}
		return constante;
	}
	
	public static String darConstanteOferta() {
		Random r = new Random();	
		Integer n = r.nextInt(6);
		return tiposOferta[n];
	}
	

}
