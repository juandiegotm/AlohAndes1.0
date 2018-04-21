package rest;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import jdk.management.resource.ResourceContext;

public class AlohAndesService {

	//----------------------------------------------------------------------------------------------------------------------------------
			// ATRIBUTOS
			//----------------------------------------------------------------------------------------------------------------------------------
			
			/**
			 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
			 */
			@Context
			protected ServletContext context;
			

			//----------------------------------------------------------------------------------------------------------------------------------
			// METODOS DE INICIALIZACION
			//----------------------------------------------------------------------------------------------------------------------------------
			/**
			 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
			 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
			 */
			protected String getPath() {
				return context.getRealPath("WEB-INF/ConnectionData");
			}


			protected String doErrorMessage(Exception e){
				return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
			}
			
			protected void setContext(ServletContext context){
				this.context = context;
			}
	
}
