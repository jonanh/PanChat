package panchat;

import panchat.connector.SocketConnector;
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.ChatList;
import panchat.data.UserList;
import panchat.data.User;
import panchat.messages.MessageChat;
import panchat.order.CausalMatrixLayer;
import panchat.order.OrderLayer;
import panchat.protocol.JoinChannel;
import panchat.ui.main.PanchatUI;

public class Panchat {

	// Connector
	private SocketConnector connector;

	// Datos
	private User usuario;
	private UserList listaUsuarios;
	private ChatRoomList listaCanales;
	private ChatList listaConversaciones;

	// Linkers
	private OrderLayer linker;
	private CausalMatrixLayer causalLinker;

	/**
	 * Crea nueva instancia de panchat
	 * 
	 * @param nombreUsuario
	 */
	public Panchat(String nombreUsuario) {
		// Apartir de un String obtiene un usuario valido, buscano la IP del
		// ordenador actual, y buscando un puerto disponible a partir del 5000
		this(new User(nombreUsuario));
	}

	/**
	 * Crea nueva instancia de panchat
	 * 
	 * @param pUsuario
	 */
	public Panchat(User pUsuario) {
		this.usuario = pUsuario;

		this.listaCanales = new ChatRoomList();
		this.listaUsuarios = new UserList(listaCanales);
		this.listaConversaciones = new ChatList(this);

		// Nos añadimos a nuestra propia lista de usuarios
		this.listaUsuarios.add(usuario);

		this.connector = new SocketConnector(this);
		this.causalLinker = new CausalMatrixLayer(this);
		this.linker = new OrderLayer(this);

		// Como el hilo MulticastListenerThread depende de causalLinker, lo
		// arrancamos después para evitar una condicción de carrera al
		// instanciar las clases.
		this.connector.arrancarThreads();
	}

	/*
	 * Getters
	 */

	/**
	 * Devuelve la lista de usuarios del chat
	 * 
	 * @return
	 */
	public UserList getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * Devuelve la lista de canales del chat
	 * 
	 * @return
	 */
	public ChatRoomList getChannelList() {
		return listaCanales;
	}

	/**
	 * Devuelve la lista de conversaciones
	 * 
	 * @return
	 */
	public ChatList getListaConversaciones() {
		return listaConversaciones;
	}

	/**
	 * Devuelve la lista de canales del chat
	 * 
	 * @return
	 */
	public User getUsuario() {
		return usuario;
	}

	/**
	 * Devuelve el causal linker asociado al chat
	 * 
	 * @return
	 */
	public CausalMatrixLayer getCausalLinker() {
		return causalLinker;
	}

	/**
	 * Devuelve el linker asociado al chat
	 * 
	 * @return
	 */
	public OrderLayer getLinker() {
		return linker;
	}

	/**
	 * Devuelve la clase connector
	 * 
	 * @return
	 */
	public SocketConnector getConnector() {
		return connector;
	}

	/*
	 * Acciones para el GUI
	 */

	/**
	 * Desregistra el cliente e inicia la terminación de la aplicación
	 */
	public void accionDesegistrarCliente() {
		connector.enviarSaludo(false);

		connector.closeSockets();
	}

	/**
	 * Inicia una conversación con el usuario
	 * 
	 * @param user
	 */
	public void accionInscribirCanal(String nombre) {
		ChatRoom canal = new ChatRoom(nombre, listaUsuarios);
		listaCanales.addChannel(canal);

		accionIniciarConversacionCanal(canal);
	}

	/**
	 * Inicia una conversación con el usuario
	 * 
	 * @param usuario
	 */
	public void accionIniciarConversacion(User usuario) {
		listaConversaciones.getChatWindow(usuario);
	}

	/**
	 * Cierra una conversación con el usuario
	 * 
	 * @param usuario
	 */
	public void accionCerrarConversacion(User usuario) {
		this.listaConversaciones.delete(usuario);
	}

	/**
	 * Inicia la conversación de un canal
	 * 
	 * @param user
	 */
	public void accionIniciarConversacionCanal(ChatRoom canal) {
		if (!canal.contains(usuario)) {
			canal.joinUser(usuario);

			listaCanales.setModified();

			listaConversaciones.getChatRoomWindow(canal);

			// Notificamos a todo el mundo sobre el nuevo canal
			JoinChannel inscripcion = new JoinChannel(canal, usuario,
					true);

			causalLinker.sendMsg(this.listaUsuarios.getUserList(),
					inscripcion);
		}

	}

	/**
	 * Cierra la conversación de un canal
	 * 
	 * @param user
	 */
	public void accionCerrarConversacionCanal(ChatRoom canal) {
		// Lo añadimos a la lista de conversaciones
		listaConversaciones.delete(canal);

		// Notificamos a todo el mundo sobre el nuevo canal
		JoinChannel inscripcion = new JoinChannel(canal, usuario,
				false);

		causalLinker.sendMsg(listaUsuarios.getUserList(), inscripcion);

		// Nos borramos del listado de usuarios conectados del canal
		canal.leaveUser(usuario);

		if (canal.getNumUsuariosConectados() == 0) {

			listaCanales.deleteChannel(canal);
		}

		listaCanales.setModified();
	}

	/**
	 * Envia un comentario a un usuario
	 * 
	 * @param usuario
	 * @param pComentario
	 */
	public void escribirComentario(User usuario, String pComentario) {
		causalLinker.sendMsg(usuario, pComentario);
	}

	/**
	 * Envia un comentario a un canal
	 * 
	 * @param user
	 * @param pComentario
	 */
	public void escribirComentarioCanal(ChatRoom pCanal, String pComentario) {
		MessageChat mensaje = new MessageChat(pCanal, pComentario);
		causalLinker.sendMsg(pCanal.getUserList(), mensaje);
	}

	/**
	 * Invitar un usuario un canal
	 * 
	 * @param channel
	 * @param user
	 */
	public void invitarUsuario(ChatRoom pCanal, User pUsuario) {
		// Lo añadimos a la lista de conversaciones
		listaCanales.getChannel(pCanal.getName()).joinUser(
				pUsuario);

		listaCanales.setModified();

		// Notificamos a todo el mundo sobre el nuevo canal
		JoinChannel inscripcion = new JoinChannel(pCanal, pUsuario,
				true);

		causalLinker
				.sendMsg(this.listaUsuarios.getUserList(), inscripcion);
	}

	public static void main(String[] args) {
		new PanchatUI();
	}

}
