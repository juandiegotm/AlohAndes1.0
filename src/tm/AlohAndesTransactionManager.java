package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import dao.DAOOferta;
import dao.DAOOperador;
import dao.DAOPersonaHabilitada;
import dao.DAOReserva;
import vos.Oferta;
import vos.PersonaHabilitada;
import vos.Reserva;
import vos.ReservaColectiva;

public class AlohAndesTransactionManager {

	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	public static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	public static String CONNECTION_DATA_PATH;



	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	protected String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	protected String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	protected String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	protected String driver;

	/**
	 * Atributo que representa la conexion a la base de datos
	 */
	protected Connection conn;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE CONEXION E INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * <b>Metodo Contructor de la Clase ParranderosTransactionManager</b> <br/>
	 * <b>Postcondicion: </b>	Se crea un objeto  ParranderosTransactionManager,
	 * 						 	Se inicializa el path absoluto del archivo de conexion,
	 * 							Se inicializna los atributos para la conexion con la Base de Datos
	 * @param contextPathP Path absoluto que se encuentra en el servidor del contexto del deploy actual
	 * @throws IOException Se genera una excepcion al tener dificultades con la inicializacion de la conexion<br/>
	 * @throws ClassNotFoundException 
	 */
	public AlohAndesTransactionManager(String contextPathP) {

		try {
			CONNECTION_DATA_PATH = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
			initializeConnectionData();
		} 
		catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo encargado de inicializar los atributos utilizados para la conexion con la Base de Datos.<br/>
	 * <b>post: </b> Se inicializan los atributos para la conexion<br/>
	 * @throws IOException Se genera una excepcion al no encontrar el archivo o al tener dificultades durante su lectura<br/>
	 * @throws ClassNotFoundException 
	 */
	public void initializeConnectionData() throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(new File(AlohAndesTransactionManager.CONNECTION_DATA_PATH));
		Properties properties = new Properties();

		properties.load(fileInputStream);
		fileInputStream.close();

		this.url = properties.getProperty("url");
		this.user = properties.getProperty("usuario");
		this.password = properties.getProperty("clave");
		this.driver = properties.getProperty("driver");

		Class.forName(driver);
	}

	/**
	 * Metodo encargado de generar una conexion con la Base de Datos.<br/>
	 * <b>Precondicion: </b>Los atributos para la conexion con la Base de Datos han sido inicializados<br/>
	 * @return Objeto Connection, el cual hace referencia a la conexion a la base de datos
	 * @throws SQLException Cualquier error que se pueda llegar a generar durante la conexion a la base de datos
	 */
	public Connection darConexion() throws SQLException {
		System.out.println("[AlohAndes APP] Attempting Connection to: " + url + " - By User: " + user);
		return DriverManager.getConnection(url, user, password);
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS TRANSACCIONALES DE OFERTA. 
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que modela la transaccion que retorna todos las personas habilitadas
	 * de la base de datos. <br/>
	 * @return List<Bebedor> - Lista de personas habilitadas que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Oferta> getAllOferta() throws Exception {
		DAOOferta dao = new DAOOferta();
		List<Oferta> ofertas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertas = dao.getOfertas();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}

	public ArrayNode getOfertasMasPopulares() throws Exception {
		DAOOferta dao = new DAOOferta();
		ArrayNode ofertas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertas = dao.getOfertasMasPopulares();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}

	public ArrayNode getIndiceOcupacionPorOferta() throws Exception {
		DAOOferta dao = new DAOOferta();
		ArrayNode ofertas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertas = dao.getIndiceOcupacionPorOferta();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}

	/**
	 * RFC4
	 */
	public ArrayNode getOfertasEnRangoDeFechasYCiertosServicios(Date fechaInicio, Date fechaFinal, List<String> servicios) throws Exception {
		DAOOferta dao = new DAOOferta();
		ArrayNode ofertas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertas = dao.getOfertasEnRangoDeFechasYCiertosServicios(fechaInicio, fechaFinal, servicios);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}

	public Oferta buscarOfertaPorId(Long id) throws Exception {
		DAOOferta dao = new DAOOferta();
		Oferta oferta = null;
		try {
			this.conn = darConexion();
			dao.setConn(conn);

			oferta = dao.buscarOfertaPorId(id);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return oferta;

	}

	public void retirarOferta(Long idOferta) throws Exception {
		DAOOferta dao = new DAOOferta();
		try {
			this.conn = darConexion();
			dao.setConn(conn);

			List<Reserva> reservasDeOferta = darReservasPorOferta(idOferta);

			if(!reservasDeOferta.isEmpty()) {
				throw new Exception("Aun existen reservas vinculadas a esta oferta");
			}

			dao.retirarOferta(idOferta);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	/**
	 * RFC9
	 */
	public List<Oferta> getOfertasConPocaDemanda() throws Exception {
		DAOOferta dao = new DAOOferta();
		List<Oferta> ofertasConPocaDemanda;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertasConPocaDemanda = dao.getOfertasConPocaDemanda();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertasConPocaDemanda;
	}


	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS TRANSACCIONALES DE OPERADOR. 
	//----------------------------------------------------------------------------------------------------------------------------------


	public ArrayNode getOperadorDinero() throws Exception {
		DAOOperador dao = new DAOOperador();
		ArrayNode operadores = null;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			operadores = dao.ingresosDeOperadores();

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return operadores;
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS TRANSACCIONALES DE RESERVA. 
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * RF4 - Crea una nueva reserva de la oferta pasada por parametro. 
	 * @param idPersona de la persona que realiza la petici�n. 
	 * @param idOferta de la oferta que se quiere reservar. 
	 * @param reserva con datos como la fecha de inicio, la fecha final,
	 * y la cantidad que se quiere reservar. 
	 * @throws Exception
	 */
	public void crearReserva(Long idPersona, Long idOferta, Reserva reserva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();
		DAOOferta daoOferta = new DAOOferta();
		DAOPersonaHabilitada daoPersonaHabilitada = new DAOPersonaHabilitada();

		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoOferta.setConn(conn);
			daoPersonaHabilitada.setConn(conn);

			this.conn.setAutoCommit(false);

			PersonaHabilitada personaBuscada = daoPersonaHabilitada.buscarPersonaHabilitadaPorId(idPersona);

			if(personaBuscada == null) {
				throw new Exception("El usuario " + idPersona + " no existe.");
			}

			Oferta ofertaRequerida = daoOferta.buscarOfertaPorId(idOferta);
			if(ofertaRequerida == null) {
				throw new Exception("La oferta con id: " + idOferta + " no existe");
			}

			if(ofertaRequerida.getCantidadDisponible() == 0) {
				throw new Exception("La oferta no est� disponible");
			}

			if(ofertaRequerida.getCantidadDisponible() < reserva.getCantidad()) {
				throw new Exception("No hay una cantidad suficiente para reservar");
			}

			daoReserva.crearReserva(idPersona, idOferta, reserva, null);
			daoOferta.actualizarCantidadReserva(idOferta, ofertaRequerida.getCantidadDisponible()-reserva.getCantidad());

			this.conn.commit();

		}

		catch (SQLException sqlException) {
			System.out.println("Rollback");
			this.conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();


			throw sqlException;
		} 
		catch (Exception exception) {
			System.out.println("Rollback");
			this.conn.rollback();
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {

				daoReserva.cerrarRecursos();
				if(this.conn!=null){

					//deshace todos los cambios realizados en los datos
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	
	/**
	 * RF5 - Elimina una reserva asociada la usuario y con el id pasados por parametros.
	 * @param idPersona de la persona asociada con la reserva.
	 * @param idReserva de la reserva que se quiere eliminar. 
	 * @throws Exception
	 */
	public void eliminarReserva(Long idPersona, Long idReserva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();
		DAOPersonaHabilitada daoPersonaHabilitada = new DAOPersonaHabilitada();
		DAOOferta daoOferta = new DAOOferta();
		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoPersonaHabilitada.setConn(conn);
			daoOferta.setConn(conn);


			conn.setAutoCommit(false);

			PersonaHabilitada personaBuscada = daoPersonaHabilitada.buscarPersonaHabilitadaPorId(idPersona); ;
			if(personaBuscada == null) 
				throw new Exception("El usuario " + idPersona + " no existe.");

			Reserva reservaBuscada = daoReserva.darReserva(idReserva);

			if(reservaBuscada == null) 
				throw new Exception("El usuario no tiene ninguna reserva asociada con el id "+ idReserva);

			Oferta ofertaBuscada = daoOferta.ofertaDeReserva(idReserva); //Esto se hace primero, porque luego de eliminar la reserva, no se puede obtener esta informaci�n. 
			daoReserva.deleteReserva(idReserva);
			daoOferta.actualizarCantidadReserva(ofertaBuscada.getIdOferta(), reservaBuscada.getCantidad()+ofertaBuscada.getCantidadDisponible());

			conn.commit();

		}

		catch (SQLException sqlException) {
			System.out.println("ROLLBACK");
			conn.rollback();

			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.out.println("ROLLBACK");
			conn.rollback();

			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	public ObjectNode crearReservaColectiva(ReservaColectiva reservaColectiva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();
		ObjectNode respuesta;

		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);

			//Inicio de la transacci�n
			conn.setAutoCommit(false);
			respuesta = daoReserva.crearReservaColectiva(reservaColectiva);
			System.out.println("PASO FINAL: HACIENDO COMMIT A LA BASE DE DATOS");
			conn.commit();

		}

		catch (SQLException sqlException) {
			System.out.println("Rollback");
			//deshace todos los cambios realizados en los datos
			conn.rollback();
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.out.println("Rollback");
			//deshace todos los cambios realizados en los datos
			conn.rollback();
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return respuesta;
	}

	public void eliminarReservaColectiva(Long IdReservaColectiva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();

		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.cancelarReservaColectiva(IdReservaColectiva);

		}

		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

	}

	public List<Reserva> darReservasPorOferta(Long idOferta) throws Exception {

		List<Reserva> reservas = null;
		DAOReserva daoReserva = new DAOReserva();

		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);

			reservas = daoReserva.reservasDeOferta(idOferta);
		}

		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return reservas;
	}



	/**
	 * RFC7
	 */
	public ArrayNode getOperacionDeAlohAndes(String periodoParam, String tipoOferta) throws Exception {
		DAOReserva dao = new DAOReserva();

		ArrayNode operacionDeAlohAndes;
		try {
			this.conn = darConexion();
			dao.setConn(conn);

			operacionDeAlohAndes = dao.getOperacionDeAlohAndes(periodoParam, tipoOferta);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return operacionDeAlohAndes;

	}


	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS TRANSACCIONALES DE PERSONAHABILITADA. 
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que modela la transaccion que retorna todos las personas habilitadas
	 * de la base de datos. <br/>
	 * @return List<Bebedor> - Lista de personas habilitadas que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<PersonaHabilitada> getAllBPersonasHabilitadas() throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		List<PersonaHabilitada> personas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			personas = dao.getPersonasHabiltadas();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return personas;
	}

	public PersonaHabilitada buscarPersonaHabilitadaPorId(Long id) throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		PersonaHabilitada persona = null;
		try {
			this.conn = darConexion();
			dao.setConn(conn);

			persona = dao.buscarPersonaHabilitadaPorId(id);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}

		return persona;

	}

	public void agregarPersonaHabilitada(PersonaHabilitada persona) throws Exception{
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		try {
			this.conn = darConexion();
			dao.setConn(conn);

			PersonaHabilitada buscado = dao.buscarPersonaHabilitadaPorId(persona.getIdPersonaHabilitada());

			if(buscado != null) {
				throw new Exception("La persona con el ID: " + persona.getIdPersonaHabilitada() + " ya existe.");
			}

			dao.agregarPersonaHabilitada(persona);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * RFC8
	 */
	public List<PersonaHabilitada> getClientesFrecuentes(Long idOferta) throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		List<PersonaHabilitada> clientesFrecuentes;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			clientesFrecuentes = dao.getClientesFrecuentes(idOferta);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientesFrecuentes;
	}


	/**
	 * RFC5 
	 * @return
	 * @throws Exception
	 */
	public ArrayNode getUsoDeAlohAndesPorCadaUsuario() throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		ArrayNode personas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			personas = dao.getUsoDeAlohAndesPorCadaUsuario();


		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return personas;
	}

	public ArrayNode getUsoDeAlohAndesPorUsuarioEspecifico(Long id) throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		ArrayNode personas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			personas = dao.getUsoDeAlohAndesPorUsuarioEspecifico(id);


		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return personas;
	}

	/**
	 * RFC10_PARTE1 - Mostrar los usuarios (persona habilitada) que han hecho reservas en el rango de fechas indicado
 	*@throws Exception
	 */
	public ArrayNode  getConsumoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(String fechaInicio, String fechaFinal, String tipoDeOferta, String datoOrdenar)  throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		ArrayNode personashabilitadas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);
		
			personashabilitadas = dao.getConsumoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(fechaInicio, fechaFinal, tipoDeOferta, datoOrdenar);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return personashabilitadas;
	}

	/**
	 * RFC10_PARTE2 - Mostrar los usuarios (persona habilitada) que han hecho reservas en el rango de fechas indicado
 	*@throws Exception
	 */
	public ArrayNode  getConsumoDeAlohAndesDadoRangoFechasAgrupadoTipoOferta(String fechaInicio, String fechaFinal)  throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		ArrayNode personashabilitadas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			personashabilitadas = dao.getConsumoDeAlohAndesDadoRangoFechasAgrupadoTipoOferta(fechaInicio, fechaFinal);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return personashabilitadas;
	}

	
	/**
	 * RFC11 - Mostrar los usuarios (persona habilitada) que NO han hecho reservas en el rango de fechas indicado y del tipo de oferta 
 	 * @throws Exception
	 */
	public ArrayNode  getConsumoNegativoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(String fechaInicio, String fechaFinal, String tipoDeOferta, String datoOrdenar)  throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		ArrayNode personashabilitadas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			personashabilitadas = dao.getConsumoNegativoDeAlohAndesDadoRangoFechasTipoOfertaYdatoOrden(fechaInicio, fechaFinal, tipoDeOferta, datoOrdenar);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return personashabilitadas;
	}
	
	
	
	/**
	 * RFC12_PARTE1 - Mostrar para cada semana del a�o las ofertas con MAYOR ocupacion
 	 * @throws Exception
	 */
	public ArrayNode  getSemanasConOfertasDeMayorOcupacion()  throws Exception {
		DAOOferta dao = new DAOOferta();
		ArrayNode ofertas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertas = dao.getSemanasConOfertasDeMayorOcupacion();

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}
	
	
	/**
	 * RFC12_PARTE2 - Mostrar para cada semana del a�o las ofertas con MENOR ocupacion
 	 * @throws Exception
	 */
	public ArrayNode  getSemanasConOfertasDeMenorOcupacion()  throws Exception {
		DAOOferta dao = new DAOOferta();
		ArrayNode ofertas;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			ofertas = dao.getSemanasConOfertasDeMenorOcupacion();

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}
	
	/**
	 * RFC12_PARTE3 - Mostrar para cada semana del a�o los operadores MAS solicitados
 	 * @throws Exception
	 */
	public ArrayNode  getSemanasConOperadoresMasSolicitados()  throws Exception {
		DAOOperador dao = new DAOOperador();
		ArrayNode operadores;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			operadores = dao.getSemanasConOperadoresMasSolicitados();

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return operadores;
	}
	
	
	/**
	 * RFC12_PARTE4 - Mostrar para cada semana del a�o los operadores MENOS solicitados
 	 * @throws Exception
	 */
	public ArrayNode  getSemanasConOperadoresMenosSolicitados()  throws Exception {
		DAOOperador dao = new DAOOperador();
		ArrayNode operadores;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			operadores = dao.getSemanasConOperadoresMenosSolicitados();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return operadores;
	}
	
	/**
	 * RFC13 - Mostrar los buenos clientes con sus categorias
 	 * @throws Exception
	 */
	public ArrayNode  getBuenosClientes()  throws Exception {
		DAOPersonaHabilitada dao = new DAOPersonaHabilitada();
		ArrayNode operadores;
		try 
		{
			this.conn = darConexion();
			dao.setConn(conn);

			operadores = dao.getBuenosClientes();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				dao.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return operadores;
	}
}
