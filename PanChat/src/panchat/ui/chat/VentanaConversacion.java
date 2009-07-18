package panchat.ui.chat;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import panchat.Panchat;
import panchat.data.Usuario;

public class VentanaConversacion extends JFrame implements IVentanaConversacion {

	private static final long serialVersionUID = 1L;

	private final Panchat panchat;

	private final Usuario usuario;

	private PanelConversacion panelConversacion;

	public VentanaConversacion(Panchat pPanchat, Usuario pUsuario) {

		this.panchat = pPanchat;

		this.usuario = pUsuario;

		// Creamos el panel de la conversacion y lo añadimos a la ventana
		panelConversacion = new PanelConversacion(this);

		this.getContentPane().add(panelConversacion);

		pack();

		// Establecemos el nombre de la ventana
		String nombreVentana = panchat.getUsuario().nickName
				+ "  conversando con " + usuario.nickName;
		this.setTitle(nombreVentana);

		/*
		 * Establecemos el método de cerrado como destruir los recursos de la
		 * ventana al cerrar
		 */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		/*
		 * Cambiamos el listener de la ventana para gestionar nosotros la
		 * correcta terminación de la aplicación
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panchat.accionCerrarConversacion(usuario);
			}
		});

		setSize(750, 500);
		setVisible(true);
	}

	@Override
	public void eventoNuevoComentario(String comentario) {
		String nickName = panchat.getUsuario().nickName;
		String entrada = "<" + nickName + "> " + comentario;

		panchat.escribirComentario(usuario, entrada);
		this.escribirComentario(entrada);
	}

	/**
	 * Escribir nuevo comentario
	 * 
	 * @param comentario
	 */
	public void escribirComentario(String comentario) {
		panelConversacion.escribirComentario(comentario);
	}
}