package panchat;

import panchat.channels.ListaCanales;
import panchat.connector.Connector;
import panchat.linker.CausalLinker;
import panchat.linker.Linker;
import panchat.users.ListaUsuarios;
import panchat.users.Usuario;

public class Panchat {
	private ListaUsuarios listaUsuarios;
	private ListaCanales listaCanales;
	private Usuario usuario;
	private CausalLinker causalLinker;
	private Linker linker;
	private Connector connector;

	/**
	 * Crea nueva instancia de panchat
	 * 
	 * @param nombreUsuario
	 */
	public Panchat(String nombreUsuario) {
		// Apartir de un String obtiene un usuario valido, buscano la IP del
		// ordenador actual, y buscando un puerto disponible a partir del 5000
		this(new Usuario(nombreUsuario));
	}

	/**
	 * Crea nueva instancia de panchat
	 * 
	 * @param pUsuario
	 */
	public Panchat(Usuario pUsuario) {
		this.usuario = pUsuario;
		this.listaCanales = new ListaCanales();
		this.listaUsuarios = new ListaUsuarios(listaCanales);
		// Nos añadimos a nuestra propia lista de usuarios
		this.listaUsuarios.añadirUsuario(usuario);

		this.connector = new Connector(this);
		this.causalLinker = new CausalLinker(this);
		this.linker = new Linker(this);

		// Como el hilo MulticastListenerThread depende de causalLinker, lo
		// arrancamos después para evitar una condicción de carrera al
		// instanciar las clases.
		this.connector.arrancarMulticastListenerThread();
	}

	/**
	 * Devuelve la lista de usuarios del chat
	 * 
	 * @return
	 */
	public ListaUsuarios getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * Devuelve la lista de canales del chat
	 * 
	 * @return
	 */
	public ListaCanales getListaCanales() {
		return listaCanales;
	}

	/**
	 * Devuelve la lista de canales del chat
	 * 
	 * @return
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Devuelve el causal linker asociado al chat
	 * 
	 * @return
	 */
	public CausalLinker getCausalLinker() {
		return causalLinker;
	}

	/**
	 * Devuelve el linker asociado al chat
	 * 
	 * @return
	 */
	public Linker getLinker() {
		return linker;
	}

	/**
	 * Devuelve la clase connector
	 * 
	 * @return
	 */
	public Connector getConnector() {
		return connector;
	}

	public void desegistrarCliente() {
		connector.enviarSaludo(false);

		connector.closeSockets();
	}

}
