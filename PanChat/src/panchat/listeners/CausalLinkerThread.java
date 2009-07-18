package panchat.listeners;

import java.io.IOException;
import java.net.MulticastSocket;

import panchat.Panchat;
import panchat.data.Canal;
import panchat.data.ListaCanales;
import panchat.data.Usuario;
import panchat.linker.CausalLinker;
import panchat.messages.CausalMessage;
import panchat.share.protocolo.InscripcionCanal;
import panchat.share.protocolo.MessageChat;
import panchat.share.protocolo.SaludoListaCanales;

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

				if (objeto instanceof SaludoListaCanales)

					registrarSaludoCanales((SaludoListaCanales) objeto, cm
							.getUsuario());

				else if (objeto instanceof InscripcionCanal)

					inscribirCanal((InscripcionCanal) objeto, cm.getUsuario());

				else if (objeto instanceof MessageChat)

					escribirMensajeCanal((MessageChat) objeto);

				else if (objeto instanceof String)

					escribirMensaje((String) objeto, cm.getUsuario());

			} catch (IOException e1) {
				// Se ha cerrado el socket
			}
		}

		printDebug("Terminado");
	}

	private void registrarSaludoCanales(SaludoListaCanales saludo,
			Usuario usuario) {

		for (Canal canal : saludo.lista) {
			ListaCanales listaCanales = panchat.getListaCanales();

			Usuario usuarioObtenido = panchat.getListaUsuarios().getUsuario(
					usuario.uuid);

			Canal canalObtenido = listaCanales.getCanal(canal.getNombreCanal());

			if (canalObtenido == null) {
				canalObtenido = new Canal(canal.getNombreCanal(), panchat
						.getListaUsuarios());
				listaCanales.añadirCanal(canalObtenido);
			}

			canalObtenido.anyadirUsuario(usuarioObtenido);
		}
	}

	private void inscribirCanal(InscripcionCanal inscripcion, Usuario usuario) {

		ListaCanales listaCanales = panchat.getListaCanales();

		Usuario usuarioObtenido = panchat.getListaUsuarios().getUsuario(
				usuario.uuid);

		Canal canalObtenido = listaCanales.getCanal(inscripcion.canal
				.getNombreCanal());

		if (canalObtenido != null) {

			canalObtenido.eliminarUsuario(usuarioObtenido);

			if (canalObtenido.getNumUsuariosConectados() == 0)
				listaCanales.eliminarCanal(canalObtenido);
		}
	}

	private void escribirMensajeCanal(MessageChat objeto) {

		String nombreCanal = objeto.canal.getNombreCanal();

		ListaCanales listaCanales = panchat.getListaCanales();

		Canal canalObtenido = listaCanales.getCanal(nombreCanal);

		if (canalObtenido != null) {

			panchat.getListaConversaciones().getVentanaConversacion(
					canalObtenido).escribirComentario(objeto.mensaje);

		}
	}

	private void escribirMensaje(String comentario, Usuario usuario) {

		panchat.getListaConversaciones().getVentanaConversacion(usuario)
				.escribirComentario(comentario);

	}

	private void printDebug(String string) {
		String msgClase = "MulticastListenerThread.java: ";
		if (DEBUG)
			System.out.println(msgClase + string);
	}
}
