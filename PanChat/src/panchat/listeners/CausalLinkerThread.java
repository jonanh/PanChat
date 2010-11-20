package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.data.ChatRoom;
import panchat.data.ChatRoomList;
import panchat.data.User;
import panchat.linker.CausalLinker;
import panchat.messages.CausalMessage;
import panchat.messages.JoinChannel;
import panchat.messages.MessageChat;
import panchat.messages.SaludoListaCanales;

public class CausalLinkerThread extends Thread {

	public final static boolean DEBUG = false;

	private MulticastSocket socket;

	private Panchat panchat;

	private CausalLinker causalLinker;

	public CausalLinkerThread(MulticastSocket pSocket, Panchat pPanchat) {
		this.socket = pSocket;
		this.panchat = pPanchat;
		this.causalLinker = panchat.getCausalLinker();
	}

	public void run() {

		while (!socket.isClosed()) {

			try {
				// Leyendo objeto

				CausalMessage cm = causalLinker.handleMsg();

				Object objeto = cm.getContent();

				printDebug("objeto leído");

				if (objeto instanceof SaludoListaCanales) {

					printDebug("SaludoListaCanales");

					registrarSaludoCanales((SaludoListaCanales) objeto, cm
							.getUsuario());

				} else if (objeto instanceof JoinChannel) {

					printDebug("InscripcionCanal");

					inscribirCanal((JoinChannel) objeto, cm.getUsuario());

				} else if (objeto instanceof MessageChat) {

					printDebug("MessageChat");

					escribirMensajeCanal((MessageChat) objeto);

				} else if (objeto instanceof String) {

					printDebug("Message");

					escribirMensaje((String) objeto, cm.getUsuario());
				}

			} catch (IOException e1) {
				// Se ha cerrado el socket
			}
		}

		printDebug("Terminado");
	}

	private void registrarSaludoCanales(SaludoListaCanales saludo,
			User usuario) {

		printDebug("Saludo canal recibido");

		ChatRoomList listaCanales = panchat.getChannelList();

		for (ChatRoom canal : saludo.lista) {

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

	private void inscribirCanal(JoinChannel inscripcion, User usuario) {

		ChatRoomList listaCanales = panchat.getChannelList();

		User usuarioInscripcionObtenido = panchat.getListaUsuarios()
				.getUser(inscripcion.user.uuid);
		User usuarioEnvioObtenido = panchat.getListaUsuarios().getUser(
				usuario.uuid);

		ChatRoom canalObtenido = listaCanales.getChannel(inscripcion.channel
				.getName());

		printDebug("usuarioInscripcionObtenido : " + usuarioInscripcionObtenido);
		printDebug("usuarioEnvioObtenido : " + usuarioEnvioObtenido);
		printDebug("canalObtenido : " + canalObtenido);

		// El canal no está registrado
		if (canalObtenido == null) {

			if (inscripcion.joing) {

				printDebug("El canal no existía, creamos el canal y lo añadimos");

				// Creamos el canal
				ChatRoom canal = new ChatRoom(inscripcion.channel.getName(),
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

	private void escribirMensajeCanal(MessageChat objeto) {

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

	private void printDebug(String string) {
		String msgClase = "CausalLinkerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
