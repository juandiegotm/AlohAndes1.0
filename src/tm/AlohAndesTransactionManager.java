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

	public void crearReserva(Long idPersona, Long idOferta, Reserva reserva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();

		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);

			PersonaHabilitada personaBuscada = buscarPersonaHabilitadaPorId(idPersona);
			Oferta ofertaRequerida = buscarOfertaPorId(idOferta);
			if(personaBuscada == null) {
				throw new Exception("El usuario " + idPersona + " no existe.");
			}

			if(ofertaRequerida == null) {
				throw new Exception("La oferta con id: " + idOferta + " no existe");
			}

			if(ofertaRequerida.getCantidadDisponible() == 0) {
				throw new Exception("La oferta no está disponible");
			}

			//Se requeries disminuir en uno la oferta

			daoReserva.crearReserva(idPersona, idOferta, reserva);

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

	public ObjectNode crearReservaColectiva(ReservaColectiva reservaColectiva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();
		ObjectNode respuesta;

		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);

			respuesta = daoReserva.crearReservaColectiva(reservaColectiva);

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

	public void eliminarReserva(Long idPersona, Long idReserva) throws Exception {

		DAOReserva daoReserva = new DAOReserva();
		//Agregar busqueda de servicio
		try {
			this.conn = darConexion();
			daoReserva.setConn(conn);

			PersonaHabilitada personaBuscada = buscarPersonaHabilitadaPorId(idPersona);
			if(personaBuscada == null) {
				throw new Exception("El usuario " + idPersona + " no existe.");
			}

			daoReserva.deleteReserva(idReserva);

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


}
