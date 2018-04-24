package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DAOAlohAndes {

	//----------------------------------------------------------------------------------------------------------------------------------
		// CONSTANTES
		//----------------------------------------------------------------------------------------------------------------------------------

		/**
		 * Constante para indicar el usuario Oracle del estudiante
		 */

		public final static String CARLOS_MARIO_USER =  "ISIS2304A721810";
		public final static String JUAN_DIEGO_USER = "ISIS2304A981810";


		public final static String USUARIO = CARLOS_MARIO_USER;


		//----------------------------------------------------------------------------------------------------------------------------------
		// ATRIBUTOS
		//----------------------------------------------------------------------------------------------------------------------------------

		/**
		 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
		 */
		protected ArrayList<Object> recursos;

		/**
		 * Atributo que genera la conexion a la base de datos
		 */
		protected Connection conn;

		//----------------------------------------------------------------------------------------------------------------------------------
		// METODOS DE INICIALIZACION
		//----------------------------------------------------------------------------------------------------------------------------------

		/**
		 * Metodo constructor de la clase DAOBebedor <br/>
		 */
		public DAOAlohAndes() {
			recursos = new ArrayList<Object>();
		}	
		
		public void setConn(Connection connection){
			this.conn = connection;
		}

		public void cerrarRecursos() {
			for(Object ob : recursos){
				if(ob instanceof PreparedStatement)
					try {
						((PreparedStatement) ob).close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
		}

	
}
