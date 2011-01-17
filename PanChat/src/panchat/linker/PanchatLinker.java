package panchat.linker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;

import panchat.Panchat;
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.User;
import panchat.messages.ChatMessage;
import panchat.messages.JoinRoomMessage;
import panchat.messages.Message;
import panchat.messages.register.RoomListMsg;
import panchat.messages.register.RegisterUserMsg;
import panchat.order.OrderLayer;

public class PanchatLinker extends OrderLayer {

	public final static boolean DEBUG = false;
	
	Panchat panchat;
	
	public PanchatLinker(Panchat panchat, User myId, OrderLayer[] pLinker) {
		super(myId, pLinker);
		
		this.panchat = panchat;
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public Message handleMsg() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receive(Message message) {
		
		Object objeto = message.getContent();

		printDebug("objeto leído");

		if (objeto instanceof RoomListMsg) {

			printDebug("SaludoListaCanales");

			registrarSaludoCanales((RoomListMsg) objeto, cm
					.getUsuario());

		} else if (objeto instanceof JoinRoomMessage) {

			printDebug("InscripcionCanal");

			inscribirCanal((JoinRoomMessage) objeto, cm.getUsuario());

		} else if (objeto instanceof ChatMessage) {

			printDebug("MessageChat");

			escribirMensajeCanal((ChatMessage) objeto);

		} else if (objeto instanceof String) {

			printDebug("Message");

			escribirMensaje((String) objeto, cm.getUsuario());
		} else if (objeto instanceof RegisterUserMsg) {
			registrarCliente((RegisterUserMsg) objeto);
		}

	}

	@Override
	public void removeUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMsg(User destId, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendMsg(LinkedList<User> destIds, Object msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

	
	private void inscribirCanal(JoinRoomMessage inscripcion, User usuario) {

		ChatRoomList listaCanales = panchat.getChannelList();

		User usuarioInscripcionObtenido = panchat.getListaUsuarios()
				.getUser(inscripcion.user.uuid);
		User usuarioEnvioObtenido = panchat.getListaUsuarios().getUser(
				usuario.uuid);

		ChatRoom canalObtenido = listaCanales.getChannel(inscripcion.room
				.getName());

		printDebug("usuarioInscripcionObtenido : " + usuarioInscripcionObtenido);
		printDebug("usuarioEnvioObtenido : " + usuarioEnvioObtenido);
		printDebug("canalObtenido : " + canalObtenido);

		// El canal no está registrado
		if (canalObtenido == null) {

			if (inscripcion.joing) {

				printDebug("El canal no existía, creamos el canal y lo añadimos");

				// Creamos el canal
				ChatRoom canal = new ChatRoom(inscripcion.room.getName(),
						panchat.getListaUsuarios());

				// Añadimos al usuario inscrito al canal
				canal.joinUser(usuarioInscripcionObtenido);

				// Añadimos el canal a la lista de canales
				panchat.getChannelList().addChannel(canal);

				// Si he sido invitado a la conversacion
				if (usuarioInscripcionObtenido.equals(panchat.getUsuario())) {

					printDebug("he sido invitado");

					String bienvenido = panchat.getUsuario().nickName
							+ " he sido invitado a la conversacion por "
							+ usuario.nickName;

					// Crear la ventana
					panchat.getListaConversaciones().getChatRoomWindow(
							canalObtenido).escribirComentario(bienvenido);
				}

				if (DEBUG) {
					printDebug("Resultado");
					printDebug("Usuarios conectados :");
					for (int i = 0; i < canal.getNumUsuariosConectados(); i++)
						printDebug(canal.getUsuarioConectado(i).toString());

					printDebug("Usuarios desconectados :");
					for (int i = 0; i < canal.getNumUsuariosDesconectados(); i++)
						printDebug(canal.getUsuarioDesconectado(i).toString());
				}
			}
		}

		// Si el canal ya exístia y no ha sido una acción provocada por mi,
		// entonces :
		else if (!usuarioEnvioObtenido.equals(panchat.getUsuario())) {

			printDebug("El canal existía");

			if (inscripcion.joing) {

				if (!canalObtenido.contains(usuarioInscripcionObtenido)) {

					printDebug("El canal no contenia al usuario inscrito, agregandolo a la lista");
					canalObtenido
							.joinUser(usuarioInscripcionObtenido);
				}

				// Si he sido invitado a la conversacion
				if (usuarioInscripcionObtenido.equals(panchat.getUsuario())) {

					printDebug("he sido invitado");

					String bienvenido = "he sido invitado a la conversacion por "
							+ usuario.nickName;

					panchat.getListaConversaciones().getChatRoomWindow(
							canalObtenido).escribirComentario(bienvenido);
				}

				// Si estando yo conectado alguien más se ha agregado a la
				// conversacion mostramos saludo
				else if (canalObtenido.contains(panchat.getUsuario())) {

					printDebug("alguien ha entrado en una conversacion en la que ya estaba");

					String bienvenido = panchat.getUsuario().nickName
							+ " ha entrado en la conversacion";

					panchat.getListaConversaciones().getChatRoomWindow(
							canalObtenido).escribirComentario(bienvenido);
				}

				if (DEBUG) {
					printDebug("Resultado");
					printDebug("Usuarios conectados :");
					for (int i = 0; i < canalObtenido
							.getNumUsuariosConectados(); i++)
						printDebug(canalObtenido.getUsuarioConectado(i)
								.toString());

					printDebug("Usuarios desconectados :");
					for (int i = 0; i < canalObtenido
							.getNumUsuariosDesconectados(); i++)
						printDebug(canalObtenido.getUsuarioDesconectado(i)
								.toString());
				}

			} else {

				if (canalObtenido.contains(usuarioInscripcionObtenido)) {

					printDebug("El canal contenía al usuario inscrito, desregistrando al usuario del canal");

					canalObtenido
							.leaveUser(usuarioInscripcionObtenido);

					if (DEBUG) {
						printDebug("Resultado");
						printDebug("Usuarios conectados :");
						for (int i = 0; i < canalObtenido
								.getNumUsuariosConectados(); i++)
							printDebug(canalObtenido.getUsuarioConectado(i)
									.toString());

						printDebug("Usuarios desconectados :");
						for (int i = 0; i < canalObtenido
								.getNumUsuariosDesconectados(); i++)
							printDebug(canalObtenido.getUsuarioDesconectado(i)
									.toString());
					}

					if (canalObtenido.getNumUsuariosConectados() == 0) {

						printDebug("El canal ya no posee más usuarios, desregistrando");

						listaCanales.deleteChannel(canalObtenido);
					}
				}
			}
		}

		// Actualizamos la vista de canales
		panchat.getChannelList().setModified();
	}

	private void escribirMensajeCanal(ChatMessage objeto) {

		String nombreCanal = objeto.chatroom.getName();

		ChatRoomList listaCanales = panchat.getChannelList();

		ChatRoom canalObtenido = listaCanales.getChannel(nombreCanal);

		printDebug("canalObtenido : " + canalObtenido);

		if (canalObtenido != null) {

			if (canalObtenido.contains(panchat.getUsuario()))

				panchat.getListaConversaciones().getChatRoomWindow(
						canalObtenido).escribirComentario(objeto.mensaje);

		}
	}

	private void escribirMensaje(String comentario, User usuario) {

		panchat.getListaConversaciones().getChatWindow(usuario)
				.escribirComentario(comentario);

	}
	
	private void registrarSaludoCanales(RoomListMsg saludo,
			User usuario) {

		printDebug("Saludo canal recibido");

		ChatRoomList listaCanales = panchat.getChannelList();

		for (ChatRoom canal : saludo.roomList) {

			User usuarioObtenido = panchat.getListaUsuarios().getUser(
					usuario.uuid);

			ChatRoom canalObtenido = listaCanales.getChannel(canal.getName());

			if (canalObtenido == null) {

				canalObtenido = new ChatRoom(canal.getName(), panchat
						.getListaUsuarios());

				listaCanales.addChannel(canalObtenido);
			}

			canalObtenido.joinUser(usuarioObtenido);

			printDebug("añadiendo usuario " + usuarioObtenido + " al canal : "
					+ canalObtenido.toString());

		}

		listaCanales.setModified();
	}
	
	private void registrarCliente(RegisterUserMsg saludoUsuario) {

		User usuario = saludoUsuario.user;

		if (DEBUG) {
			System.out.println("MulticastListenerThread.java:"
					+ "Petición de cliente recibida");
			System.out.println("\tRecibido por " + panchat.getUsuario());
			System.out.println("\tRecibido Desde " + usuario);
		}

		// Si no contenemos a este usuario lo añadimos
		if (!panchat.getListaUsuarios().contains(usuario)) {

			/*
			 * Si es una acción de registrar lo registramos
			 */
			if (saludoUsuario.register) {

				/*
				 * Respondemos el saludo al usuario como buenos ciudadanos 0:-)
				 */
				printDebug("Repondemos el saludo al usuario");

				connector.enviarSaludo(true);

				/*
				 * Crear el socket
				 */
				printDebug("Creamos el socket");

				// Unos aceptan desde el ServerSocket y otros crean
				// sockets.

				if (usuario.uuid.compareTo(panchat.getUsuario().uuid) < 0) {

					connector.connect(usuario);

					accionesRegistro(usuario);

				} else {

					User usuarioLeido = connector.acceptConnect();

					accionesRegistro(usuarioLeido);
				}

				/*
				 * Añadir a la ListaUsuarios el usuario
				 */
				printDebug("Añadimos el usuario a ListaUsuarios");

				panchat.getListaUsuarios().addUsuario(usuario);
			}
		}
	}

	/**
	 * Realizamos la accionRegistro teniendo en cuenta que el usuario aceptado
	 * no tiene porque darse en orden, y por tanto puede ocasionarse una
	 * potencial condición de carrera que puede ser evitada tratando el elemento
	 * registrado o aceptado
	 * 
	 * @param pUsuario
	 */
	private void accionesRegistro(User pUsuario) {
		/*
		 * Añadir elementos en el CausalLinker y el Linker
		 */
		printDebug("Añadimos elementos a los Linkers");

		panchat.getCausalLinker().addUser(pUsuario);

		/*
		 * Enviamos al usuario la información que tenemos sobre los canales.
		 */
		printDebug("Enviamos información sobre canales");

		RoomListMsg saludo = new RoomListMsg(panchat
				.getListaConversaciones().getListaConversacionesCanal());

		panchat.getCausalLinker().sendMsg(pUsuario, saludo);

	}

	
	/**
	 * 
	 * Enviamos un saludo Multicast
	 * 
	 * @param Registrar
	 */
	public void enviarSaludo(boolean Registrar) {
		RegisterUserMsg msgRegistrar = new RegisterUserMsg(usuario, Registrar);

		if (Registrar)
			printDebug("Dandonos a conocer al mundo 0:-)");
		else {
			printDebug("Despidiendonos del mundo 0:-)");
			printDebug("num procesos activos : "
					+ String.valueOf(threadPool.size()));
		}
		this.escribirMultiCastSocket(msgRegistrar);
	}
	
	
	private void printDebug(String string) {
		String msgClase = "CausalLinkerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
